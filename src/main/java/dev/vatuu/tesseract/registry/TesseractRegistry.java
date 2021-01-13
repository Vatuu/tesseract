package dev.vatuu.tesseract.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Lifecycle;
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
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import org.apache.commons.io.FileUtils;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TesseractRegistry {

    private static TesseractRegistry INSTANCE;

    private final Registry<DimensionType> dimensionTypeRegistry;
    private final Registry<DimensionType> tesseractRegistry;
    private final Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry;

    private final RegistryKey<Registry<DimensionType>> tesseractRegistryKey = RegistryKey.ofRegistry(Tesseract.id("tesseract"));
    private final Map<RegistryKey<DimensionType>, List<DimensionType>> typeChildren;

    private TesseractRegistry(MinecraftServer server) {
        this.dimensionTypeRegistry = server.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY);
        this.tesseractRegistry = new SimpleRegistry<>(tesseractRegistryKey, Lifecycle.experimental());
        this.chunkGeneratorSettingsRegistry = server.getRegistryManager().get(Registry.NOISE_SETTINGS_WORLDGEN);
        this.typeChildren = new HashMap<>();
    }

    public static TesseractRegistry getInstance() {
        return INSTANCE;
    }

    public static void setInstance(MinecraftServer server) {
        INSTANCE = new TesseractRegistry(server);
    }

    public RegistryKey<DimensionType> registerDimension(DimensionType type, Identifier id) throws TesseractException {
        RegistryKey<DimensionType> key = RegistryKey.of(tesseractRegistryKey, id);

        if(tesseractRegistry.containsId(id) || typeChildren.containsKey(key))
            throw new TesseractException(String.format("DimensionType %s has already been registered!", id));

        Registry.register(tesseractRegistry, id, type);
        typeChildren.put(key, new ArrayList<>());

        return key;
    }

    public RegistryKey<ChunkGeneratorSettings> registerChunkGeneratorSettings(ChunkGeneratorSettings type, Identifier id) throws TesseractException {
        RegistryKey<ChunkGeneratorSettings> key = RegistryKey.of(Registry.NOISE_SETTINGS_WORLDGEN, id);

        if(chunkGeneratorSettingsRegistry.containsId(id))
            throw new TesseractException(String.format("ChunkGeneratorSettings %s has already been registered!", id));

        Registry.register(chunkGeneratorSettingsRegistry, id, type);

        return key;
    }

    public DimensionType getDimensionType(RegistryKey<DimensionType> key) throws TesseractException {
        try {
            return dimensionTypeRegistry.getOrThrow(key);
        } catch(IllegalStateException e) {
            throw new TesseractException(String.format("The DimensionType %s does not exist!", key));
        }
    }

    DimensionType registerWorldCopy(RegistryKey<DimensionType> type, Identifier id) throws TesseractException {
        if(dimensionTypeRegistry.containsId(id))
            throw new TesseractException(String.format("DimensionType for World %s has already been registered!", id));

        DimensionType copy = copyDimensionType(type);
        Registry.register(dimensionTypeRegistry, id, copy);

        typeChildren.computeIfPresent(type, (k, v) -> {
            v.add(copy);
            return v;
        });

        return copy;
    }

    private DimensionType copyDimensionType(RegistryKey<DimensionType> key) throws TesseractException {
        DimensionType type = getDimensionType(key);
        return DimensionType.method_32922(
                type.fixedTime,
                type.hasSkyLight(), type.hasCeiling(), type.isUltrawarm(), type.isNatural(),
                type.getCoordinateScale(), type.hasEnderDragonFight(),
                type.isPiglinSafe(), type.isBedWorking(), type.isRespawnAnchorWorking(), type.hasRaids(),
                type.getMinimumY(), type.getHeight(), type.getLogicalHeight(),
                type.getBiomeAccessType(), type.infiniburn, type.getSkyProperties(), type.ambientLight);
    }

}
