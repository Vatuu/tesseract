package dev.teamtesseract.fractal.client;

import com.mojang.serialization.Lifecycle;
import dev.teamtesseract.fractal.client.world.FractalSkyProperties;
import dev.teamtesseract.fractal.Fractal;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.impl.registry.sync.FabricRegistry;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.Collections;

public final class SkyPropertiesRegistry {

    public static final RegistryKey<Registry<FractalSkyProperties>> REGISTRY_KEY = RegistryKey.ofRegistry(Fractal.id("sky_properties"));
    public static final DefaultedRegistry<FractalSkyProperties> REGISTRY = new DefaultedRegistry<>(Fractal.id("missing").toString(), REGISTRY_KEY, Lifecycle.experimental());

    static {
        ((FabricRegistry)REGISTRY).build(Collections.singleton(RegistryAttribute.SYNCED));
        REGISTRY.defaultValue = FractalSkyProperties.MISSING;
    }
}
