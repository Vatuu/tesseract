package dev.vatuu.tesseract.impl.world;

import dev.vatuu.tesseract.api.extensions.FogColourFunction;
import dev.vatuu.tesseract.api.extensions.RenderFogFunction;
import dev.vatuu.tesseract.api.extensions.SpawnChunkPosFunction;
import dev.vatuu.tesseract.api.extensions.SpawnTopFunction;
import dev.vatuu.tesseract.impl.Tesseract;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.chunk.*;

import java.util.function.Function;

public final class DimensionSettings {

    boolean shouldBedsExplode = false;
    boolean hasVisibleSky = false;
    boolean vaporizeWater = false;
    boolean beesExplode = false;
    float cloudHeight = 128.0f;
    WorldBorder border = new WorldBorder();
    BlockPos forcedSpawn = null;
    RenderFogFunction isFogThick = (skyAngle, tickDelta) -> false;
    FogColourFunction fogColour = (position, tickDelta) -> Tesseract.getRgbColour(0, 0, 0);
    SpawnChunkPosFunction spawningBlockChunk = (pos, checkMobSpawnValidity) -> null;
    SpawnTopFunction topSpawningBlockPos = (x, z, checkMobSpawnValidity) -> null;
    Function<World, ChunkGenerator<? extends ChunkGeneratorConfig>> chunkGenerator = DEFAULT_VOID_WORLD;

    private static final Function<World, ChunkGenerator<? extends ChunkGeneratorConfig>> DEFAULT_VOID_WORLD = (world) -> {
        FlatChunkGeneratorConfig config = ChunkGeneratorType.FLAT.createSettings();
        config.getLayers().add(new FlatChunkGeneratorLayer(1, Blocks.AIR));
        config.setBiome(Biomes.THE_VOID);
        config.updateLayerBlocks();
        FixedBiomeSourceConfig biomeConfig = BiomeSourceType.FIXED.getConfig(world.getLevelProperties()).setBiome(Biomes.THE_VOID);

        return ChunkGeneratorType.FLAT.create(world, BiomeSourceType.FIXED.applyConfig(biomeConfig), config);
    };

    public boolean isBeesExplode(){
        return beesExplode;
    }
}
