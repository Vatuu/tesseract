package dev.vatuu.tesseract.impl.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.vatuu.tesseract.api.DimensionState;
import dev.vatuu.tesseract.impl.world.TesseractDimension;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.command.arguments.DimensionArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

public class WorldTeleportCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(
                CommandManager.literal("teleportWorld")
                    .requires(src -> src.hasPermissionLevel(4))
                    .executes(ctx -> activate(ctx, ctx.getSource().getPlayer().dimension, false))
                    .then(CommandManager.argument("dim", DimensionArgumentType.dimension())
                        .executes(ctx -> activate(ctx, DimensionArgumentType.getDimensionArgument(ctx, "dim"), false))
                    ));
    }

    private static int activate(CommandContext<ServerCommandSource> src, DimensionType t, boolean unregister) throws CommandSyntaxException{
        FabricDimensions.teleport(src.getSource().getPlayer(), t);
        return 1;
    }
}
