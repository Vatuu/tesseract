package dev.vatuu.tesseract.extras.lil.rendering;

import dev.vatuu.tesseract.extras.lil.TesseractSettings;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

public class TesseractRenderer {

    public TesseractSettings settings;

    private float xAngle, yAngle, zAngle, wAngle;

    private Vector4[] vertices = new Vector4[] {
            new Vector4(-1, -1, -1, 1),
            new Vector4(1, -1, -1, 1),
            new Vector4(1, 1, -1, 1),
            new Vector4(-1, 1, -1, 1),
            new Vector4(-1, -1, 1, 1),
            new Vector4(1, -1, 1, 1),
            new Vector4(1, 1, 1, 1),
            new Vector4(-1, 1, 1, 1),

            new Vector4(-1, -1, -1, -1),
            new Vector4(1, -1, -1, -1),
            new Vector4(1, 1, -1, -1),
            new Vector4(-1, 1, -1, -1),
            new Vector4(-1, -1, 1, -1),
            new Vector4(1, -1, 1, -1),
            new Vector4(1, 1, 1, -1),
            new Vector4(-1, 1, 1, -1),
    };

    public TesseractRenderer(TesseractSettings settings) {
        this.settings = settings;
    }

    public void render(MatrixStack modelview, VertexConsumerProvider vertexConsumers, float projectionDistance){
        VertexConsumer consumer = vertexConsumers.getBuffer(TesseractRenderLayers.TESSERACT_VERTEX);

        modelview.push();
        modelview.translate(0.5d, 0.5d, 0.5d);
        Matrix4f model = modelview.peek().getModel();

        Vec3d projectedPoints[] = new Vec3d[16];
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
        modelview.pop();
        updateRotation();
    }

    private void updateRotation() {
        this.xAngle += settings.rotateX;
        this.yAngle += settings.rotateY;
        this.zAngle += settings.rotateZ;
        this.wAngle += settings.rotateW;

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
