package dev.vatuu.tesseract.extensions.mixins;

import dev.vatuu.tesseract.extensions.GameJoinS2CPacketExt;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameJoinS2CPacket.class)
public abstract class GameJoinS2CPacketMixin implements GameJoinS2CPacketExt {

    @Unique private Identifier skyboxIdentifier;

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

    @Unique
    public boolean hasSkybox() {
        return skyboxIdentifier != null;
    }

    @Unique
    public Identifier getSkyboxIdentifier() {
        return skyboxIdentifier;
    }

    @Override
    public void setSkyboxIdentifier(Identifier id) {
        this.skyboxIdentifier = id;
    }
}
