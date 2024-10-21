package xyz.telosaddon.yuno.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.utils.config.Config;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static xyz.telosaddon.yuno.TelosAddon.*;

@Mixin(MessageHandler.class)
public class MixinMessageHandler {
    private boolean trackerBit = false;

    @Inject(method = "method_45745", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;)V"))
    private void onDisguisedChatLambda(MessageType.Parameters parameters, Text text, Instant instant, CallbackInfoReturnable<Boolean> cir) {
        onChat(text);
    }

    @Inject(method = "processChatMessageInternal", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"))
    private void onChatMessage(MessageType.Parameters params, SignedMessage message, Text decorated, GameProfile sender, boolean onlyShowSecureChat, Instant receptionTimestamp, CallbackInfoReturnable<Boolean> cir) {
        onChat(decorated);
    }

    @Inject(method =  "onGameMessage", at = @At("HEAD"))
    private void onGameMessage(Text message, boolean overlay, CallbackInfo ci) {
        onChat(message);
    }

    @Unique
    private void onChat(Text text) {
        String s = text.getString().trim();
        if(s.contains("has spawned at")) {
            String[] args = s.split(" ");
            String name = args[0];
            TelosAddon.getInstance().addAliveBosses(name);
        }

        if(s.contains("has been defeated")) {
            String[] args = s.split(" ");
            String name = args[0];
            if(TelosAddon.getInstance().getAliveBosses().contains(name))
                TelosAddon.getInstance().removeAliveBoss(name);
        }

        if(s.contains("discord.telosrealms.com")){ // nexus check
            TelosAddon.getInstance().getAliveBosses().clear();
            if (!TelosAddon.getInstance().getConfig().getBoolean("EnableJoinText") || TelosAddon.getInstance().getPlayTime() > 15) return; // don't spam this thing
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(10); // make this display after server join message
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    client.player.sendMessage(Text.of("§6[§e" + MOD_NAME + MOD_VERSION + "§6]" +
                            "\n§ePlease note this is a third party mod and is not affiliated with Telos Realms. "), false);
                    client.player.sendMessage(Text.literal("§eFor bugs, support, and other questions, please join our discord: §f§nhttps://discord.gg/2pa42RxuaF")
                            .setStyle(Style.EMPTY
                                    // Set the click event to open the URL
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/2pa42RxuaF"))
                                    // Optional: Set a hover text when the player hovers over the link
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Join our discord!")))
                            ), false);

                });
            }
        }

        if(!s.equals("===============================================")) return;
        if (trackerBit = !trackerBit) return; // there's 2 bars in the kill message, and guess what datatype has 2 states
        Config config = TelosAddon.getInstance().getConfig();

        config.addInt("TotalRuns", 1);
        config.addInt("NoWhiteRuns", 1);
        config.addInt("NoBlackRuns", 1);

        int newValue = TelosAddon.getInstance().getBagCounter().get("TotalRuns");
        TelosAddon.getInstance().getBagCounter().replace("TotalRuns", newValue + 1);

        int newValue2 = TelosAddon.getInstance().getBagCounter().get("NoWhiteRuns");
        TelosAddon.getInstance().getBagCounter().replace("NoWhiteRuns", newValue2 + 1);

        int newBlackValue = TelosAddon.getInstance().getBagCounter().get("NoBlackRuns");
        TelosAddon.getInstance().getBagCounter().replace("NoBlackRuns", newBlackValue + 1);
    }

}
