package dev.vatuu.tesseract.extensions.mixins;

import dev.vatuu.tesseract.world.TesseractDimension;
import dev.vatuu.tesseract.world.TesseractDimensionType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkManager.class)
public abstract class ServerChunkManagerMixin {

    @Shadow @Final private ServerWorld world;

    @Inject(method = "save(Z)V", at = @At("HEAD"), cancellable = true)
    private void save(boolean boolean_1, CallbackInfo ci){
        if(world.dimension.getType() instanceof TesseractDimensionType){
            if(!((TesseractDimension)world.dimension).shouldSave())
                ci.cancel();
        }
    }
}
