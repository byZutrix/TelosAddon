package xyz.telosaddon.yuno.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

public class ActionUtils {
    private static final float REACH_DISTANCE = 4.5F;

    public static void simulateRightClick(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null) return;

        ClientPlayerEntity player = client.player;

        // Determine the active hand
        Hand activeHand = player.getActiveHand();

        // Use the player's actual raytrace result
        HitResult hitResult = player.raycast(REACH_DISTANCE, 1.0F, false);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hitResult;

            // Interact with the block using the active hand
            client.interactionManager.interactBlock(player, activeHand, blockHit);

            // Swing the active hand (visual feedback)
            player.swingHand(activeHand);
        } else {
            // If not looking at a block, just interact with the item in the active hand
            client.interactionManager.interactItem(player, activeHand);
            player.swingHand(activeHand);
        }
    }
}
