package dev.vatuu.tesseract.extras.lil;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.extras.lil.rendering.BooleanButtonWidget;
import dev.vatuu.tesseract.extras.lil.rendering.TesseractRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class LilConfigScreen extends Screen {

    private TesseractSettings settings;
    private TesseractRenderer renderer;

    private RotationSliderWidget rotateX, rotateY, rotateZ, rotateW;
    private BooleanButtonWidget buttonWireframe;

    public LilConfigScreen(TesseractSettings settings) {
        super(new TranslatableText("tesseract configuration"));
        this.settings = settings;
        this.renderer = new TesseractRenderer(settings);
    }

    public void init(){
        this.rotateX = addButton(new RotationSliderWidget(width / 2 - 80, height / 2 - 49, 126, 20, settings.rotateX));
        this.rotateY = addButton(new RotationSliderWidget(width / 2 - 80, height / 2 - 24, 126, 20, settings.rotateY));
        this.rotateZ = addButton(new RotationSliderWidget(width / 2 - 80, height / 2 - 1, 126, 20, settings.rotateZ));
        this.rotateW = addButton(new RotationSliderWidget(width / 2 - 80, height / 2 + 24, 126, 20, settings.rotateW));
        this.buttonWireframe = addButton(new BooleanButtonWidget(width / 2 - 113, height / 2 + 50, settings.wireframe, (b) -> {}));
        addButton(new ButtonWidget(width / 2 + 80, height / 2 + 50, textRenderer.getWidth("Apply") + 10, 20, new LiteralText("Apply"), (b) -> {
            this.settings.wireframe = buttonWireframe.currentState();
            this.settings.rotateX = (float)rotateX.getValue();
            this.settings.rotateY = (float)rotateY.getValue();
            this.settings.rotateZ = (float)rotateZ.getValue();
            this.settings.rotateW = (float)rotateW.getValue();
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier(Tesseract.MOD_ID, "textures/gui/gui.png"));

        drawTexture(matrices, width / 2 - 126, height / 2 - 83, 0, 0,256, 166);
        drawTexture(matrices, width / 2 - 113, height / 2 - 49, 60, 166, 20, 20); //X
        drawTexture(matrices, width / 2 - 113, height / 2 - 24, 80, 166, 20, 20); //Y
        drawTexture(matrices, width / 2 - 113, height / 2 - 1, 100, 166, 20, 20); //Z
        drawTexture(matrices, width / 2 - 113, height / 2 + 24, 120, 166, 20, 20); //W

        textRenderer.draw(matrices, "'lil Tesseract Configuration", width / 2 - (textRenderer.getWidth("'lil Tesseract Configuration") / 2), height / 2 - 83 + 10, 0x404040);
        textRenderer.draw(matrices, "Wireframe", width / 2 - 88, height / 2 + 56, 0x404040);

        drawTesseract(width / 2 + 73,height / 2 - 20, 30);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private void drawTesseract(int x, int y, int size) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)x, (float)y, 1100.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(0.0D, 0.0D, 1000.0D);
        matrixStack.scale((float)size, (float)size, (float)size);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0.0f));
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(0.0f));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(0.0f));
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        renderer.render(matrixStack, immediate, 3);
        immediate.draw();
        RenderSystem.popMatrix();
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    private class RotationSliderWidget extends AbstractButtonWidget {

        protected double value;

        protected RotationSliderWidget(int x, int y, int width, int height, double progress) {
            super(x, y, width, height, new LiteralText(""));
            this.value = progress;
            this.updateMessage();
        }

        public double getValue() {
            return value;
        }

        public int getYImage(boolean isHovered) {
            return 0;
        }

        @Override
        protected void renderBg(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
            client.getTextureManager().bindTexture(WIDGETS_LOCATION);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = (this.isHovered() ? 2 : 1) * 20;
            drawTexture(matrices, (this.x + (int)(this.value * (double)(this.width - 8))), this.y, 0, 46 + i, 4, 20);
            drawTexture(matrices, (this.x + (int)(this.value * (double)(this.width - 8))) + 4, this.y, 196, 46 + i, 4, 20);
            super.renderBg(matrices, client, mouseX, mouseY);
        }

        public void onClick(double mouseX, double mouseY) {
            this.setValueFromMouse(mouseX);
        }

        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            boolean bl = keyCode == 263;
            if (bl || keyCode == 262) {
                float f = bl ? -1.0F : 1.0F;
                this.setValue(this.value + (double)(f / (float)(this.width - 8)));
            }

            return false;
        }

        private void setValueFromMouse(double d) {
            this.setValue((d - (double)(this.x + 4)) / (double)(this.width - 8));
        }

        private void setValue(double mouseX) {
            double d = this.value;
            this.value = MathHelper.clamp(mouseX, -1.0D, 1.0D);
            this.updateMessage();
        }

        public void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
            this.setValueFromMouse(mouseX);
            super.onDrag(mouseX, mouseY, deltaX, deltaY);
        }

        public void playDownSound(SoundManager soundManager) {
        }

        public void onRelease(double mouseX, double mouseY) {
            super.playDownSound(MinecraftClient.getInstance().getSoundManager());
        }

        public void updateMessage() {
            this.setMessage(new LiteralText(String.format("%.3f", value)));
        };
    }
}
