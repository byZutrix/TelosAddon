package xyz.telosaddon.yuno;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import xyz.telosaddon.yuno.discordrpc.DiscordRPCManager;
import xyz.telosaddon.yuno.hotkey.AbilityHotkey;
import xyz.telosaddon.yuno.hotkey.MenuHotkey;
import xyz.telosaddon.yuno.hotkey.NexusHotkey;
import xyz.telosaddon.yuno.features.ShowMainRangeFeature;
import xyz.telosaddon.yuno.features.ShowOffHandFeature;
import xyz.telosaddon.yuno.renderer.RangeRenderer;
import xyz.telosaddon.yuno.sound.SoundManager;

import xyz.telosaddon.yuno.utils.BossBarUtils;
import xyz.telosaddon.yuno.utils.config.Config;
import xyz.telosaddon.yuno.sound.CustomSound;

import java.util.*;

import java.util.logging.Logger;

import static xyz.telosaddon.yuno.utils.LocalAPI.updateAPI;

public class TelosAddon implements ClientModInitializer  {
    public static final String MOD_NAME = "RealmsAddon";
    public static final String MOD_VERSION = "v0.21d";

    public static final Logger LOGGER = Logger.getLogger(MOD_NAME);
    private final MinecraftClient mc = MinecraftClient.getInstance();
    public static TelosAddon instance;

    private static final DiscordRPCManager rpcManager = new DiscordRPCManager();
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

    private ShowMainRangeFeature showMainRangeFeature;

    private ShowOffHandFeature showOffHandFeature;



    public void initHotkeys(){
        NexusHotkey.init();
        MenuHotkey.init();
        AbilityHotkey.init();// until fixed
    }
    public void stop() {
        config.save();
        rpcManager.stop();
    }
    public void tick() {

        ClientPlayerEntity player = mc.player;
        if(player == null) return;

        if(mc.options.attackKey.isPressed() && config.getBoolean("SwingSetting")) {
            boolean canSwing = !player.getItemCooldownManager().isCoolingDown(player.getMainHandStack().getItem());
            if (!config.getBoolean("SwingIfNoCooldown") || canSwing) {
                player.swingHand(Hand.MAIN_HAND);
            }
        }

        this.showMainRangeFeature.tick();
        this.showOffHandFeature.tick();

        if(isOnTelos()) {
            tickCounter++;
            if(tickCounter >= 20) {
                playTime++;
                config.addLong("TotalPlaytime", 1);
                tickCounter = 0;
                updateAPI();
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
        bagCounter.put("NoBlackRuns", 0);
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

    @Override
    public void onInitializeClient() {
        config = new Config();
        config.load();
        instance = this;

        BossBarUtils.init();
        rpcManager.start();
        initHotkeys();
        loadBagCounter();

        soundManager = new SoundManager();

        soundManager.addSound(new CustomSound("button_click"));
        soundManager.addSound(new CustomSound("white_bag"));
        soundManager.addSound(new CustomSound("black_bag"));
        aliveBosses = new ArrayList<>();

        this.showMainRangeFeature = new ShowMainRangeFeature(config);
        this.showOffHandFeature = new ShowOffHandFeature(config);

        RangeRenderer.init();
    }

    public void run(){
        if(config.getBoolean("GammaSetting")) {
            toggleGamma(true);
        }
    }

    public ShowOffHandFeature getShowOffHandFeature() {
        return showOffHandFeature;
    }

    public ShowMainRangeFeature getShowMainRangeFeature() {
        return showMainRangeFeature;
    }

    public DiscordRPCManager getRpcManager () {
        return rpcManager;
    }
    public long getPlayTime() {
        return playTime;
    }

}
