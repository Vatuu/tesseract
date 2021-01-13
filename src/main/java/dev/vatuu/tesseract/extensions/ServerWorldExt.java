package dev.vatuu.tesseract.extensions;

import dev.vatuu.tesseract.world.DimensionState;

public interface ServerWorldExt {

    DimensionState getSaveState();
    void setSaveState(DimensionState state);
}
