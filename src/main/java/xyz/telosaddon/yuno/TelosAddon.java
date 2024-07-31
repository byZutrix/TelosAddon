package xyz.telosaddon.yuno;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;
import xyz.telosaddon.yuno.ui.TelosMenu;
import xyz.telosaddon.yuno.utils.Config;
import java.util.HashMap;
import java.util.Map;

public class TelosAddon {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    public static TelosAddon instance;
    private Config config;
    private Map<String, Integer> bagCounter;

    private KeyBinding menuKey;

    public void init() {

        config = new Config();
        config.load();

        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.telosaddon.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category.telosaddon.general"
        ));

        loadBagCounter();


        instance = this;
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

        if(menuKey.wasPressed()) {
            mc.setScreen(new TelosMenu());
        }
    }

    public void sendMessage(String message) {
        mc.inGameHud.setOverlayMessage(Text.of("§6" + message), false);
    }
    public static TelosAddon getInstance() { return instance; }
    public Config getConfig() { return config; }

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

}
