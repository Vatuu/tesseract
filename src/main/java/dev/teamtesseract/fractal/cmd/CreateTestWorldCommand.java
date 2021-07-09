package dev.teamtesseract.fractal.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.teamtesseract.fractal.registry.FractalException;
import dev.teamtesseract.fractal.registry.FractalManagement;
import dev.teamtesseract.fractal.Fractal;
import dev.teamtesseract.fractal.world.ChunkGeneratorBuilder;
import dev.teamtesseract.fractal.world.DimensionState;
import dev.teamtesseract.fractal.world.DimensionTypeBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.*;

import java.util.Random;

public class CreateTestWorldCommand {

    public static RegistryKey<World> DIMENSION_WORLD;
    public static RegistryKey<DimensionType> DIMENSION_TYPE;
    public static ChunkGenerator CHUNK_GENERATOR;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("registerTest")
                    .requires(src -> src.hasPermissionLevel(4))
                    .executes(CreateTestWorldCommand::activate)
        );

        ServerLifecycleEvents.SERVER_STARTED.register(t -> CHUNK_GENERATOR = ChunkGeneratorBuilder.createNoise(Fractal.id("test"))
                .setBiomeSource(new VanillaLayeredBiomeSource(new Random().nextLong(), false, false, t.getRegistryManager().get(Registry.BIOME_KEY)))
                .createGeneratorSettings(chunkGeneratorSettingsBuilder -> {
                    chunkGeneratorSettingsBuilder.setStructuresConfig(new StructuresConfig(true))
                            .createGenerationShapeConfig(generationShapeConfigBuilder -> {
                                generationShapeConfigBuilder.setMinY(0)
                                        .setHeight(256)
                                        .setSampling(new NoiseSamplingConfig(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D))
                                        .setTopSlide(new SlideConfig(-10, 3, 0))
                                        .setBottomSlide(new SlideConfig(-30, 0, 0))
                                        .setHorizontalSize(1)
                                        .setVerticalSize(2)
                                        .setDensityFactor(1.0D)
                                        .setDensityOffset(-0.46875D)
                                        .setSimplexSurfaceNoise(true)
                                        .setRandomDensityOffset(true)
                                        .setIslandNoiseOverride(false)
                                        .setAmplified(false);
                            })
                            .setDefaultBlock(Blocks.STONE.getDefaultState())
                            .setDefaultFluid(Blocks.WATER.getDefaultState())
                            .setBedrockCeilingY(Integer.MIN_VALUE)
                            .setBedrockFloorY(0)
                            .setSeaLevel(63)
                            .setMobGenerationDisabled(false);
                }).build());
    }

    private static int activate(CommandContext<ServerCommandSource> src) throws CommandSyntaxException {
        try {
            DIMENSION_TYPE = DimensionTypeBuilder.create(Fractal.id("telesis"))
                    .doBedsExplode(true)
                    .doBeesExplode(true)
                    .hasSkylight(false)
                    .isNatural(false)
                    .setAmbientLightLevel(0)
                    .setBlockHeight(256)
                    .setLogicalHeight(256)
                    .setMinimumY(0)
                    .setSkyProperties(Fractal.id("test"))
                    .setFixedTime(0)
                    .register();

            DIMENSION_WORLD = FractalManagement.getInstance().createWorld(
                    DIMENSION_TYPE,
                    Fractal.id("telesis_w"),
                    CHUNK_GENERATOR,
                    DimensionState.SAVE);

            src.getSource().getPlayer().teleport(
                    src.getSource().getMinecraftServer().getWorld(DIMENSION_WORLD),
                    0, 80, 0,
                    0, 0);
        } catch(FractalException e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }
}
