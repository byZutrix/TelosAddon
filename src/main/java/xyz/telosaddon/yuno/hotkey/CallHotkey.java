package xyz.telosaddon.yuno.hotkey;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.utils.LocalAPI;

import java.util.Locale;

import static xyz.telosaddon.yuno.utils.BossBarUtils.bossBarMap;


@Environment(EnvType.CLIENT)

public class CallHotkey{
    private static KeyBinding keyBinding;
    private static int callCooldown = 0;
    private static String lastBossFought = "";
    private static int lastBossPortalTimer = 0;

    private static MinecraftClient client = MinecraftClient.getInstance();
    public static void init() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.telosaddon.call",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                "category.telosaddon"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (callCooldown > 0) {
                callCooldown--;
            }
            if (lastBossPortalTimer > 0) {
                lastBossPortalTimer--;
            }

            if (keyBinding.wasPressed()) {
                if (callCooldown <=0 ) {
                    if (!LocalAPI.getCurrentCharacterFighting().equals("")){
                        lastBossFought = LocalAPI.getCurrentCharacterFighting();
                        if (TelosAddon.getInstance().getConfig().getBoolean("CallHotkeyShout")) {
                            client.getNetworkHandler().sendChatCommand("msg Pixelizedgaming " + LocalAPI.getCurrentCharacterFighting().toLowerCase(Locale.ROOT) + " tp");
                        }
                        else{
                            client.getNetworkHandler().sendChatMessage(LocalAPI.getCurrentCharacterFighting().toLowerCase(Locale.ROOT) + " tp");
                        }
                        callCooldown = 20 * 60; // 1 min cd
                    }
                    else{
                        if (client.player == null) return;
                        client.player.sendMessage(Text.of("§cYou can only use this hotkey near a boss!"), false);
                    }
                }
                else{
                    if (client.player == null) return;
                    client.player.sendMessage(Text.of("§cYou can't call a boss for another " + callCooldown/20 + " seconds!"), false);
                }
            }
        });
    }
}
