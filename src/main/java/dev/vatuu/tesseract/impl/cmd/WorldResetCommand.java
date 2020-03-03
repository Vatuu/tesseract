package dev.vatuu.tesseract.impl.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.vatuu.tesseract.api.DimensionState;
import dev.vatuu.tesseract.impl.world.TesseractDimension;
import dev.vatuu.tesseract.impl.world.TesseractDimensionType;
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

    private static int activate(CommandContext<ServerCommandSource> context, DimensionType type, boolean unregister) throws CommandSyntaxException{
        if(!(type instanceof TesseractDimensionType)){
            throw INVALID_DIMENSION.create(Registry.DIMENSION_TYPE.getId(type));
        }

        ServerWorld playerWorld = context.getSource().getPlayer().getServer().getWorld(type);
        ((TesseractDimension) playerWorld.getServer().getWorld(type).getDimension()).setSaveState(DimensionState.getByValues(true, true, unregister));
        playerWorld.getServer().save(true, true, false);

        return 1;
    }

    private static DynamicCommandExceptionType INVALID_DIMENSION = new DynamicCommandExceptionType((o) ->  new TranslatableText("%s is not a TesseractDimension!", o).formatted(Formatting.RED));
}
