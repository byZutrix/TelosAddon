package xyz.telosaddon.yuno.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.sound.SoundManager;
import xyz.telosaddon.yuno.utils.config.Config;

public abstract class AbstractCustomElement implements CustomElement{

    private final MinecraftClient mc;
    private final TelosAddon ta;
    private final Config config;
    private final SoundManager soundManager;

    public AbstractCustomElement() {
        this.mc = MinecraftClient.getInstance();
        this.ta = TelosAddon.getInstance();
        this.config = this.ta.getConfig();
        this.soundManager = this.ta.getSoundManager();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {}

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) { return false; }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) { return false; }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {}

    @Override
    public void charTyped(char chr, int modifiers) {}

    @Override
    public boolean isFocused() { return false; }

    public MinecraftClient getMinecraftClient() { return this.mc; }
    public TelosAddon getTelosAddon() { return this.ta; }
    public Config getConfig() { return this.config; }
    public SoundManager getSoundManager() { return this.soundManager; }

}
