package dev.vatuu.tesseract.client.world;

import dev.vatuu.tesseract.client.skybox.EndSkyboxRenderer;
import dev.vatuu.tesseract.client.skybox.OverworldSkyboxRenderer;
import dev.vatuu.tesseract.client.skybox.SkyboxRenderer;
import dev.vatuu.tesseract.interfaces.FogColourFunction;
import dev.vatuu.tesseract.interfaces.FogThiccFunction;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.math.Vec3d;

public class TesseractSkyProperties extends SkyProperties {

    public static final TesseractSkyProperties OVERWORLD = new TesseractSkyProperties(
            128, true, new OverworldSkyboxRenderer(),false, false,
            (c, s) -> c.multiply(s * 0.94F + 0.06F, s * 0.94F + 0.06F, s * 0.91F + 0.09F),
            (x, y) -> false);

    public static final TesseractSkyProperties THE_END = new TesseractSkyProperties(
            Float.NaN, false, new EndSkyboxRenderer(),true, false,
            (c, s) -> c.multiply(0.15000000596046448D),
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
