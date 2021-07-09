package dev.teamtesseract.fractal.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.teamtesseract.fractal.client.rendering.LilTesseractRenderer;
import dev.teamtesseract.fractal.client.rendering.TesseractRenderer;
import dev.teamtesseract.fractal.client.rendering.Vec4f;
import dev.teamtesseract.fractal.lil.LilTesseractSettings;
import dev.teamtesseract.fractal.Fractal;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class LilConfigScreen extends Screen {

    private static final Text TITLE = new TranslatableText("gui.tesseract.lil_tesseract.title");
    private static final Identifier TEXTURE = new Identifier(Fractal.MOD_ID, "textures/gui/gui.png");

    private final LilTesseractSettings settings;
    private final TesseractRenderer renderer;

    private RotationSliderWidget rotateX, rotateY, rotateZ, rotateW;
    private CheckboxWidget buttonWireframe;

    public LilConfigScreen(LilTesseractSettings settings) {
        super(TITLE);
        this.settings = settings;
        this.renderer = new LilTesseractRenderer();
    }

    @Override
    public void init() {
        this.rotateX = addDrawableChild(new RotationSliderWidget(width / 2 - 80, height / 2 - 49, 126, 20, settings.rotations.x()));
        this.rotateY = addDrawableChild(new RotationSliderWidget(width / 2 - 80, height / 2 - 24, 126, 20, settings.rotations.y()));
        this.rotateZ = addDrawableChild(new RotationSliderWidget(width / 2 - 80, height / 2 - 1, 126, 20, settings.rotations.z()));
        this.rotateW = addDrawableChild(new RotationSliderWidget(width / 2 - 80, height / 2 + 24, 126, 20, settings.rotations.w()));
        this.buttonWireframe = addDrawableChild(new CheckboxWidget(width /2 - 113, height / 2 + 50, 20, 20, new TranslatableText("gui.tesseract.lil_tesseract.wireframe"), settings.isWireframe));
        Text applyText = new TranslatableText("gui.tesseract.lil_tesseract.apply");
        addDrawableChild(new ButtonWidget(width / 2 + 80, height / 2 + 50, textRenderer.getWidth(applyText) + 10, 20, applyText, (b) -> {
            this.settings.isWireframe = buttonWireframe.isChecked();
            this.settings.rotations = new Vec4f(rotateX.getValue(), rotateY.getValue(), rotateZ.getValue(), rotateW.getValue());
        }));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        renderBackground(stack);
        RenderSystem.setShaderTexture(0, TEXTURE);

        drawTexture(stack, width / 2 - 126, height / 2 - 83, 0, 0,256, 166);
        drawTexture(stack, width / 2 - 113, height / 2 - 49, 60, 166, 20, 20); //X
        drawTexture(stack, width / 2 - 113, height / 2 - 24, 80, 166, 20, 20); //Y
        drawTexture(stack, width / 2 - 113, height / 2 - 1, 100, 166, 20, 20); //Z
        drawTexture(stack, width / 2 - 113, height / 2 + 24, 120, 166, 20, 20); //W

        int titleWidth = textRenderer.getWidth(TITLE);
        textRenderer.draw(stack, TITLE, width / 2F - titleWidth / 2F, height / 2F - 73, Formatting.DARK_GRAY.getColorValue());

        drawTesseract(stack,width / 2 + 73,height / 2 - 20);

        super.render(stack, mouseX, mouseY, delta);

        renderer.updateRotation(new Vec4f(rotateX.getValue(), rotateY.getValue(), rotateZ.getValue(), rotateW.getValue()));
    }

    private void drawTesseract(MatrixStack stack, int x, int y) {
        stack.push();

        stack.translate((float)x, (float)y, 1100.0F);
        stack.scale(1.0F, 1.0F, -1.0F);
        stack.translate(0.0D, 0.0D, 1000.0D);
        stack.scale(30.0F, 30.0F, 30.0F);
        stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(60.0f));
        stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45.0f));
        stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(0.0f));
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        renderer.render(stack, immediate, 3);
        immediate.draw();

        stack.pop();
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    private static class RotationSliderWidget extends ButtonWidget {

        protected double value;

        protected RotationSliderWidget(int x, int y, int width, int height, double progress) {
            super(x, y, width, height, new LiteralText(""), (b) -> {});
            this.value = progress;
            this.updateMessage();
        }

        public float getValue() {
            return (float)value;
        }

        public int getYImage(boolean isHovered) {
            return 0;
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

        public void playDownSound(SoundManager soundManager) { }

        public void onRelease(double mouseX, double mouseY) {
            super.playDownSound(MinecraftClient.getInstance().getSoundManager());
        }

        public void updateMessage() {
            this.setMessage(new LiteralText(String.format("%.3f", value)));
        };
    }
}
