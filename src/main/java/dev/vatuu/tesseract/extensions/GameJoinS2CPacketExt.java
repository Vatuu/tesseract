package dev.vatuu.tesseract.extensions;

import net.minecraft.util.Identifier;

public interface GameJoinS2CPacketExt {

    boolean hasSkybox();
    Identifier getSkyboxIdentifier();
    void setSkyboxIdentifier(Identifier id);
}
