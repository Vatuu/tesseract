package dev.teamtesseract.fractal.extensions;

import dev.teamtesseract.fractal.world.DimensionState;

public interface ServerWorldExt {

    DimensionState getSaveState();
    void setSaveState(DimensionState state);
}