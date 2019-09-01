package dev.vatuu.tesseract.world;

import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.extensions.TriFunction;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.gen.chunk.*;

import java.util.function.BiFunction;
import java.util.function.Function;

public class DimensionSettings {

    public boolean shouldBedsExplode = false;
    public boolean shouldRenderSkybox = false;
    public boolean vaporizeWater = false;
    public float cloudHeight = 128.0f;
    public BiFunction<Integer, Integer, Boolean> shouldRenderFog = (i1, i2) -> false;
    public BiFunction<Float, Float, Vec3d> fogColour = (f1, f2) -> Tesseract.getRgbColour(0, 0, 0);
    public BiFunction<ChunkPos, Boolean, BlockPos> spawningBlockChunk = (pos, b) -> null;
    public TriFunction<Integer, Integer, Boolean, BlockPos> topSpawningBlockPos = (i1, i2, b) -> null;
    public WorldBorder border = new WorldBorder();
    public BlockPos forcedSpawn = null;
    public Function<World, ChunkGenerator<? extends ChunkGeneratorConfig>> chunkGenerator = DEFAULT_VOID_WORLD;

    private static Function<World, ChunkGenerator<? extends ChunkGeneratorConfig>> DEFAULT_VOID_WORLD = (w) -> {
        FlatChunkGeneratorConfig config = ChunkGeneratorType.FLAT.createSettings();
        config.getLayers().add(new FlatChunkGeneratorLayer(1, Blocks.AIR));
        config.setBiome(Biomes.THE_VOID);
        config.updateLayerBlocks();
        FixedBiomeSourceConfig biomeConfig = BiomeSourceType.FIXED.getConfig().setBiome(Biomes.THE_VOID);

        return ChunkGeneratorType.FLAT.create(w, BiomeSourceType.FIXED.applyConfig(biomeConfig), config);
    };
}
