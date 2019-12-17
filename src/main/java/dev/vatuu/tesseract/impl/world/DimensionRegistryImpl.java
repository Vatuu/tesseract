package dev.vatuu.tesseract.impl.world;

import dev.vatuu.tesseract.api.DimensionRegistry;
import dev.vatuu.tesseract.impl.extensions.mixins.SimpleRegistryMixin;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

import java.util.function.BiFunction;

public class DimensionRegistryImpl implements DimensionRegistry {

    private static DimensionRegistryImpl INSTANCE;

    private Int2ObjectArrayMap<DimensionType> registered;

    private DimensionRegistryImpl(){
        registered = new Int2ObjectArrayMap<>();
    }

    public static DimensionRegistryImpl getInstance(){
        if(INSTANCE == null) return (INSTANCE = new DimensionRegistryImpl());
        else return INSTANCE;
    }

    public DimensionType registerDimensionType(Identifier name, boolean hasSkyLight, BiFunction<World, DimensionType, ? extends Dimension> factory, BiomeAccessType biomeAccessor){
        int id = 5 + registered.size();
        TesseractDimensionType t = new TesseractDimensionType(id, name.getPath(), hasSkyLight, factory, biomeAccessor);
        registered.put(id, t);
        return Registry.register(Registry.DIMENSION, id, name.toString(), t);
    }

    public static boolean unregister(DimensionType type){
        return ((SimpleRegistryMixin)Registry.DIMENSION).getEntries().values().remove(type);
    }
}
