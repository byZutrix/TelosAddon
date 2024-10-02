package xyz.telosaddon.yuno.ui.tabs;

import net.minecraft.util.Util;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.ui.CustomElement;
import xyz.telosaddon.yuno.ui.CustomUiManager;
import xyz.telosaddon.yuno.ui.elements.CustomButton;
import xyz.telosaddon.yuno.ui.elements.CustomText;
import xyz.telosaddon.yuno.ui.elements.CustomTextField;

import java.util.Arrays;
import java.util.List;

public class HomeTab extends AbstractTab{

    private final CustomUiManager uiManager;
    private List<CustomElement> elements;

    public HomeTab(CustomUiManager uiManager) {
        this.uiManager = uiManager;
    }

    public void loadButtons() {
        boolean swingSetting = getConfig().getBoolean("SwingSetting");
        boolean gammaSetting = getConfig().getBoolean("GammaSetting");
        boolean fpsSetting = getConfig().getBoolean("FPSSetting");
        boolean pingSetting = getConfig().getBoolean("PingSetting");
        boolean playtimeSetting = getConfig().getBoolean("PlaytimeSetting");
        boolean spawnBossesSetting = getConfig().getBoolean("SpawnBossesSetting");
        boolean soundSetting = getConfig().getBoolean("SoundSetting");
        boolean discordRPCSetting = getConfig().getBoolean("DiscordRPCSetting");
        boolean RPCShowLocationSetting = getConfig().getBoolean("RPCShowLocationSetting");
        boolean RPCShowFightingSetting = getConfig().getBoolean("RPCShowFightingSetting");
        String discordDefaultStatusMessage = getConfig().getString("DiscordDefaultStatusMessage");

        var discordStatusTextField = new CustomTextField(170, 129, 150, 20, discordDefaultStatusMessage);
        this.elements = Arrays.asList(

                new CustomButton(8, 83, 150, 20, "Hold To Swing", (button, toggled) -> {
                    toggle("SwingSetting", button.getText(), toggled);
                }).setToggled(swingSetting),

                new CustomButton(8, 106, 150, 20, "Gamma", (button, toggled) -> {
                    toggle("GammaSetting", button.getText(), toggled);
                    TelosAddon.getInstance().toggleGamma(toggled);
                }).setToggled(gammaSetting),

                new CustomButton(8, 129, 150, 20, "Show FPS", (button, toggled) -> {
                    toggle("FPSSetting", button.getText(), toggled);
                }).setToggled(fpsSetting),

                new CustomButton(8, 152, 150, 20, "Show Ping", (button, toggled) -> {
                    toggle("PingSetting", button.getText(), toggled);
                }).setToggled(pingSetting),

                new CustomButton(8, 175, 150, 20, "Show Playtime", (button, toggled) -> {
                    toggle("PlaytimeSetting", button.getText(), toggled);
                }).setToggled(playtimeSetting),

                new CustomButton(8, 198, 150, 20, "Show Spawned Bosses", (button, toggled) -> {
                    toggle("SpawnBossesSetting", button.getText(), toggled);
                }).setToggled(spawnBossesSetting),

                new CustomButton(8, 221, 150, 20, "Custom Bag Sounds", (button, toggled) -> {
                    toggle("SoundSetting", button.getText(), toggled);
                }).setToggled(soundSetting),
                new CustomButton(170, 83, 150, 20, "Discord Rich Presence", ((button, toggled) -> {
                    toggle("DiscordRPCSetting", button.getText(), toggled);
                })).setToggled(discordRPCSetting),
                new CustomText(170, 110, "Change default status text:"),
                discordStatusTextField,
                new CustomButton(170, 152, 150, 20, "Confirm", ((button) -> {
                    getConfig().set("DiscordDefaultStatusMessage", discordStatusTextField.getText());
                })),
                new CustomButton(170, 175, 150, 20, "Show location", ((button, toggled) -> {
                    toggle("RPCShowLocationSetting", button.getText(), toggled);
                })).setToggled(RPCShowLocationSetting),
                new CustomButton(170, 198, 150, 20, "Show current boss", ((button, toggled) -> {
                    toggle("RPCShowFightingSetting", button.getText(), toggled);
                })).setToggled(RPCShowFightingSetting)

        );

        uiManager.getCustomElements().addAll(this.elements);
    }



}
