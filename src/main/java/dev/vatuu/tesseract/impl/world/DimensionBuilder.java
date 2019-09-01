package dev.vatuu.tesseract.impl.world;

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

public class DimensionBuilder implements dev.vatuu.tesseract.api.DimensionBuilder {

    private DimensionSettings dim = new DimensionSettings();

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

    public DimensionBuilder renderFog(RenderFogFunction bi){
        dim.shouldRenderFog = bi;
        return this;
    }

    public DimensionBuilder fogColour(int red, int green, int blue){
        dim.fogColour = (f1, f2) -> Tesseract.getRgbColour(red, green, blue);
        return this;
    }

    public DimensionBuilder fogColour(FogColourFunction bi){
        dim.fogColour = bi;
        return this;
    }

    public DimensionBuilder spawningBlockChunk(SpawnChunkPosFunction bi){
        dim.spawningBlockChunk = bi;
        return this;
    }

    public DimensionBuilder topSpawningBlockPos(SpawnTopFunction tri){
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
