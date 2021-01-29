package dev.vatuu.tesseract.registry;

import com.mojang.serialization.Lifecycle;
import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.extensions.SimpleRegistryExt;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

import java.util.HashMap;
import java.util.Map;

public class TesseractRegistry {

    private static TesseractRegistry INSTANCE;

    private final Registry<DimensionType> dimensionTypeRegistry;
    private final Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry;

    private final RegistryKey<Registry<DimensionType>> tesseractRegistryKey = RegistryKey.ofRegistry(Tesseract.id("tesseract"));
    private final Registry<DimensionType> tesseractRegistry;
    private final Map<RegistryKey<World>, Pair<RegistryKey<DimensionType>, RegistryKey<DimensionType>>> worldSpecificTracker;

    private TesseractRegistry(MinecraftServer server) {
        this.dimensionTypeRegistry = server.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY);
        this.tesseractRegistry = new SimpleRegistry<>(tesseractRegistryKey, Lifecycle.experimental());
        this.chunkGeneratorSettingsRegistry = server.getRegistryManager().get(Registry.NOISE_SETTINGS_WORLDGEN);
        this.worldSpecificTracker = new HashMap<>();
    }

    public static TesseractRegistry getInstance() {
        return INSTANCE;
    }

    public static void setInstance(MinecraftServer server) {
        INSTANCE = new TesseractRegistry(server);
    }

    public RegistryKey<DimensionType> registerDimension(DimensionType type, Identifier id) throws TesseractException {
        RegistryKey<DimensionType> key = RegistryKey.of(tesseractRegistryKey, id);

        if(tesseractRegistry.containsId(id))
            throw new TesseractException(String.format("DimensionType %s has already been registered!", id));
        if(worldSpecificTracker.values().stream().anyMatch(p -> p.getRight().equals(key)))
            throw new TesseractException(String.format("DimensionType %s has already been registered as a world-specific type!", id));
        if(dimensionTypeRegistry.containsId(id))
            throw new TesseractException(String.format("DimensionType %s has already been registered in a non-tesseract context!", id));

        Registry.register(tesseractRegistry, id, type);

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
            return tesseractRegistry.getOrThrow(key);
        } catch (IllegalStateException e) {
            throw new TesseractException(String.format("The DimensionType %s does not exist!", key));
        }
    }

    DimensionType registerWorldCopy(RegistryKey<World> world, RegistryKey<DimensionType> type, Identifier id) throws TesseractException {
        RegistryKey<DimensionType> key = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, id);

        if(tesseractRegistry.containsId(id))
            throw new TesseractException(String.format("World-specific DimensionType %s is already a regular DimensionType!", id));
        if(worldSpecificTracker.values().stream().anyMatch(p -> p.getRight().equals(RegistryKey.of(Registry.DIMENSION_TYPE_KEY, id))))
            throw new TesseractException(String.format("\"World-specific DimensionType %s has already been registered!", id));
        if(dimensionTypeRegistry.containsId(id))
            throw new TesseractException(String.format("World-specific DimensionType %s has already been registered in a non-tesseract context!", id));

        DimensionType copy = copyDimensionType(type);
        Registry.register(dimensionTypeRegistry, id, copy);

        worldSpecificTracker.put(world, new Pair<>(type, key));

        return copy;
    }

    @SuppressWarnings("unchecked")
    void unregisterWorldCopy(RegistryKey<World> world) throws TesseractException {
        if(world.equals(World.OVERWORLD) || world.equals(World.NETHER) || world.equals(World.END))
            throw new TesseractException("Vanilla Worlds do not have world-specific types!");
        if(!worldSpecificTracker.containsKey(world))
            throw new TesseractException(String.format("%s is not a world-specific dimension type!", world));

        Pair<RegistryKey<DimensionType>, RegistryKey<DimensionType>> types = worldSpecificTracker.remove(world);
        ((SimpleRegistryExt<DimensionType>)dimensionTypeRegistry).remove(types.getRight());
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
