package dev.vatuu.tesseract.impl.extensions.mixins;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(EndPortalBlockEntityRenderer.class)
public interface EndPortalBlockEntityRendererAccessor {

    @Accessor(value = "field_21732")
    static List<RenderLayer> getEndEffectRenderLayers() {
        throw new IllegalStateException("Have you heard about Tessentials?");
    }
}
