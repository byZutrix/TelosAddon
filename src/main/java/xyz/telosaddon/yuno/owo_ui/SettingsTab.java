package xyz.telosaddon.yuno.owo_ui;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.gui.screen.Screen;

public class SettingsTab extends BaseUIModelScreen<FlowLayout> {

    private final Screen parent;

    public SettingsTab(@Nullable Screen parent) {
        super(FlowLayout.class, DataSource.asset(Identifier.of("telosaddon", "settingstab")));
        this.parent = parent;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        //tab buttons
        rootComponent.childById(ButtonComponent.class, "Gui").onPress(button -> {
            this.client.setScreen(new GuiTab(this));
        });
        rootComponent.childById(ButtonComponent.class, "Home").onPress(button -> {
            this.client.setScreen(new HomeTab());
        });
        rootComponent.childById(ButtonComponent.class, "Teleport").onPress(button -> {
            this.client.setScreen(new TeleportTab(this));
        });
        rootComponent.childById(ButtonComponent.class, "Range").onPress(button -> {
            this.client.setScreen(new RangeTab(this));
        });
    }
}