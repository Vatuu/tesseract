package dev.vatuu.tesseract.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.io.IOException;

public interface AbstractS2CPacket extends ClientPlayNetworking.PlayChannelHandler {

    Identifier getId();

    void encode(PacketByteBuf buffer) throws IOException;
    void decode(PacketByteBuf buffer) throws IOException;

    void onReceive(MinecraftClient client, ClientPlayNetworkHandler handler);

    default void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buffer, PacketSender sender) {
        try {
            this.decode(buffer);
            client.execute(() -> this.onReceive(client, handler));
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}