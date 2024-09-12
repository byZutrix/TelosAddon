package xyz.telosaddon.yuno.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;

public class HoldToSwingFeature {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final PlayerEntity player = mc.player;

    public static void holdToSwing(){

        if(!mc.options.attackKey.isPressed() || player == null) return;

        ItemStack heldItem = player.getMainHandStack();

        if(heldItem == null || heldItem.getName().getString().equals("Air")) return;

        //Check if Item is on cooldown. If yes don't swing
        ItemCooldownManager cooldownManager = player.getItemCooldownManager();
        if(cooldownManager.isCoolingDown(heldItem.getItem())) return;

        //Check if Item is a weapon. If not don't swing
        if(!isStackWeapon(heldItem)) return;

        //Swing
        player.swingHand(Hand.MAIN_HAND);
    }

    private static boolean isStackWeapon(ItemStack stack){
        //Check if Item is a weapon. If not don't swing
        NbtComponent nbtComponent = stack.getComponents().get(DataComponentTypes.CUSTOM_DATA);
        if(nbtComponent == null) return false;

        NbtCompound nbtData = nbtComponent.copyNbt();
        if(!nbtData.contains("MYTHIC_TYPE")) return false;

        String itemType = nbtData.getString("MYTHIC_TYPE");
        return itemType.matches("^(sword|staff|dagger|bow|katana).*");
    }
}
