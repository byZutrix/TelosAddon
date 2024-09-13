package xyz.telosaddon.yuno.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import xyz.telosaddon.yuno.utils.NbtUtils;

import java.util.Optional;

public class AutoNexusFeature {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final ClientPlayNetworkHandler networkHandler = mc.getNetworkHandler();
    private static boolean nexus = false;

    public static void autoNexus(){
        PlayerEntity player = mc.player;

        if(player == null) return;

        int slot = -1;
        for (int i = 0; i < 9; i++){
            ItemStack stack = player.getInventory().getStack(i);

            if(isItemNexus(stack)){
                slot = i;
                break;
            }
        }

        if(slot == -1) return;

        player.getInventory().selectedSlot = slot;

        nexus = true;
    }

    public static void tick(){
        PlayerEntity player = mc.player;

        if(!nexus || player == null) return;
        nexus = false;

        ItemStack heldItem = player.getMainHandStack();
        if(heldItem.isEmpty()) return;

        PlayerInteractItemC2SPacket packet = new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, player.getYaw(), player.getPitch());
        if(networkHandler == null) return;

        networkHandler.sendPacket(packet);
    }

    private static boolean isItemNexus(ItemStack stack){
        Optional<String> mythicTypeOptional = NbtUtils.getMythicType(stack);
        return mythicTypeOptional.map(s -> s.equals("nexus")).orElse(false);
    }
}
