package dev.vatuu.tesseract.extensions.mixins;

import dev.vatuu.tesseract.extensions.SkyboxInjectExt;
import dev.vatuu.tesseract.registry.TesseractRegistry;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @ModifyArg(method = "onPlayerConnect", at = @At( value = "INVOKE", target = "net/minecraft/server/network/ServerPlayNetworkHandler.sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 0))
    private Packet<?> injectSkyPropertiesJoin(Packet<?> packet) {
        ((SkyboxInjectExt)packet).setSkyboxIdentifier(TesseractRegistry.getInstance().getSkyProperties(((GameJoinS2CPacket)packet).getDimensionType()));
        return packet;
    }

    @ModifyArg(method = "respawnPlayer", at = @At( value = "INVOKE", target = "net/minecraft/server/network/ServerPlayNetworkHandler.sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 1))
    private Packet<?> injectSkyPropertiesRespawn(Packet<?> packet) {
        ((SkyboxInjectExt)packet).setSkyboxIdentifier(TesseractRegistry.getInstance().getSkyProperties(((PlayerRespawnS2CPacket)packet).method_29445()));
        return packet;
    }
}
