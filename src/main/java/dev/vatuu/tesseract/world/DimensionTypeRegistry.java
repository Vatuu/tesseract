package dev.vatuu.tesseract.world;

import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.extensions.mixins.SimpleRegistryMixin;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class DimensionTypeRegistry {

    private Map<Integer, DimensionType> registered;

    public DimensionTypeRegistry(){
        registered = new HashMap<>();
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
