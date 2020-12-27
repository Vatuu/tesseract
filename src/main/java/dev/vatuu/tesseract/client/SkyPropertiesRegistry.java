package dev.vatuu.tesseract.client;

import com.mojang.serialization.Lifecycle;
import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.client.world.TesseractSkyProperties;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.impl.registry.sync.FabricRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.Collections;

public final class SkyPropertiesRegistry {

    public static final RegistryKey<Registry<TesseractSkyProperties>> REGISTRY_KEY = RegistryKey.ofRegistry(Tesseract.id("skyproperties_renderer"));
    public static final MutableRegistry<TesseractSkyProperties> REGISTRY = new SimpleRegistry<>(REGISTRY_KEY, Lifecycle.experimental());

    public static final RegistryKey<TesseractSkyProperties> OVERWORLD_KEY = RegistryKey.of(REGISTRY_KEY, new Identifier("overworld"));
    public static final RegistryKey<TesseractSkyProperties> THE_END_KEY = RegistryKey.of(REGISTRY_KEY, new Identifier("the_end"));

    static {
        ((FabricRegistry)REGISTRY).build(Collections.singleton(RegistryAttribute.SYNCED));
        Registry.register(REGISTRY, OVERWORLD_KEY.getValue(), TesseractSkyProperties.OVERWORLD);
        Registry.register(REGISTRY, THE_END_KEY.getValue(), TesseractSkyProperties.THE_END);

    }
}
