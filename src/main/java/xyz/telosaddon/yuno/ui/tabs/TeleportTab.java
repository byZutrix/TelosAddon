package xyz.telosaddon.yuno.ui.tabs;

import net.minecraft.client.MinecraftClient;
import xyz.telosaddon.yuno.ui.CustomElement;
import xyz.telosaddon.yuno.ui.CustomUiManager;
import xyz.telosaddon.yuno.ui.elements.CustomButton;
import xyz.telosaddon.yuno.utils.LocalAPI;

import java.util.ArrayList;
import java.util.List;

import static xyz.telosaddon.yuno.utils.RealmUtils.nexusThenTp;

public class TeleportTab{
    private final CustomUiManager uiManager;
    private List<CustomElement> elements;

    public TeleportTab(CustomUiManager uiManager) {
        this.uiManager = uiManager;
        elements = new ArrayList<>();
        final String[] serverNames = {"Hub-1", "Hub-2", "Hub-3", "Hub-4", "Hub-5", "Hub-6", "Hub-7",
                "Galla", "Twilight", "Epenon", "Spire", "Eternia", "Terra", "Develyn",
                "Xeros", "Wahr", "Walms", "Yollixar", "Valoria", "Nymeris", "Kyndra",
                "Wynnthera", "Runedar", "Quinthor", "Pyrelia", "Ashburn", "Xelia", "Skyblock"};

        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 4; j++){
                int finalI = i;
                int finalJ = j;
                elements.add(new CustomButton(8 + i * (BTN_WIDTH + 7), 83 + j * (BTN_HEIGHT + 7), BTN_WIDTH, BTN_HEIGHT, serverNames[j*7+i], (button) -> {
                    joinWorld(serverNames[finalJ *7+ finalI]);
                }).setTextInMiddle(true));
            }
        }
    }

    final int BTN_WIDTH = 55;
    final int BTN_HEIGHT = 20;

    public void loadButtons() {

        uiManager.getCustomElements().addAll(this.elements);
    }

    private void joinWorld(String worldName){
        if (MinecraftClient.getInstance().player == null) return;

        if (!LocalAPI.getCurrentCharacterWorld().contains("Hub")){
            nexusThenTp(worldName);
        }
        else{
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand("joinq " + worldName);
        }

    }
}
