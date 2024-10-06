package xyz.telosaddon.yuno.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Util;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.ui.elements.CustomButton;
import xyz.telosaddon.yuno.ui.tabs.*;
import xyz.telosaddon.yuno.utils.config.Config;

import java.util.ArrayList;
import java.util.List;

public class CustomUiManager {

    private final MinecraftClient mc;
    private final Config config;
    private List<CustomElement> customElements;
    private Tabs currentTab;

    public CustomUiManager() {
        this.customElements = new ArrayList<>();
        this.mc = MinecraftClient.getInstance();
        this.config = TelosAddon.getInstance().getConfig();
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        for(CustomElement element : this.customElements) {
            element.render(context, mouseX, mouseY, delta);
        }

    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        for(CustomElement element : this.customElements) {
            if(element.mouseClicked(mouseX, mouseY, button))
                return true;
        }
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {

        for(CustomElement element : this.customElements) {
            if(element.isFocused() && element.mouseDragged(mouseX, mouseY, button, deltaX, deltaY))
                return true;
        }
        return false;

    }

    public void keyPressed(int keyCode, int scanCode, int modifiers) {

        for(CustomElement element : this.customElements) {
            if(element.isFocused()) {
                element.keyPressed(keyCode, scanCode, modifiers);
            }
        }

    }

    public void charTyped(char chr, int modifiers) {

        for(CustomElement element : this.customElements) {

            if(element.isFocused()) {
                element.charTyped(chr, modifiers);
            }

        }
    }

    public void switchTab(Tabs tabType) {
        TelosAddon.getInstance().setEditMode(false);
        this.currentTab = tabType;
        clearCustomElements();
        switch (tabType) {
            case HOME -> {
                HomeTab homeTab = new HomeTab(this);
                homeTab.loadButtons();
            }
            case GUI -> {
                GuiTab guiTab = new GuiTab(this);
                guiTab.loadButtons();
            }
            case SETTINGS -> {
                SettingsTab settingsTab = new SettingsTab(this);
                settingsTab.loadButtons();
            }
            case TELEPORT -> {
                TeleportTab tpTab = new TeleportTab(this);
                tpTab.loadButtons();
            }
            case RANGE -> {
                RangeTab rangeTab = new RangeTab(this);
                rangeTab.loadButtons();
            }
            case AUCTION -> {
                AuctionTab auctionTab = new AuctionTab(this);
                auctionTab.loadButtons();
            }
        }
    }

    public void addCustomElement(CustomElement customElement) {
        this.customElements.add(customElement);
    }

    public void clearCustomElements() {
        this.customElements.clear();

        this.addCustomElement(new CustomButton(8, 55, 50, 20, "Home", (button -> {
            if(this.getCurrentTab() != Tabs.HOME)
                this.switchTab(Tabs.HOME);

        }), isOnTab(Tabs.HOME)).setTextInMiddle(true));

        this.addCustomElement(new CustomButton(63, 55, 50, 20, "Gui", (button -> {
            if(this.getCurrentTab() != Tabs.GUI)
                this.switchTab(Tabs.GUI);

        }), isOnTab(Tabs.GUI)).setTextInMiddle(true));

        this.addCustomElement(new CustomButton(118, 55, 50, 20, "Settings", (button -> {
            if(this.getCurrentTab() != Tabs.SETTINGS)
                this.switchTab(Tabs.SETTINGS);
        }), isOnTab(Tabs.SETTINGS)).setTextInMiddle(true));

        this.addCustomElement(new CustomButton(173, 55, 50, 20, "Teleport", (button -> {
            if(this.getCurrentTab() != Tabs.TELEPORT)
                this.switchTab(Tabs.TELEPORT);
        }), isOnTab(Tabs.TELEPORT)).setTextInMiddle(true));

        this.addCustomElement(new CustomButton(228, 55, 50, 20, "Range", (button -> {
            if(this.getCurrentTab() != Tabs.RANGE)
                this.switchTab(Tabs.RANGE);
        }), isOnTab(Tabs.RANGE)).setTextInMiddle(true));

        this.addCustomElement(new CustomButton(283, 55, 50, 20, "Auction", (button -> {
            if(this.getCurrentTab() != Tabs.AUCTION)
                this.switchTab(Tabs.AUCTION);
        }), isOnTab(Tabs.AUCTION)).setTextInMiddle(true));

        this.addCustomElement(new CustomButton(8, mc.getWindow().getScaledHeight() - 51, 150, 20, "Join my Discord", (button) -> {
            Util.getOperatingSystem().open("https://discord.gg/sDDeDkxFF6");
        }).setTextInMiddle(true));

        this.addCustomElement(new CustomButton(8, mc.getWindow().getScaledHeight() - 28, 150, 20, "Done", (button) -> {
            mc.setScreen(null);
        }).setTextInMiddle(true));
    }

    public void editMode() {
        this.customElements.clear();

        this.addCustomElement(new CustomButton(8, mc.getWindow().getScaledHeight() - 28, 150, 20, "Done", (button) -> {
            TelosAddon.getInstance().setEditMode(false);
            this.switchTab(Tabs.HOME);
        }).setTextInMiddle(true));
    }

    public List<CustomElement> getCustomElements() { return this.customElements; }
    public Tabs getCurrentTab() { return this.currentTab; }
    private boolean isOnTab(Tabs tab) {
        return tab == this.currentTab;
    }
}
