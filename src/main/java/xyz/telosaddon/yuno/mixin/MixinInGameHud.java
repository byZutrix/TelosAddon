package xyz.telosaddon.yuno.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.utils.config.Config;
import xyz.telosaddon.yuno.utils.FontHelper;

import java.util.*;
import java.util.List;

import static xyz.telosaddon.yuno.utils.LocalAPI.getCurrentClientPing;
import static xyz.telosaddon.yuno.utils.TabListUtils.getPing;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {

    @Shadow @Final
    private MinecraftClient client;

    @Shadow public abstract void clear();

    @Shadow public abstract void render(DrawContext context, RenderTickCounter tickCounter);

    @Shadow private int titleRemainTicks;

    @Shadow private @Nullable Text title;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {

        if(client.inGameHud.getDebugHud().shouldShowDebugHud()) return;

        TextRenderer tr = client.textRenderer;

        int width = client.getWindow().getScaledWidth();
        //int height = client.getWindow().getScaledHeight();

        Config config = TelosAddon.getInstance().getConfig();

        if(config.getInteger("BagX") == -1)
            config.set("BagX", width - 130);

        boolean isEditMode = TelosAddon.getInstance().isEditMode();
        String fontName = config.getString("Font");

        boolean fpsSetting = config.getBoolean("FPSSetting");
        boolean pingSetting = config.getBoolean("PingSetting");
        boolean playtimeSetting = config.getBoolean("PlaytimeSetting");
        boolean spawnBossesSetting = config.getBoolean("SpawnBossesSetting");

        int greenBags = config.getBoolean("LifetimeSetting") ? config.getInteger("GreenBags") : TelosAddon.getInstance().getBagCounter().get("GreenBags");
        int goldBags = config.getBoolean("LifetimeSetting") ? config.getInteger("GoldBags") : TelosAddon.getInstance().getBagCounter().get("GoldBags");
        int whiteBags = config.getBoolean("LifetimeSetting") ? config.getInteger("WhiteBags") : TelosAddon.getInstance().getBagCounter().get("WhiteBags");
        int blackBags = config.getBoolean("LifetimeSetting") ? config.getInteger("BlackBags") : TelosAddon.getInstance().getBagCounter().get("BlackBags");
        int xMasBags = config.getBoolean("LifetimeSetting") ? config.getInteger("XMasBags") : TelosAddon.getInstance().getBagCounter().get("XMasBags");
        int crosses = config.getBoolean("LifetimeSetting") ? config.getInteger("Crosses") : TelosAddon.getInstance().getBagCounter().get("Crosses");
        int relics = config.getBoolean("LifetimeSetting") ? config.getInteger("Relics") : TelosAddon.getInstance().getBagCounter().get("Relics");
        int totalRuns = config.getBoolean("LifetimeSetting") ? config.getInteger("TotalRuns") : TelosAddon.getInstance().getBagCounter().get("TotalRuns");
        int noWhitesRuns = config.getBoolean("LifetimeSetting") ? config.getInteger("NoWhiteRuns") : TelosAddon.getInstance().getBagCounter().get("NoWhiteRuns");
        int noBlackRuns = config.getBoolean("LifetimeSetting") ? config.getInteger("NoBlackRuns") : TelosAddon.getInstance().getBagCounter().get("NoBlackRuns");

        List<String> bagTexts = new ArrayList<>();
        if(config.getBoolean("GreenSetting") || isEditMode)
            bagTexts.add("Green Bags§7: §f" + greenBags);
        if(config.getBoolean("GoldSetting") || isEditMode)
            bagTexts.add("Gold Bags§7: §f" + goldBags);
        if(config.getBoolean("WhiteSetting") || isEditMode)
            bagTexts.add("White Bags§7: §f" + whiteBags);
        if(config.getBoolean("BlackSetting") || isEditMode)
            bagTexts.add("Black Bags§7: §f" + blackBags);
        if(config.getBoolean("XMasSetting") || isEditMode)
            bagTexts.add("XMas Bags§7: §f" + xMasBags);
        if(config.getBoolean("CrossSetting") || isEditMode)
            bagTexts.add("Crosses§7: §f" + crosses);
        if(config.getBoolean("RelicSetting") || isEditMode)
            bagTexts.add("Relics§7: §f" + relics);
        if(config.getBoolean("TotalRunSetting") || isEditMode)
            bagTexts.add("Total Runs§7: §f" + totalRuns);
        if(config.getBoolean("NoWhiteRunSetting") || isEditMode)
            bagTexts.add("No Whites Runs§7: §f" + noWhitesRuns);
        if(config.getBoolean("NoBlackRunSetting") || isEditMode)
            bagTexts.add("No Black Runs§7: §f" + noBlackRuns);

        int bagY = config.getInteger("BagY");
        int bagX = config.getInteger("BagX");

        int yBackground = bagY - 25;
        if(!bagTexts.isEmpty()) {
            String title = config.getBoolean("LifetimeSetting") ? "Lifetime Stats" : "Session Stats";
            context.fill(bagX, yBackground, bagX + 120, bagY + bagTexts.size() * 13 + 5, config.getInteger("FillColor"));
            context.drawBorder(bagX, yBackground, 120, bagY - yBackground + bagTexts.size() * 13 + 5, config.getInteger("BorderColor"));
            context.drawHorizontalLine(bagX + 10, bagX + 110, bagY - 4, config.getInteger("BorderColor"));

            int titleWidth = tr.getWidth(FontHelper.toCustomFont(title, fontName));
            int midX = (bagX + (bagX + 120)) / 2;

            //context.drawText(tr, toCustomFont(title), midX - titleWidth / 2, bagY - 15, config.getInteger("MenuColor"), false);
            context.drawText(tr, FontHelper.toCustomFont(title, fontName), midX - titleWidth / 2, bagY - 15, config.getInteger("MenuColor"), true);
        }
        for(int i = 0; i < bagTexts.size(); i++) {
            context.drawText(tr, FontHelper.toCustomFont(bagTexts.get(i), fontName), bagX + 10, bagY + (i * 13), config.getInteger("MenuColor"), true);
        }

        int infoX = config.getInteger("InfoX");
        int infoY = config.getInteger("InfoY");
        List<String> infoList = new ArrayList<>();
        if(fpsSetting || isEditMode)
            infoList.add("FPS§7: §f" + client.getCurrentFps());

        if(pingSetting || isEditMode)
            infoList.add("Ping§7: §f" + getCurrentClientPing());

        if(playtimeSetting || isEditMode)
            infoList.add("Playtime§7: §f" + TelosAddon.getInstance().getPlaytimeText());

        if(spawnBossesSetting || isEditMode) {
            for (String boss : TelosAddon.getInstance().getAliveBosses()) {
                infoList.add("Boss Spawned§7: §f" + boss);
            }
            if(isEditMode) {
                infoList.add("Boss Spawned§7: §fNAME");
                infoList.add("Boss Spawned§7: §fNAME");
            }
        }
        int infoHeight = infoList.size() * 10;
        int infoWidth = 150;
        int bagHeight = bagTexts.size() * 13 + 5;
        int bagWidth = 120;

        if(isEditMode) {
            if(TelosAddon.getInstance().infoWidth != infoWidth)
                TelosAddon.getInstance().infoWidth = infoWidth;

            if(TelosAddon.getInstance().infoHeight != infoHeight)
                TelosAddon.getInstance().infoHeight = infoHeight;

            if(TelosAddon.getInstance().bagHeight != bagHeight)
                TelosAddon.getInstance().bagHeight = bagHeight;

            if(TelosAddon.getInstance().bagWidth != bagWidth)
                TelosAddon.getInstance().bagWidth = bagWidth;
        }

        for(int i = 0; i < infoList.size(); i++)
            context.drawText(tr, FontHelper.toCustomFont(infoList.get(i), fontName), infoX, infoY + i * 10, config.getInteger("MenuColor"), true);
    }







}
