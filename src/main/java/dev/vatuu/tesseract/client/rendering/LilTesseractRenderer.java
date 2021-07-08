package dev.vatuu.tesseract.client.rendering;

import dev.vatuu.tesseract.lil.LilTesseractBlockEntity;
import dev.vatuu.tesseract.lil.LilTesseractSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class LilTesseractRenderer extends TesseractRenderer implements BlockEntityRenderer<LilTesseractBlockEntity> {

    @Override
    public void render(LilTesseractBlockEntity blockEntity, float tickDelta, MatrixStack stack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        this.render(stack, vertexConsumers, 3);

        LilTesseractSettings settings = blockEntity.settings;
        this.updateRotation(settings.rotations);
    }
}
