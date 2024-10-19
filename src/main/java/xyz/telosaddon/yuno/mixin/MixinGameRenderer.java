package xyz.telosaddon.yuno.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.sound.SoundManager;
import xyz.telosaddon.yuno.utils.config.Config;

import java.util.Objects;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Inject(method = "showFloatingItem", at = @At("HEAD"))
    private void showFloatingItem(ItemStack floatingItem, CallbackInfo ci) {
        if(!floatingItem.getComponents().isEmpty() && !floatingItem.getComponents().contains(DataComponentTypes.CUSTOM_MODEL_DATA)) return;

        int customModelData = Objects.requireNonNull(floatingItem.getComponents().get(DataComponentTypes.CUSTOM_MODEL_DATA)).value();

        Config config = TelosAddon.getInstance().getConfig();
        SoundManager soundManager = TelosAddon.getInstance().getSoundManager();
        boolean soundSetting = config.getBoolean("SoundSetting");
        switch (customModelData) {
            case 11 -> {
                addBag("WhiteBags", config);

                config.set("NoWhiteRuns", 0);
                TelosAddon.getInstance().getBagCounter().replace("NoWhiteRuns", 0);

                if(soundSetting)
                    soundManager.playSound("white_bag");

            }
            case 10 -> {
                addBag("BlackBags", config);

                config.set("NoBlackRuns", 0);
                TelosAddon.getInstance().getBagCounter().replace("NoBlackRuns", 0);

                if(soundSetting)
                    soundManager.playSound("black_bag");

            }
            case 15 -> addBag("GoldBags", config);
            case 12 -> addBag("Crosses", config);
            case 6,9 -> {
                addBag("EventBags", config);
            }
            case 13 -> addBag("GreenBags", config);
            case 8 -> addBag("Relics", config);
            case 14 -> addBag("Runes", config);
            default -> {
            }
        }
    }

    @Unique
    private void addBag(String name, Config config) {

        config.addInt(name, 1);
        int newValue = TelosAddon.getInstance().getBagCounter().get(name);
        TelosAddon.getInstance().getBagCounter().replace(name, newValue + 1);

    }
}
