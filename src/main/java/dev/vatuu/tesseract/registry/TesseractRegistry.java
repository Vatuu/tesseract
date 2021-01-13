package dev.vatuu.tesseract.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.cmd.CreateTestWorldCommand;
import dev.vatuu.tesseract.extensions.ServerWorldExt;
import dev.vatuu.tesseract.network.PacketS2CSyncDimensionTypes;
import dev.vatuu.tesseract.world.DimensionState;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorderListener;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class TesseractRegistry {

    private static TesseractRegistry INSTANCE;

    private final MinecraftServer server;
    private final DynamicRegistryManager registries;
    private final SaveProperties saveProperties;

    private final Registry<DimensionType> dimensionTypeRegistry;
    //private final Registry<Codec<? extends ChunkGenerator>> chunkGeneratorRegistry;
    private final Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry;

    private TesseractRegistry(MinecraftServer server) {
        this.server = server;
        this.registries = server.getRegistryManager();
        this.saveProperties = server.getSaveProperties();
        this.dimensionTypeRegistry = registries.get(Registry.DIMENSION_TYPE_KEY);
        //this.chunkGeneratorRegistry = registries.get(Registry.CHUNK_GENERATOR_KEY);
        this.chunkGeneratorSettingsRegistry = registries.get(Registry.NOISE_SETTINGS_WORLDGEN);
    }

    public static TesseractRegistry getInstance() {
        return INSTANCE;
    }

    public RegistryKey<DimensionType> registerDimension(DimensionType type, Identifier id) throws TesseractRegistryException {
        RegistryKey<DimensionType> key = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, id);

        if(dimensionTypeRegistry.containsId(id))
            throw new TesseractRegistryException(String.format("DimensionType %s has already been registered!", id));

        Registry.register(dimensionTypeRegistry, id, type);

        return key;
    }

//    public RegistryKey<ChunkGeneratorSettings> registerChunkGenerator(ChunkGeneratorSettings type, Identifier id) throws TesseractRegistryException {
//        RegistryKey<Codec<? extends ChunkGenerator>> key = RegistryKey.of(Registry.CHUNK_GENERATOR_KEY, id);
//
//        if(chunkGeneratorRegistry.containsId(id))
//            throw new TesseractRegistryException(String.format("ChunkGenerator %s has already been registered!", id));
//
//        Registry.register(chunkGeneratorRegistry, id, type);
//
//        return key;
//    }

    public RegistryKey<ChunkGeneratorSettings> registerChunkGeneratorSettings(ChunkGeneratorSettings type, Identifier id) throws TesseractRegistryException {
        RegistryKey<ChunkGeneratorSettings> key = RegistryKey.of(Registry.NOISE_SETTINGS_WORLDGEN, id);

        if(chunkGeneratorSettingsRegistry.containsId(id))
            throw new TesseractRegistryException(String.format("ChunkGeneratorSettings %s has already been registered!", id));

        Registry.register(chunkGeneratorSettingsRegistry, id, type);

        return key;
    }

    public RegistryKey<World> createWorld(RegistryKey<DimensionType> type, Identifier id, DimensionState state) throws TesseractRegistryException {
        DimensionType dimType = this.dimensionTypeRegistry.get(type.getValue());
        if(dimType == null)
            throw new TesseractRegistryException(String.format("DimensionType %s has not been registered. [%s]", type, id));

        RegistryKey<World> key = RegistryKey.of(Registry.DIMENSION, id);
        if(server.worlds.containsKey(key))
            throw new TesseractRegistryException(String.format("World %s has already been created and not removed.", id));

        NoiseChunkGenerator chunkGen = GeneratorOptions.createOverworldGenerator(registries.get(Registry.BIOME_KEY), registries.get(Registry.NOISE_SETTINGS_WORLDGEN), (new Random()).nextLong());
        UnmodifiableLevelProperties properties = new UnmodifiableLevelProperties(saveProperties, saveProperties.getMainWorldProperties());

        ServerWorld world = new ServerWorld(
                server,
                server.workerExecutor,
                server.session, properties,
                key, dimType,
                server.worldGenerationProgressListenerFactory.create(11),
                chunkGen,
                false,
                BiomeAccess.hashSeed(server.getSaveProperties().getGeneratorOptions().getSeed()),
                ImmutableList.of(),
                false);
        server.worlds.get(World.OVERWORLD).getWorldBorder().addListener(new WorldBorderListener.WorldBorderSyncer(world.getWorldBorder()));
        ((ServerWorldExt)world).setSaveState(state);
        server.worlds.put(key, world);

        PacketS2CSyncDimensionTypes packet = new PacketS2CSyncDimensionTypes(
                this.server.getPlayerManager().registryManager,
                Lists.newArrayList(this.server.getWorldRegistryKeys()));
        PlayerLookup.all(this.server).forEach(p -> Tesseract.INSTANCE.getNetworkHandler().sendToClient(p, packet));

        return key;
    }

    public void destroyWorld(RegistryKey<World> type) throws TesseractRegistryException {
        ServerWorld w = server.getWorld(type);

        if(w == null)
            throw new TesseractRegistryException(String.format("World %s does not exist!", type));

        try {
            new ArrayList<>(w.getPlayers()).forEach(p -> {
                Vec3d pos = new Vec3d(0, 64, 0);
                p.teleport(server.getOverworld(), pos.getX(), pos.getY(), pos.getZ(), p.yaw, p.pitch);
            });

            w.close();
            server.worlds.remove(CreateTestWorldCommand.DIMENSION_WORLD);
        } catch(IOException e) {
            e.printStackTrace();
            throw new TesseractRegistryException("Something went wrong! " + e.getMessage());
        }
    }

    public void resetWorld(RegistryKey<World> world) {
        try {
            File worldRoot = FabricLoader.getInstance().getGameDir().resolve("saves").resolve(server.getSaveProperties().getLevelName()).toFile();
            System.out.println(DimensionType.getSaveDirectory(world, worldRoot));
            FileUtils.deleteDirectory(DimensionType.getSaveDirectory(world, worldRoot));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void setInstance(MinecraftServer server) {
        INSTANCE = new TesseractRegistry(server);
    }
}
