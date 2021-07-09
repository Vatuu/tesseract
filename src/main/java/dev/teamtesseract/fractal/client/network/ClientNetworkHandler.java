package dev.teamtesseract.fractal.client.network;

import dev.teamtesseract.fractal.network.AbstractS2CPacket;
import dev.teamtesseract.fractal.network.PacketS2CSyncDimensionTypes;
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
