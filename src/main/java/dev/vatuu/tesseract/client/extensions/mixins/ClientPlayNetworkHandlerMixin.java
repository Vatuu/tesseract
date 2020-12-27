package dev.vatuu.tesseract.client.extensions.mixins;

import dev.vatuu.tesseract.client.extensions.ClientWorldExtension;
import dev.vatuu.tesseract.extensions.GameJoinS2CPacketExt;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow private ClientWorld world;

    @Inject(method = "onGameJoin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;joinWorld(Lnet/minecraft/client/world/ClientWorld;)V", shift = At.Shift.BEFORE))
    private void handleSkyProperties(GameJoinS2CPacket packet, CallbackInfo info) {
        if(((GameJoinS2CPacketExt)packet).hasSkybox())
            ((ClientWorldExtension)this.world).setSkybox(((GameJoinS2CPacketExt)packet).getSkyboxIdentifier());
    }
}
