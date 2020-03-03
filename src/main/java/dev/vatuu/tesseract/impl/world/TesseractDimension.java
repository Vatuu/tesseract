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

    TesseractDimension(World world, DimensionType type, DimensionSettings settings){
        super(world, type);
        this.type = type;
        this.settings = settings;
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
    @Override public BlockPos getSpawningBlockInChunk(ChunkPos pos, boolean checkMobSpawnValidity) { return settings.spawningBlockChunk.apply(pos, checkMobSpawnValidity); }
    @Override public BlockPos getTopSpawningBlockPosition(int x, int z, boolean checkMobSpawnValidity) { return settings.topSpawningBlockPos.apply(x, z, checkMobSpawnValidity); }
    @Override public boolean hasVisibleSky() { return settings.hasVisibleSky; }
    @Override public Vec3d modifyFogColor(Vec3d pos,  float tickDelta) { return settings.fogColour.apply(pos, tickDelta); }
    @Override public boolean canPlayersSleep() { return !settings.shouldBedsExplode; }
    @Override public boolean isFogThick(int x, int iz) { return settings.isFogThick.apply(x, iz); }
    @Override public DimensionType getType() { return type; }

    public DimensionState getSaveState() { return saveState; }
    public void setSaveState(DimensionState state) { saveState = state; }

    public DimensionSettings getSettings() { return settings; }
}
