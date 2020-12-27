package dev.vatuu.tesseract.extras.lil;

import dev.vatuu.tesseract.extras.lil.rendering.TesseractRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class LilTesseractRenderer implements BlockEntityRenderer<LilTesseractBlockEntity> {

    private TesseractRenderer renderer;

    public LilTesseractRenderer() {
        this.renderer = new TesseractRenderer(new TesseractSettings());
    }

    @Override
    public void render(LilTesseractBlockEntity blockEntity, float tickDelta, MatrixStack modelview, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Vec3d pos = new Vec3d(blockEntity.getPos().getX(), blockEntity.getPos().getY(), blockEntity.getPos().getZ());
        float distance = (float)Math.sqrt(MinecraftClient.getInstance().gameRenderer.getCamera().getPos().squaredDistanceTo(pos));

        renderer.render(modelview, vertexConsumers, distance);
    }
}
