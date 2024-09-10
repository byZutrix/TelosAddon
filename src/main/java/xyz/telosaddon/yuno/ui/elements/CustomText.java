package xyz.telosaddon.yuno.ui.elements;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import xyz.telosaddon.yuno.ui.AbstractCustomElement;
import xyz.telosaddon.yuno.utils.FontHelper;

public class CustomText extends AbstractCustomElement {

    private int x;
    private int y;
    private String text;
    private int color = -1;

    public CustomText(int x, int y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;
    }

    public CustomText(int x, int y, String text, int color) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TextRenderer tr = getMinecraftClient().textRenderer;
        int textColor = color == -1 ? 0xFFFFFFFF : this.color;

        Text customText = FontHelper.toCustomFont(this.text, getConfig().getString("Font"));
        context.drawText(tr, customText, this.x, this.y, textColor, true);
    }


}
