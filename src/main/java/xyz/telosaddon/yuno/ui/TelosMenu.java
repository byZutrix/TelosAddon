package xyz.telosaddon.yuno.ui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.utils.Config;

import java.util.Arrays;
import java.util.List;


@Environment(EnvType.CLIENT)
public class TelosMenu extends Screen {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private List<CustomButton> customButtonList;
    private Config config;
    private int menuColor;

    public TelosMenu() {
        super(Text.literal("Telos Menu"));
        config = TelosAddon.getInstance().getConfig();
        menuColor = config.getInteger("MenuColor");
    }

    @Override
    protected void init() {
        boolean swingSetting = config.getBoolean("SwingSetting");
        boolean gammaSetting = config.getBoolean("GammaSetting");
        boolean fpsSetting = config.getBoolean("FPSSetting");

        boolean greenSetting = config.getBoolean("GreenSetting");
        boolean goldSetting = config.getBoolean("GoldSetting");
        boolean whiteSetting = config.getBoolean("WhiteSetting");
        boolean blackSetting = config.getBoolean("BlackSetting");
        boolean xMasSetting = config.getBoolean("XMasSetting");
        boolean crossSetting = config.getBoolean("CrossSetting");
        boolean relicSetting = config.getBoolean("RelicSetting");
        boolean totalRunSetting = config.getBoolean("TotalRunSetting");
        boolean noWhiteRunSetting = config.getBoolean("NoWhiteRunSetting");
        boolean lifetimeSetting = config.getBoolean("LifetimeSetting");

        // 23 y-value
        customButtonList = Arrays.asList(

                new CustomButton(8, 55, 150, 20, "Hold To Swing", (button, toggled) -> {
                    toggle("SwingSetting", button.getText(), toggled);
                }).setToggled(swingSetting),

                new CustomButton(8, 78, 150, 20, "Gamma", (button, toggled) -> {
                    toggle("GammaSetting", button.getText(), toggled);
                    TelosAddon.getInstance().toggleGamma(toggled);
                }).setToggled(gammaSetting),

                new CustomButton(8, 101, 150, 20, "Show FPS", (button, toggled) -> {
                    toggle("FPSSetting", button.getText(), toggled);
                }).setToggled(fpsSetting),



                new CustomButton(8, 124, 150, 20, "GreenBag Counter", (button, toggled) -> {
                    toggle("GreenSetting", button.getText(), toggled);
                }).setToggled(greenSetting),

                new CustomButton(8, 147, 150, 20, "GoldBag Counter", (button, toggled) -> {
                    toggle("GoldSetting", button.getText(), toggled);
                }).setToggled(goldSetting),

                new CustomButton(8, 170, 150, 20, "WhiteBag Counter", (button, toggled) -> {
                    toggle("WhiteSetting", button.getText(), toggled);
                }).setToggled(whiteSetting),

                new CustomButton(8, 193, 150, 20, "BlackBag Counter", (button, toggled) -> {
                    toggle("BlackSetting", button.getText(), toggled);
                }).setToggled(blackSetting),

                new CustomButton(8, 216, 150, 20, "XMasBag Counter", (button, toggled) -> {
                    toggle("XMasSetting", button.getText(), toggled);
                }).setToggled(xMasSetting),

                new CustomButton(8, 239, 150, 20, "Cross Counter", (button, toggled) -> {
                    toggle("CrossSetting", button.getText(), toggled);
                }).setToggled(crossSetting),

                new CustomButton(8, 262, 150, 20, "Relic Counter", (button, toggled) -> {
                    toggle("RelicSetting", button.getText(), toggled);
                }).setToggled(relicSetting),

                new CustomButton(8, 285, 150, 20, "Total Boss Runs", (button, toggled) -> {
                    toggle("TotalRunSetting", button.getText(), toggled);
                }).setToggled(totalRunSetting),

                new CustomButton(8, 308, 150, 20, "No Whites Runs", (button, toggled) -> {
                    toggle("NoWhiteRunSetting", button.getText(), toggled);
                }).setToggled(noWhiteRunSetting),

                new CustomButton(8, 331, 150, 20, "Lifetime Counter", (button, toggled) -> {
                    toggle("LifetimeSetting", button.getText(), toggled);
                }).setToggled(lifetimeSetting),



                new CustomButton(8, height - 28, 150, 20, "Done", (button) -> {
                    mc.setScreen(null);
                })

        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (CustomButton customButton : customButtonList) {
            if (customButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.renderBackground(context, mouseX, mouseY, delta);
        TextRenderer tr = mc.textRenderer;
        drawTitle(context, tr);

        for (CustomButton customButton : customButtonList) {
            customButton.render(context, mouseX, mouseY, delta);
        }

        String bugText = "§cPlease report bugs to §7'§4kamiyuno§7'§c on Discord§7!";
        int bugTextWidth = tr.getWidth(bugText);
        context.drawText(tr, bugText, width - bugTextWidth - 4, height - 12, 0xFFFFFF, false);

    }

    private void drawTitle(DrawContext context, TextRenderer tr) {
        String title = "Telos Addon | BETA-v0.1";
        float titleScale = 1.5f;
        context.getMatrices().push();
        context.getMatrices().scale(titleScale, titleScale, titleScale);
        context.drawText(tr, title, (int) (10 / titleScale), (int) (35 / titleScale), menuColor, false);
        context.getMatrices().pop();
    }

    private void toggle(String settingName, String btnName, boolean toggled) {
        config.set(settingName, toggled);
        String msg = toggled ? "set §7'§6" + btnName + "§7'§6 to §a§lTRUE§7!" : "Set §7'§6" + btnName +"§7'§6 to §c§lFALSE§7!";
        TelosAddon.getInstance().sendMessage(msg);
    }

}
