package dev.vatuu.tesseract.impl.extensions;

import net.minecraft.util.math.BlockPos;

@FunctionalInterface
public interface SpawnTopFunction{
    BlockPos apply(int i1, int i2, boolean b);
}
