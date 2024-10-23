package xyz.telosaddon.yuno;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class InitializeCommands {

    Boolean isInGuildChat = false;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void initializeCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("gc")
                .then(argument("message", StringArgumentType.string())
                        .executes(context -> {
                            final String message = StringArgumentType.getString(context, "message");
                            if (!isInGuildChat) {
                                isInGuildChat = true;
                                MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("chat guild");
                            }
                            MinecraftClient.getInstance().getNetworkHandler().sendChatMessage(message);

                            scheduler.schedule(() -> {
                                // Exit guild chat
                                MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("chat guild");
                                isInGuildChat = false;
                            }, 500, TimeUnit.MILLISECONDS);
                            isInGuildChat = false;
                            return 1;
                        })
                )
        ));

        ClientSendMessageEvents.COMMAND.register((command -> {
            if (command.equals("chat guild")) {
                isInGuildChat = !isInGuildChat;
            }
        }));

    }
}
