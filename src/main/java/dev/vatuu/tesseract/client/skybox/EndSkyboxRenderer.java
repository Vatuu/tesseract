package dev.vatuu.tesseract.client.skybox;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class EndSkyboxRenderer extends SkyboxRenderer {

    private static final Identifier END_SKY = new Identifier("textures/environment/end_sky.png");

    public void renderSky(MatrixStack matrices, ClientWorld w, Camera cam, float tickDelta) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, END_SKY);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        for(int i = 0; i < 6; ++i) {
            matrices.push();

            if (i == 1) {
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
            }

            if (i == 2) {
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
            }

            if (i == 3) {
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180.0F));
            }

            if (i == 4) {
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
            }

            if (i == 5) {
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
            }

            Matrix4f matrix4f = matrices.peek().getModel();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(0.0F, 0.0F).color(40, 40, 40, 255).next();
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(0.0F, 16.0F).color(40, 40, 40, 255).next();
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(16.0F, 16.0F).color(40, 40, 40, 255).next();
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(16.0F, 0.0F).color(40, 40, 40, 255).next();
            tessellator.draw();
            matrices.pop();
        }

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
