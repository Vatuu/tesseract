package dev.vatuu.tesseract.impl.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.command.arguments.BlockPosArgumentType;
import net.minecraft.command.arguments.DimensionArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public class ChangeDimensionCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(
                CommandManager.literal("changeDimension")
                        .requires(src -> src.hasPermissionLevel(4))
                        .executes(ctx -> activate(ctx, DimensionType.OVERWORLD, new BlockPos(0, 64, 0)))
                        .then(CommandManager.argument("dim", DimensionArgumentType.dimension())
                                .executes(ctx -> activate(ctx, DimensionArgumentType.getDimensionArgument(ctx, "dim"), new BlockPos(0, 64, 0)))
                                .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                        .executes(ctx -> activate(ctx, DimensionArgumentType.getDimensionArgument(ctx, "dim"),BlockPosArgumentType.getBlockPos(ctx, "pos")))
                                )
                        ));
    }

    private static int activate(CommandContext<ServerCommandSource> context, DimensionType type, BlockPos pos) throws CommandSyntaxException {
        PlayerEntity entity = context.getSource().getPlayer();
        FabricDimensions.teleport(entity, type);
        entity.teleport(pos.getX(), pos.getY(), pos.getZ());
        return 0;
    }
}
