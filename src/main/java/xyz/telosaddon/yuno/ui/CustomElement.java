package xyz.telosaddon.yuno.ui;

import net.minecraft.client.gui.DrawContext;

public interface CustomElement {
    void render(DrawContext context, int mouseX, int mouseY, float delta);

    boolean mouseClicked(double mouseX, double mouseY, int button);

    boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY);

    void keyPressed(int keyCode, int scanCode, int modifiers);

    void charTyped(char chr, int modifiers);

    boolean isFocused();
}
