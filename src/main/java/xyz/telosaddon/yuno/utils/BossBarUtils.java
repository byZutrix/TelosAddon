package xyz.telosaddon.yuno.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.hud.ClientBossBar;
import xyz.telosaddon.yuno.mixin.AccessorBossBars;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public class BossBarUtils {
    public static Map<UUID, ClientBossBar> bossBarMap;

    public static void init(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            bossBarMap = ((AccessorBossBars) client.inGameHud.getBossBarHud()).getBossBars();
        });
    }



}
