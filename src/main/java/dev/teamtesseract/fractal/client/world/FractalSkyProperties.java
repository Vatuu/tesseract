package dev.teamtesseract.fractal.client.world;

import dev.teamtesseract.fractal.client.skybox.SkyboxRenderer;
import dev.teamtesseract.fractal.client.skybox.MissingSkyboxRenderer;
import dev.teamtesseract.fractal.interfaces.FogColourFunction;
import dev.teamtesseract.fractal.interfaces.FogThiccFunction;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.math.Vec3d;

public class FractalSkyProperties extends SkyProperties {

    public static final FractalSkyProperties MISSING = new FractalSkyProperties(
            Float.NaN, false, new MissingSkyboxRenderer(), true, false,
            (c, s) -> new Vec3d(0,0,0),
            (x, y) -> false);

    private final SkyboxRenderer skybox;
    private final FogColourFunction fogColour;
    private final FogThiccFunction fogThick;

    public FractalSkyProperties(float cloudsHeight, boolean alternateSkyColor, SkyboxRenderer skyboxType, boolean brightenLighting, boolean darkened, FogColourFunction fogColourFunction, FogThiccFunction fogThick) {
        super(cloudsHeight, alternateSkyColor, SkyType.NONE, brightenLighting, darkened);
        this.skybox = skyboxType;
        this.fogColour = fogColourFunction;
        this.fogThick = fogThick;
    }

    @Override
    public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
        return fogColour.apply(color, sunHeight);
    }

    @Override
    public boolean useThickFog(int camX, int camY) {
        return fogThick.apply(camX, camY);
    }

    public SkyboxRenderer getSkybox() {
        return skybox;
    }
}
