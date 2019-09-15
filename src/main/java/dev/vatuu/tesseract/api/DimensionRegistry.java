package dev.vatuu.tesseract.api;

import dev.vatuu.tesseract.impl.extensions.mixins.SimpleRegistryMixin;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

public class DimensionRegistry {

    private DimensionRegistry() {

    }

    public static boolean unregister(DimensionType t){
        return ((SimpleRegistryMixin) Registry.DIMENSION).getEntries().values().remove(t);
    }
}
