package dev.vatuu.tesseract.impl.world;


import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

import java.util.function.BiFunction;

public class TesseractDimensionType extends DimensionType {
    TesseractDimensionType(int id, String name, boolean hasSky, BiFunction<World, DimensionType, ? extends Dimension> create) {
        super(id, name.toUpperCase(), name.toLowerCase(), create, hasSky);
    }
}