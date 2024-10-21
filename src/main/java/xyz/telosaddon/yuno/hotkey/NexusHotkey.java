package xyz.telosaddon.yuno.hotkey;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;
import xyz.telosaddon.yuno.TelosAddon;

import java.nio.charset.StandardCharsets;

import static xyz.telosaddon.yuno.TelosAddon.LOGGER;

@Environment(EnvType.CLIENT)
public class NexusHotkey {
    private static KeyBinding keyBinding;

    public static void init() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.telosaddon.nexus",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.telosaddon"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) {
                TelosAddon.getInstance().sendMessage("Nexus hotkey pressed!");
                LOGGER.info("Nexus hotkey pressed!");
                useNexus();
            }
        });
    }
    public static void useNexus(){
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.player != null;
        PlayerInventory inv = client.player.getInventory();
        int invslot = inv.selectedSlot;
        for (int i = 0; i < 9; i++) {
            ItemStack item = inv.getStack(i);
            if (item.getName().getString().hashCode() == 1307700015){ // hacky solution but it works
                invslot = i;
            }
        }
        int savedNexusSlot = invslot;
        scrollToSlot(inv, savedNexusSlot);
        assert client.interactionManager != null;
        client.interactionManager.interactItem(client.player, client.player.getActiveHand());
    }

    private static void scrollToSlot(PlayerInventory inv, int slot){
        int diff = inv.selectedSlot - slot;
        int dist = Math.abs(diff);
        for(int j = 0; j <  dist; j++) {
            inv.scrollInHotbar(diff);
        }
    }
}
