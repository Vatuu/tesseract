package dev.teamtesseract.fractal.extensions.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkGeneratorSettings.class)
public interface ChunkGeneratorSettingsInvoker {
    @Invoker("<init>")
    static ChunkGeneratorSettings init(StructuresConfig structuresConfig, GenerationShapeConfig generationShapeConfig, BlockState defaultBlock, BlockState defaultFluid, int bedrockCeilingY, int bedrockFloorY, int seaLevel, int minSurfaceLevel, boolean mobGenerationDisabled, boolean aquifers, boolean noiseCaves, boolean deepslate, boolean oreVeins, boolean noodleCaves) {
        throw new RuntimeException("If this is thrown Mixin failed");
    }
}
