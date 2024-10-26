package xyz.telosaddon.yuno.hotkey;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import org.lwjgl.glfw.GLFW;
import xyz.telosaddon.yuno.ui.TelosMenu;
import xyz.telosaddon.yuno.ui.tabs.TeleportMenuScreen;

@Environment(EnvType.CLIENT)
public class TeleportMenuHotkey {
    private static KeyBinding keyBinding;

    public static void init() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.telosaddon.tpmenu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "category.telosaddon"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) {
                client.setScreen(new TeleportMenuScreen());

            }
        });
    }
}
