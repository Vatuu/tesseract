package dev.vatuu.tesseract.client.rendering;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.Vec3d;

public record Vec4f(float x, float y, float z, float w) {

    public static final Vec4f ZERO = new Vec4f(0, 0, 0, 0);

    public Vec3d projectTo3d(float distance) {
        float d = 1 / (distance - w);

        float[][] matrix = new float[][] {
                {d, 0, 0},
                {0, d, 0},
                {0, 0, d},
        };

        float xn = matrix[0][0] * x + matrix[1][0] * y + matrix[2][0] * z;
        float yn = matrix[0][1] * x + matrix[1][1] * y + matrix[2][1] * z;
        float zn = matrix[0][2] * x + matrix[1][2] * y + matrix[2][2] * z;

        return new Vec3d(xn, yn, zn);
    }

    public Vec4f rotateX(float angle) {
        float[][] rotateYZ = new float[][]{
                {1, 0, 0, 0},
                {0, (float) Math.cos(angle), (float) -Math.sin(angle), 0},
                {0, (float) Math.sin(angle), (float) Math.cos(angle), 0},
                {0, 0, 0, 1}
        };

        return matrixMultiplication4x4(rotateYZ);
    }

    public Vec4f rotateY(float angle) {
        float[][] rotateXZ = new float[][]{
                {(float) Math.cos(angle), 0, (float) -Math.sin(angle), 0},
                {0, 1, 0, 0},
                {(float) Math.sin(angle), 0, (float) Math.cos(angle), 0},
                {0, 0, 0, 1}
        };

        return matrixMultiplication4x4(rotateXZ);
    }

    public Vec4f rotateZ(float angle) {
        float[][] rotateXY = new float[][]{
                {(float) Math.cos(angle), (float) -Math.sin(angle), 0, 0},
                {(float) Math.sin(angle), (float) Math.cos(angle), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };

        return matrixMultiplication4x4(rotateXY);
    }

    public Vec4f rotateW(float angle) {
        float[][] rotateZW = new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, (float) Math.cos(angle), (float) -Math.sin(angle)},
                {0, 0, (float) Math.sin(angle), (float) Math.cos(angle)},
        };

        return matrixMultiplication4x4(rotateZW);
    }

    public Vec4f matrixMultiplication4x4(float[][] mat) {
        float xn = mat[0][0] * x + mat[1][0] * y + mat[2][0] * z + mat[3][0] * w;
        float yn = mat[0][1] * x + mat[1][1] * y + mat[2][1] * z + mat[3][1] * w;
        float zn = mat[0][2] * x + mat[1][2] * y + mat[2][2] * z + mat[3][2] * w;
        float wn = mat[0][3] * x + mat[1][3] * y + mat[2][3] * z + mat[3][3] * w;

        return new Vec4f(xn, yn, zn, wn);
    }

    public static final Codec<Vec4f> CODEC = Codec.FLOAT.listOf().xmap(l -> new Vec4f(l.get(0), l.get(1), l.get(2), l.get(3)), v -> Lists.newArrayList(v.x, v.y, v.z, v.w));
}