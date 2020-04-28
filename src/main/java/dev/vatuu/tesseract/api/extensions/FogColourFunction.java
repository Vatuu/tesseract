package dev.vatuu.tesseract.api.extensions;

import net.minecraft.util.math.Vec3d;

public interface FogColourFunction {
    Vec3d apply(float skyAngle, float tickDelta);
}
