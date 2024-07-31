package xyz.telosaddon.yuno.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.telosaddon.yuno.TelosAddon;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    private TelosAddon telosAddon;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/System;currentTimeMillis()J"))
    private void init(CallbackInfo ci) {
        System.out.println("[TelosAddon] Loading Client...");
        telosAddon = new TelosAddon();
        telosAddon.init();
        System.out.println("[TelosAddon] Client Loaded!");
    }

    @Inject(method = "run", at = @At("HEAD"))
    private void run(CallbackInfo ci) {
        telosAddon.run();
    }

    @Inject(at = @At("HEAD"), method = "stop")
    private void stop(CallbackInfo info) {
        telosAddon.stop();
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo info) {
        telosAddon.tick();
    }

}
