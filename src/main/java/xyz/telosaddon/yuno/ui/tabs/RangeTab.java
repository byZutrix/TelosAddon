package xyz.telosaddon.yuno.ui.tabs;

import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.ui.CustomElement;
import xyz.telosaddon.yuno.ui.CustomUiManager;
import xyz.telosaddon.yuno.ui.elements.CustomButton;
import xyz.telosaddon.yuno.ui.elements.CustomText;
import xyz.telosaddon.yuno.ui.elements.CustomTextField;
import xyz.telosaddon.yuno.utils.config.Config;
import xyz.telosaddon.yuno.utils.config.SerializeUtils;

import java.util.Arrays;
import java.util.List;

public class RangeTab extends AbstractTab {
	private final CustomUiManager uiManager;
	private List<CustomElement> elements;
	private final Config config;

	public RangeTab(CustomUiManager uiManager) {
		this.uiManager = uiManager;
		this.config = TelosAddon.getInstance().getConfig();
	}

	public void loadButtons() {
		boolean showMainRangeSetting = getConfig().getBoolean("ShowMainRangeFeatureEnabled");
		boolean showOffHandRangeSetting = getConfig().getBoolean("ShowOffHandRangeFeatureEnabled");
		double showMainRangeHeight = getConfig().getDouble("ShowMainRangeFeatureHeight");
		double showOffHandRangeHeight = getConfig().getDouble("ShowOffHandRangeFeatureHeight");
		boolean showMainRangeInFirstPersonSetting = getConfig().getBoolean("ShowMainRangeInFirstPerson");
		boolean showOffHandRangeInFirstPersonSetting = getConfig().getBoolean("ShowOffHandRangeInFirstPerson");

		var mainRangeHeightField = new CustomTextField(8, 141, 250, 20, "" + showMainRangeHeight);
		var mainRangeColorField = new CustomTextField(8, 176, 250, 20, "#AARRGGBB");

		var offHandRangeHeightField = new CustomTextField(8, 300, 250, 20, "" + showOffHandRangeHeight);
		var offHandRangeColorField = new CustomTextField(8, 335, 250, 20, "#AARRGGBB");
		this.elements = Arrays.asList(
				// Main Hand
				new CustomButton(8, 83, 250, 20, "Show Main Hand's range", (button, toggled) -> {
					toggle("ShowMainRangeFeatureEnabled", button.getText(), toggled);
				}).setToggled(showMainRangeSetting),
				new CustomButton(8, 106, 250, 20, "Show Main Hand's range in first person", (button, toggled) -> {
					toggle("ShowMainRangeInFirstPerson", button.getText(), toggled);
				}).setToggled(showMainRangeInFirstPersonSetting),
				new CustomText(8, 129, "Main Hand's range circle's height:"),
				mainRangeHeightField,
				new CustomButton(263, 141, 40, 20, "Apply", (button) -> {
					String input = mainRangeHeightField.getText();
					try {
						float height = Float.parseFloat(input);
						config.set("ShowMainRangeFeatureHeight", (double) height);
					} catch (NumberFormatException e) {
						TelosAddon.getInstance().sendMessage("Wrong Format! Not a float!");
					}
				}).setTextInMiddle(true),
				new CustomText(8, 164, "Main Hand's range circle's color:"),
				mainRangeColorField,
				new CustomButton(263, 176, 40, 20, "Apply", (button) -> {
					String input = mainRangeColorField.getText();
					try {
						int color = SerializeUtils.parseHexARGB(input);
						config.set("ShowMainRangeFeatureColor", color);
					} catch (Exception e) {
						TelosAddon.getInstance().sendMessage("Wrong Format! Use #AARRGGBB!");
					}
				}).setTextInMiddle(true),

				// OffHand
				new CustomButton(8, 242, 250, 20, "Show Off Hand's range", (button, toggled) -> {
					toggle("ShowOffHandRangeFeatureEnabled", button.getText(), toggled);
				}).setToggled(showOffHandRangeSetting),
				new CustomButton(8, 265, 250, 20, "Show Off Hand's range in first person", (button, toggled) -> {
					toggle("ShowOffHandRangeInFirstPerson", button.getText(), toggled);
				}).setToggled(showOffHandRangeInFirstPersonSetting),
				new CustomText(8, 288, "Off Hand's range circle's height:"),
				offHandRangeHeightField,
				new CustomButton(263, 300, 40, 20, "Apply", (button) -> {
					String input = offHandRangeHeightField.getText();
					try {
						float height = Float.parseFloat(input);
						config.set("ShowOffHandRangeFeatureHeight", (double) height);
					} catch (NumberFormatException e) {
						TelosAddon.getInstance().sendMessage("Wrong Format! Not a float!");
					}
				}).setTextInMiddle(true),
				new CustomText(8, 323, "Off Hand's range circle's color:"),
				offHandRangeColorField,
				new CustomButton(263, 335, 40, 20, "Apply", (button) -> {
					String input = offHandRangeColorField.getText();
					try {
						int color = SerializeUtils.parseHexARGB(input);
						config.set("ShowOffHandRangeFeatureColor", color);
					} catch (Exception e) {
						TelosAddon.getInstance().sendMessage("Wrong Format! Use #AARRGGBB!");
					}
				}).setTextInMiddle(true)
		);

		uiManager.getCustomElements().addAll(this.elements);
	}
}
