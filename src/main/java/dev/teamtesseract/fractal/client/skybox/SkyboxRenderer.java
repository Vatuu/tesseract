package dev.teamtesseract.fractal.client.skybox;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

public abstract class SkyboxRenderer {

    protected static final VertexFormat skyVertexFormat = VertexFormats.POSITION;

    protected MinecraftClient client;

    protected VertexBuffer starsBuffer, lightSkyBuffer, darkSkyBuffer;

    public SkyboxRenderer() {
        this.client = MinecraftClient.getInstance();
        populateBuffers();
    }

    public abstract void renderSky(MatrixStack stack, ClientWorld world, Camera cam, float tickDelta);

    public void renderStars(VertexConsumer builder) { }

    public void renderSkyHalf(VertexConsumer builder, float y, boolean isBottom) { }
    
    private void populateBuffers() {
        // Stars
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        if (this.starsBuffer != null)
            this.starsBuffer.close();

        this.starsBuffer = new VertexBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, this.skyVertexFormat);
        this.renderStars(bufferBuilder);
        bufferBuilder.end();
        this.starsBuffer.upload(bufferBuilder);

        // Light Half
        tessellator = Tessellator.getInstance();
        bufferBuilder = tessellator.getBuffer();
        if (this.lightSkyBuffer != null)
            this.lightSkyBuffer.close();

        this.lightSkyBuffer = new VertexBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, this.skyVertexFormat);
        this.renderSkyHalf(bufferBuilder, 16.0F, false);
        bufferBuilder.end();
        this.lightSkyBuffer.upload(bufferBuilder);

        // Dark Half
        tessellator = Tessellator.getInstance();
        bufferBuilder = tessellator.getBuffer();
        if (this.darkSkyBuffer != null)
            this.darkSkyBuffer.close();

        this.darkSkyBuffer = new VertexBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, this.skyVertexFormat);
        this.renderSkyHalf(bufferBuilder, -16.0F, true);
        bufferBuilder.end();
        this.darkSkyBuffer.upload(bufferBuilder);
    }

    protected TextureManager getTextureManager() {
        return this.client.getTextureManager();
    }
}
