package xyz.telosaddon.yuno.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.util.Identifier;
import xyz.telosaddon.yuno.mixin.AccessorBossBars;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import static xyz.telosaddon.yuno.TelosAddon.LOGGER;

/*
boss bar map: check the 2nd boss bar in the list to id the boss
chungus : -1083980771
illarious : 452824575
astaroth :
glumi :
lotil :
tidol : 453134978
valus : 1757112170
oozul : 1472054207
freddy :
anubis :-1258344668

shadowflame: 908391166
loa : 1996713601
valerion:
nebula:
ophanim:
prismara :
omnipotent :
thalassar : 1757423534
silex : 1735775594
chronos : -624873662
gfreddy :
kurvaros : -1258333136

warden:
herald:
reaper:
defender:
asmodeus:
seraphim:

onyx guards:
onyx:
 */
public class BossBarUtils {
    public static Map<UUID, ClientBossBar> bossBarMap;

    public static void init(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            bossBarMap = ((AccessorBossBars) client.inGameHud.getBossBarHud()).getBossBars();
        });
    }


}
