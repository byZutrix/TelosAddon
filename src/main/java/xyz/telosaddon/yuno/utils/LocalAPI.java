package xyz.telosaddon.yuno.utils;

import net.minecraft.entity.boss.BossBar;
import xyz.telosaddon.yuno.TelosAddon;

import java.util.Optional;
import java.util.logging.Level;

import static xyz.telosaddon.yuno.TelosAddon.LOGGER;
import static xyz.telosaddon.yuno.utils.BossBarUtils.bossBarMap;
import static xyz.telosaddon.yuno.utils.TabListUtils.*;

// place to put stuff that I don't really know where else to put
public class LocalAPI {

    private static String currentCharacterType = "";
    private static String currentCharacterClass = "";
    private static int currentCharacterLevel = -1;
    private static String currentCharacterWorld = "";
    private static String currentCharacterArea = "";
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
        updateCharacterArea();
        Optional<String> realm = TabListUtils.getServer();
        realm.ifPresent(s -> currentCharacterWorld = s);
    }

    private static void updateCharacterArea(){
        if (bossBarMap != null) {
            Object[] preArray = bossBarMap.values().toArray();
            if (preArray.length > 5 && preArray[1] instanceof BossBar && preArray[3] instanceof BossBar) {

                BossBar areaBar = (BossBar) preArray[3];
                String area = stripAllFormatting(areaBar.getName().getString());
                currentCharacterArea = area.replaceAll("[^a-zA-z ]+", ""); // idk why but theres numbers at the end so we gotta trim that off


                BossBar bossBar = (BossBar) preArray[1]; // add what boss we're fighting
                //LOGGER.log(Level.INFO, "Bossbar hashcode:" + bossBar.getName().hashCode()); // keep this until i can fill out all the bosses
                switch (bossBar.getName().hashCode()){
                    case -1083980771 -> currentCharacterFighting = "Chungus";
                    case 452824575 -> currentCharacterFighting = "Illarious";
                    case 2125535338 -> currentCharacterFighting = "Astaroth";
                    case -1083975966 -> currentCharacterFighting = "Glumi";
                    case 2125159587 -> currentCharacterFighting = "Lotil";
                    case 453134978 -> currentCharacterFighting = "Tidol";
                    case 1757112170 -> currentCharacterFighting = "Valus";
                    case 1472054207 -> currentCharacterFighting = "Oozul";
                    case 2035818623 -> currentCharacterFighting = "Freddy";
                    case 1258344668 -> currentCharacterFighting = "Anubis"; //bugged

                    case 908391166 -> currentCharacterFighting = "Shadowflare";
                    case 1996713601 -> currentCharacterFighting = "Loa";
                    // insert astaroth bosses here
                    case -1624135070 -> currentCharacterFighting = "Prismara";
                    case 2125160548 -> currentCharacterFighting = "Omnipotent";
                    case 1757423534 -> currentCharacterFighting = "Thalassar";
                    case 1735775594 -> currentCharacterFighting = "Silex";
                    case -624873662 -> currentCharacterFighting = "Chronos";
                    case -1338784736 -> currentCharacterFighting = "Golden Freddy";
                    case -1258333136 -> currentCharacterFighting = "Kurvaros";

                    case 2008511319 -> currentCharacterFighting = "Warden";
                    case 2008512280 -> currentCharacterFighting = "Herald";
                    case 2008513241 -> currentCharacterFighting = "Reaper";
                    case 2008514202 -> currentCharacterFighting = "Defender";
                    case 1757100638 -> currentCharacterFighting = "Asmodeus";
                    case 1735762140 -> currentCharacterFighting = "Seraphim";

                    case 1216094805 -> currentCharacterFighting = "Onyx Castle";
                    case 1757905956 -> currentCharacterFighting = "Onyx";

                    case -1083171609 -> currentCharacterFighting = "Pirate's Cove";
                    case 1997519880 -> currentCharacterFighting = "Thornwood Wargrove";
                    default -> currentCharacterFighting = "";
                }

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


    public static String getCurrentCharacterFighting() {
        return currentCharacterFighting;
    }

    public static String getCurrentCharacterArea() {
        return currentCharacterArea;
    }

    public static String getCurrentClientPing() {
        return currentClientPing;
    }
}
