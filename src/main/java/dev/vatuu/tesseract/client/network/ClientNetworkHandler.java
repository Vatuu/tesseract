package dev.vatuu.tesseract.client.network;

import dev.vatuu.tesseract.network.AbstractS2CPacket;
import dev.vatuu.tesseract.network.PacketS2CSyncDimensionTypes;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

import java.io.IOException;

public class ClientNetworkHandler {

    public ClientNetworkHandler() {
        register(new PacketS2CSyncDimensionTypes());
    }

    public void sendToServer(AbstractC2SPacket packet) {
        try {
            PacketByteBuf buffer = PacketByteBufs.create();
            packet.encode(buffer);
            ClientPlayNetworking.send(packet.getId(), buffer);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    private void register(AbstractS2CPacket packet) {
        ClientPlayNetworking.registerGlobalReceiver(packet.getId(), packet);
    }
}
