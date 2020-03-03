package dev.vatuu.tesseract.api;

import dev.vatuu.tesseract.api.extensions.FogColourFunction;
import dev.vatuu.tesseract.api.extensions.RenderFogFunction;
import dev.vatuu.tesseract.api.extensions.SpawnChunkPosFunction;
import dev.vatuu.tesseract.api.extensions.SpawnTopFunction;
import dev.vatuu.tesseract.impl.world.DimensionBuilderImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;

import java.util.function.Function;

public interface DimensionBuilder {

    static DimensionBuilder create() { return new DimensionBuilderImpl(); }

    DimensionBuilder bedsExplode(boolean shouldExplode);

    DimensionBuilder beesExplode(boolean shouldExplode);

    DimensionBuilder visibleSky(boolean hasVisibleSky);

    DimensionBuilder vaporizeWater(boolean vaporizeWater);

    DimensionBuilder cloudHeight(float cloudHeight);

    DimensionBuilder renderFog(boolean shouldRenderFog);

    DimensionBuilder renderFog(RenderFogFunction bi);

    DimensionBuilder fogColour(int red, int green, int blue);

    DimensionBuilder fogColour(FogColourFunction bi);

    DimensionBuilder spawningBlockChunk(SpawnChunkPosFunction bi);

    DimensionBuilder topSpawningBlockPos(SpawnTopFunction tri);

    DimensionBuilder worldBorder(WorldBorder b);

    DimensionBuilder forcedSpawnPoint(BlockPos pos);

    DimensionBuilder chunkGenerator(Function<World, ChunkGenerator<? extends ChunkGeneratorConfig>> generator);

    Dimension build(World w, DimensionType t);
}
