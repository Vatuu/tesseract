package dev.vatuu.tesseract.client.rendering;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

public class TesseractRenderer {

    private float xAngle, yAngle, zAngle, wAngle;

    private Vec4f[] vertices = new Vec4f[] {
            new Vec4f(-1, -1, -1, 1),
            new Vec4f(1, -1, -1, 1),
            new Vec4f(1, 1, -1, 1),
            new Vec4f(-1, 1, -1, 1),
            new Vec4f(-1, -1, 1, 1),
            new Vec4f(1, -1, 1, 1),
            new Vec4f(1, 1, 1, 1),
            new Vec4f(-1, 1, 1, 1),

            new Vec4f(-1, -1, -1, -1),
            new Vec4f(1, -1, -1, -1),
            new Vec4f(1, 1, -1, -1),
            new Vec4f(-1, 1, -1, -1),
            new Vec4f(-1, -1, 1, -1),
            new Vec4f(1, -1, 1, -1),
            new Vec4f(1, 1, 1, -1),
            new Vec4f(-1, 1, 1, -1),
    };

    public void render(MatrixStack stack, VertexConsumerProvider vertexConsumers, float projectionDistance){
        VertexConsumer consumer = vertexConsumers.getBuffer(TesseractRenderLayers.TESSERACT_VERTEX);

        stack.push();

        stack.translate(0.5d, 0.5d, 0.5d);
        stack.scale(.5F, .5F, .5F);
        Matrix4f model = stack.peek().getModel();

        Vec3d[] projectedPoints = new Vec3d[16];
        for (int i = 0; i < vertices.length; i++)
            projectedPoints[i] = vertices[i]
                    .rotateX(xAngle)
                    .rotateY(yAngle)
                    .rotateZ(zAngle)
                    .rotateW(wAngle)
                    .projectTo3d(projectionDistance);

        for (int i = 0; i < vertices.length; i++)
            DrawUtils.renderCube(projectedPoints[i], 0.025f, consumer, model);

        consumer = vertexConsumers.getBuffer(TesseractRenderLayers.TESSERACT_SIDE);
        for(int i = 0; i < 4; i++) {
            DrawUtils.draw2Line(projectedPoints, i, (i + 1) % 4, 8, consumer, model);
            DrawUtils.draw2Line(projectedPoints, i + 4, ((i + 1) % 4) + 4, 8, consumer, model);
            DrawUtils.draw2Line(projectedPoints, i, i + 4, 8, consumer, model);

            DrawUtils.draw2Line(projectedPoints, i, i + 8, 4, consumer, model);
        }

        stack.pop();
    }

    public void updateRotation(Vec4f rotation) {
        this.xAngle += rotation.x();
        this.yAngle += rotation.y();
        this.zAngle += rotation.z();
        this.wAngle += rotation.z();

        if(xAngle > 360)
            xAngle -= 360;
        if(xAngle < -360)
            xAngle += 360;

        if(yAngle > 360)
            yAngle -= 360;
        if(yAngle < -360)
            yAngle += 360;

        if(zAngle > 360)
            zAngle -= 360;
        if(zAngle < -360)
            zAngle += 360;

        if(wAngle > 360)
            wAngle -= 360;
        if(wAngle < -360)
            wAngle += 360;
    }
}
