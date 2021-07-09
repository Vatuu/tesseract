package dev.teamtesseract.fractal.registry;

import com.mojang.serialization.Lifecycle;
import dev.teamtesseract.fractal.Fractal;
import dev.teamtesseract.fractal.extensions.SimpleRegistryExt;
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

public class FractalRegistry {

    private static FractalRegistry INSTANCE;

    private final Registry<DimensionType> dimensionTypeRegistry;
    private final Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry;

    private final RegistryKey<Registry<DimensionType>> fractalRegistryKey = RegistryKey.ofRegistry(Fractal.id("fractal"));
    private final Registry<DimensionType> fractalRegistry;
    private final Map<RegistryKey<World>, Pair<RegistryKey<DimensionType>, RegistryKey<DimensionType>>> worldSpecificTracker;

    private FractalRegistry(MinecraftServer server) {
        this.dimensionTypeRegistry = server.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY);
        this.fractalRegistry = new SimpleRegistry<>(fractalRegistryKey, Lifecycle.experimental());
        this.chunkGeneratorSettingsRegistry = server.getRegistryManager().get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
        this.worldSpecificTracker = new HashMap<>();
    }

    public static FractalRegistry getInstance() {
        return INSTANCE;
    }

    public static void setInstance(MinecraftServer server) {
        INSTANCE = new FractalRegistry(server);
    }

    public RegistryKey<DimensionType> registerDimension(DimensionType type, Identifier id) throws FractalException {
        RegistryKey<DimensionType> key = RegistryKey.of(fractalRegistryKey, id);

        if(fractalRegistry.containsId(id))
            throw new FractalException(String.format("DimensionType %s has already been registered!", id));
        if(worldSpecificTracker.values().stream().anyMatch(p -> p.getRight().equals(key)))
            throw new FractalException(String.format("DimensionType %s has already been registered as a world-specific type!", id));
        if(dimensionTypeRegistry.containsId(id))
            throw new FractalException(String.format("DimensionType %s has already been registered in a non-fractal context!", id));

        Registry.register(fractalRegistry, id, type);

        return key;
    }

    public RegistryKey<ChunkGeneratorSettings> registerChunkGeneratorSettings(ChunkGeneratorSettings type, Identifier id) throws FractalException {
        RegistryKey<ChunkGeneratorSettings> key = RegistryKey.of(Registry.CHUNK_GENERATOR_SETTINGS_KEY, id);

        if(chunkGeneratorSettingsRegistry.containsId(id))
            throw new FractalException(String.format("ChunkGeneratorSettings %s has already been registered!", id));

        Registry.register(chunkGeneratorSettingsRegistry, id, type);

        return key;
    }

    public DimensionType getDimensionType(RegistryKey<DimensionType> key) throws FractalException {
        try {
            return fractalRegistry.getOrThrow(key);
        } catch (IllegalStateException e) {
            throw new FractalException(String.format("The DimensionType %s does not exist!", key));
        }
    }

    DimensionType registerWorldCopy(RegistryKey<World> world, RegistryKey<DimensionType> type, Identifier id) throws FractalException {
        RegistryKey<DimensionType> key = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, id);

        if(fractalRegistry.containsId(id))
            throw new FractalException(String.format("World-specific DimensionType %s is already a regular DimensionType!", id));
        if(worldSpecificTracker.values().stream().anyMatch(p -> p.getRight().equals(RegistryKey.of(Registry.DIMENSION_TYPE_KEY, id))))
            throw new FractalException(String.format("\"World-specific DimensionType %s has already been registered!", id));
        if(dimensionTypeRegistry.containsId(id))
            throw new FractalException(String.format("World-specific DimensionType %s has already been registered in a non-fractal context!", id));

        DimensionType copy = copyDimensionType(type);
        Registry.register(dimensionTypeRegistry, id, copy);

        worldSpecificTracker.put(world, new Pair<>(type, key));

        return copy;
    }

    @SuppressWarnings("unchecked")
    void unregisterWorldCopy(RegistryKey<World> world) throws FractalException {
        if(world.equals(World.OVERWORLD) || world.equals(World.NETHER) || world.equals(World.END))
            throw new FractalException("Vanilla Worlds do not have world-specific types!");
        if(!worldSpecificTracker.containsKey(world))
            throw new FractalException(String.format("%s is not a world-specific dimension type!", world));

        Pair<RegistryKey<DimensionType>, RegistryKey<DimensionType>> types = worldSpecificTracker.remove(world);
        ((SimpleRegistryExt<DimensionType>)dimensionTypeRegistry).remove(types.getRight());
    }

    private DimensionType copyDimensionType(RegistryKey<DimensionType> key) throws FractalException {
        DimensionType type = getDimensionType(key);
        return DimensionType.create(
                type.fixedTime,
                type.hasSkyLight(), type.hasCeiling(), type.isUltrawarm(), type.isNatural(),
                type.getCoordinateScale(), type.hasEnderDragonFight(),
                type.isPiglinSafe(), type.isBedWorking(), type.isRespawnAnchorWorking(), type.hasRaids(),
                type.getMinimumY(), type.getHeight(), type.getLogicalHeight(),
                type.getBiomeAccessType(), type.infiniburn, type.getSkyProperties(), type.ambientLight);
    }

}
