package dev.vatuu.tesseract.network;

import dev.vatuu.tesseract.client.network.AbstractC2SPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;

public final class NetworkHandler {

    public NetworkHandler() { }

    public void sendToClient(ServerPlayerEntity entity, AbstractS2CPacket packet) {
        try {
            PacketByteBuf buffer = PacketByteBufs.create();
            packet.encode(buffer);
            ServerPlayNetworking.send(entity, packet.getId(), buffer);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    private void register(AbstractC2SPacket packet) {
        ServerPlayNetworking.registerGlobalReceiver(packet.getId(), (s, p, h, b, sender) -> packet.onReceive(s, p, h));
    }
}