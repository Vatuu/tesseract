package dev.vatuu.tesseract.impl.world;

import dev.vatuu.tesseract.api.DimensionFactory;
import dev.vatuu.tesseract.api.DimensionRegistry;
import dev.vatuu.tesseract.impl.extensions.mixins.SimpleRegistryMixin;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Map;

public class DimensionRegistryImpl implements DimensionRegistry {

    private static DimensionRegistryImpl INSTANCE;

    private final HashMap<Identifier, DimensionType> registered = new HashMap<>();

    private DimensionRegistryImpl() {
        registered.put(new Identifier("overworld"), DimensionType.OVERWORLD);
        registered.put(new Identifier("the_nether"), DimensionType.THE_NETHER);
        registered.put(new Identifier("the_end"), DimensionType.THE_END);
    }

    public static DimensionRegistryImpl getInstance(){
        if(INSTANCE == null) return (INSTANCE = new DimensionRegistryImpl());
        else return INSTANCE;
    }
    public DimensionType registerDimensionType(Identifier name, DimensionFactory factory, BiomeAccessType biomeAccessor){
        return registerDimensionType(name, true, factory, biomeAccessor);
    }

    public DimensionType registerDimensionType(Identifier name, boolean hasSkyLight, DimensionFactory factory, BiomeAccessType biomeAccessor){
        int id = 5 + registered.size();
        TesseractDimensionType t = new TesseractDimensionType(id, name.getPath(), hasSkyLight, factory::build, biomeAccessor);
        registered.put(name, t);
        return Registry.register(Registry.DIMENSION_TYPE, id, name.toString(), t);
    }

    public DimensionType getDimensionType(Identifier id) {
        return registered.get(id);
    }

    public Identifier getDimensionName(DimensionType t) {
        return registered.entrySet().stream()
                .filter(e -> e.getValue().equals(t))
                .map(Map.Entry::getKey)
                .findFirst()
                .get();
    }

    public boolean unregister(Identifier id) {
        return unregister(registered.get(id));
    }

    public boolean unregister(DimensionType t){
        if(t == null)
            return false;
        registered.remove(getDimensionName(t));
        return ((SimpleRegistryMixin)Registry.DIMENSION_TYPE).getEntries().values().remove(t);
    }
}
