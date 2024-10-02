package xyz.telosaddon.yuno.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.telosaddon.yuno.TelosAddon;

import static xyz.telosaddon.yuno.TelosAddon.getInstance;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(method = "createWorlds", at = @At("HEAD"))
    private void createWorlds(CallbackInfo ci) {
        TelosAddon ta = TelosAddon.getInstance();
        ta.clearAliveBosses();
        getInstance().getRpcManager().start();
    }

}
