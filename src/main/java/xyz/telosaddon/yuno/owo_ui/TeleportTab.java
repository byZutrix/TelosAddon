package xyz.telosaddon.yuno.owo_ui;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.gui.screen.Screen;

public class TeleportTab extends BaseUIModelScreen<FlowLayout> {

    private final Screen parent;

    public TeleportTab(@Nullable Screen parent) {
        super(FlowLayout.class, DataSource.asset(Identifier.of("telosaddon", "teleporttab")));
        this.parent = parent;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        //tab buttons
        rootComponent.childById(ButtonComponent.class, "Gui").onPress(button -> {
            this.client.setScreen(new GuiTab(this));
        });
        rootComponent.childById(ButtonComponent.class, "Settings").onPress(button -> {
            this.client.setScreen(new SettingsTab(this));
        });
        rootComponent.childById(ButtonComponent.class, "Home").onPress(button -> {
            this.client.setScreen(new HomeTab());
        });
        rootComponent.childById(ButtonComponent.class, "Range").onPress(button -> {
            this.client.setScreen(new RangeTab(this));
        });
    }
}