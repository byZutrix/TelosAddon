package xyz.telosaddon.yuno.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.boss.BossBar;
import xyz.telosaddon.yuno.TelosAddon;

import java.util.Optional;
import java.util.logging.Level;

import static xyz.telosaddon.yuno.TelosAddon.LOGGER;
import static xyz.telosaddon.yuno.utils.BossBarUtils.bossBarMap;
import static xyz.telosaddon.yuno.utils.TabListUtils.getPing;

// place to put stuff that I don't really know where else to put
public class LocalAPI {

    private static String currentCharacterType = "";
    private static String currentCharacterClass = "";
    private static int currentCharacterLevel = -1;
    private static String currentCharacterWorld = "";
    private static String rawBossBarData = "";
    private static String currentCharacterFighting = "";
    private static String currentClientPing = "";

    public static void updateAPI(){
        if (!TelosAddon.getInstance().isOnTelos()) return;
        Optional<String> info = TabListUtils.getCharInfo();
        if (info.isEmpty()) return;

        String[] charInfo = info.get().split(" ");
        if (charInfo.length < 4) return;

        switch (charInfo[0].hashCode()) {
            case 880 -> currentCharacterType = "Normal";
            case 881 -> currentCharacterType = "Hardcore";
            case 882 -> currentCharacterType = "Softcore";
            default -> // 1771717 -> 1771734 inclusive
                    currentCharacterType = "GHardcore";
        }
        try {
            currentCharacterLevel = Integer.parseInt(charInfo[2]);
            currentCharacterClass = charInfo[3];

        }catch (Exception e){
            e.printStackTrace();
        }
        currentClientPing = getPing().isPresent() ? getPing().get() : String.valueOf(-1);

        if (bossBarMap != null) {
            Object[] preArray = bossBarMap.values().toArray();
            if (preArray.length > 0 && preArray[1] instanceof BossBar) {
                BossBar bossBar = (BossBar) preArray[1];
                LOGGER.log(Level.INFO, "Current Boss Bar Hashcode: " + bossBar.getName().hashCode());
            }
        }


    }

    public static String getCurrentCharacterType() {
        return currentCharacterType;
    }
    public static String getCurrentCharacterClass() {
        return currentCharacterClass;
    }

    public static int getCurrentCharacterLevel() {
        return currentCharacterLevel;
    }

    public static String getCurrentCharacterWorld() {
        return currentCharacterWorld;
    }

    public static String getRawBossBarData() {
        return rawBossBarData;
    }

    public static String getCurrentCharacterFighting() {
        return currentCharacterFighting;
    }

    public static String getCurrentClientPing() {
        return currentClientPing;
    }
    public static void setRawBossBarData(String rawBossBarData) {
        LocalAPI.rawBossBarData = rawBossBarData;
    }
}
