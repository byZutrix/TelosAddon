package xyz.telosaddon.yuno.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.utils.Config;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {

    @Shadow @Final
    private MinecraftClient client;

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if(client.inGameHud.getDebugHud().shouldShowDebugHud()) return;

        TextRenderer tr = client.textRenderer;

        int width = client.getWindow().getScaledWidth();
        //int height = client.getWindow().getScaledHeight();

        Config config = TelosAddon.getInstance().getConfig();

        boolean fpsSetting = config.getBoolean("FPSSetting");

        int greenBags = config.getBoolean("LifetimeSetting") ? config.getInteger("GreenBags") : TelosAddon.getInstance().getBagCounter().get("GreenBags");
        int goldBags = config.getBoolean("LifetimeSetting") ? config.getInteger("GoldBags") : TelosAddon.getInstance().getBagCounter().get("GoldBags");
        int whiteBags = config.getBoolean("LifetimeSetting") ? config.getInteger("WhiteBags") : TelosAddon.getInstance().getBagCounter().get("WhiteBags");
        int blackBags = config.getBoolean("LifetimeSetting") ? config.getInteger("BlackBags") : TelosAddon.getInstance().getBagCounter().get("BlackBags");
        int xMasBags = config.getBoolean("LifetimeSetting") ? config.getInteger("XMasBags") : TelosAddon.getInstance().getBagCounter().get("XMasBags");
        int crosses = config.getBoolean("LifetimeSetting") ? config.getInteger("Crosses") : TelosAddon.getInstance().getBagCounter().get("Crosses");
        int relics = config.getBoolean("LifetimeSetting") ? config.getInteger("Relics") : TelosAddon.getInstance().getBagCounter().get("Relics");
        int totalRuns = config.getBoolean("LifetimeSetting") ? config.getInteger("TotalRuns") : TelosAddon.getInstance().getBagCounter().get("TotalRuns");
        int noWhitesRuns = config.getBoolean("LifetimeSetting") ? config.getInteger("NoWhiteRuns") : TelosAddon.getInstance().getBagCounter().get("NoWhiteRuns");

        List<String> bagTexts = new ArrayList<>();
        if(config.getBoolean("GreenSetting"))
            bagTexts.add("§6Green Bags§7: §f" + greenBags);
        if(config.getBoolean("GoldSetting"))
            bagTexts.add("§6Gold Bags§7: §f" + goldBags);
        if(config.getBoolean("WhiteSetting"))
            bagTexts.add("§6White Bags§7: §f" + whiteBags);
        if(config.getBoolean("BlackSetting"))
            bagTexts.add("§6Black Bags§7: §f" + blackBags);
        if(config.getBoolean("XMasSetting"))
            bagTexts.add("§6XMas Bags§7: §f" + xMasBags);
        if(config.getBoolean("CrossSetting"))
            bagTexts.add("§6Crosses§7: §f" + crosses);
        if(config.getBoolean("RelicSetting"))
            bagTexts.add("§6Relics§7: §f" + relics);
        if(config.getBoolean("TotalRunSetting"))
            bagTexts.add("§6Total Runs§7: §f" + totalRuns);
        if(config.getBoolean("NoWhiteRunSetting"))
            bagTexts.add("§6No Whites Runs§7: §f" + noWhitesRuns);

        int yText = 60;
        int yBackground = yText - 25;
        if(!bagTexts.isEmpty()) {
            String title = config.getBoolean("LifetimeSetting") ? "§6§lLifetime Stats" : "§6§lSession Stats";
            context.fill(width - 130, yBackground, width - 10, yText + bagTexts.size() * 13 + 5, new Color(0, 0, 0, 60).getRGB());
            context.drawBorder(width - 130, yBackground, 120, yText - yBackground + bagTexts.size() * 13 + 5, new Color(255, 255, 255, 50).getRGB());
            context.drawHorizontalLine(width - 120, width - 20, yText - 4, new Color(255, 255, 255, 50).getRGB());

            int titleWidth = tr.getWidth(title);
            int midX = ((width - 130) + (width - 10)) / 2;
            context.drawText(tr, title, midX - titleWidth / 2, yText - 15, 0xFFFFFF, false);
        }
        for(int i = 0; i < bagTexts.size(); i++) {
            context.drawText(tr, bagTexts.get(i), width - 120, yText + (i * 13), 0xFFFFFF, false);
        }

        if(fpsSetting) {
            int fps = client.getCurrentFps();
            context.drawText(tr, "§f" + fps + " FPS", 4, 4, 0xFFFFFF, false);
        }
    }

}
