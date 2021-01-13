package dev.vatuu.tesseract.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.registry.TesseractManagement;
import dev.vatuu.tesseract.registry.TesseractRegistry;
import dev.vatuu.tesseract.registry.TesseractException;
import dev.vatuu.tesseract.world.ChunkGeneratorBuilder;
import dev.vatuu.tesseract.world.DimensionState;
import dev.vatuu.tesseract.world.DimensionTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseSamplingConfig;
import net.minecraft.world.gen.chunk.SlideConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;

import java.util.Random;

public class CreateTestWorldCommand {

    public static RegistryKey<World> DIMENSION_WORLD;
    public static RegistryKey<DimensionType> DIMENSION_TYPE;
    public static final ChunkGenerator CHUNK_GENERATOR = ChunkGeneratorBuilder.createNoise(new Identifier("tesseract:test"))
            .setBiomeSource(new VanillaLayeredBiomeSource(new Random().nextLong(), false, false, registries.get(Registry.BIOME_KEY)))
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
            }).build();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("registerTest")
                    .requires(src -> src.hasPermissionLevel(4))
                    .executes(CreateTestWorldCommand::activate)
        );
    }

    private static int activate(CommandContext<ServerCommandSource> src) throws CommandSyntaxException {
        try {
            DIMENSION_TYPE = DimensionTypeBuilder.create(Tesseract.id("telesis"))
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

            DIMENSION_WORLD = TesseractManagement.getInstance().createWorld(DIMENSION_TYPE, Tesseract.id("telesis"), CHUNK_GENERATOR, DimensionState.RESET);

            src.getSource().getPlayer().moveToWorld(src.getSource().getMinecraftServer().getWorld(DIMENSION_WORLD));
            src.getSource().getPlayer().teleport(0, 128, 0);
        } catch(TesseractException e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }
}
