package dev.vatuu.tesseract.impl.world;

import dev.vatuu.tesseract.api.DimensionRegistry;
import dev.vatuu.tesseract.impl.Tesseract;
import dev.vatuu.tesseract.api.extensions.FogColourFunction;
import dev.vatuu.tesseract.api.extensions.RenderFogFunction;
import dev.vatuu.tesseract.api.extensions.SpawnTopFunction;
import dev.vatuu.tesseract.api.extensions.SpawnChunkPosFunction;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.chunk.*;

import java.util.function.Function;

final class DimensionSettings {

    boolean shouldBedsExplode = false;
    boolean shouldRenderSkybox = false;
    boolean vaporizeWater = false;
    float cloudHeight = 128.0f;
    RenderFogFunction isFogThick = (i1, i2) -> false;
    FogColourFunction fogColour = (f1, f2) -> Tesseract.getRgbColour(0, 0, 0);
    SpawnChunkPosFunction spawningBlockChunk = (pos, b) -> null;
    SpawnTopFunction topSpawningBlockPos = (i1, i2, b) -> null;
    WorldBorder border = new WorldBorder();
    BlockPos forcedSpawn = null;
    Function<World, ChunkGenerator<? extends ChunkGeneratorConfig>> chunkGenerator = DEFAULT_VOID_WORLD;

    private static Function<World, ChunkGenerator<? extends ChunkGeneratorConfig>> DEFAULT_VOID_WORLD = (w) -> {
        FlatChunkGeneratorConfig config = ChunkGeneratorType.FLAT.createSettings();
        config.getLayers().add(new FlatChunkGeneratorLayer(1, Blocks.AIR));
        config.setBiome(Biomes.THE_VOID);
        config.updateLayerBlocks();
        FixedBiomeSourceConfig biomeConfig = BiomeSourceType.FIXED.getConfig(w.getLevelProperties()).setBiome(Biomes.THE_VOID);

        return ChunkGeneratorType.FLAT.create(w, BiomeSourceType.FIXED.applyConfig(biomeConfig), config);
    };
}
