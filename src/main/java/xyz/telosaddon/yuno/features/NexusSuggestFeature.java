package xyz.telosaddon.yuno.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.utils.config.Config;

import java.util.ArrayList;

public class NexusSuggestFeature extends AbstractFeature {
    private final ArrayList<Float> previousPlayerHealthValues = new ArrayList<Float>();
    private final MinecraftClient mc = MinecraftClient.getInstance();

    private float lastDeltaPlayerHealth = 0;

    public NexusSuggestFeature(Config config) {
        super(config, "NexusSuggestFeature");
    }

    public void tick() {
        if (!this.isEnabled()) return;

        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        float currentPlayerHealth = player.getHealth();
        float currentPlayerMaxHealth = player.getMaxHealth();
        float playerHealthPercent = currentPlayerHealth / currentPlayerMaxHealth;

        if (playerHealthPercent <= 0.5 && previousPlayerHealthValues.size() > 0) {
            float deltaPlayerHealth = currentPlayerHealth - previousPlayerHealthValues.get(0);
            float immediateDeltaPlayerHealth = currentPlayerHealth - previousPlayerHealthValues.get(previousPlayerHealthValues.size() - 1);

            if (-immediateDeltaPlayerHealth > 0) {
                lastDeltaPlayerHealth = deltaPlayerHealth;
            }

            if (-lastDeltaPlayerHealth >= currentPlayerHealth) {
                Text title = Text.literal(getConfig().getString("NexusSuggestText")).formatted(Formatting.BOLD).formatted(TelosAddon.instance.tickCounter % 4 < 2 ? Formatting.AQUA : Formatting.DARK_AQUA);

                MinecraftClient.getInstance().inGameHud.setTitleTicks(0, 1, 0);
                MinecraftClient.getInstance().inGameHud.setTitle(title);

                if (TelosAddon.instance.tickCounter % 2 == 0) {
                    MinecraftClient.getInstance().player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.0f);
                }
            }
        }

        previousPlayerHealthValues.add(currentPlayerHealth);

        if (previousPlayerHealthValues.size() > getConfig().getDouble("NexusSuggestFrameLength") * 20) {
            previousPlayerHealthValues.remove(0);
        }
    }
}
