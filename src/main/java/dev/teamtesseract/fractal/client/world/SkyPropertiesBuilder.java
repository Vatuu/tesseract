package dev.teamtesseract.fractal.client.world;

import dev.teamtesseract.fractal.client.skybox.SkyboxRenderer;
import dev.teamtesseract.fractal.client.SkyPropertiesRegistry;
import dev.teamtesseract.fractal.client.skybox.EndSkyboxRenderer;
import dev.teamtesseract.fractal.interfaces.FogColourFunction;
import dev.teamtesseract.fractal.interfaces.FogThiccFunction;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class SkyPropertiesBuilder {

    private float cloudHeight = Float.NaN;
    private boolean alternateSkyColor = false, brightLightning = false, darkened = false;
    private SkyboxRenderer skybox = new EndSkyboxRenderer();
    private FogColourFunction fogColourFunction = (c, s) -> c;
    private FogThiccFunction fogThickFunction = (x, y) -> false;

    private SkyPropertiesBuilder() { }

    public static SkyPropertiesBuilder create() {
        return new SkyPropertiesBuilder();
    }

    public SkyPropertiesBuilder setCloudHeight(float height) {
        this.cloudHeight = height;
        return this;
    }

    public SkyPropertiesBuilder hasAlternateSkyColour(boolean has) {
        this.alternateSkyColor = has;
        return this;
    }

    public SkyPropertiesBuilder hasBrightLightning(boolean has) {
        this.brightLightning = has;
        return this;
    }

    public SkyPropertiesBuilder isDarkened(boolean is) {
        this.darkened = is;
        return this;
    }

    public SkyPropertiesBuilder setSkybox(SkyboxRenderer type) {
        this.skybox = type;
        return this;
    }

    public SkyPropertiesBuilder setFogColorFunction(FogColourFunction func) {
        this.fogColourFunction = func;
        return this;
    }

    public SkyPropertiesBuilder setFogThickFunction(FogThiccFunction func) {
        this.fogThickFunction = func;
        return this;
    }

    public FractalSkyProperties build() {
        return new FractalSkyProperties(cloudHeight, alternateSkyColor, skybox, brightLightning, darkened, fogColourFunction, fogThickFunction);
    }

    public RegistryKey<FractalSkyProperties> register(Identifier id) {
        RegistryKey<FractalSkyProperties> key = RegistryKey.of(SkyPropertiesRegistry.REGISTRY_KEY, id);
        Registry.register(SkyPropertiesRegistry.REGISTRY, id, build());
        return key;
    }
}
