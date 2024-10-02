package xyz.telosaddon.yuno.discordrpc;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.utils.LocalAPI;

import java.time.OffsetDateTime;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static xyz.telosaddon.yuno.TelosAddon.MOD_NAME;
import static xyz.telosaddon.yuno.TelosAddon.MOD_VERSION;

public class DiscordRPCManager implements IPCListener, ClientPlayConnectionEvents.Disconnect,ClientPlayConnectionEvents.Join {

    private static final long APPLICATION_ID = 1290485474452045865L;
    private static final long UPDATE_PERIOD = 4200L;

    private static final Logger logger = TelosAddon.LOGGER;

    private IPCClient client;
    private OffsetDateTime startTimestamp;

    private Timer updateTimer;
    private boolean connected;

    public void start() {
        try{
            logger.info("Starting Discord RPC...");
            if (isActive()) {
                return;
            }
            startTimestamp = OffsetDateTime.now();
            client = new IPCClient(APPLICATION_ID);
            client.setListener(this);
            try {
                client.connect();
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to connect to Discord RPC!");
                logger.log(Level.WARNING, e.toString());
            }
        } catch (Throwable e) {
            logger.log(Level.WARNING,"Discord RPC has thrown an unexpected error while trying to start...");
            logger.log(Level.WARNING, e.toString());
        }
    }


    public void stop() {
        if (isActive()) {
            connected = false;
            client.close();
        }
    }
    public void updatePresence() {
        if (!isActive()) return;
        if (!TelosAddon.getInstance().isOnTelos() || !connected){
            client.sendRichPresence(new RichPresence.Builder().build());
            System.out.println("Not on telos");
            return;
        }
        String largeImageDescription = "telosrealms.com";
        String smallImageDescription = LocalAPI.getCurrentCharacterType() + " Lv" + LocalAPI.getCurrentCharacterLevel() + " " + LocalAPI.getCurrentCharacterClass();
        String detailsString = LocalAPI.getCurrentCharacterWorld() + " | " + LocalAPI.getCurrentCharacterArea();

        // TODO: make these configurable
        String stateString = (LocalAPI.getCurrentCharacterFighting().length() > 0
                ? ("Fighting " + LocalAPI.getCurrentCharacterFighting())
                : " ~Just chillin'");
        RichPresence presence = new RichPresence.Builder()
                .setState(stateString)
                .setDetails(detailsString)
                .setStartTimestamp(startTimestamp)
                .setLargeImage("teloslogo", largeImageDescription)
                .setSmallImage(LocalAPI.getCurrentCharacterType().toLowerCase(), smallImageDescription)
                .build();
        client.sendRichPresence(presence);
    }
    @Override
    public void onReady(IPCClient client) {
        logger.info("Discord RPC started.");
        connected = true;
    }

    @Override
    public void onDisconnect(IPCClient client, Throwable t) {
        this.client = null;
        connected = false;
    }
    public boolean isActive() {
        return client != null && connected;
    }

    @Override
    public void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        connected = false;
    }

    @Override
    public void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        connected = true;
    }
}
