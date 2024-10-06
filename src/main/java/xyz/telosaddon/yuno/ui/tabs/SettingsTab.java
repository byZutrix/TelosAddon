package xyz.telosaddon.yuno.ui.tabs;

import net.minecraft.client.MinecraftClient;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.ui.CustomElement;
import xyz.telosaddon.yuno.ui.CustomUiManager;
import xyz.telosaddon.yuno.ui.elements.CustomButton;
import xyz.telosaddon.yuno.ui.elements.CustomDropdown;
import xyz.telosaddon.yuno.ui.elements.CustomText;
import xyz.telosaddon.yuno.ui.elements.CustomTextField;
import xyz.telosaddon.yuno.utils.config.Config;
import xyz.telosaddon.yuno.utils.config.SerializeUtils;

import java.util.Arrays;
import java.util.List;

public class SettingsTab extends AbstractTab{

    private final MinecraftClient mc;
    private final Config config;
    private final CustomUiManager uiManager;
    private List<CustomElement> elements;

    public SettingsTab(CustomUiManager uiManager) {
        this.uiManager = uiManager;
        this.config = TelosAddon.getInstance().getConfig();
        this.mc = MinecraftClient.getInstance();

    }

    public void loadButtons() {

        var guiColorTextField = new CustomTextField(8, 95, 150, 20, "#000000");
        this.elements = Arrays.asList(
                new CustomText(8, 83, "Change Gui Color:"),
                guiColorTextField,
                new CustomButton(163, 95, 40, 20, "Apply", (button) -> {
                    String input = guiColorTextField.getText();
                    try {
                        int color = SerializeUtils.parseHexRGB(input);
                        config.set("MenuColor", color);
                    } catch (Exception e) {
                        TelosAddon.getInstance().sendMessage("Wrong Format! Use #000000");
                    }
                }).setTextInMiddle(true),
                new CustomButton(8, 118, 150, 20, "Edit Display Positions", (button -> {
                    TelosAddon.getInstance().setEditMode(true);
                    this.uiManager.editMode();
                })).setTextInMiddle(true),
                new CustomButton(8, 141, 150, 20, "Reset Display Positions", (button -> {
                    config.set("InfoX", 4);
                    config.set("InfoY", 4);
                    config.set("BagX", -1);
                    config.set("BagY", 60);
                })).setTextInMiddle(true),
                new CustomDropdown(8, 166, 150, 20, "Change Font", Arrays.asList(
                        "Default",
                        "Arial",
                        "Nokia CF",
                        "Roboto",
                        "Comic Sans",
                        "Silkscreen"
                ), ((button, value) -> {
                    switch (value) {
                        case "Nokia CF" -> config.set("Font", "nokiacf");
                        case "Roboto" -> config.set("Font", "roboto");
                        case "Silkscreen" -> config.set("Font", "silkscreen");
                        case "Arial" -> config.set("Font", "arial");
                        case "Comic Sans" -> config.set("Font", "comic_sans");
                        default -> config.set("Font", "default");
                    }
                }))



        );

        uiManager.getCustomElements().addAll(this.elements);
    }

}
