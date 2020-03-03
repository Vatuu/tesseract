package dev.vatuu.tesseract.impl.extras.lil.rendering;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;
import net.minecraft.client.render.VertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.OptionalDouble;

public class TesseractRenderLayers {

    public static RenderLayer TESSERACT_VERTEX = TesseractVertexRenderLayer.getLayer();
    public static RenderLayer TESSERACT_SIDE = TesseractConnectionRenderLayer.getLayer();

    private static class TesseractVertexRenderLayer extends RenderLayer {

        private static final VertexFormat VERTEX_FORMAT = new VertexFormat(ImmutableList.<VertexFormatElement>builder()
                .add(VertexFormats.POSITION_ELEMENT)
                .add(VertexFormats.COLOR_ELEMENT)
                .build()
        );

        private static final RenderLayer LAYER = RenderLayer.of("tesseract_vertex", VERTEX_FORMAT, GL11.GL_QUADS, 256, RenderLayer.MultiPhaseParameters.builder()
                .transparency(TRANSLUCENT_TRANSPARENCY)
                .build(true)
        );


        private TesseractVertexRenderLayer(String name, VertexFormat vertexFormat, int drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
            super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
        }

        public static RenderLayer getLayer() {
            return LAYER;
        }
    }

    private static class TesseractConnectionRenderLayer extends RenderLayer {

        private static final VertexFormat VERTEX_FORMAT = new VertexFormat(ImmutableList.<VertexFormatElement>builder()
                .add(VertexFormats.POSITION_ELEMENT)
                .add(VertexFormats.COLOR_ELEMENT)
                .build()
        );

        private static final RenderLayer LAYER = RenderLayer.of("tesseract_side", VERTEX_FORMAT, GL11.GL_LINES, 256, RenderLayer.MultiPhaseParameters.builder()
                .transparency(TRANSLUCENT_TRANSPARENCY)
                .lineWidth(new LineWidth(OptionalDouble.of(2)))
                .build(true)
        );

        private TesseractConnectionRenderLayer(String name, VertexFormat vertexFormat, int drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
            super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
        }

        public static RenderLayer getLayer() {
            return LAYER;
        }
    }
}
