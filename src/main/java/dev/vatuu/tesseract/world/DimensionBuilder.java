package dev.vatuu.tesseract.world;

import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.extensions.TriFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;

import java.util.function.BiFunction;
import java.util.function.Function;

public class DimensionBuilder {

    private DimensionSettings dim;

    public DimensionBuilder(){
        dim = new DimensionSettings();
    }

    public DimensionBuilder bedsExplode(boolean shouldExplode){
        dim.shouldBedsExplode = shouldExplode;
        return this;
    }

    public DimensionBuilder renderSkybox(boolean shouldRenderSkybox){
        dim.shouldRenderSkybox = shouldRenderSkybox;
        return this;
    }

    public DimensionBuilder vaporizeWater(boolean vaporizeWater){
        dim.vaporizeWater = vaporizeWater;
        return this;
    }

    public DimensionBuilder cloudHeight(float cloudHeight){
        dim.cloudHeight = cloudHeight;
        return this;
    }

    public DimensionBuilder renderFog(boolean shouldRenderFog){
        dim.shouldRenderFog = (i1, i2) -> shouldRenderFog;
        return this;
    }

    public DimensionBuilder renderFog(BiFunction<Integer, Integer, Boolean> bi){
        dim.shouldRenderFog = bi;
        return this;
    }

    public DimensionBuilder fogColour(int red, int green, int blue){
        dim.fogColour = (f1, f2) -> Tesseract.getRgbColour(red, green, blue);
        return this;
    }

    public DimensionBuilder fogColour(BiFunction<Float, Float, Vec3d> bi){
        dim.fogColour = bi;
        return this;
    }

    public DimensionBuilder spawningBlockChunk(BiFunction<ChunkPos, Boolean, BlockPos> bi){
        dim.spawningBlockChunk = bi;
        return this;
    }

    public DimensionBuilder topSpawningBlockPos(TriFunction<Integer, Integer, Boolean, BlockPos> tri){
        dim.topSpawningBlockPos = tri;
        return this;
    }

     public DimensionBuilder worldBorder(WorldBorder b){
        dim.border = b;
        return this;
     }

     public DimensionBuilder forcedSpawnPoint(BlockPos pos){
        dim.forcedSpawn = pos;
        return this;
     }

    public DimensionBuilder chunkGenerator(Function<World, ChunkGenerator<? extends ChunkGeneratorConfig>> generator){
        dim.chunkGenerator = generator;
        return this;
    }

    public Dimension build(World w, DimensionType t){
        return new TesseractDimension(w, t, dim);
    }
}
