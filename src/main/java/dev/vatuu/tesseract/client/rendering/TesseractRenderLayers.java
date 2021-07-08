package dev.vatuu.tesseract.client.rendering;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

import java.util.OptionalDouble;

public class TesseractRenderLayers {

    public static RenderLayer TESSERACT_VERTEX = TesseractVertexRenderLayer.getLayer();
    public static RenderLayer TESSERACT_SIDE = TesseractConnectionRenderLayer.getLayer();

    private static class TesseractVertexRenderLayer extends RenderLayer {

        private static final RenderLayer LAYER = RenderLayer.of("tesseract_vertex", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS, 256, RenderLayer.MultiPhaseParameters.builder()
                .shader(Shader.COLOR_SHADER)
                .build(true)
        );

        private TesseractVertexRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
            super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
        }

        public static RenderLayer getLayer() {
            return LAYER;
        }
    }

    private static class TesseractConnectionRenderLayer extends RenderLayer {

        private static final RenderLayer LAYER = RenderLayer.of("tesseract_side", VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.DEBUG_LINES, 256, RenderLayer.MultiPhaseParameters.builder()
                .shader(Shader.LINES_SHADER)
                .lineWidth(new LineWidth(OptionalDouble.of(2)))
                .build(true)
        );

        private TesseractConnectionRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
            super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
        }

        public static RenderLayer getLayer() {
            return LAYER;
        }
    }
}
