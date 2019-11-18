package dev.vatuu.tesseract.impl.world;

import dev.vatuu.tesseract.api.DimensionState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.chunk.*;

public class TesseractDimension extends OverworldDimension {

    private DimensionType type;
    private DimensionState saveState;
    private DimensionSettings settings;

    public BlockPos entryPoint;

    TesseractDimension(World w, DimensionType t, DimensionSettings s){
        super(w, t);
        this.type = t;
        this.settings = s;
        this.saveState = DimensionState.SAVE;
        this.entryPoint = new BlockPos(0, 64, 0);
    }

    @Override public float getCloudHeight() { return settings.cloudHeight; }
    @Override public BlockPos getForcedSpawnPoint() { return settings.forcedSpawn; }
    @Override public boolean doesWaterVaporize() { return settings.vaporizeWater; }
    @Override public boolean hasSkyLight() { return type.hasSkyLight(); }
    @Override public boolean isNether() { return false; }
    @Override public WorldBorder createWorldBorder() { return settings.border; }
    @Override public ChunkGenerator<? extends ChunkGeneratorConfig> createChunkGenerator() { return settings.chunkGenerator.apply(this.world); }
    @Override public BlockPos getSpawningBlockInChunk(ChunkPos pos, boolean b) { return settings.spawningBlockChunk.apply(pos, b); }
    @Override public BlockPos getTopSpawningBlockPosition(int i1, int i2, boolean b) { return settings.topSpawningBlockPos.apply(i1, i2, b); }
    @Override public boolean hasVisibleSky() { return settings.shouldRenderSkybox; }
    @Override public Vec3d getFogColor(float f1, float f2) { return settings.fogColour.apply(f1, f2); }
    @Override public boolean canPlayersSleep() { return !settings.shouldBedsExplode; }
    @Override public boolean isFogThick(int i1, int i2) { return settings.isFogThick.apply(i1, i2); }
    @Override public DimensionType getType() { return type; }

    public DimensionState getSaveState() { return saveState; }
    public void setSaveState(DimensionState state) { saveState = state; }
}
