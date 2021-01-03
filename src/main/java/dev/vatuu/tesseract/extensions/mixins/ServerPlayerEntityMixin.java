package dev.vatuu.tesseract.extensions.mixins;

import com.mojang.authlib.GameProfile;
import dev.vatuu.tesseract.extensions.SkyboxInjectExt;
import dev.vatuu.tesseract.registry.TesseractRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ScreenHandlerListener {

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @ModifyArg(method = "moveToWorld", at = @At( value = "INVOKE", target = "net/minecraft/server/network/ServerPlayNetworkHandler.sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 0))
    private Packet<?> injectSkyPropertiesMoveToWorld(Packet<?> packet) {
        ((SkyboxInjectExt)packet).setSkyboxIdentifier(TesseractRegistry.getInstance().getSkyProperties(((PlayerRespawnS2CPacket)packet).method_29445()));
        return packet;
    }

    @ModifyArg(method = "teleport", at = @At( value = "INVOKE", target = "net/minecraft/server/network/ServerPlayNetworkHandler.sendPacket(Lnet/minecraft/network/Packet;)V", ordinal = 0))
    private Packet<?> injectSkyPropertiesTeleport(Packet<?> packet) {
        ((SkyboxInjectExt)packet).setSkyboxIdentifier(TesseractRegistry.getInstance().getSkyProperties(((PlayerRespawnS2CPacket)packet).method_29445()));
        return packet;
    }
}
