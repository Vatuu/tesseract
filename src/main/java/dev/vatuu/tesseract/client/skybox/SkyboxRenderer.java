package dev.vatuu.tesseract.client.skybox;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

public abstract class SkyboxRenderer {

    protected final MinecraftClient client;
    protected final TextureManager textureManager;

    protected final VertexFormat skyVertexFormat;

    protected VertexBuffer starsBuffer, lightSkyBuffer, darkSkyBuffer;

    public SkyboxRenderer() {
        this.client = MinecraftClient.getInstance();
        this.textureManager = client.getTextureManager();

        this.skyVertexFormat = VertexFormats.POSITION;

        populateBuffers();
    }

    public abstract void renderSky(MatrixStack stack, ClientWorld world, Camera cam, float tickDelta);

    public void renderStars(BufferBuilder builder) { }

    public void renderSkyHalf(BufferBuilder builder, float y, boolean isBottom) { }


    private void populateBuffers() {
        // Stars
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        if (this.starsBuffer != null)
            this.starsBuffer.close();

        this.starsBuffer = new VertexBuffer();
        this.renderStars(bufferBuilder);
        bufferBuilder.end();
        this.starsBuffer.upload(bufferBuilder);

        // Light Half
        tessellator = Tessellator.getInstance();
        bufferBuilder = tessellator.getBuffer();
        if (this.lightSkyBuffer != null)
            this.lightSkyBuffer.close();

        this.lightSkyBuffer = new VertexBuffer();
        this.renderSkyHalf(bufferBuilder, 16.0F, false);
        bufferBuilder.end();
        this.lightSkyBuffer.upload(bufferBuilder);

        // Dark Half
        tessellator = Tessellator.getInstance();
        bufferBuilder = tessellator.getBuffer();
        if (this.darkSkyBuffer != null)
            this.darkSkyBuffer.close();

        this.darkSkyBuffer = new VertexBuffer();
        this.renderSkyHalf(bufferBuilder, -16.0F, true);
        bufferBuilder.end();
        this.darkSkyBuffer.upload(bufferBuilder);
    }
}
