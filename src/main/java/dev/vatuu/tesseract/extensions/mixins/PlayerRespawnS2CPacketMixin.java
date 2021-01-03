package dev.vatuu.tesseract.extensions.mixins;

import dev.vatuu.tesseract.extensions.SkyboxInjectExt;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRespawnS2CPacket.class)
public abstract class PlayerRespawnS2CPacketMixin implements SkyboxInjectExt {


    @Unique
    private Identifier skyboxIdentifier;

    @Inject(method = "write", at = @At("RETURN"))
    private void write(PacketByteBuf buf, CallbackInfo info) {
        if(hasSkybox()) {
            buf.writeBoolean(true);
            buf.writeIdentifier(skyboxIdentifier);
        } else
            buf.writeBoolean(false);
    }

    @Inject(method = "read", at = @At("RETURN"))
    private void read(PacketByteBuf buf, CallbackInfo info) {
        boolean hasSkybox = buf.readBoolean();
        if(hasSkybox)
            this.skyboxIdentifier = buf.readIdentifier();
        else
            this.skyboxIdentifier = null;
    }

    @Override
    public boolean hasSkybox() {
        return skyboxIdentifier != null;
    }

    @Override
    public Identifier getSkyboxIdentifier() {
        return skyboxIdentifier;
    }

    @Override
    public void setSkyboxIdentifier(Identifier id) {
        this.skyboxIdentifier = id;
    }
}
