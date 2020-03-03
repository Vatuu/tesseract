package dev.vatuu.tesseract.impl.extensions.mixins;

import com.sun.org.apache.bcel.internal.generic.ARETURN;
import dev.vatuu.tesseract.impl.extras.fancy.TesseractCapeFeatureRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRenderDispatcher dispatcher, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowSize) {
        super(dispatcher, model, shadowSize);
    }

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Z)V")
    private void constructor(EntityRenderDispatcher entityRenderDispatcher, boolean bl, CallbackInfo ci) {
        this.addFeature(new TesseractCapeFeatureRenderer(this));
    }
}
