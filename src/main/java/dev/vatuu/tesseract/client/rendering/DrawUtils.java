package dev.vatuu.tesseract.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.math.*;

import java.util.stream.Stream;

public final class DrawUtils {

    public static void draw2Line(Vec3d[] points, int i, int j, int offset, VertexConsumer consumer, Matrix4f model) {
        Vec3d p1 = points[i];
        Vec3d p2 = points[j];
        Vec3d p3 = points[i + offset];
        Vec3d p4 = points[j + offset];

        consumer.vertex(model, (float)p1.x, (float)p1.y, (float)p1.z).color(0.0f, 0.0f, 1.0f, 1.0f).next();
        consumer.vertex(model, (float)p2.x, (float)p2.y, (float)p2.z).color(0.0f, 0.0f, 1.0f, 1.0f).next();

        consumer.vertex(model, (float)p3.x, (float)p3.y, (float)p3.z).color(0.0f, 0.0f, 1.0f, 1.0f).next();
        consumer.vertex(model, (float)p4.x, (float)p4.y, (float)p4.z).color(0.0f, 0.0f, 1.0f, 1.0f).next();
    }

    public static void quadOffsetAxis(Vec3d pos, float radius, Direction dir, VertexConsumer consumer, Matrix4f model) {
        Vec3i dirVector = dir.getVector();
        Vec3d offset = pos.add(new Vec3d(dirVector.getX() * radius, dirVector.getY() * radius, dirVector.getZ() * radius));
        Vec3d[] points = new Vec3d[4];
        switch(dir.getAxis()) {
            case X:
                points[0] = offset.add(new Vec3d(0, radius, radius));
                points[1] = offset.add(new Vec3d(0, -radius, radius));
                points[2] = offset.add(new Vec3d(0, -radius, -radius));
                points[3] = offset.add(new Vec3d(0, radius, -radius));
                break;
            case Y:
                points[0] = offset.add(new Vec3d(radius, 0, radius));
                points[1] = offset.add(new Vec3d(radius, 0, -radius));
                points[2] = offset.add(new Vec3d(-radius, 0, -radius));
                points[3] = offset.add(new Vec3d(-radius, 0, radius));
                break;
            case Z:
                points[0] = offset.add(new Vec3d(radius, radius, 0));
                points[1] = offset.add(new Vec3d(-radius, radius, 0));
                points[2] = offset.add(new Vec3d(-radius, -radius, 0));
                points[3] = offset.add(new Vec3d(radius, -radius, 0));
                break;
        }

        if(dir.getDirection().offset() < 0) {
            Vec3d t = points[1];
            points[1] = points[3];
            points[3] = t;
        }

        for (Vec3d point : points)
            consumer.vertex(model, (float) point.x, (float) point.y, (float) point.z).color(0.0f, 0.0f, 1.0f, 1f).next();
    }

    public static void renderCube(Vec3d pos, float radius, VertexConsumer consumer, Matrix4f model) {
        Stream.of(Direction.values()).forEach(dir -> DrawUtils.quadOffsetAxis(pos, radius, dir, consumer, model));
    }

    public static void drawLineStrip(Vec3d pos, Vec4f colour, float lineWidth, VertexConsumer consumer, Camera cam, Matrix3f normals) {
        Vec3f normal = Vec3f.ZERO;

        RenderSystem.lineWidth(lineWidth);
        consumer.vertex(pos.getX(), pos.getY(), pos.getZ())
                .color(colour.x(), colour.y(), colour.z(), colour.w())
                .normal(normals, normal.getX(), normal.getY(), normal.getZ())
                .next();
    }
}
