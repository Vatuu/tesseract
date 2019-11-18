package dev.vatuu.tesseract.impl.extensions.mixins;

import dev.vatuu.tesseract.impl.world.TesseractDimension;
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
    private void save(boolean flush, CallbackInfo ci){
        if(world.dimension instanceof TesseractDimension){
            if(((TesseractDimension)world.dimension).getSaveState().shouldReset())
                ci.cancel();
        }
    }
}
