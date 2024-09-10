package xyz.telosaddon.yuno.sound;

import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoundManager {

    private final MinecraftClient mc;
    private List<CustomSound> sounds;

    public SoundManager() {
        this.mc = MinecraftClient.getInstance();
        this.sounds = new ArrayList<>();
    }

    public void addSound(CustomSound sound) {
        this.sounds.add(sound);
        Registry.register(Registries.SOUND_EVENT, sound.getId(), sound.getEvent());
    }

    public void playSound(String name) {
        this.mc.world.playSound(
                this.mc.player,
                this.mc.player.getBlockPos(),
                this.getSoundByName(name).getEvent(),
                SoundCategory.PLAYERS,
                1.0f,
                1.0f
        );
    }

    public CustomSound getSoundByName(String name) {
        Optional<CustomSound> sound = this.sounds.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst();
        return sound.orElse(
                new CustomSound("button_click")
        );
    }

}
