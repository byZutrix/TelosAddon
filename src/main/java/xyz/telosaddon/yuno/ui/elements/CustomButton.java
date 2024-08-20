package xyz.telosaddon.yuno.ui.elements;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import xyz.telosaddon.yuno.ui.AbstractCustomElement;
import xyz.telosaddon.yuno.utils.FontHelper;

import java.awt.*;

public class CustomButton extends AbstractCustomElement {

    private int x;
    private int y;
    private final int width;
    private final int height;
    private final String text;
    protected boolean hovered;
    protected CustomButton.PressAction onPress;
    protected CustomButton.ToggleAction onToggle;
    private boolean toggled = false;
    private boolean isOnTab = false;
    private boolean textInMiddle = false;


    public CustomButton(int x, int y, int width, int height, String text, CustomButton.PressAction onPress) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.onPress = onPress;
    }

    public CustomButton(int x, int y, int width, int height, String text, CustomButton.PressAction onPress, boolean isOnTab) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.onPress = onPress;
        this.isOnTab = isOnTab;
    }

    public CustomButton(int x, int y, int width, int height, String text, CustomButton.ToggleAction onToggle) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.onToggle = onToggle;
    }

    public CustomButton setToggled(boolean toggled) {
        this.toggled = toggled;
        return this;
    }

    public CustomButton setTextInMiddle(boolean value) {
        this.textInMiddle = value;
        return this;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TextRenderer tr = getMinecraftClient().textRenderer;
        this.hovered = context.scissorContains(mouseX, mouseY)
                && isHovered(mouseX, mouseY);

        int fillColor = this.hovered ? new Color(0, 0, 0, 150).getRGB() : getConfig().getInteger("FillColor");

        context.fill(this.x, this.y, this.x + this.width, this.y + this.height, fillColor);
        context.drawBorder(this.x, this.y, this.width, this.height, getConfig().getInteger("BorderColor"));

        if(this.isOnTab)
            context.drawHorizontalLine(this.x + 5, this.x + this.width - 8, this.y + this.height - 5, addAlpha(getConfig().getInteger("MenuColor"), 200));

        if(this.onToggle != null) {
            int boxSize = 10;
            int boxX = this.x + this.width - boxSize - 5;
            int boxY = this.y + (this.height - boxSize) / 2;

            int boxColor = addAlpha(getConfig().getInteger("MenuColor"), 200);
            context.drawBorder(boxX, boxY,  boxSize, boxSize, boxColor);
            if(toggled) {
                context.fill(boxX + 2, boxY + 2, boxX + boxSize - 2, boxY + boxSize - 2, boxColor);
            }
        }

        Text buttonText = FontHelper.toCustomFont(this.text, getConfig().getString("Font"));
        int textX = this.x + 4;

        if(this.textInMiddle)
            textX = this.x + (this.width - tr.getWidth(buttonText)) / 2;

        context.drawText(tr, buttonText, textX, this.y + (this.height - 8) / 2, 0xFFFFFF, true);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= this.x
                && mouseY >= this.y
                && mouseX < this.x + this.width
                && mouseY < this.y + this.height;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0 && isHovered((int) mouseX, (int) mouseY)) {

            if(onPress != null) {
                this.onPress.onPress(this);
            } else if (onToggle != null) {
                this.toggled = !this.toggled;
                this.onToggle.onToggle(this, this.toggled);
            }

            getSoundManager().playSound("button_click");

            return true;
        }
        return false;
    }

    private int addAlpha(int color, int alpha) {
        alpha = Math.min(255, Math.max(0, alpha));

        if((color & 0xFF000000) != 0)
            return color;
        return color | (alpha << 24);
    }

    public String getText() { return this.text; }

    @Environment(EnvType.CLIENT)
    public interface PressAction {
        void onPress(CustomButton button);
    }

    @Environment(EnvType.CLIENT)
    public interface ToggleAction {
        void onToggle(CustomButton button, boolean toggled);
    }
}
