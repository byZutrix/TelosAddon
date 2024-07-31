package xyz.telosaddon.yuno.ui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import xyz.telosaddon.yuno.utils.Config;

import java.awt.*;

public class CustomButton {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final int x;
    private int y;
    private final int width;
    private final int height;
    private final String text;
    protected boolean hovered;
    protected CustomButton.PressAction onPress;
    protected CustomButton.ToggleAction onToggle;
    private boolean toggled = false;
    private int menuColor;

    public CustomButton(int x, int y, int width, int height, String text, CustomButton.PressAction onPress) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.onPress = onPress;
        menuColor = new Color(255, 215, 0).getRGB();
    }

    public CustomButton(int x, int y, int width, int height, String text, CustomButton.ToggleAction onToggle) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.onToggle = onToggle;
        menuColor = new Color(255, 215, 0).getRGB();
    }

    public CustomButton setToggled(boolean toggled) {
        this.toggled = toggled;
        return this;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TextRenderer tr = mc.textRenderer;
        this.hovered = context.scissorContains(mouseX, mouseY)
                && isHovered(mouseX, mouseY);

        int fillColor = this.hovered ? new Color(0, 0, 0, 150).getRGB() : new Color(0, 0, 0, 100).getRGB();

        context.fill(this.x, this.y, this.x + this.width, this.y + this.height, fillColor);
        context.drawBorder(this.x, this.y, this.width, this.height, new Color(255, 255 ,255, 50).getRGB());

        if(onToggle != null) {
            int boxSize = 10;
            int boxX = this.x + this.width - boxSize - 5;
            int boxY = this.y + (this.height - boxSize) / 2;

            context.drawBorder(boxX, boxY,  boxSize, boxSize, menuColor);
            if(toggled) {
                context.fill(boxX + 2, boxY + 2, boxX + boxSize - 2, boxY + boxSize - 2, menuColor);
            }
        }

        context.drawText(tr, this.text, this.x + 4, this.y + (this.height - 8) / 2, 0xFFFFFF, false);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= this.x
                && mouseY >= this.y
                && mouseX < this.x + this.width
                && mouseY < this.y + this.height;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0 && isHovered((int) mouseX, (int) mouseY)) {

            if(onPress != null) {
                this.onPress.onPress(this);
            } else if (onToggle != null) {
                this.toggled = !this.toggled;
                this.onToggle.onToggle(this, this.toggled);
            }


            return true;
        }
        return false;
    }

    public void setMenuColor(int color) {
        this.menuColor = color;
    }

    public int getMenuColor() { return this.menuColor; }

    public void setY(int y) {
        this.y = y;
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
