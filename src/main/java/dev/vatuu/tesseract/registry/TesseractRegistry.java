package dev.vatuu.tesseract.registry;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

public class TesseractRegistry {

    private static TesseractRegistry INSTANCE;

    private final Registry<DimensionType> dimensionTypeRegistry;

    private TesseractRegistry(MinecraftServer server) {
        this.dimensionTypeRegistry = server.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY);;
    }

    public static TesseractRegistry getInstance() {
        return INSTANCE;
    }

    public RegistryKey<DimensionType> registerDimension(DimensionType type, Identifier id) throws TesseractException {
        RegistryKey<DimensionType> key = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, id);

        if(dimensionTypeRegistry.containsId(id))
            throw new TesseractException(String.format("DimensionType %s has already been registered!", id));

        Registry.register(dimensionTypeRegistry, id, type);

        return key;
    }

    public static void setInstance(MinecraftServer server) {
        INSTANCE = new TesseractRegistry(server);
    }
}
