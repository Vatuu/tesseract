package dev.vatuu.tesseract.extensions.mixins;

import dev.vatuu.tesseract.extensions.GameJoinS2CPacketExt;
import dev.vatuu.tesseract.registry.TesseractRegistry;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerManager.class)
public abstract class PlayerHandlerMixin {

    @ModifyArg(method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At( value = "INVOKE", target = "net/minecraft/server/network/ServerPlayNetworkHandler.sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 0))
    private GameJoinS2CPacket injectSkyProperties( GameJoinS2CPacket packet ) {
        ((GameJoinS2CPacketExt)packet).setSkyboxIdentifier(TesseractRegistry.getInstance().getSkyProperties(packet.getDimensionType()));
        return packet;
    }
}
