package xyz.telosaddon.yuno.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.utils.config.Config;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(DrawContext.class)
public class MixinDrawContext {
    private final Pattern pricePattern = Pattern.compile("\\* Price: ([0-9,]+)", Pattern.CASE_INSENSITIVE);
    private final Config config = TelosAddon.getInstance().getConfig();

    @Inject(method = "Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;II)V", at = @At("HEAD"))
    private void drawTooptip(TextRenderer textRenderer, List<Text> text, Optional data, int x, int y, CallbackInfo callbackInfo) {
        if (!config.getBoolean("ShowListedPrice")) return;

        for (int i = 0; i < text.size(); i++) {
            Text tooltipLine = text.get(i);

            String tooltipString = tooltipLine.getString();
            String formattedString = tooltipString.replaceAll("§[0-9a-z]", "");
            Matcher priceMatcher = pricePattern.matcher(formattedString);

            if (!priceMatcher.find()) continue;

            String formattedPriceString = priceMatcher.group(1).replaceAll(",", "");

            int price;

            try {
                price = Integer.parseInt(formattedPriceString);
            }
            catch (Exception err) {
                continue;
            }

            int basePrice = (int)Math.ceil(price / 1.1);

            // TODO: Ideally the listed price would be on a new line but adding the extra line crashes the game.
            text.set(i, Text.literal(tooltipString + " §7(§3" + NumberFormat.getNumberInstance(Locale.US).format(basePrice) + "§7)"));
        }
    }
}
