package dev.vatuu.tesseract.impl.world;

import dev.vatuu.tesseract.api.DimensionFactory;
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

public class DimensionFactoryImpl implements DimensionFactory {

    private final DimensionSettings dim = new DimensionSettings();

    public DimensionFactory bedsExplode(boolean shouldExplode){
        dim.shouldBedsExplode = shouldExplode;
        return this;
    }

    public DimensionFactory visibleSky(boolean hasVisibleSky){
        dim.hasVisibleSky = hasVisibleSky;
        return this;
    }

    public DimensionFactory vaporizeWater(boolean vaporizeWater){
        dim.vaporizeWater = vaporizeWater;
        return this;
    }

    public DimensionFactory cloudHeight(float cloudHeight){
        dim.cloudHeight = cloudHeight;
        return this;
    }

    public DimensionFactory renderFog(boolean shouldRenderFog){
        dim.isFogThick = (i1, i2) -> shouldRenderFog;
        return this;
    }

    public DimensionFactory renderFog(RenderFogFunction bi){
        dim.isFogThick = bi;
        return this;
    }

    public DimensionFactory fogColour(int red, int green, int blue){
        dim.fogColour = (f1, f2) -> Tesseract.getRgbColour(red, green, blue);
        return this;
    }

    public DimensionFactory fogColour(FogColourFunction bi){
        dim.fogColour = bi;
        return this;
    }

    public DimensionFactory spawningBlockChunk(SpawnChunkPosFunction bi){
        dim.spawningBlockChunk = bi;
        return this;
    }

    public DimensionFactory topSpawningBlockPos(SpawnTopFunction tri){
        dim.topSpawningBlockPos = tri;
        return this;
    }

     public DimensionFactory worldBorder(WorldBorder b){
        dim.border = b;
        return this;
     }

     public DimensionFactory beesExplode(boolean b){
        dim.beesExplode = b;
        return this;
     }

     public DimensionFactory forcedSpawnPoint(BlockPos pos){
        dim.forcedSpawn = pos;
        return this;
     }

     public DimensionFactory forcedSpawnPoint(int x, int y, int z) {
        dim.forcedSpawn = new BlockPos(x, y, z);
        return this;
     }

    public DimensionFactory chunkGenerator(Function<World, ChunkGenerator<? extends ChunkGeneratorConfig>> generator){
        dim.chunkGenerator = generator;
        return this;
    }

    public Dimension build(World w, DimensionType t){
        return new TesseractDimension(w, t, dim);
    }
}
