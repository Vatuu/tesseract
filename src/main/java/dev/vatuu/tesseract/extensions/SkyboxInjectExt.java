package dev.vatuu.tesseract.extensions;

import net.minecraft.util.Identifier;

public interface SkyboxInjectExt {

    boolean hasSkybox();
    Identifier getSkyboxIdentifier();
    void setSkyboxIdentifier(Identifier id);
}
