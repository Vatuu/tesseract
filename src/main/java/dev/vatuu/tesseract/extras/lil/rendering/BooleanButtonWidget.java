package dev.vatuu.tesseract.extras.lil.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.vatuu.tesseract.Tesseract;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class BooleanButtonWidget extends ButtonWidget {

    private boolean state;
    private ButtonIcon stateTex;

    public BooleanButtonWidget(int x, int y, boolean start, PressAction action){
        super(x, y, 20, 20, new LiteralText(""), action);
        this.state = start;
        this.stateTex = state ? ButtonIcon.TRUE : ButtonIcon.FALSE;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.visible){
            MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier(Tesseract.MOD_ID, "textures/gui/gui.png"));
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int yt;
            if (this.isHovered())
                yt = stateTex.yDifTex;
            else
                yt = stateTex.yTex;

            if(!active)
                yt = stateTex.yDisTex;

            this.drawTexture(matrices, x, y, stateTex.xTex, yt, 20, 20);
        }
    }

    @Override
    public void onPress() {
        this.state = !state;
        this.stateTex = state ? ButtonIcon.TRUE : ButtonIcon.FALSE;
        this.onPress.onPress(this);
    }

    public boolean currentState(){
        return state;
    }

    private enum ButtonIcon {

        TRUE(20, 166, 186, 206),
        FALSE(0, 166, 186, 206);

        public int xTex, yTex, yDifTex, yDisTex;

        ButtonIcon(int x, int y, int yDif, int yDis) {
            this.xTex = x;
            this.yTex = y;
            this.yDifTex = yDif;
            this.yDisTex = yDis;
        }
    }
}
