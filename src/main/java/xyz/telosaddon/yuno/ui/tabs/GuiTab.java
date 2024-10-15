package xyz.telosaddon.yuno.ui.tabs;

import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.ui.CustomElement;
import xyz.telosaddon.yuno.ui.elements.CustomButton;
import xyz.telosaddon.yuno.ui.CustomUiManager;
import xyz.telosaddon.yuno.utils.config.Config;

import java.util.Arrays;
import java.util.List;

public class GuiTab {

    private final Config config;
    private final CustomUiManager uiManager;
    private List<CustomElement> elements;

    public GuiTab(CustomUiManager uiManager) {
        config = TelosAddon.getInstance().getConfig();
        this.uiManager = uiManager;
    }

    public void loadButtons() {
        boolean greenSetting = config.getBoolean("GreenSetting");
        boolean goldSetting = config.getBoolean("GoldSetting");
        boolean whiteSetting = config.getBoolean("WhiteSetting");
        boolean blackSetting = config.getBoolean("BlackSetting");
        boolean xMasSetting = config.getBoolean("XMasSetting");
        boolean crossSetting = config.getBoolean("CrossSetting");
        boolean relicSetting = config.getBoolean("RelicSetting");
        boolean totalRunSetting = config.getBoolean("TotalRunSetting");
        boolean noWhiteRunSetting = config.getBoolean("NoWhiteRunSetting");
        boolean noBlackRunSetting = config.getBoolean("NoBlackRunSetting");
        boolean lifetimeSetting = config.getBoolean("LifetimeSetting");

        this.elements = Arrays.asList(

                new CustomButton(8, 83, 150, 20, "GreenBag Counter", (button, toggled) -> {
                    toggle("GreenSetting", button.getText(), toggled);
                }).setToggled(greenSetting),

                new CustomButton(8, 106, 150, 20, "GoldBag Counter", (button, toggled) -> {
                    toggle("GoldSetting", button.getText(), toggled);
                }).setToggled(goldSetting),

                new CustomButton(8, 129, 150, 20, "WhiteBag Counter", (button, toggled) -> {
                    toggle("WhiteSetting", button.getText(), toggled);
                }).setToggled(whiteSetting),

                new CustomButton(8, 152, 150, 20, "BlackBag Counter", (button, toggled) -> {
                    toggle("BlackSetting", button.getText(), toggled);
                }).setToggled(blackSetting),

                new CustomButton(8, 175, 150, 20, "XMasBag Counter", (button, toggled) -> {
                    toggle("XMasSetting", button.getText(), toggled);
                }).setToggled(xMasSetting),

                new CustomButton(8, 198, 150, 20, "Cross Counter", (button, toggled) -> {
                    toggle("CrossSetting", button.getText(), toggled);
                }).setToggled(crossSetting),

                new CustomButton(8, 221, 150, 20, "Relic Counter", (button, toggled) -> {
                    toggle("RelicSetting", button.getText(), toggled);
                }).setToggled(relicSetting),

                new CustomButton(8, 244, 150, 20, "Total Boss Runs", (button, toggled) -> {
                    toggle("TotalRunSetting", button.getText(), toggled);
                }).setToggled(totalRunSetting),

                new CustomButton(8, 267, 150, 20, "No Whites Runs", (button, toggled) -> {
                    toggle("NoWhiteRunSetting", button.getText(), toggled);
                }).setToggled(noWhiteRunSetting),

                new CustomButton(8, 290, 150, 20, "No Black Runs", (button, toggled) -> {
                    toggle("NoBlackRunSetting", button.getText(), toggled);
                }).setToggled(noBlackRunSetting),

                new CustomButton(175, 83, 150, 20, "Lifetime Counter", (button, toggled) -> {
                    toggle("LifetimeSetting", button.getText(), toggled);
                }).setToggled(lifetimeSetting)

        );

        uiManager.getCustomElements().addAll(this.elements);
    }

    public void toggle(String name, String btnName, boolean toggled) {
        config.set(name, toggled);
        String toggleText = toggled ? "§6Set §7'§f" + btnName + "§7' §6to §a§lTRUE§7!" : "§6Set §7'§f" + btnName + "§7' §6to §c§lFALSE§7!";
        TelosAddon.getInstance().sendMessage(toggleText);
    }

}
