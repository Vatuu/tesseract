package dev.vatuu.tesseract.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.vatuu.tesseract.registry.TesseractRegistry;
import dev.vatuu.tesseract.registry.TesseractRegistryException;
import dev.vatuu.tesseract.world.DimensionState;
import net.minecraft.command.CommandException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.apache.commons.lang3.concurrent.ConcurrentException;

import java.io.File;
import java.util.ConcurrentModificationException;

public class RegenerateWorldCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("regenerateTest")
                        .requires(src -> src.hasPermissionLevel(4))
                        .executes(RegenerateWorldCommand::apply)
        );
    }

    private static int apply(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        try {
            TesseractRegistry.getInstance().destroyWorld(CreateTestWorldCommand.DIMENSION_WORLD);
            TesseractRegistry.getInstance().resetWorld(CreateTestWorldCommand.DIMENSION_WORLD);

            CreateTestWorldCommand.DIMENSION_WORLD = TesseractRegistry.getInstance().createWorld(
                    CreateTestWorldCommand.DIMENSION_TYPE,
                    CreateTestWorldCommand.DIMENSION_WORLD.getValue(),
                    DimensionState.RESET);

            ctx.getSource().getPlayer().teleport(ctx.getSource().getMinecraftServer().getWorld(CreateTestWorldCommand.DIMENSION_WORLD), 0, 64, 0, 0, 0);
        }catch(TesseractRegistryException e) {
            ctx.getSource().sendError(new LiteralText("Something went wrong! Check the console."));
            e.printStackTrace();
        }

        return 1;
    }
}
