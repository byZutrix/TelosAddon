package xyz.telosaddon.yuno.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightmapTextureManager.class)
public class MixinLightmapTextureManager {

    @Unique
    private final MinecraftClient mc = MinecraftClient.getInstance();

    @ModifyExpressionValue(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F", ordinal = 2))
    private float allowNegativeGamma(float original) {
        float gamma = mc.options.getGamma().getValue().floatValue();
        if(gamma < 0) {
            return gamma;
        }

        return original;
    }

}
