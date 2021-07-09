package dev.teamtesseract.fractal.client.skybox;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.teamtesseract.fractal.Fractal;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class MissingSkyboxRenderer extends SkyboxRenderer {

    @Override
    public void renderSky(MatrixStack stack, ClientWorld world, Camera cam, float tickDelta) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, Fractal.id("missing_texture"));

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        for(int i = 0; i < 6; ++i) {
            stack.push();
            switch(i) {
                case 1 -> stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
                case 2 -> stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
                case 3 -> stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(180.0F));
                case 4 -> stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
                case 5 -> stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
            }

            Matrix4f matrix4f = stack.peek().getModel();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(0.0F, 0.0F).next();
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(0.0F, 16.0F).next();
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(16.0F, 16.0F).next();
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(16.0F, 0.0F).next();
            tessellator.draw();

            stack.pop();
        }

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
