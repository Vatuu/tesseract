package dev.vatuu.tesseract.api;

import dev.vatuu.tesseract.api.extensions.FogColourFunction;
import dev.vatuu.tesseract.api.extensions.RenderFogFunction;
import dev.vatuu.tesseract.api.extensions.SpawnChunkPosFunction;
import dev.vatuu.tesseract.api.extensions.SpawnTopFunction;
import dev.vatuu.tesseract.impl.world.DimensionFactoryImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;

import java.util.function.Function;

public interface DimensionFactory {

    static DimensionFactory create() { return new DimensionFactoryImpl(); }

    DimensionFactory bedsExplode(boolean shouldExplode);

    DimensionFactory beesExplode(boolean shouldExplode);

    DimensionFactory visibleSky(boolean hasVisibleSky);

    DimensionFactory vaporizeWater(boolean vaporizeWater);

    DimensionFactory cloudHeight(float cloudHeight);

    DimensionFactory renderFog(boolean shouldRenderFog);

    DimensionFactory renderFog(RenderFogFunction bi);

    DimensionFactory fogColour(int red, int green, int blue);

    DimensionFactory fogColour(FogColourFunction bi);

    DimensionFactory spawningBlockChunk(SpawnChunkPosFunction bi);

    DimensionFactory topSpawningBlockPos(SpawnTopFunction tri);

    DimensionFactory worldBorder(WorldBorder b);

    DimensionFactory forcedSpawnPoint(BlockPos pos);

    DimensionFactory forcedSpawnPoint(int x, int y, int z);

    DimensionFactory chunkGenerator(Function<World, ChunkGenerator<? extends ChunkGeneratorConfig>> generator);

    Dimension build(World w, DimensionType t);
}
