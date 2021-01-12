package dev.vatuu.tesseract.client.extensions.mixins;

import dev.vatuu.tesseract.client.SkyPropertiesRegistry;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkyProperties.class)
public abstract class SkyPropertiesMixin {

    @Shadow @Final private static Object2ObjectMap<Identifier, SkyProperties> BY_IDENTIFIER;

    @Inject(method = "byDimensionType", at = @At("HEAD"), cancellable = true)
    private static void tesseractSkyboxes(DimensionType type, CallbackInfoReturnable<SkyProperties> info) {
        Identifier id = type.getSkyProperties();
        if(!BY_IDENTIFIER.containsKey(id))
            info.setReturnValue(SkyPropertiesRegistry.REGISTRY.get(id));
    }
}
