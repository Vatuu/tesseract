package dev.vatuu.tesseract.extensions;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public interface SpawnChunkPosFunction {
    BlockPos apply(ChunkPos pos, boolean b);
}
