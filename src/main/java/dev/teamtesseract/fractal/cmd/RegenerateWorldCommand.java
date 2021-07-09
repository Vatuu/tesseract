package dev.teamtesseract.fractal.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.teamtesseract.fractal.registry.FractalException;
import dev.teamtesseract.fractal.registry.FractalManagement;
import dev.teamtesseract.fractal.world.DimensionState;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

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
            FractalManagement.getInstance().unloadWorld(CreateTestWorldCommand.DIMENSION_WORLD);
            FractalManagement.getInstance().resetWorld(CreateTestWorldCommand.DIMENSION_WORLD);

            CreateTestWorldCommand.DIMENSION_WORLD = FractalManagement.getInstance().createWorld(
                    CreateTestWorldCommand.DIMENSION_TYPE,
                    CreateTestWorldCommand.DIMENSION_WORLD.getValue(),
                    CreateTestWorldCommand.CHUNK_GENERATOR,
                    DimensionState.RESET);

            ctx.getSource().getPlayer().teleport(ctx.getSource().getMinecraftServer().getWorld(CreateTestWorldCommand.DIMENSION_WORLD), 0, 80, 0, 0, 0);
        }catch(FractalException e) {
            ctx.getSource().sendError(new LiteralText("Something went wrong! Check the console."));
            e.printStackTrace();
        }

        return 1;
    }
}
