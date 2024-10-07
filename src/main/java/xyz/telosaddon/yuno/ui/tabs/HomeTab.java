package xyz.telosaddon.yuno.ui.tabs;

import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.ui.CustomElement;
import xyz.telosaddon.yuno.ui.CustomUiManager;
import xyz.telosaddon.yuno.ui.elements.CustomButton;
import xyz.telosaddon.yuno.ui.elements.CustomText;
import xyz.telosaddon.yuno.ui.elements.CustomTextField;
import xyz.telosaddon.yuno.utils.config.Config;

import java.util.Arrays;
import java.util.List;

import static xyz.telosaddon.yuno.utils.TabListUtils.stripAllFormatting;


public class HomeTab extends AbstractTab{

    private final CustomUiManager uiManager;
    private List<CustomElement> elements;
    private final Config config;

    public HomeTab(CustomUiManager uiManager) {
        this.uiManager = uiManager;
        this.config = TelosAddon.getInstance().getConfig();
    }

    public void loadButtons() {
        boolean swingSetting = getConfig().getBoolean("SwingSetting");
        boolean swingIfNoCooldown = getConfig().getBoolean("SwingIfNoCooldown");
        boolean gammaSetting = getConfig().getBoolean("GammaSetting");
        boolean fpsSetting = getConfig().getBoolean("FPSSetting");
        boolean pingSetting = getConfig().getBoolean("PingSetting");
        boolean playtimeSetting = getConfig().getBoolean("PlaytimeSetting");
        boolean spawnBossesSetting = getConfig().getBoolean("SpawnBossesSetting");
        boolean soundSetting = getConfig().getBoolean("SoundSetting");
        boolean noFrontCameraFeatureEnabled = getConfig().getBoolean("NoFrontCameraFeatureEnabled");
        boolean nexusSuggestFeatureEnabled = getConfig().getBoolean("NexusSuggestFeatureEnabled");
        boolean discordRPCSetting = getConfig().getBoolean("DiscordRPCSetting");
        boolean RPCShowLocationSetting = getConfig().getBoolean("RPCShowLocationSetting");
        boolean RPCShowFightingSetting = getConfig().getBoolean("RPCShowFightingSetting");
        String discordDefaultStatusMessage = getConfig().getString("DiscordDefaultStatusMessage");
        String nexusSuggestText = getConfig().getString("NexusSuggestText");
        double nexusSuggestFrameLength = getConfig().getDouble("NexusSuggestFrameLength");

        var discordStatusTextField = new CustomTextField(170, 129, 150, 20, discordDefaultStatusMessage);
        var nexusSuggestTextField = new CustomTextField(170, 292, 150, 20, nexusSuggestText);
        var nexusSuggestFrameLengthField = new CustomTextField(170, 359, 150, 20, "" + nexusSuggestFrameLength);
        this.elements = Arrays.asList(

                new CustomButton(8, 83, 150, 20, "Hold To Swing", (button, toggled) -> {
                    toggle("SwingSetting", button.getText(), toggled);
                }).setToggled(swingSetting),

                new CustomButton(8, 106, 150, 20, "Swing Only If No Cooldown", (button, toggled) -> {
                    toggle("SwingIfNoCooldown", button.getText(), toggled);
                }).setToggled(swingIfNoCooldown),

                new CustomButton(8, 129, 150, 20, "Gamma", (button, toggled) -> {
                    toggle("GammaSetting", button.getText(), toggled);
                    TelosAddon.getInstance().toggleGamma(toggled);
                }).setToggled(gammaSetting),

                new CustomButton(8, 152, 150, 20, "Show FPS", (button, toggled) -> {
                    toggle("FPSSetting", button.getText(), toggled);
                }).setToggled(fpsSetting),

                new CustomButton(8, 175, 150, 20, "Show Ping", (button, toggled) -> {
                    toggle("PingSetting", button.getText(), toggled);
                }).setToggled(pingSetting),

                new CustomButton(8, 198, 150, 20, "Show Playtime", (button, toggled) -> {
                    toggle("PlaytimeSetting", button.getText(), toggled);
                }).setToggled(playtimeSetting),

                new CustomButton(8, 221, 150, 20, "Show Spawned Bosses", (button, toggled) -> {
                    toggle("SpawnBossesSetting", button.getText(), toggled);
                }).setToggled(spawnBossesSetting),

                new CustomButton(8, 244, 150, 20, "Custom Bag Sounds", (button, toggled) -> {
                    toggle("SoundSetting", button.getText(), toggled);
                }).setToggled(soundSetting),

                new CustomButton(8, 267, 150, 20, "Disable Front Camera", ((button, toggled) -> {
                    toggle("NoFrontCameraFeatureEnabled", button.getText(), toggled);
                })).setToggled(noFrontCameraFeatureEnabled),

                new CustomButton(170, 83, 150, 20, "Discord Rich Presence", ((button, toggled) -> {
                    toggle("DiscordRPCSetting", button.getText(), toggled);
                })).setToggled(discordRPCSetting),

                new CustomText(170, 115, "Change default status text:"),
                discordStatusTextField,
                new CustomButton(170, 152, 150, 20, "Confirm change", ((button) -> {
                    if (!isValidInputString(discordStatusTextField.getText())){
                        String toggleText ="§6There was an §cerror §6with your input! Please try again!";
                        TelosAddon.getInstance().sendMessage(toggleText);
                    }
                    getConfig().set("DiscordDefaultStatusMessage", stripAllFormatting(discordStatusTextField.getText()));
                })),

                new CustomButton(170, 175, 150, 20, "Show location", ((button, toggled) -> {
                    toggle("RPCShowLocationSetting", button.getText(), toggled);
                })).setToggled(RPCShowLocationSetting),

                new CustomButton(170, 198, 150, 20, "Show current boss", ((button, toggled) -> {
                    toggle("RPCShowFightingSetting", button.getText(), toggled);
                })).setToggled(RPCShowFightingSetting),

                new CustomButton(170, 230, 150, 20, "Nexus Alert", ((button, toggled) -> {
                    toggle("NexusSuggestFeatureEnabled", button.getText(), toggled);
                })).setToggled(nexusSuggestFeatureEnabled),

                new CustomText(170, 278, "Change default alert text:"),
                nexusSuggestTextField,
                new CustomButton(170, 315, 150, 20, "Confirm change", (button) -> {
                    if (!isValidInputString(nexusSuggestTextField.getText())){
                        String toggleText ="§6There was an §cerror §6with your input! Please try again!";
                        TelosAddon.getInstance().sendMessage(toggleText);
                    }
                    getConfig().set("NexusSuggestText", stripAllFormatting(nexusSuggestTextField.getText()));
                }),
                new CustomText(170, 347, "Nexus Alert DPS frame length:"),
                nexusSuggestFrameLengthField,
                new CustomButton(170, 382, 150, 20, "Confirm change", (button) -> {
                    String input = nexusSuggestFrameLengthField.getText();
                    try {
                        float length = Float.parseFloat(input);
                        config.set("NexusSuggestFrameLength", (double) length);
                    } catch (NumberFormatException e) {
                        TelosAddon.getInstance().sendMessage("Wrong Format! Not a float!");
                    }
                })
        );

        uiManager.getCustomElements().addAll(this.elements);
    }


    private static boolean isValidInputString(String input){
        return (input.length() > 0 && input.length() < 23);
    }



}
