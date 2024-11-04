package xyz.telosaddon.yuno.owo_ui;

import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.CheckboxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.minecraft.util.Identifier;
import xyz.telosaddon.yuno.utils.config.Config;

import java.util.function.Consumer;

public class HomeTab extends BaseUIModelScreen<FlowLayout> {
    public HomeTab() {
        super(FlowLayout.class, DataSource.asset(Identifier.of("telosaddon", "hometab")));
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
        rootComponent.childById(ButtonComponent.class, "Teleport").onPress(button -> {
            this.client.setScreen(new TeleportTab(this));
        });
        rootComponent.childById(ButtonComponent.class, "Range").onPress(button -> {
            this.client.setScreen(new RangeTab(this));
        });

        //the other buttons
        rootComponent.childById(CheckboxComponent.class, "SwingSetting").onChanged(listener -> {
            System.out.println("test");
        });
        rootComponent.childById(CheckboxComponent.class, "SwingIfNoCooldown");
        rootComponent.childById(CheckboxComponent.class, "GammaSetting");
        rootComponent.childById(CheckboxComponent.class, "FPSSetting");
        rootComponent.childById(CheckboxComponent.class, "PingSetting");
        rootComponent.childById(CheckboxComponent.class, "PlaytimeSetting");
        rootComponent.childById(CheckboxComponent.class, "SpawnBossesSetting");
        rootComponent.childById(CheckboxComponent.class, "SoundSetting");
        rootComponent.childById(CheckboxComponent.class, "DiscordRPCSetting");
        rootComponent.childById(CheckboxComponent.class, "DiscordDefaultStatusMessage");
        rootComponent.childById(CheckboxComponent.class, "RPCShowLocationSetting");
        rootComponent.childById(CheckboxComponent.class, "RPCShowFightingSetting");
    }
}
