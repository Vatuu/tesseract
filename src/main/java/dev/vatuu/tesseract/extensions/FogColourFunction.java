package dev.vatuu.tesseract.extensions;

import net.minecraft.util.math.Vec3d;

public interface FogColourFunction {
    Vec3d apply(float f1, float f2);
}
