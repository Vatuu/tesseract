package dev.vatuu.tesseract.client.extensions.mixins;

import dev.vatuu.tesseract.client.rendering.TesseractCapeFeatureRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context context, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowSize) {
        super(context, model, shadowSize);
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    private void constructor(EntityRendererFactory.Context context, boolean bl, CallbackInfo ci) {
        this.addFeature(new TesseractCapeFeatureRenderer(this));
    }
}
