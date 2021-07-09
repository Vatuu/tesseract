package dev.teamtesseract.fractal.client.rendering;

import dev.teamtesseract.fractal.lil.LilTesseractBlockEntity;
import dev.teamtesseract.fractal.lil.LilTesseractSettings;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class LilTesseractRenderer extends TesseractRenderer implements BlockEntityRenderer<LilTesseractBlockEntity> {

    @Override
    public void render(LilTesseractBlockEntity blockEntity, float tickDelta, MatrixStack stack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        this.render(stack, vertexConsumers, 3);

        LilTesseractSettings settings = blockEntity.settings;
        this.updateRotation(settings.rotations);
    }
}
