package dev.vatuu.tesseract.cmd;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.network.PacketS2CSyncDimensionTypes;
import dev.vatuu.tesseract.registry.TesseractRegistry;
import dev.vatuu.tesseract.registry.TesseractRegistryException;
import dev.vatuu.tesseract.world.DimensionState;
import dev.vatuu.tesseract.world.DimensionTypeBuilder;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

public class CreateTestWorldCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("registerTest")
                    .requires(src -> src.hasPermissionLevel(4))
                    .executes(CreateTestWorldCommand::activate)
        );
    }

    private static int activate(CommandContext<ServerCommandSource> src) {
        try {
            RegistryKey<DimensionType> telesis = DimensionTypeBuilder.create(Tesseract.id("telesis"))
                    .doBedsExplode(true)
                    .doBeesExplode(true)
                    .hasSkylight(true)
                    .isNatural(false)
                    .setAmbientLightLevel(11)
                    .setBlockHeight(256)
                    .setLogicalHeight(256)
                    .setMinimumY(0)
                    .setSkyProperties(Tesseract.id("telesis_sky"))
                    .register();

            TesseractRegistry.getInstance().createWorld(telesis, Tesseract.id("telesis"), DimensionState.RESET);

            PacketS2CSyncDimensionTypes packet = new PacketS2CSyncDimensionTypes(
                    src.getSource().getMinecraftServer().getPlayerManager().registryManager,
                    Lists.newArrayList(src.getSource().getMinecraftServer().getWorldRegistryKeys()));
            PlayerLookup.all(src.getSource().getMinecraftServer()).forEach(p -> Tesseract.INSTANCE.getNetworkHandler().sendToClient(p, packet));

        } catch(TesseractRegistryException e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }
}
