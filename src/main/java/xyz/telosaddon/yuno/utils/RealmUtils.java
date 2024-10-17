package xyz.telosaddon.yuno.utils;

import net.minecraft.client.MinecraftClient;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.hotkey.NexusHotkey;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Future;

public class RealmUtils {

    public static Future<Void> asyncTeleport(String worldName){
        return CompletableFuture.runAsync(()->{
            int waitTime = 15;
            MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("joinq " + worldName);
            while (!LocalAPI.getCurrentCharacterWorld().equals(worldName)){
                try {
                    Thread.sleep(1000);
                    waitTime--;
                    if (waitTime == 0) {
                        TelosAddon.getInstance().sendMessage("§cError teleporting to " + worldName);
                        throw new CompletionException(new Exception("Error teleporting to " + worldName));
                    }
                } catch (InterruptedException e) {
                    TelosAddon.getInstance().sendMessage("§cError teleporting to " + worldName);
                    throw new CompletionException(e);
                }
            }
        }).exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });
    }

    //todo: handle queues (maybe read from action bar)
    public static Future<Void> nexusThenTp(String worldName){
        return CompletableFuture.supplyAsync(()-> {
            String worldToJoin = worldName;
            NexusHotkey.useNexus();
            int waitTime = 15;
            while (!LocalAPI.getCurrentCharacterWorld().contains("Hub")){
                try {
                    Thread.sleep(1000); // local api only updated every second
                    waitTime--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new CompletionException(e);
                }
                if (waitTime == 0) {
                    TelosAddon.getInstance().sendMessage("§cError teleporting to " + worldName);
                    throw new CompletionException(new Exception("Error teleporting to hub"));
                }
            }
            return worldToJoin;
        }).thenAcceptAsync(RealmUtils::asyncTeleport);

    }

}

