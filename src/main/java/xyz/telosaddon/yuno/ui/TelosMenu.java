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
import xyz.telosaddon.yuno.utils.FontHelper;


@Environment(EnvType.CLIENT)
public class TelosMenu extends Screen {

    private final MinecraftClient mc;
    private final CustomUiManager customUiManager;
    private final Config config;

    public TelosMenu() {
        super(Text.literal("Telos Menu"));
        this.mc = MinecraftClient.getInstance();
        this.config = TelosAddon.getInstance().getConfig();
        this.customUiManager = new CustomUiManager();
    }

    @Override
    protected void init() {
        this.customUiManager.switchTab(Tabs.HOME);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(!TelosAddon.getInstance().isEditMode()) {
            super.render(context, mouseX, mouseY, delta);

            TextRenderer tr = mc.textRenderer;
            drawTitle(context, tr);

            String bugString = "§cPlease report bugs to §7'§4kamiyuno§7'§c on Discord§7!";
            Text bugText = FontHelper.toCustomFont(bugString, config.getString("Font"));
            int bugTextWidth = tr.getWidth(bugText);
            context.drawText(tr, bugText, width - bugTextWidth - 4, height - 12, 0xFFFFFF, true);
        }

        this.customUiManager.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if(this.customUiManager.mouseClicked(mouseX, mouseY, button))
            return true;

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {

        boolean isEditMode = TelosAddon.getInstance().isEditMode();
        if(isEditMode) {
            int infoX = config.getInteger("InfoX");
            int infoY = config.getInteger("InfoY");
            int infoWidth = TelosAddon.getInstance().infoWidth;
            int infoHeight = TelosAddon.getInstance().infoHeight;

            int bagX = config.getInteger("BagX");
            int bagY = config.getInteger("BagY");
            int bagWidth = TelosAddon.getInstance().bagWidth;
            int bagHeight = TelosAddon.getInstance().bagHeight;

            if(mouseX >= infoX + 10 && mouseY >= infoY + 10  && mouseX < infoX + infoWidth + 10 && mouseY < infoY + infoHeight + 10) {

                config.set("InfoX", (int) mouseX - infoWidth / 2);
                config.set("InfoY", (int) mouseY - infoHeight / 2);

                return true;
            }

            if(mouseX >= bagX + 10 && mouseY >= bagY + 10  && mouseX < bagX + bagWidth + 10 && mouseY < bagY + bagHeight + 10) {

                config.set("BagX", (int) mouseX - bagWidth / 2);
                config.set("BagY", (int) mouseY - bagHeight / 2);

                return true;
            }
        }

        if(this.customUiManager.mouseDragged(mouseX, mouseY, button, deltaX, deltaY))
            return true;

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.customUiManager.keyPressed(keyCode, scanCode, modifiers);

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        this.customUiManager.charTyped(chr, modifiers);

        return super.charTyped(chr, modifiers);
    }

    @Override
    public void close() {
        this.client.setScreen(null);
        TelosAddon.getInstance().setEditMode(false);
    }

    private void drawTitle(DrawContext context, TextRenderer tr) {
        String title = "Telos Addon | BETA-v0.21b";
        Text titleText = FontHelper.toCustomFont(title, config.getString("Font"));

        float titleScale = 1.5f;
        context.getMatrices().push();
        context.getMatrices().scale(titleScale, titleScale, titleScale);
        context.drawText(tr, titleText, (int) (10 / titleScale), (int) (35 / titleScale), config.getInteger("MenuColor"), true);
        context.getMatrices().pop();
    }

}
