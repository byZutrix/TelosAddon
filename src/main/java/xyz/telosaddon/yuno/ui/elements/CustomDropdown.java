package xyz.telosaddon.yuno.ui.elements;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import xyz.telosaddon.yuno.ui.AbstractCustomElement;
import xyz.telosaddon.yuno.utils.FontHelper;

import java.awt.*;
import java.util.List;

public class CustomDropdown extends AbstractCustomElement {

    private int x;
    private int y;
    private int width;
    private int height;
    private String text;
    private List<String> list;
    private boolean isOpen = false;
    private boolean hovered = false;
    protected CustomDropdown.PressAction onPress;

    public CustomDropdown(int x, int y, int width, int height, String text, List<String> list, CustomDropdown.PressAction onPress) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.list = list;
        this.onPress = onPress;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TextRenderer tr = getMinecraftClient().textRenderer;
        this.hovered = context.scissorContains(mouseX, mouseY)
                && isHovered(mouseX, mouseY);

        int fillColor = this.hovered ? new Color(0, 0, 0, 150).getRGB() : getConfig().getInteger("FillColor");

        context.fill(this.x, this.y, this.x + this.width, this.y + this.height, fillColor);
        context.drawBorder(this.x, this.y, this.width, this.height, getConfig().getInteger("BorderColor"));

        if(this.isOpen) {

            for(int i = 0; i < this.list.size(); i++) {
                int boxY = this.y + this.height * (i + 1);
                int fillColor2 = getConfig().getInteger("FillColor");
                if(getDropdownButton(mouseX, mouseY) == i)
                    fillColor2 = new Color(0, 0, 0, 150).getRGB();

                context.fill(this.x, boxY, this.x + this.width, boxY + this.height, fillColor2);

                Text buttonText = FontHelper.toCustomFont(this.list.get(i), getConfig().getString("Font"));
                context.drawText(tr, buttonText, this.x + (this.width - tr.getWidth(buttonText)) / 2, boxY + (this.height - 8) / 2,
                        0xFFFFFF, true);
            }

            context.drawBorder(this.x, this.y + this.height, this.width, this.height * this.list.size(), getConfig().getInteger("BorderColor"));
        }

        Text buttonText = FontHelper.toCustomFont(this.text, getConfig().getString("Font"));
        context.drawText(tr, this.text, this.x + (this.width - tr.getWidth(buttonText)) / 2, this.y + (this.height - 8) / 2, 0xFFFFFF, true);

        String openText = this.isOpen ? "△" : "▽";
        context.drawText(tr, openText, this.x + this.width - 12, this.y + (this.height - 8) / 2, 0xFFFFFF, true);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isHovered((int) mouseX, (int) mouseY)) {
            this.isOpen = !this.isOpen;

            getSoundManager().playSound("button_click");

            return true;
        }

        if(button == 0 && getDropdownButton((int) mouseX, (int) mouseY) != -1) {

            String value = this.list.get(getDropdownButton((int) mouseX, (int) mouseY));
            this.onPress.onPress(this, value);

            getSoundManager().playSound("button_click");

            return true;
        }

        return false;
    }

    public int getDropdownButton(int mouseX, int mouseY) {
        for(int i = 0; i < this.list.size(); i++) {
            int boxY = this.y + this.height * (i + 1);

            if(mouseX >= this.x
                    && mouseY >= boxY
                    && mouseX < this.x + this.width
                    && mouseY < boxY + this.height) {
                return i;
            }

        }
        return -1;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= this.x
                && mouseY >= this.y
                && mouseX < this.x + this.width
                && mouseY < this.y + this.height;
    }

    @Environment(EnvType.CLIENT)
    public interface PressAction {
        void onPress(CustomDropdown button, String value);
    }
}
