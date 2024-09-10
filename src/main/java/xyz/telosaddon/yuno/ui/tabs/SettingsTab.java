package xyz.telosaddon.yuno.ui.tabs;

import net.minecraft.client.MinecraftClient;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.ui.CustomElement;
import xyz.telosaddon.yuno.ui.CustomUiManager;
import xyz.telosaddon.yuno.ui.elements.CustomButton;
import xyz.telosaddon.yuno.ui.elements.CustomDropdown;
import xyz.telosaddon.yuno.ui.elements.CustomText;
import xyz.telosaddon.yuno.ui.elements.CustomTextField;
import xyz.telosaddon.yuno.utils.Config;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsTab {

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

        this.elements = Arrays.asList(
                new CustomTextField(8, 95, 150, 20, "#000000"),
                new CustomText(8, 83, "Change Gui Color:"),
                new CustomButton(163, 95, 40, 20, "Apply", (button) -> {
                    if(this.elements.get(0) == null && !(this.elements.get(0) instanceof CustomTextField)) return;
                    CustomTextField customTextField = (CustomTextField) this.elements.get(0);
                    String input = customTextField.getText();

                    setMenuColor(input);
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
                new CustomDropdown(8, 164, 150, 20, "Change Font", Arrays.asList(
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


    public void setMenuColor(String input) {
        if(input == null) {
            TelosAddon.getInstance().sendMessage("Wrong Format! Use #000000");
            return;
        }

        Pattern hexPattern = Pattern.compile("^#[0-9A-Fa-f]{6}$");
        Matcher matcher = hexPattern.matcher(input);

        if(!matcher.matches()) {
            TelosAddon.getInstance().sendMessage("Wrong Format! Use #000000");
            return;
        }

        input = input.substring(1);
        int color = Integer.parseInt(input, 16);
        config.set("MenuColor", color);
    }

}
