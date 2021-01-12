package dev.vatuu.tesseract.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.network.PacketS2CSyncDimensionTypes;
import dev.vatuu.tesseract.world.DimensionState;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorderListener;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.level.UnmodifiableLevelProperties;

import java.util.Random;

public class TesseractRegistry {

    private static TesseractRegistry INSTANCE;

    private final MinecraftServer server;
    private final DynamicRegistryManager registries;
    private final SaveProperties saveProperties;

    private final Registry<DimensionType> dimensionTypeRegistry;

    private TesseractRegistry(MinecraftServer server) {
        this.server = server;
        this.registries = server.getRegistryManager();
        this.saveProperties = server.getSaveProperties();
        this.dimensionTypeRegistry = registries.get(Registry.DIMENSION_TYPE_KEY);;
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
        server.worlds.put(key, world);

        PacketS2CSyncDimensionTypes packet = new PacketS2CSyncDimensionTypes(
                this.server.getPlayerManager().registryManager,
                Lists.newArrayList(this.server.getWorldRegistryKeys()));
        PlayerLookup.all(this.server).forEach(p -> Tesseract.INSTANCE.getNetworkHandler().sendToClient(p, packet));

        return key;
    }

    public static void setInstance(MinecraftServer server) {
        INSTANCE = new TesseractRegistry(server);
    }
}
