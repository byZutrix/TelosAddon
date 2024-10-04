package xyz.telosaddon.yuno.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.telosaddon.yuno.TelosAddon;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {


    @Inject(at = @At("TAIL"), method = "run")
    private void run(CallbackInfo ci) {
        TelosAddon.getInstance().run();
    }

    @Inject(at = @At("HEAD"), method = "stop")
    private void stop(CallbackInfo info) {
        TelosAddon.getInstance().stop();
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo info) {
        TelosAddon.getInstance().tick();
    }

}
