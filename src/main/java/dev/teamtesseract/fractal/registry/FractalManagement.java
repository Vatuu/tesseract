package dev.teamtesseract.fractal.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import dev.teamtesseract.fractal.Fractal;
import dev.teamtesseract.fractal.extensions.ServerWorldExt;
import dev.teamtesseract.fractal.network.PacketS2CSyncDimensionTypes;
import dev.teamtesseract.fractal.world.DimensionState;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorderListener;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class FractalManagement {

    public static FractalManagement INSTANCE;

    private final MinecraftServer server;
    private final SaveProperties saveProperties;

    private FractalManagement(MinecraftServer server) {
        this.server = server;
        this.saveProperties = server.getSaveProperties();
    }

    public static FractalManagement getInstance() {
        return INSTANCE;
    }

    public RegistryKey<World> createWorld(RegistryKey<DimensionType> type, Identifier id, ChunkGenerator chunkGen, DimensionState state) throws FractalException {
        RegistryKey<World> key = RegistryKey.of(Registry.WORLD_KEY, id);
        if(server.worlds.containsKey(key))
            throw new FractalException(String.format("World %s has already been created and not removed.", id));

        DimensionType wtype = FractalRegistry.getInstance().registerWorldCopy(key, type, id);

        ServerWorld world = new ServerWorld(
                server,
                server.workerExecutor,
                server.session, new UnmodifiableLevelProperties(saveProperties, saveProperties.getMainWorldProperties()),
                key, wtype,
                server.worldGenerationProgressListenerFactory.create(11),
                chunkGen,
                false,
                BiomeAccess.hashSeed(server.getSaveProperties().getGeneratorOptions().getSeed()),
                ImmutableList.of(),
                !wtype.hasFixedTime());
        server.worlds.get(World.OVERWORLD).getWorldBorder().addListener(new WorldBorderListener.WorldBorderSyncer(world.getWorldBorder()));
        //noinspection ConstantConditions
        ((ServerWorldExt)world).setSaveState(state);
        server.worlds.put(key, world);

        sendSyncPacket();

        return key;
    }

    public void unloadWorld(RegistryKey<World> type) throws FractalException {
        if(type.equals(World.OVERWORLD))
            throw new FractalException("The Overworld cannot be unloaded!");

        ServerWorld w = server.getWorld(type);

        if(w == null)
            throw new FractalException(String.format("World %s does not exist!", type));

        try {
            new ArrayList<>(w.getPlayers()).forEach(p -> {
                BlockPos pos = server.getOverworld().getSpawnPos();
                p.teleport(server.getOverworld(), pos.getX(), pos.getY(), pos.getZ(), p.getYaw(), p.getPitch());
            });

            w.close();
            server.worlds.remove(type);

            if(!type.equals(World.NETHER) && !type.equals(World.END))
                FractalRegistry.getInstance().unregisterWorldCopy(type);

            sendSyncPacket();
        } catch(IOException e) {
            e.printStackTrace();
            throw new FractalException("Something went wrong! " + e.getMessage());
        }
    }

    public void resetWorld(RegistryKey<World> world) throws FractalException {
        if(world.getValue().equals(World.OVERWORLD))
            throw new FractalException("The Overworld cannot be reset!");

        try {
            File worldRoot = FabricLoader.getInstance().getGameDir().resolve("saves").resolve(server.getSaveProperties().getLevelName()).toFile();
            System.out.println(DimensionType.getSaveDirectory(world, worldRoot));
            FileUtils.deleteDirectory(DimensionType.getSaveDirectory(world, worldRoot));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void setInstance(MinecraftServer server) {
        INSTANCE = new FractalManagement(server);
    }

    private void sendSyncPacket() {
        PacketS2CSyncDimensionTypes packet = new PacketS2CSyncDimensionTypes(
                this.server.getPlayerManager().registryManager,
                Lists.newArrayList(this.server.getWorldRegistryKeys()));
        PlayerLookup.all(this.server).forEach(p -> Fractal.INSTANCE.getNetworkHandler().sendToClient(p, packet));

    }
}
