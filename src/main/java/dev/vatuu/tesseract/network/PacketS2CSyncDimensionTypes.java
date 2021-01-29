package dev.vatuu.tesseract.network;

import com.google.common.collect.Sets;
import dev.vatuu.tesseract.Tesseract;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PacketS2CSyncDimensionTypes implements AbstractS2CPacket {

    private static final Identifier ID = Tesseract.id("sync_dimensions");

    private DynamicRegistryManager.Impl registry;
    private List<RegistryKey<World>> worlds;

    public PacketS2CSyncDimensionTypes() {}
    public PacketS2CSyncDimensionTypes(DynamicRegistryManager.Impl registry, List<RegistryKey<World>> worlds) {
        this.registry = registry;
        this.worlds = worlds;
    }

    @Override
    public Identifier getId() { return ID; }

    public void encode(PacketByteBuf buffer) throws IOException{
        buffer.writeVarInt(worlds.size());
        worlds.forEach(k -> buffer.writeIdentifier(k.getValue()));
        buffer.encode(DynamicRegistryManager.Impl.CODEC, this.registry);
    }

    public void decode(PacketByteBuf buffer) throws IOException {
        this.worlds = new ArrayList<>();

        int size = buffer.readVarInt();
        for(int i = 0; i < size; i++)
            this.worlds.add(RegistryKey.of(Registry.DIMENSION, buffer.readIdentifier()));
        this.registry = buffer.decode(DynamicRegistryManager.Impl.CODEC);
    }

    @Override
    public void onReceive(MinecraftClient client, ClientPlayNetworkHandler handler) {
        handler.registryManager = this.registry;
        handler.worldKeys = Sets.newLinkedHashSet(this.worlds);
        Collections.shuffle(worlds);
    }
}
