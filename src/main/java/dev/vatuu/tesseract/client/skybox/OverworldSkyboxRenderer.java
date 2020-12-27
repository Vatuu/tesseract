package dev.vatuu.tesseract.client.skybox;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

public class OverworldSkyboxRenderer extends SkyboxRenderer {

    private static final Identifier SUN = new Identifier("textures/environment/sun.png");
    private static final Identifier MOON_PHASES = new Identifier("textures/environment/moon_phases.png");

    @Override
    public void renderSky(MatrixStack stack, ClientWorld w, Camera cam, float delta) {
        RenderSystem.disableTexture();
        Vec3d vec3d = w.method_23777(cam.getPos(), delta);
        float f = (float)vec3d.x;
        float g = (float)vec3d.y;
        float h = (float)vec3d.z;
        BackgroundRenderer.setFogBlack();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.depthMask(false);
        RenderSystem.enableFog();
        RenderSystem.color3f(f, g, h);
        this.lightSkyBuffer.bind();
        this.skyVertexFormat.startDrawing(0L);
        this.lightSkyBuffer.draw(stack.peek().getModel());
        VertexBuffer.unbind();
        this.skyVertexFormat.endDrawing();
        RenderSystem.disableFog();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float[] fs = w.getSkyProperties().getFogColorOverride(w.getSkyAngle(delta), delta);
        float r;
        float s;
        float o;
        float p;
        float q;
        if (fs != null) {
            RenderSystem.disableTexture();
            RenderSystem.shadeModel(7425);
            stack.push();
            stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
            r = MathHelper.sin(w.getSkyAngleRadians(delta)) < 0.0F ? 180.0F : 0.0F;
            stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(r));
            stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
            float j = fs[0];
            s = fs[1];
            float l = fs[2];
            Matrix4f matrix4f = stack.peek().getModel();
            bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
            bufferBuilder.vertex(matrix4f, 0.0F, 100.0F, 0.0F).color(j, s, l, fs[3]).next();
            boolean m = true;

            for(int n = 0; n <= 16; ++n) {
                o = (float)n * 6.2831855F / 16.0F;
                p = MathHelper.sin(o);
                q = MathHelper.cos(o);
                bufferBuilder.vertex(matrix4f, p * 120.0F, q * 120.0F, -q * 40.0F * fs[3]).color(fs[0], fs[1], fs[2], 0.0F).next();
            }

            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            stack.pop();
            RenderSystem.shadeModel(7424);
        }

        RenderSystem.enableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        stack.push();
        r = 1.0F - w.getRainGradient(delta);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, r);
        stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
        stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(w.getSkyAngle(delta) * 360.0F));
        Matrix4f matrix4f2 = stack.peek().getModel();
        s = 30.0F;
        this.textureManager.bindTexture(SUN);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f2, -s, 100.0F, -s).texture(0.0F, 0.0F).next();
        bufferBuilder.vertex(matrix4f2, s, 100.0F, -s).texture(1.0F, 0.0F).next();
        bufferBuilder.vertex(matrix4f2, s, 100.0F, s).texture(1.0F, 1.0F).next();
        bufferBuilder.vertex(matrix4f2, -s, 100.0F, s).texture(0.0F, 1.0F).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        s = 20.0F;
        this.textureManager.bindTexture(MOON_PHASES);
        int t = w.getMoonPhase();
        int u = t % 4;
        int v = t / 4 % 2;
        float somethingElse = (float)(u) / 4.0F;
        o = (float)(v) / 2.0F;
        p = (float)(u + 1) / 4.0F;
        q = (float)(v + 1) / 2.0F;
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f2, -s, -100.0F, s).texture(p, q).next();
        bufferBuilder.vertex(matrix4f2, s, -100.0F, s).texture(somethingElse, q).next();
        bufferBuilder.vertex(matrix4f2, s, -100.0F, -s).texture(somethingElse, o).next();
        bufferBuilder.vertex(matrix4f2, -s, -100.0F, -s).texture(p, o).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.disableTexture();
        float aa = w.method_23787(delta) * r;
        if (aa > 0.0F) {
            RenderSystem.color4f(aa, aa, aa, aa);
            this.starsBuffer.bind();
            this.skyVertexFormat.startDrawing(0L);
            this.starsBuffer.draw(stack.peek().getModel());
            VertexBuffer.unbind();
            this.skyVertexFormat.endDrawing();
        }

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableFog();
        stack.pop();
        RenderSystem.disableTexture();
        RenderSystem.color3f(0.0F, 0.0F, 0.0F);
        double d = client.player.getCameraPosVec(delta).y - w.getLevelProperties().getSkyDarknessHeight();
        if (d < 0.0D) {
            stack.push();
            stack.translate(0.0D, 12.0D, 0.0D);
            this.darkSkyBuffer.bind();
            this.skyVertexFormat.startDrawing(0L);
            this.darkSkyBuffer.draw(stack.peek().getModel());
            VertexBuffer.unbind();
            this.skyVertexFormat.endDrawing();
            stack.pop();
        }

        if (w.getSkyProperties().isAlternateSkyColor()) {
            RenderSystem.color3f(f * 0.2F + 0.04F, g * 0.2F + 0.04F, h * 0.6F + 0.1F);
        } else {
            RenderSystem.color3f(f, g, h);
        }

        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
        RenderSystem.disableFog();
    }

    @Override
    public void renderStars(BufferBuilder builder) {
        Random random = new Random(10842L);
        builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        for(int i = 0; i < 1500; ++i) {
            double d = random.nextFloat() * 2.0F - 1.0F;
            double e = random.nextFloat() * 2.0F - 1.0F;
            double f = random.nextFloat() * 2.0F - 1.0F;
            double g = 0.15F + random.nextFloat() * 0.1F;
            double h = d * d + e * e + f * f;
            if (h < 1.0D && h > 0.01D) {
                h = 1.0D / Math.sqrt(h);
                d *= h;
                e *= h;
                f *= h;
                double j = d * 100.0D;
                double k = e * 100.0D;
                double l = f * 100.0D;
                double m = Math.atan2(d, f);
                double n = Math.sin(m);
                double o = Math.cos(m);
                double p = Math.atan2(Math.sqrt(d * d + f * f), e);
                double q = Math.sin(p);
                double r = Math.cos(p);
                double s = random.nextDouble() * 3.141592653589793D * 2.0D;
                double t = Math.sin(s);
                double u = Math.cos(s);

                for(int v = 0; v < 4; ++v) {
                    double x = (double)((v & 2) - 1) * g;
                    double y = (double)((v + 1 & 2) - 1) * g;
                    double aa = x * u - y * t;
                    double ab = y * u + x * t;
                    double ad = aa * q + 0.0D * r;
                    double ae = 0.0D * q - aa * r;
                    double af = ae * n - ab * o;
                    double ah = ab * n + ae * o;
                    builder.vertex(j + af, k + ad, l + ah).next();
                }
            }
        }
    }

    @Override
    public void renderSkyHalf(BufferBuilder builder, float y, boolean isBottom) {
        builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        for(int k = -384; k <= 384; k += 64) {
            for(int l = -384; l <= 384; l += 64) {
                float f = (float)k;
                float g = (float)(k + 64);
                if (isBottom) {
                    g = (float)k;
                    f = (float)(k + 64);
                }

                builder.vertex(f, y, l).next();
                builder.vertex(g, y, l).next();
                builder.vertex(g, y, l + 64).next();
                builder.vertex(f, y, l + 64).next();
            }
        }
    }
}
