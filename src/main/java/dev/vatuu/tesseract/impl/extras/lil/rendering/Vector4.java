package dev.vatuu.tesseract.impl.extras.lil.rendering;

import net.minecraft.util.math.Vec3d;

public class Vector4 {

    public float x, y, z, w;

    //Z-Axis Rotation

    public Vector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

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

    public Vector4 rotateX(float angle) {
        float[][] rotateYZ = new float[][]{
                {1, 0, 0, 0},
                {0, (float) Math.cos(angle), (float) -Math.sin(angle), 0},
                {0, (float) Math.sin(angle), (float) Math.cos(angle), 0},
                {0, 0, 0, 1}
        };

        return matrixMultiplication4x4(rotateYZ);
    }

    public Vector4 rotateY(float angle) {
        float[][] rotateXZ = new float[][]{
                {(float) Math.cos(angle), 0, (float) -Math.sin(angle), 0},
                {0, 1, 0, 0},
                {(float) Math.sin(angle), 0, (float) Math.cos(angle), 0},
                {0, 0, 0, 1}
        };

        return matrixMultiplication4x4(rotateXZ);
    }

    public Vector4 rotateZ(float angle) {
        float[][] rotateXY = new float[][]{
                {(float) Math.cos(angle), (float) -Math.sin(angle), 0, 0},
                {(float) Math.sin(angle), (float) Math.cos(angle), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };

        return matrixMultiplication4x4(rotateXY);
    }

    public Vector4 rotateW(float angle) {
        float[][] rotateZW = new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, (float) Math.cos(angle), (float) -Math.sin(angle)},
                {0, 0, (float) Math.sin(angle), (float) Math.cos(angle)},
        };

        return matrixMultiplication4x4(rotateZW);
    }

    public Vector4 matrixMultiplication4x4(float[][] mat) {
        float xn = mat[0][0] * x + mat[1][0] * y + mat[2][0] * z + mat[3][0] * w;
        float yn = mat[0][1] * x + mat[1][1] * y + mat[2][1] * z + mat[3][1] * w;
        float zn = mat[0][2] * x + mat[1][2] * y + mat[2][2] * z + mat[3][2] * w;
        float wn = mat[0][3] * x + mat[1][3] * y + mat[2][3] * z + mat[3][3] * w;

        return new Vector4(xn, yn, zn, wn);
    }
}