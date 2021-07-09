package dev.teamtesseract.fractal.client.skybox;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
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
        boolean isFogThick = w.getSkyProperties().useThickFog(MathHelper.floor(cam.getPos().getX()), MathHelper.floor(cam.getPos().getY())) || this.client.inGameHud.getBossBarHud().shouldThickenFog();
        BackgroundRenderer.applyFog(cam, BackgroundRenderer.FogType.FOG_SKY, MinecraftClient.getInstance().gameRenderer.getViewDistance(), isFogThick);

        RenderSystem.disableTexture();
        Vec3d vec3d = w.method_23777(this.client.gameRenderer.getCamera().getPos(), delta);
        Matrix4f model = stack.peek().getModel();
        float g = (float)vec3d.x;
        float h = (float)vec3d.y;
        float i = (float)vec3d.z;
        BackgroundRenderer.setFogBlack();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(g, h, i, 1.0F);
        Shader shader = RenderSystem.getShader();
        this.lightSkyBuffer.setShader(stack.peek().getModel(), model, shader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float[] fs = w.getSkyProperties().getFogColorOverride(w.getSkyAngle(delta), delta);
        float s, t, p, q, r;
        if (fs != null) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.disableTexture();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            stack.push();
            stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
            s = MathHelper.sin(w.getSkyAngleRadians(delta)) < 0.0F ? 180.0F : 0.0F;
            stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(s));
            stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
            float k = fs[0];
                t = fs[1];
                float m = fs[2];
                Matrix4f matrix4f2 = stack.peek().getModel();
                bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
                bufferBuilder.vertex(matrix4f2, 0.0F, 100.0F, 0.0F).color(k, t, m, fs[3]).next();
                for(int o = 0; o <= 16; ++o) {
                    p = (float)o * 6.2831855F / 16.0F;
                    q = MathHelper.sin(p);
                    r = MathHelper.cos(p);
                    bufferBuilder.vertex(matrix4f2, q * 120.0F, r * 120.0F, -r * 40.0F * fs[3]).color(fs[0], fs[1], fs[2], 0.0F).next();
                }

                bufferBuilder.end();
                BufferRenderer.draw(bufferBuilder);
                stack.pop();
            }

            RenderSystem.enableTexture();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
            stack.push();
            s = 1.0F - w.getRainGradient(delta);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, s);
            stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
            stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(w.getSkyAngle(delta) * 360.0F));
            Matrix4f matrix4f3 = stack.peek().getModel();
            t = 30.0F;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, SUN);
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f3, -t, 100.0F, -t).texture(0.0F, 0.0F).next();
            bufferBuilder.vertex(matrix4f3, t, 100.0F, -t).texture(1.0F, 0.0F).next();
            bufferBuilder.vertex(matrix4f3, t, 100.0F, t).texture(1.0F, 1.0F).next();
            bufferBuilder.vertex(matrix4f3, -t, 100.0F, t).texture(0.0F, 1.0F).next();
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            t = 20.0F;
            RenderSystem.setShaderTexture(0, MOON_PHASES);
            int u = w.getMoonPhase();
            int v = u % 4;
            int something = u / 4 % 2;
            float x = (float)(v) / 4.0F;
            p = (float)(something) / 2.0F;
            q = (float)(v + 1) / 4.0F;
            r = (float)(something + 1) / 2.0F;
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            bufferBuilder.vertex(matrix4f3, -t, -100.0F, t).texture(q, r).next();
            bufferBuilder.vertex(matrix4f3, t, -100.0F, t).texture(x, r).next();
            bufferBuilder.vertex(matrix4f3, t, -100.0F, -t).texture(x, p).next();
            bufferBuilder.vertex(matrix4f3, -t, -100.0F, -t).texture(q, p).next();
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            RenderSystem.disableTexture();
            float ab = w.method_23787(delta) * s;
            if (ab > 0.0F) {
                RenderSystem.setShaderColor(ab, ab, ab, ab);
                BackgroundRenderer.method_23792();
                this.starsBuffer.setShader(stack.peek().getModel(), model, GameRenderer.getPositionShader());
                BackgroundRenderer.applyFog(cam, BackgroundRenderer.FogType.FOG_SKY, MinecraftClient.getInstance().gameRenderer.getViewDistance(), isFogThick);
            }

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            stack.pop();
            RenderSystem.disableTexture();
            RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
            double d = this.client.player.getCameraPosVec(delta).y - w.getLevelProperties().getSkyDarknessHeight(w);
            if (d < 0.0D) {
                stack.push();
                stack.translate(0.0D, 12.0D, 0.0D);
                this.darkSkyBuffer.setShader(stack.peek().getModel(), model, shader);
                stack.pop();
            }

            if (w.getSkyProperties().isAlternateSkyColor()) {
                RenderSystem.setShaderColor(g * 0.2F + 0.04F, h * 0.2F + 0.04F, i * 0.6F + 0.1F, 1.0F);
            } else {
                RenderSystem.setShaderColor(g, h, i, 1.0F);
            }

            RenderSystem.enableTexture();
            RenderSystem.depthMask(true);
    }

    @Override
    public void renderStars(VertexConsumer builder) {
        Random random = new Random(10842L);

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
    public void renderSkyHalf(VertexConsumer builder, float y, boolean isBottom) {
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
