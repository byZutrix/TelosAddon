package xyz.telosaddon.yuno.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class CustomSound {

    private Identifier id;
    private SoundEvent event;
    private String soundName;

    public CustomSound(String soundName) {
        this.soundName = soundName;
        this.id = Identifier.of("telosaddon:" + soundName);
        this.event = SoundEvent.of(id);
    }

    public Identifier getId() {
        return this.id;
    }

    public SoundEvent getEvent() {
        return this.event;
    }

    public String getName() {
        return this.soundName;
    }

}
