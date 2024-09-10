package xyz.telosaddon.yuno.ui.elements;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import xyz.telosaddon.yuno.ui.AbstractCustomElement;

public class CustomSlider extends AbstractCustomElement {

    private int x;
    private int y;
    private int width;
    private int height;
    private String text;

    public CustomSlider(int x, int y, int width, int height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TextRenderer tr = getMinecraftClient().textRenderer;



    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(button == 0) {

            return true;
        }
        return false;
    }

}
