package xyz.telosaddon.yuno.discordrpc;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.utils.LocalAPI;

import java.time.OffsetDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscordRPCManager implements IPCListener {

    private static final long APPLICATION_ID = 1290485474452045865L;

    private static final Logger logger = TelosAddon.LOGGER;

    private IPCClient client;
    private OffsetDateTime startTimestamp;

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

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if ((System.currentTimeMillis()/50) % 100 == 0) updatePresence();
        });
    }


    public void stop() {
        logger.log(Level.INFO, "Attempting to disconnect RPC \nConnectedStatus: " + connected);
        connected = false;
        client.close();
    }

    public void updatePresence() {
        if (!isActive()) return;
        if (!TelosAddon.getInstance().isOnTelos() || !TelosAddon.getInstance().getConfig().getBoolean("DiscordRPCSetting")){
            client.sendRichPresence(null);
            return;
        }
        String largeImageDescription = "telosrealms.com";
        String smallImageDescription = LocalAPI.getCurrentCharacterType() + " Lv" + LocalAPI.getCurrentCharacterLevel() + " " + LocalAPI.getCurrentCharacterClass();
        RichPresence presence = new RichPresence.Builder()
                .setState(getStateString())
                .setDetails(getDetailsString())
                .setStartTimestamp(startTimestamp)
                .setLargeImage("teloslogo", largeImageDescription)
                .setSmallImage(LocalAPI.getCurrentCharacterType().toLowerCase(), smallImageDescription)
                .build();
        client.sendRichPresence(presence);
    }

    private String getDetailsString(){
        if (TelosAddon.getInstance().getConfig().getBoolean("RPCShowLocationSetting")){
            return LocalAPI.getCurrentCharacterWorld() + " | " + LocalAPI.getCurrentCharacterArea();
        } else{
            return "In an Unknown Place";
        }
    }

    private String getStateString(){
        return (LocalAPI.getCurrentCharacterFighting().length() > 0 && TelosAddon.getInstance().getConfig().getBoolean("RPCShowFightingSetting")
                ? ("Fighting " + LocalAPI.getCurrentCharacterFighting())
                : TelosAddon.getInstance().getConfig().getString("DiscordDefaultStatusMessage"));
    }
    @Override
    public void onReady(IPCClient client) {
        logger.info("Discord RPC started.");
        connected = true;
    }

    @Override
    public void onDisconnect(IPCClient client, Throwable t) {
        logger.info("Discord RPC disconnected.");
        this.client = null;
        connected = false;
    }
    public boolean isActive() {
        return client != null && connected;
    }

    public IPCClient getClient() {
        return client;
    }

}
