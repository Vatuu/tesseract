package dev.vatuu.tesseract.interfaces;

import net.minecraft.util.math.Vec3d;

@FunctionalInterface
public interface FogColourFunction {
    Vec3d apply(Vec3d color, float sunHeight);
}
