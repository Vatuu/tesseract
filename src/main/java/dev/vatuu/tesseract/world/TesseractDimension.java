package dev.vatuu.tesseract.world;

import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSourceType;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.chunk.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class TesseractDimension extends OverworldDimension {

    private DimensionType type;
    private DimensionState shouldSave;

    public BlockPos entryPoint;

    public TesseractDimension(World w, DimensionType t, BlockPos pos){
        super(w, t);
        this.type = t;
        this.shouldSave = DimensionState.SAVE;
        this.entryPoint = pos;
    }

    public DimensionType getType() {
        return type;
    }

    @Override
    public ChunkGenerator<? extends ChunkGeneratorConfig> createChunkGenerator() {
        FlatChunkGeneratorConfig config = ChunkGeneratorType.FLAT.createSettings();
        config.getLayers().add(new FlatChunkGeneratorLayer(1, Blocks.AIR));
        config.setBiome(Biomes.THE_VOID);
        config.updateLayerBlocks();
        FixedBiomeSourceConfig biomeConfig = BiomeSourceType.FIXED.getConfig().setBiome(Biomes.THE_VOID);

        return ChunkGeneratorType.FLAT.create(this.world, BiomeSourceType.FIXED.applyConfig(biomeConfig), config);
    }

    @Override
    public BlockPos getTopSpawningBlockPosition(int int_1, int int_2, boolean boolean_1) {
        return null;
    }

    @Override
    public BlockPos getSpawningBlockInChunk(ChunkPos chunkPos_1, boolean boolean_1) {
        return null;
    }

    @Override
    public boolean canPlayersSleep() {
        return false;
    }


    public boolean shouldSave() {
        return shouldSave == DimensionState.SAVE;
    }

    public boolean shouldUnregister() {
        return shouldSave == DimensionState.UNLOAD_UNREGISTER;
    }

    public void setSaveState(boolean shouldSave, boolean unregister){
        this.shouldSave = shouldSave ? DimensionState.SAVE : (unregister ? DimensionState.UNLOAD_UNREGISTER : DimensionState.UNLOAD);
    }

    private enum DimensionState {
        SAVE,
        UNLOAD,
        UNLOAD_UNREGISTER
    }
}
