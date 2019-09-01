package dev.vatuu.tesseract.impl.world;

import dev.vatuu.tesseract.impl.extensions.mixins.SimpleRegistryMixin;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

import java.util.function.BiFunction;

public class DimensionRegistry implements dev.vatuu.tesseract.api.DimensionRegistry {

    private static DimensionRegistry INSTANCE;

    private Int2ObjectMap<DimensionType> registered;

    private DimensionRegistry(){
        registered = Int2ObjectMaps.emptyMap();
    }

    public static DimensionRegistry getInstance(){
        if(INSTANCE == null)
            return (INSTANCE = new DimensionRegistry());
        else
            return INSTANCE;
    }

    public DimensionType registerDimensionType(Identifier name, boolean hasSkyLight, BiFunction<World, DimensionType, ? extends Dimension> create){
        int id = registered.size() + 3;
        TesseractDimensionType t = new TesseractDimensionType(id, name.getPath(), hasSkyLight, create);
        registered.put(id, t);
        return Registry.register(Registry.DIMENSION, id, name.toString(), t);
    }

    public static boolean unregister(DimensionType t){
        return ((SimpleRegistryMixin)Registry.DIMENSION).getEntries().values().remove(t);
    }
}
