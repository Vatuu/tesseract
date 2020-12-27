package dev.vatuu.tesseract.extras.fancy;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

public class TesseractCapeFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private static final UUID YOURS_TRUELY = UUID.fromString("275df345-c3c5-4768-9af8-440d9cdf593e");

    private static final Random RANDOM = new Random(31100L);
    private static final int LAYER_COUNT = 15;
    private static final float LAYER_0_INTENSITY = 0.15F;
    private static final List<RenderLayer> RENDER_LAYERS = IntStream.range(0, 16).mapToObj((i) -> RenderLayer.getEndPortal(i + 1)).collect(ImmutableList.toImmutableList());

    public TesseractCapeFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    public void render(MatrixStack stack, VertexConsumerProvider provider, int light, AbstractClientPlayerEntity p, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
        if (p.getUuid().equals(YOURS_TRUELY) && p.canRenderCapeTexture() && !p.isInvisible() && p.isPartVisible(PlayerModelPart.CAPE)) {
            ItemStack itemStack = p.getEquippedStack(EquipmentSlot.CHEST);
            if (itemStack.getItem() != Items.ELYTRA) {
                stack.push();
                stack.translate(0.0D, 0.0D, 0.125D);
                double x = MathHelper.lerp(tickDelta, p.prevCapeX, p.capeX) - MathHelper.lerp(tickDelta, p.prevX, p.getX());
                double y = MathHelper.lerp(tickDelta, p.prevCapeY, p.capeY) - MathHelper.lerp(tickDelta, p.prevY, p.getY());
                double z = MathHelper.lerp(tickDelta, p.prevCapeZ, p.capeZ) - MathHelper.lerp(tickDelta, p.prevZ, p.getZ());

                double o = MathHelper.sin(p.bodyYaw * 0.017453292F);
                double pold = (-MathHelper.cos(p.bodyYaw * 0.017453292F));

                float rotXPositive = (float)y * 10.0F;
                rotXPositive = MathHelper.clamp(rotXPositive, -6.0F, 32.0F);

                float rotXNegative = (float)(x * o + z * pold * 100.0F);
                rotXNegative = MathHelper.clamp(rotXNegative, 0.0F, 150.0F);

                float s = (float)(x * pold - z * o) * 100.0F;
                s = MathHelper.clamp(s, -20.0F, 20.0F);

                if (rotXNegative < 0.0F) {
                    rotXNegative = 0.0F;
                }

                float t = MathHelper.lerp(tickDelta, p.prevStrideDistance, p.strideDistance);
                rotXPositive += MathHelper.sin(MathHelper.lerp(tickDelta, p.prevHorizontalSpeed, p.horizontalSpeed) * 6.0F) * 32.0F * t;
                if (p.isInSneakingPose()) {
                    rotXPositive += 25.0F;
                }

                stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(6.0F + rotXNegative / 2.0F + rotXPositive));
                stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(s / 2.0F));
                stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - s / 2.0F));

                RANDOM.setSeed(31100L);

                float r = (RANDOM.nextFloat() * 0.5F + 0.1F) * LAYER_0_INTENSITY;
                float g = (RANDOM.nextFloat() * 0.5F + 0.4F) * LAYER_0_INTENSITY;
                float b = (RANDOM.nextFloat() * 0.5F + 0.5F) * LAYER_0_INTENSITY;
                getContextModel().cloak.render(stack, provider.getBuffer(RENDER_LAYERS.get(0)), light, OverlayTexture.DEFAULT_UV, r, g, b, 1.0F);

                for(int i = 1; i < LAYER_COUNT; i++) {
                    VertexConsumer consumer = provider.getBuffer(RENDER_LAYERS.get(i));
                    float intensity = 2.0F / (float)(18 - i);
                    r = (RANDOM.nextFloat() * 0.5F + 0.1F) * intensity;
                    g = (RANDOM.nextFloat() * 0.5F + 0.4F) * intensity;
                    b = (RANDOM.nextFloat() * 0.5F + 0.5F) * intensity;

                    getContextModel().cloak.render(stack, consumer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1.0F);
                }

                stack.pop();
            }
        }
    }
}
