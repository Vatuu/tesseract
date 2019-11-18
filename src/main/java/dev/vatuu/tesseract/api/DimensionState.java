package dev.vatuu.tesseract.api;

public enum DimensionState {
    SAVE(false, false, false),
    SAVE_UNLOAD(true, false, false),
    SAVE_UNREGISTER(true, false, true),
    RESET(true, true, false),
    RESET_UNREGISTER(true, true, true);

    private boolean unload;
    private boolean reset;
    private boolean unregister;

    DimensionState(boolean unload, boolean reset, boolean unregister){
        this.unload = unload;
        this.reset = reset;
        this.unregister = unregister;
    }

    public boolean shouldUnload(){
        return unload;
    }

    public boolean shouldReset(){
        return reset;
    }

    public boolean shouldUnregister(){
        return unregister;
    }

    public static DimensionState getByValues(boolean unload, boolean reset, boolean unregister) {
        if (reset) {
            if (unregister) return RESET_UNREGISTER;
            else return RESET;
        } else {
            if (unregister) return SAVE_UNREGISTER;
            if (unload) return SAVE_UNLOAD;
        }
        return SAVE;
    }
}
