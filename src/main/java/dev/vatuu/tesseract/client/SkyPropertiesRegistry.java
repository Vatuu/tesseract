package dev.vatuu.tesseract.client;

import com.mojang.serialization.Lifecycle;
import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.client.world.TesseractSkyProperties;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.impl.registry.sync.FabricRegistry;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.Collections;

public final class SkyPropertiesRegistry {

    public static final RegistryKey<Registry<TesseractSkyProperties>> REGISTRY_KEY = RegistryKey.ofRegistry(Tesseract.id("sky_properties"));
    public static final DefaultedRegistry<TesseractSkyProperties> REGISTRY = new DefaultedRegistry<>(Tesseract.id("missing").toString(), REGISTRY_KEY, Lifecycle.experimental());

    static {
        ((FabricRegistry)REGISTRY).build(Collections.singleton(RegistryAttribute.SYNCED));
        REGISTRY.defaultValue = TesseractSkyProperties.MISSING;
    }
}
