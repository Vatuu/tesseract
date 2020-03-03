package dev.vatuu.tesseract.impl.extras.lil;

public class TesseractSettings {

    public float rotateX, rotateY, rotateZ, rotateW;
    public boolean wireframe;

    public TesseractSettings() {
        this.rotateX = this.rotateY = this.rotateZ = this.rotateW = 0.0f;
        wireframe = false;
    }
}
