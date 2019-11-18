package dev.vatuu.tesseract.api;

import dev.vatuu.tesseract.impl.world.DimensionRegistryImpl;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeAccessType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

import java.util.function.BiFunction;

public interface DimensionRegistry {

    static DimensionRegistry getInstance() { return DimensionRegistryImpl.getInstance(); }

    DimensionType registerDimensionType(Identifier name, boolean hasSkyLight, BiFunction<World, DimensionType, ? extends Dimension> create, BiomeAccessType biomeAccessor);
}
