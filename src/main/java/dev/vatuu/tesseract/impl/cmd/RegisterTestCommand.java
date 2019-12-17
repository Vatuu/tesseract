package dev.vatuu.tesseract.impl.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.vatuu.tesseract.api.DimensionRegistry;
import dev.vatuu.tesseract.impl.Tesseract;
import dev.vatuu.tesseract.impl.world.DimensionBuilderImpl;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.source.HorizontalVoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionType;

public class RegisterTestCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(
                CommandManager.literal("registerTest")
                        .requires(src -> src.hasPermissionLevel(4))
                        .executes(ctx -> activate(ctx, "test", false))
                        .then(CommandManager.argument("id", StringArgumentType.word())
                                .executes(ctx -> activate(ctx, StringArgumentType.getString(ctx, "id"), false))
                                .then(CommandManager.argument("save", BoolArgumentType.bool())
                                        .executes(ctx -> activate(ctx, StringArgumentType.getString(ctx, "id"), BoolArgumentType.getBool(ctx, "save")))
                                )
                        ));
    }

    private static int activate(CommandContext<ServerCommandSource> src, String id, boolean save) throws CommandSyntaxException {
        DimensionType dim = DimensionRegistry.getInstance().registerDimensionType(new Identifier(Tesseract.MOD_ID, id), true, (w, d) -> new DimensionBuilderImpl()
                .bedsExplode(true)
                .vaporizeWater(true)
                .beesExplode(true)
                .forcedSpawnPoint(new BlockPos(0, 64, 0))
        .build(w, d), HorizontalVoronoiBiomeAccessType.INSTANCE);
        return 1;
    }
}
