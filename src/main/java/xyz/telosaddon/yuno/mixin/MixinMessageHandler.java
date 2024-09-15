package xyz.telosaddon.yuno.mixin;

import com.google.common.collect.EvictingQueue;
import com.mojang.authlib.GameProfile;
import net.fabricmc.loader.impl.lib.sat4j.minisat.core.CircularBuffer;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.utils.Config;

import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedDeque;

@Mixin(MessageHandler.class)
public class MixinMessageHandler {

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

        if(!s.startsWith("Your rank:")) return;
        Config config = TelosAddon.getInstance().getConfig();

        config.addInt("TotalRuns", 1);
        config.addInt("NoWhiteRuns", 1);
        int newValue = TelosAddon.getInstance().getBagCounter().get("TotalRuns");
        TelosAddon.getInstance().getBagCounter().replace("TotalRuns", newValue + 1);

        int newValue2 = TelosAddon.getInstance().getBagCounter().get("NoWhiteRuns");
        TelosAddon.getInstance().getBagCounter().replace("NoWhiteRuns", newValue2 + 1);
    }

}
