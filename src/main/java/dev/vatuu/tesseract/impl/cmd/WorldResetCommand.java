package dev.vatuu.tesseract.impl.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.vatuu.tesseract.api.DimensionState;
import dev.vatuu.tesseract.impl.world.TesseractDimension;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.minecraft.command.arguments.DimensionArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

public class WorldResetCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(
                CommandManager.literal("resetWorld")
                    .requires(src -> src.hasPermissionLevel(4))
                    .executes(ctx -> activate(ctx, ctx.getSource().getPlayer().dimension, false))
                    .then(CommandManager.argument("dim", DimensionArgumentType.dimension())
                        .executes(ctx -> activate(ctx, DimensionArgumentType.getDimensionArgument(ctx, "dim"), false))
                        .then(CommandManager.argument("unreg", BoolArgumentType.bool())
                            .executes(ctx -> activate(ctx, DimensionArgumentType.getDimensionArgument(ctx, "dim"), BoolArgumentType.getBool(ctx, "unreg")))
                        )
                    ));
    }

    private static int activate(CommandContext<ServerCommandSource> src, DimensionType t, boolean unregister) throws CommandSyntaxException{
        ServerWorld serverWorld = src.getSource().getPlayer().getServer().getWorld(t);

        if(!(serverWorld.getServer().getWorld(t).getDimension() instanceof TesseractDimension)) {
            throw INVALID_DIMENSION.create(Registry.DIMENSION.getId(t));
        }

        ((TesseractDimension)serverWorld.getServer().getWorld(t).getDimension()).setSaveState(DimensionState.getByValues(true, true, unregister));
        serverWorld.getServer().save(true, true, false);
        return 1;
    }

    private static DynamicCommandExceptionType INVALID_DIMENSION = new DynamicCommandExceptionType((o) ->  new TranslatableText("%s is not a FabricDimension!", o).formatted(Formatting.RED));
}
