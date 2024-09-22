package xyz.telosaddon.yuno;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.world.LevelLoadingScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;
import xyz.telosaddon.yuno.hotkey.AbilityHotkey;
import xyz.telosaddon.yuno.hotkey.MenuHotkey;
import xyz.telosaddon.yuno.hotkey.NexusHotkey;
import xyz.telosaddon.yuno.sound.SoundManager;
import xyz.telosaddon.yuno.ui.TelosMenu;
import xyz.telosaddon.yuno.utils.Config;
import xyz.telosaddon.yuno.sound.CustomSound;

import java.util.*;

public class TelosAddon {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    public static TelosAddon instance;
    private SoundManager soundManager;
    private Config config;
    private Map<String, Integer> bagCounter;
    private long playTime = 0;
    private int tickCounter = 0;
    private List<String> aliveBosses;
    private boolean editMode = false;

    public int infoWidth;
    public int infoHeight;
    public int bagWidth;
    public int bagHeight;

    private KeyBinding menuKey;
    private KeyBinding nexusKey;

    public void init() {

        config = new Config();
        config.load();
        initHotkeys();

        loadBagCounter();

        soundManager = new SoundManager();

        soundManager.addSound(new CustomSound("button_click"));
        soundManager.addSound(new CustomSound("white_bag"));
        soundManager.addSound(new CustomSound("black_bag"));
        aliveBosses = new ArrayList<>();

        instance = this;
    }

    public void initHotkeys(){
        NexusHotkey.init();
        MenuHotkey.init();
        AbilityHotkey.init();
    }

    public void run() {
        if(config.getBoolean("GammaSetting")) {
            toggleGamma(true);
        }
    }

    public void stop() {
        config.save();
    }
    public void tick() {
        ClientPlayerEntity player = mc.player;
        if(player == null) return;

        if(mc.options.attackKey.isPressed() && config.getBoolean("SwingSetting"))
            player.swingHand(Hand.MAIN_HAND);

        if(isOnTelos()) {
            tickCounter++;
            if(tickCounter >= 20) {
                playTime++;
                config.addLong("TotalPlaytime", 1);
                tickCounter = 0;
            }
        }
    }

    public void sendMessage(String message) {
        mc.inGameHud.setOverlayMessage(Text.of("§6" + message), false);
    }
    public static TelosAddon getInstance() { return instance; }
    public Config getConfig() { return config; }
    public SoundManager getSoundManager() { return soundManager; }

    public void toggleGamma(boolean b) {
        Double newGamma = b ? config.getDouble("NewGamma") : config.getDouble("NormalGamma");
        mc.options.getGamma().setValue(newGamma);
    }

    private void loadBagCounter() {
        bagCounter = new HashMap<>();
        bagCounter.put("GreenBags", 0);
        bagCounter.put("GoldBags", 0);
        bagCounter.put("WhiteBags", 0);
        bagCounter.put("BlackBags", 0);
        bagCounter.put("XMasBags", 0);
        bagCounter.put("Crosses", 0);
        bagCounter.put("Relics", 0);
        bagCounter.put("TotalRuns", 0);
        bagCounter.put("NoWhiteRuns", 0);
    }

    public Map<String, Integer> getBagCounter() {
        return this.bagCounter;
    }

    public String getPlaytimeText() {
        long hours = this.playTime / 3600;
        long minutes = (this.playTime % 3600) / 60;
        long seconds = this.playTime % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public boolean isOnTelos() {
        String serverIP = mc.getCurrentServerEntry() != null ? mc.getCurrentServerEntry().address : "Null";
        if(mc.world != null && !mc.isPaused() && serverIP.contains("telosrealms.com")) {
            return true;
        }
        return false;
    }

    public void addAliveBosses(String name) { this.aliveBosses.add(name); }
    public void removeAliveBoss(String name) { this.aliveBosses.remove(name); }
    public void clearAliveBosses() { this.aliveBosses.clear(); }
    public List<String> getAliveBosses() { return this.aliveBosses; }
    public boolean isEditMode() { return this.editMode; }
    public void setEditMode(boolean value) { this.editMode = value; }

}
