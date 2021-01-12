package dev.vatuu.tesseract.client.world;

import dev.vatuu.tesseract.client.skybox.MissingSkyboxRenderer;
import dev.vatuu.tesseract.client.skybox.SkyboxRenderer;
import dev.vatuu.tesseract.interfaces.FogColourFunction;
import dev.vatuu.tesseract.interfaces.FogThiccFunction;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.math.Vec3d;

public class TesseractSkyProperties extends SkyProperties {

    public static final TesseractSkyProperties MISSING = new TesseractSkyProperties(
            Float.NaN, false, new MissingSkyboxRenderer(), true, false,
            (c, s) -> new Vec3d(0,0,0),
            (x, y) -> false);

    private final SkyboxRenderer skybox;
    private final FogColourFunction fogColour;
    private final FogThiccFunction fogThick;

    public TesseractSkyProperties(float cloudsHeight, boolean alternateSkyColor, SkyboxRenderer skyboxType, boolean brightenLighting, boolean darkened, FogColourFunction fogColourFunction, FogThiccFunction fogThick) {
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
