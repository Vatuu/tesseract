package dev.vatuu.tesseract.world;

import com.mojang.serialization.Codec;
import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.extensions.mixins.ChunkGeneratorSettingsInvoker;
import dev.vatuu.tesseract.registry.TesseractException;
import dev.vatuu.tesseract.registry.TesseractRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.*;

import java.util.OptionalLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ChunkGeneratorBuilder {

    private ChunkGeneratorBuilder() {

    }

    /*
     * Creates a NoiseChunkGenerator. (Used for Overworld, Nether, End, etc)
     */
    public static NoiseChunkGeneratorBuilder createNoise(Identifier id) {
        return new NoiseChunkGeneratorBuilder(id);
    }

    public static class NoiseChunkGeneratorBuilder {

        private BiomeSource source;
        private long seed;
        private ChunkGeneratorSettings settings;
        private final Identifier name;

        protected NoiseChunkGeneratorBuilder(Identifier id) {
            this.name = id;
        }

        public NoiseChunkGeneratorBuilder setBiomeSource(BiomeSource source) {
            this.source = source;
            return this;
        }

        public NoiseChunkGeneratorBuilder setSeed(long seed) {
            this.seed = seed;
            return this;
        }

        public NoiseChunkGeneratorBuilder createGeneratorSettings(Consumer<ChunkGeneratorSettingsBuilder> builder) {
            ChunkGeneratorSettingsBuilder settingsBuilder = new ChunkGeneratorSettingsBuilder(name, this);
            builder.accept(settingsBuilder);

            this.settings = settingsBuilder.build();
            return this;
        }

        public NoiseChunkGenerator build() {
            return new NoiseChunkGenerator(source, seed, () -> settings);
        }

    }

    public static class ChunkGeneratorSettingsBuilder {

        private final NoiseChunkGeneratorBuilder parent;
        private StructuresConfig structuresConfig;
        private GenerationShapeConfig generationShapeConfig;
        private BlockState defaultBlock, defaultFluid;
        private int bedrockCeilingY, bedrockFloorY, seaLevel;
        private boolean mobGenerationDisabled;

        public ChunkGeneratorSettingsBuilder(Identifier id, NoiseChunkGeneratorBuilder parent) {
            this.parent = parent;
        }

        public ChunkGeneratorSettingsBuilder setStructuresConfig(StructuresConfig structuresConfig) {
            this.structuresConfig = structuresConfig;
            return this;
        }

        public ChunkGeneratorSettingsBuilder createGenerationShapeConfig(Consumer<GenerationShapeConfigBuilder> builder) {
            GenerationShapeConfigBuilder configBuilder = new GenerationShapeConfigBuilder(this);
            builder.accept(configBuilder);

            this.generationShapeConfig = configBuilder.build();
            return this;
        }

        public ChunkGeneratorSettingsBuilder setDefaultBlock(BlockState defaultBlock) {
            this.defaultBlock = defaultBlock;
            return this;
        }

        public ChunkGeneratorSettingsBuilder setDefaultFluid(BlockState defaultFluid) {
            this.defaultFluid = defaultFluid;
            return this;
        }

        public ChunkGeneratorSettingsBuilder setBedrockCeilingY(int bedrockCeilingY) {
            this.bedrockCeilingY = bedrockCeilingY;
            return this;
        }

        public ChunkGeneratorSettingsBuilder setBedrockFloorY(int bedrockFloorY) {
            this.bedrockFloorY = bedrockFloorY;
            return this;
        }

        public ChunkGeneratorSettingsBuilder setSeaLevel(int seaLevel) {
            this.seaLevel = seaLevel;
            return this;
        }

        public ChunkGeneratorSettingsBuilder setMobGenerationDisabled(boolean mobGenerationDisabled) {
            this.mobGenerationDisabled = mobGenerationDisabled;
            return this;
        }

        public NoiseChunkGeneratorBuilder getParent() {
            return parent;
        }

        private ChunkGeneratorSettings build() {
            return ChunkGeneratorSettingsInvoker.init(structuresConfig, generationShapeConfig, defaultBlock, defaultFluid, bedrockCeilingY, bedrockFloorY, seaLevel, mobGenerationDisabled);
        }

        public RegistryKey<ChunkGeneratorSettings> register() throws TesseractException {
            return TesseractRegistry.getInstance().registerChunkGeneratorSettings(build(), getParent().name);
        }

    }

    public static class GenerationShapeConfigBuilder {

        private final ChunkGeneratorSettingsBuilder parent;
        private int minY, height, horizontalSize, verticalSize;
        private double densityFactor, densityOffset;
        private boolean simplexSurfaceNoise, randomDensityOffset, islandNoiseOverride, amplified;
        private NoiseSamplingConfig sampling;
        private SlideConfig topSlide, bottomSlide;

        public GenerationShapeConfigBuilder(ChunkGeneratorSettingsBuilder parent) {
            this.parent = parent;
        }

        public GenerationShapeConfigBuilder setMinY(int minY) {
            this.minY = minY;
            return this;
        }

        public GenerationShapeConfigBuilder setHeight(int height) {
            this.height = height;
            return this;
        }

        public GenerationShapeConfigBuilder setSampling(NoiseSamplingConfig sampling) {
            this.sampling = sampling;
            return this;
        }

        public GenerationShapeConfigBuilder setTopSlide(SlideConfig topSlide) {
            this.topSlide = topSlide;
            return this;
        }

        public GenerationShapeConfigBuilder setBottomSlide(SlideConfig bottomSlide) {
            this.bottomSlide = bottomSlide;
            return this;
        }

        public GenerationShapeConfigBuilder setHorizontalSize(int horizontalSize) {
            this.horizontalSize = horizontalSize;
            return this;
        }

        public GenerationShapeConfigBuilder setVerticalSize(int verticalSize) {
            this.verticalSize = verticalSize;
            return this;
        }

        public GenerationShapeConfigBuilder setDensityFactor(double densityFactor) {
            this.densityFactor = densityFactor;
            return this;
        }

        public GenerationShapeConfigBuilder setDensityOffset(double densityOffset) {
            this.densityOffset = densityOffset;
            return this;
        }

        public GenerationShapeConfigBuilder setSimplexSurfaceNoise(boolean simplexSurfaceNoise) {
            this.simplexSurfaceNoise = simplexSurfaceNoise;
            return this;
        }

        public GenerationShapeConfigBuilder setRandomDensityOffset(boolean randomDensityOffset) {
            this.randomDensityOffset = randomDensityOffset;
            return this;
        }

        public GenerationShapeConfigBuilder setIslandNoiseOverride(boolean islandNoiseOverride) {
            this.islandNoiseOverride = islandNoiseOverride;
            return this;
        }

        public GenerationShapeConfigBuilder setAmplified(boolean amplified) {
            this.amplified = amplified;
            return this;
        }

        public ChunkGeneratorSettingsBuilder getParent() {
            return parent;
        }

        private GenerationShapeConfig build() {
            return GenerationShapeConfig.method_32994(minY, height, sampling, topSlide, bottomSlide, horizontalSize, verticalSize, densityFactor, densityOffset, simplexSurfaceNoise, randomDensityOffset, islandNoiseOverride, amplified);
        }
        
    }

}
