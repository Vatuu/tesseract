package dev.vatuu.tesseract.cmd;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.registry.TesseractRegistry;
import dev.vatuu.tesseract.registry.TesseractRegistryException;
import dev.vatuu.tesseract.world.DimensionState;
import dev.vatuu.tesseract.world.DimensionTypeBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class CreateTestWorldCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("registerTest")
                    .requires(src -> src.hasPermissionLevel(4))
                    .executes(CreateTestWorldCommand::activate)
        );
    }

    private static int activate(CommandContext<ServerCommandSource> src) throws CommandSyntaxException {
        try {
            RegistryKey<DimensionType> telesis = DimensionTypeBuilder.create(Tesseract.id("telesis"))
                    .doBedsExplode(true)
                    .doBeesExplode(true)
                    .hasSkylight(false)
                    .isNatural(false)
                    .setAmbientLightLevel(0)
                    .setBlockHeight(256)
                    .setLogicalHeight(256)
                    .setMinimumY(0)
                    .setSkyProperties(Tesseract.id("test"))
                    .setFixedTime(0)
                    .register();

            RegistryKey<World> world = TesseractRegistry.getInstance().createWorld(telesis, Tesseract.id("telesis"), DimensionState.RESET);

            src.getSource().getPlayer().teleport(src.getSource().getMinecraftServer().getWorld(world), 0, 64, 0, 0, 0);

        } catch(TesseractRegistryException e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }
}
