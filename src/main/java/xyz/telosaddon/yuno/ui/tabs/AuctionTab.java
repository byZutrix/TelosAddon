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

public class AuctionTab extends AbstractTab{
    private final CustomUiManager uiManager;
    private List<CustomElement> elements;
    private final Config config;

    public AuctionTab(CustomUiManager uiManager) {
        this.uiManager = uiManager;
        this.config = TelosAddon.getInstance().getConfig();
    }

    public void loadButtons() {
        boolean showListedPriceSetting = getConfig().getBoolean("ShowListedPrice");

        this.elements = Arrays.asList(
                new CustomButton(8, 83, 150, 20, "Show Listed Prices", (button, toggled) -> {
                    toggle("ShowListedPrice", button.getText(), toggled);
                }).setToggled(showListedPriceSetting)
        );

        uiManager.getCustomElements().addAll(this.elements);
    }

}
