package dev.vatuu.tesseract.client.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.io.IOException;

public interface AbstractC2SPacket extends ServerPlayNetworking.PlayChannelHandler {

    Identifier getId();

    void encode(PacketByteBuf buffer) throws IOException;
    void decode(PacketByteBuf buffer) throws IOException;

    void onReceive(MinecraftServer client, ServerPlayerEntity player, ServerPlayNetworkHandler handler);

    default void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buffer, PacketSender sender) {
        try {
            this.decode(buffer);
            server.execute(() -> this.onReceive(server, player, handler));
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}