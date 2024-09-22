package xyz.telosaddon.yuno.ui.tabs;

import net.minecraft.client.MinecraftClient;
import xyz.telosaddon.yuno.ui.CustomElement;
import xyz.telosaddon.yuno.ui.CustomUiManager;
import xyz.telosaddon.yuno.ui.elements.CustomButton;

import java.util.Arrays;
import java.util.List;

public class TeleportTab{
    private final CustomUiManager uiManager;
    private List<CustomElement> elements;

    public TeleportTab(CustomUiManager uiManager) {
        this.uiManager = uiManager;
    }

    public void loadButtons() {
        final String[] serverToJoin = {"Hub-1"};
        this.elements = Arrays.asList(
                new CustomButton(8, 83, 40, 20, "Hub-1", (button) -> {
                    joinWorld("Hub-1");
                }).setTextInMiddle(true),
                new CustomButton(63, 83, 40, 20, "Hub-2", (button) -> {
                    joinWorld("Hub-2");
                }).setTextInMiddle(true),
                new CustomButton(118, 83, 40, 20, "Hub-3", (button) -> {
                    joinWorld("Hub-3");
                }).setTextInMiddle(true),
                new CustomButton(173, 83, 40, 20, "Hub-4", (button) -> {
                    joinWorld("Hub-4");
                }).setTextInMiddle(true),
                new CustomButton(8, 110, 40, 20, "Galla", (button) -> {
                    joinWorld("Galla");
                }).setTextInMiddle(true),
                new CustomButton(63, 110, 40, 20, "Twilight", (button) -> {
                    joinWorld("Twilight");
                }).setTextInMiddle(true),
                new CustomButton(118, 110, 40, 20, "Epenon", (button) -> {
                    joinWorld("Epenon");
                }).setTextInMiddle(true),
                new CustomButton(173, 110, 40, 20, "Spire", (button) -> {
                    joinWorld("Spire");
                }).setTextInMiddle(true),
                new CustomButton(8, 137, 40, 20, "Eternia", (button) -> {
                    joinWorld("Eternia");
                }).setTextInMiddle(true),
                new CustomButton(63, 137, 40, 20, "Terra", (button) -> {
                    joinWorld("Terra");
                }).setTextInMiddle(true),
                new CustomButton(118, 137, 40, 20, "Develyn", (button) -> {
                    joinWorld("Develyn");
                }).setTextInMiddle(true),
                new CustomButton(173, 137, 40, 20, "Xeros", (button) -> {
                    joinWorld("Xeros");
                }).setTextInMiddle(true),
                new CustomButton(8, 164, 40, 20, "Wahr", (button) -> {
                    joinWorld("Wahr");
                }).setTextInMiddle(true),
                new CustomButton(63, 164, 40, 20, "Walms", (button) -> {
                    joinWorld("Walms");
                }).setTextInMiddle(true),
                new CustomButton(118, 164, 40, 20, "Yollixar", (button) -> {
                    joinWorld("Yollixar");
                }).setTextInMiddle(true),
                new CustomButton(173, 164, 40, 20, "Valoria", (button) -> {
                    joinWorld("Valoria");
                }).setTextInMiddle(true),
                new CustomButton(8, 191, 40, 20, "Nymeris", (button) -> {
                    joinWorld("Nymeris");
                }).setTextInMiddle(true),
                new CustomButton(63, 191, 40, 20, "Kyndra", (button) -> {
                    joinWorld("Kyndra");
                }).setTextInMiddle(true),
                new CustomButton(118, 191, 40, 20, "Wynnthera", (button) -> {
                    joinWorld("Wynnthera");
                }).setTextInMiddle(true),
                new CustomButton(173, 191, 40, 20, "Runedar", (button) -> {
                    joinWorld("Runedar");
                }).setTextInMiddle(true),
                new CustomButton(8, 218, 40, 20, "Quinthor", (button) -> {
                    joinWorld("Quinthor");
                }).setTextInMiddle(true),
                new CustomButton(63, 218, 40, 20, "Pyrelia", (button) -> {
                    joinWorld("Pyrelia");
                }).setTextInMiddle(true),
                new CustomButton(118, 218, 40, 20, "Ashburn", (button) -> {
                    joinWorld("Ashburn");
                }).setTextInMiddle(true),
                new CustomButton(173, 218, 40, 20, "Xelia", (button) -> {
                    joinWorld("Xelia");
                }).setTextInMiddle(true),
                new CustomButton(8, 245, 40, 20, "Skyblock", (button) -> {
                    joinWorld("Skyblock");
                }).setTextInMiddle(true)
                );

        uiManager.getCustomElements().addAll(this.elements);
    }

    private void joinWorld(String worldName){
        if (MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().player.networkHandler.sendChatCommand("joinq " + worldName);
    }
}
