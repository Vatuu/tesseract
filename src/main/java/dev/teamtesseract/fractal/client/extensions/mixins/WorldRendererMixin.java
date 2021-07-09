package dev.teamtesseract.fractal.client.extensions.mixins;

import dev.teamtesseract.fractal.client.world.FractalSkyProperties;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Ljava/lang/Runnable;run()V", shift = At.Shift.AFTER), cancellable = true)
    public void renderFractalSkybox(MatrixStack stack, Matrix4f matrix4f, float delta, Runnable runnable, CallbackInfo info) {
        ClientWorld w = this.client.world;
        if(w.getSkyProperties() instanceof FractalSkyProperties) {
            FractalSkyProperties sky = (FractalSkyProperties) w.getSkyProperties();
            sky.getSkybox().renderSky(stack, w, client.gameRenderer.getCamera(), delta);
            info.cancel();
        }
    }
}
