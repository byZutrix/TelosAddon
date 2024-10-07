package xyz.telosaddon.yuno.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import xyz.telosaddon.yuno.utils.config.Config;

import java.util.function.Function;

public class NoFrontCameraFeature extends AbstractFeature {
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public NoFrontCameraFeature(Config config) {
        super(config, "NoFrontCameraFeature");
    }

    public void tick() {
        if (!this.isEnabled()) return;
        if (mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT) mc.options.setPerspective(Perspective.FIRST_PERSON);
    }
}
