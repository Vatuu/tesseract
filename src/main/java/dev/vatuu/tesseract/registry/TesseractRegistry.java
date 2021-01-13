package dev.vatuu.tesseract.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.cmd.CreateTestWorldCommand;
import dev.vatuu.tesseract.extensions.ServerWorldExt;
import dev.vatuu.tesseract.network.PacketS2CSyncDimensionTypes;
import dev.vatuu.tesseract.world.DimensionState;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import org.apache.commons.io.FileUtils;

public class TesseractRegistry {

    private static TesseractRegistry INSTANCE;

    private final Registry<DimensionType> dimensionTypeRegistry;
    private final Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry;

    private TesseractRegistry(MinecraftServer server) {
        this.dimensionTypeRegistry = server.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY);
        this.chunkGeneratorSettingsRegistry = server.getRegistryManager().get(Registry.NOISE_SETTINGS_WORLDGEN);
    }

    public static TesseractRegistry getInstance() {
        return INSTANCE;
    }

    public RegistryKey<DimensionType> registerDimension(DimensionType type, Identifier id) throws TesseractException {
        RegistryKey<DimensionType> key = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, id);

        if(dimensionTypeRegistry.containsId(id))
            throw new TesseractException(String.format("DimensionType %s has already been registered!", id));

        Registry.register(dimensionTypeRegistry, id, type);

        return key;
    }

    public RegistryKey<ChunkGeneratorSettings> registerChunkGeneratorSettings(ChunkGeneratorSettings type, Identifier id) throws TesseractException {
        RegistryKey<ChunkGeneratorSettings> key = RegistryKey.of(Registry.NOISE_SETTINGS_WORLDGEN, id);

        if(chunkGeneratorSettingsRegistry.containsId(id))
            throw new TesseractException(String.format("ChunkGeneratorSettings %s has already been registered!", id));

        Registry.register(chunkGeneratorSettingsRegistry, id, type);

        return key;
    }

    public static void setInstance(MinecraftServer server) {
        INSTANCE = new TesseractRegistry(server);
    }
}
