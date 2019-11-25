package dev.vatuu.tesseract.impl.world;

import dev.vatuu.tesseract.api.DimensionBuilder;
import dev.vatuu.tesseract.impl.Tesseract;
import dev.vatuu.tesseract.api.extensions.FogColourFunction;
import dev.vatuu.tesseract.api.extensions.RenderFogFunction;
import dev.vatuu.tesseract.api.extensions.SpawnChunkPosFunction;
import dev.vatuu.tesseract.api.extensions.SpawnTopFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;

import java.util.function.Function;

public class DimensionBuilderImpl implements DimensionBuilder {

    private DimensionSettings dim = new DimensionSettings();

    public DimensionBuilderImpl bedsExplode(boolean shouldExplode){
        dim.shouldBedsExplode = shouldExplode;
        return this;
    }

    public DimensionBuilderImpl visibleSky(boolean hasVisibleSky){
        dim.hasVisibleSky = hasVisibleSky;
        return this;
    }

    public DimensionBuilderImpl vaporizeWater(boolean vaporizeWater){
        dim.vaporizeWater = vaporizeWater;
        return this;
    }

    public DimensionBuilderImpl cloudHeight(float cloudHeight){
        dim.cloudHeight = cloudHeight;
        return this;
    }

    public DimensionBuilderImpl renderFog(boolean shouldRenderFog){
        dim.isFogThick = (i1, i2) -> shouldRenderFog;
        return this;
    }

    public DimensionBuilderImpl renderFog(RenderFogFunction bi){
        dim.isFogThick = bi;
        return this;
    }

    public DimensionBuilderImpl fogColour(int red, int green, int blue){
        dim.fogColour = (f1, f2) -> Tesseract.getRgbColour(red, green, blue);
        return this;
    }

    public DimensionBuilderImpl fogColour(FogColourFunction bi){
        dim.fogColour = bi;
        return this;
    }

    public DimensionBuilderImpl spawningBlockChunk(SpawnChunkPosFunction bi){
        dim.spawningBlockChunk = bi;
        return this;
    }

    public DimensionBuilderImpl topSpawningBlockPos(SpawnTopFunction tri){
        dim.topSpawningBlockPos = tri;
        return this;
    }

     public DimensionBuilderImpl worldBorder(WorldBorder b){
        dim.border = b;
        return this;
     }

     public DimensionBuilderImpl beesExplode(boolean b){
        dim.beesExplode = b;
        return this;
     }

     public DimensionBuilderImpl forcedSpawnPoint(BlockPos pos){
        dim.forcedSpawn = pos;
        return this;
     }

    public DimensionBuilderImpl chunkGenerator(Function<World, ChunkGenerator<? extends ChunkGeneratorConfig>> generator){
        dim.chunkGenerator = generator;
        return this;
    }

    public Dimension build(World w, DimensionType t){
        return new TesseractDimension(w, t, dim);
    }
}
