package dev.vatuu.tesseract.api.extensions;

import net.minecraft.util.math.BlockPos;

@FunctionalInterface
public interface SpawnTopFunction{
    BlockPos apply(int x, int z, boolean checkMobSpawnValidity);
}
