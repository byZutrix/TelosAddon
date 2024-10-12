package xyz.telosaddon.yuno.ui.tabs;

import com.google.gson.Gson;
import xyz.telosaddon.yuno.gson.AllItem;
import xyz.telosaddon.yuno.gson.Statistics;
import xyz.telosaddon.yuno.ui.CustomElement;
import xyz.telosaddon.yuno.ui.CustomUiManager;
import xyz.telosaddon.yuno.ui.elements.CustomButton;
import xyz.telosaddon.yuno.ui.elements.CustomText;
import xyz.telosaddon.yuno.ui.elements.CustomTextField;
import xyz.telosaddon.yuno.gson.PlayerData;
import xyz.telosaddon.yuno.utils.PlayerDataCache;
import xyz.telosaddon.yuno.utils.TelosApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerInfoTab extends AbstractTab {
    private final CustomUiManager uiManager;
    private List<CustomElement> elements; // Initialized in the constructor
    private final TelosApi telosApi;
    private PlayerData playerData; // Will be null until fetched
    private static final long CACHE_LIFETIME = 60000; // 1 minute (in milliseconds)
    private static final PlayerDataCache playerDataCache = new PlayerDataCache(CACHE_LIFETIME); // Static cache instance

    public PlayerInfoTab(CustomUiManager uiManager) {
        this.uiManager = uiManager;
        this.telosApi = new TelosApi();
        this.elements = new ArrayList<>(); // Initialize elements list

        // Load initial UI
        loadButtons();
    }

    public void loadButtons() {
        // Create text field and fetch button
        CustomTextField playerNameField = new CustomTextField(8, 83, 150, 20, "Player Name");
        CustomButton fetchButton = new CustomButton(163, 83, 40, 20, "Fetch", (button) -> {
            String input = playerNameField.getText();
            fetchData(input); // Fetch data using the input from the text field
        }).setTextInMiddle(true);

        // Clear existing elements to avoid duplicates
        elements.clear();

        // Add player data as CustomText elements based on the API response
        if (playerData != null) {
            // player info
            elements.add(new CustomText(8, 110, "Username: " + playerData.getData().getUsername()));
            elements.add(new CustomText(8, 130, "Last Played: " + new java.util.Date(playerData.getData().getLastPlayed())));
            elements.add(new CustomText(8, 150, "Rank: " + playerData.getData().getRank().substring(6)));
            elements.add(new CustomText(8, 170, "Glory: " + playerData.getData().getBalance()));

            String active = playerData.getData().getCharacters().getSelected();

            // character info
            int y_offset = 20;
            for (AllItem character : playerData.getData().getCharacters().getAll()) {
                elements.add(new CustomText(8, 180 + y_offset, "Class: " + character.getType().substring(6)));
                elements.add(new CustomText(108, 180 + y_offset, "Level: " + character.getLevel()));
                elements.add(new CustomText(178, 180 + y_offset, "Fame: " + character.getFame()));
                elements.add(new CustomText(258, 180 + y_offset, "Mode: " + character.getRuleset().getType()));
                if (Objects.equals(character.getUniqueId(), active)) {
                    elements.add(new CustomText(388, 180 + y_offset, "Active Character"));
                }


                y_offset += 20;
            }

            Statistics stats = playerData.getData().getStatistics();

            elements.add(new CustomText(750, 20, "White bags:"));
            elements.add(new CustomText(750, 40, "Tidol: " + stats.getRotmcRoyalTidol()));
            elements.add(new CustomText(750, 40, "Tidol: " + stats.getRotmcRoyalTidol()));
            elements.add(new CustomText(750, 60, "Astaroth: " + stats.getRotmcRoyalAstaroth()));
            elements.add(new CustomText(750, 80, "Glumi: " + stats.getRotmcRoyalGlumi()));
            elements.add(new CustomText(750, 100, "Anubis: " + stats.getRotmcRoyalAnubis()));
            elements.add(new CustomText(750, 120, "Oozul: " + stats.getRotmcRoyalOozul()));
            elements.add(new CustomText(750, 140, "Freddy: " + stats.getRotmcRoyalFreddy()));
            elements.add(new CustomText(750, 160, "Chungus: " + stats.getRotmcRoyalChungus()));
            elements.add(new CustomText(750, 180, "Lotil: " + stats.getRotmcRoyalLotil()));
            elements.add(new CustomText(750, 200, "Illarius: " + stats.getRotmcRoyalIllarius()));
            elements.add(new CustomText(750, 220, "Valus: " + stats.getRotmcRoyalValus()));
            elements.add(new CustomText(750, 240, "Reaper: " + stats.getRotmcRoyalReaper()));
            elements.add(new CustomText(750, 260, "Warden: " + stats.getRotmcRoyalWarden()));
            elements.add(new CustomText(750, 280, "Herald: " + stats.getRotmcRoyalHerald()));
            elements.add(new CustomText(750, 300, "Defender: " + stats.getRotmcRoyalDefender()));
            elements.add(new CustomText(850, 40, "Malfas: " + stats.getRotmcRoyalMalfas()));
            elements.add(new CustomText(850, 60, "Heptavius: " + stats.getRotmcRoyalHeptavius()));
            elements.add(new CustomText(850, 80, "Arctic Colossus: " + stats.getRotmcRoyalArcticColossus()));
            elements.add(new CustomText(850, 100, "Frozen Goliath: " + stats.getRotmcRoyalOog()));
            elements.add(new CustomText(850, 120, "Magnus: " + stats.getRotmcRoyalMagnus()));
            elements.add(new CustomText(850, 140, "Pyro: " + stats.getRotmcRoyalPyro()));
            elements.add(new CustomText(850, 160, "Ashenclaw: " + stats.getRotmcRoyalAshenclaw()));
            elements.add(new CustomText(850, 180, "Cryo: " + stats.getRotmcRoyalCryo()));
            elements.add(new CustomText(850, 200, "Corvack: " + stats.getRotmcRoyalCorvack()));
            elements.add(new CustomText(850, 220, "Omnipotent: " + stats.getRotmcRoyalOmnipotent()));
            elements.add(new CustomText(850, 240, "Prismara: " + stats.getRotmcRoyalPrismara()));
            elements.add(new CustomText(850, 260, "Thalassar: " + stats.getRotmcRoyalThalassar()));
            elements.add(new CustomText(850, 280, "Osiris & Orion: " + stats.getRotmcRoyalDarkChampion()));
            elements.add(new CustomText(850, 300, "Golden Freddy: " + stats.getRotmcRoyalGoldenFreddy()));
            elements.add(new CustomText(850, 320, "Chronos: " + stats.getRotmcRoyalChronos()));
            elements.add(new CustomText(850, 340, "Kurvaros: " + stats.getRotmcRoyalKurvaros()));
            elements.add(new CustomText(850, 360, "Silex: " + stats.getRotmcRoyalSilex()));
            elements.add(new CustomText(850, 380, "Loa: " + stats.getRotmcRoyalLoa()));
            elements.add(new CustomText(850, 400, "Shadowflare: " + stats.getRotmcRoyalShadowflare()));

            elements.add(new CustomText(650, 20, "Black bags:"));
            elements.add(new CustomText(650, 40, "Omnipotent: " + stats.getRotmcBloodshotOmnipotent()));
            elements.add(new CustomText(650, 60, "Chronos: " + stats.getRotmcBloodshotChronos()));
            elements.add(new CustomText(650, 80, "Kurvaros: " + stats.getRotmcBloodshotKurvaros()));
            elements.add(new CustomText(650, 100, "Silex: " + stats.getRotmcBloodshotSilex()));
            elements.add(new CustomText(650, 120, "Shadowflare: " + stats.getRotmcBloodshotShadowflare()));
            elements.add(new CustomText(650, 140, "Valerion: " + stats.getRotmcBloodshotValerion()));
            elements.add(new CustomText(650, 160, "Nebula: " + stats.getRotmcBloodshotNebula()));
            elements.add(new CustomText(650, 180, "Ophanim: " + stats.getRotmcBloodshotOphanim()));
            elements.add(new CustomText(650, 200, "Asmodeus: " + stats.getRotmcBloodshotAsmodeus()));
            elements.add(new CustomText(650, 220, "Seraphim: " + stats.getRotmcBloodshotSeraphim()));


        } else {
            elements.add(new CustomText(8, 110, "No player data available. Please enter a name and fetch."));
        }

        // Add the input field and fetch button
        elements.add(playerNameField);
        elements.add(fetchButton);

        // Update the UI manager with the current elements
        uiManager.clearCustomElements(); // Clear previous elements
        uiManager.getCustomElements().addAll(elements); // Add updated elements
    }

    private void fetchData(String playerName) {
        // Check cache first
        String cachedData = playerDataCache.get(playerName);
        if (cachedData != null) {
            // If we have valid cached data, use it
            System.out.println("from cached data");
            playerData = new Gson().fromJson(cachedData, PlayerData.class);
            loadButtons(); // Update the UI
        } else {
            // If not cached or expired, fetch from API
            System.out.println("from api");
            telosApi.fetchPlayerData(playerName, new TelosApi.ApiResponseCallback() {
                @Override
                public void onSuccess(String response) {
                    playerData = new Gson().fromJson(response, PlayerData.class);
                    playerDataCache.put(playerName, response); // Cache the response
                    loadButtons(); // Update the UI
                }

                @Override
                public void onFailure(IOException e) {
                    e.printStackTrace();
                    // Clear player data on failure
                    playerData = null;
                    loadButtons(); // Update the UI
                }
            });
        }
    }
}
