package dev.vatuu.tesseract.impl.world;


import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

import java.util.function.BiFunction;

public class TesseractDimensionType extends DimensionType {
    TesseractDimensionType(int dimensionId, String name, boolean hasSkyLight, BiFunction<World, DimensionType, ? extends Dimension> factory, BiomeAccessType biomeAccessor) {
        super(dimensionId, name.toUpperCase(), name.toLowerCase(), factory, hasSkyLight, biomeAccessor);
    }
}