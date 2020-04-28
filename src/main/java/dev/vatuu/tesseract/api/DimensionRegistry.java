package dev.vatuu.tesseract.api;

import dev.vatuu.tesseract.impl.world.DimensionRegistryImpl;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

import java.util.function.BiFunction;

public interface DimensionRegistry {

    static DimensionRegistry getInstance() { return DimensionRegistryImpl.getInstance(); }

    DimensionType registerDimensionType(Identifier name, DimensionFactory factory, BiomeAccessType biomeAccessor);
    DimensionType registerDimensionType(Identifier name, boolean hasSkyLight, DimensionFactory factory, BiomeAccessType biomeAccessor);

    DimensionType getDimensionType(Identifier id);
    Identifier getDimensionName(DimensionType type);

    boolean unregister(DimensionType type);
    boolean unregister(Identifier id);
}
