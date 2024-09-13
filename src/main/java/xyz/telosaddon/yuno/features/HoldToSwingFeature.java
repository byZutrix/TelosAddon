package xyz.telosaddon.yuno.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import xyz.telosaddon.yuno.utils.NbtUtils;

import java.util.Optional;

public class HoldToSwingFeature {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void holdToSwing(){
        PlayerEntity player = mc.player;

        if(!mc.options.attackKey.isPressed() || player == null) return;

        ItemStack heldItem = player.getMainHandStack();

        if(heldItem == null || heldItem.getName().getString().equals("Air")) return;

        //Check if Item is on cooldown. If yes don't swing
        ItemCooldownManager cooldownManager = player.getItemCooldownManager();
        if(cooldownManager.isCoolingDown(heldItem.getItem())) return;

        Optional<String> mythicTypeOptional = NbtUtils.getMythicType(heldItem);
        if(mythicTypeOptional.isEmpty()) return;

        if(!mythicTypeOptional.get().matches("^(sword|staff|dagger|bow|katana).*")) return;

        //Swing
        player.swingHand(Hand.MAIN_HAND);
    }
}
