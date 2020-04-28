package dev.vatuu.tesseract.impl.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.vatuu.tesseract.api.DimensionFactory;
import dev.vatuu.tesseract.api.DimensionRegistry;
import dev.vatuu.tesseract.impl.Tesseract;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSourceConfig;
import net.minecraft.world.biome.source.HorizontalVoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;

public class RegisterTestCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(
                CommandManager.literal("createDimension")
                        .requires(src -> src.hasPermissionLevel(4))
                        .executes(ctx -> activate(ctx, "test", false))
                        .then(CommandManager.argument("id", StringArgumentType.word())
                                .executes(ctx -> activate(ctx, StringArgumentType.getString(ctx, "id"), false))
                                .then(CommandManager.argument("save", BoolArgumentType.bool())
                                        .executes(ctx -> activate(ctx, StringArgumentType.getString(ctx, "id"), BoolArgumentType.getBool(ctx, "save")))
                                )
                        ));
    }

    private static int activate(CommandContext<ServerCommandSource> src, String id, boolean save) {
        DimensionFactory factory = DimensionFactory.create()
                .bedsExplode(true)
                .beesExplode(true)
                .forcedSpawnPoint(0, 4, 0)
                .vaporizeWater(true)
                .visibleSky(true)
                .chunkGenerator(world -> new FlatChunkGenerator(world, new FixedBiomeSource(new FixedBiomeSourceConfig(null)), FlatChunkGeneratorConfig.getDefaultConfig()));

        DimensionRegistry.getInstance().registerDimensionType(new Identifier(Tesseract.MOD_ID, id), factory, HorizontalVoronoiBiomeAccessType.INSTANCE);
        return 1;
    }
}
