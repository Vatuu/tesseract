package dev.vatuu.tesseract.client.skybox;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;

public abstract class SkyboxRenderer {

    protected static final VertexFormat skyVertexFormat = VertexFormats.POSITION;

    protected MinecraftClient client;
    protected TextureManager textureManager;

    protected VertexBuffer starsBuffer, lightSkyBuffer, darkSkyBuffer;

    public SkyboxRenderer() {
        ClientLifecycleEvents.CLIENT_STARTED.register(t -> {
            this.client = t;
            this.textureManager = t.getTextureManager();
            populateBuffers();
        });
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
}
