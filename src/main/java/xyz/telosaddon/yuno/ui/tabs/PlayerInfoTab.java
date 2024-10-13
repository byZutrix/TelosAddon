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

            elements.add(new CustomText(725, 20, "White bags:"));
            elements.add(new CustomText(725, 40, "Tidol: " + stats.getRotmcBossTidol() + " (" + stats.getRotmcRoyalTidol() + ")"));
            elements.add(new CustomText(725, 60, "Astaroth: " + stats.getRotmcBossAstaroth() + " (" + stats.getRotmcRoyalAstaroth() + ")"));
            elements.add(new CustomText(725, 80, "Glumi: " + stats.getRotmcBossGlumi() + " (" + stats.getRotmcRoyalGlumi() + ")"));
            elements.add(new CustomText(725, 100, "Anubis: " + stats.getRotmcBossAnubis() + " (" + stats.getRotmcRoyalAnubis() + ")"));
            elements.add(new CustomText(725, 120, "Oozul: " + stats.getRotmcBossOozul() + " (" + stats.getRotmcRoyalOozul() + ")"));
            elements.add(new CustomText(725, 140, "Freddy: " + stats.getRotmcBossFreddy() + " (" + stats.getRotmcRoyalFreddy() + ")"));
            elements.add(new CustomText(725, 160, "Chungus: " + stats.getRotmcBossChungus() + " (" + stats.getRotmcRoyalChungus() + ")"));
            elements.add(new CustomText(725, 180, "Lotil: " + stats.getRotmcBossLotil() + " (" + stats.getRotmcRoyalLotil() + ")"));
            elements.add(new CustomText(725, 200, "Illarius: " + stats.getRotmcBossIllarius() + " (" + stats.getRotmcRoyalIllarius() + ")"));
            elements.add(new CustomText(725, 220, "Valus: " + stats.getRotmcBossValus() + " (" + stats.getRotmcRoyalValus() + ")"));
            elements.add(new CustomText(725, 240, "Reaper: " + stats.getRotmcBossReaper() + " (" + stats.getRotmcRoyalReaper() + ")"));
            elements.add(new CustomText(725, 260, "Warden: " + stats.getRotmcBossWarden() + " (" + stats.getRotmcRoyalWarden() + ")"));
            elements.add(new CustomText(725, 280, "Herald: " + stats.getRotmcBossHerald() + " (" + stats.getRotmcRoyalHerald() + ")"));
            elements.add(new CustomText(725, 300, "Defender: " + stats.getRotmcBossDefender() + " (" + stats.getRotmcRoyalDefender() + ")"));
            elements.add(new CustomText(825, 40, "Malfas: " + stats.getRotmcBossMalfas() + " (" + stats.getRotmcRoyalMalfas() + ")"));
            elements.add(new CustomText(825, 60, "Heptavius: " + stats.getRotmcBossHeptavius() + " (" + stats.getRotmcRoyalHeptavius() + ")"));
            elements.add(new CustomText(825, 80, "Arctic Colossus: " + stats.getRotmcBossArcticColossus() + " (" + stats.getRotmcRoyalArcticColossus() + ")"));
            elements.add(new CustomText(825, 100, "Frozen Goliath: " + stats.getRotmcBossOog() + " (" + stats.getRotmcRoyalOog() + ")"));
            elements.add(new CustomText(825, 120, "Magnus: " + stats.getRotmcBossMagnus() + " (" + stats.getRotmcRoyalMagnus() + ")"));
            elements.add(new CustomText(825, 140, "Pyro: " + stats.getRotmcBossPyro() + " (" + stats.getRotmcRoyalPyro() + ")"));
            elements.add(new CustomText(825, 160, "Ashenclaw: " + stats.getRotmcBossAshenclaw() + " (" + stats.getRotmcRoyalAshenclaw() + ")"));
            elements.add(new CustomText(825, 180, "Cryo: " + stats.getRotmcBossCryo() + " (" + stats.getRotmcRoyalCryo() + ")"));
            elements.add(new CustomText(825, 200, "Corvack: " + stats.getRotmcBossCorvack() + " (" + stats.getRotmcRoyalCorvack() + ")"));
            elements.add(new CustomText(825, 220, "Omnipotent: " + stats.getRotmcBossOmnipotent() + " (" + stats.getRotmcRoyalOmnipotent() + ")"));
            elements.add(new CustomText(825, 240, "Prismara: " + stats.getRotmcBossPrismara() + " (" + stats.getRotmcRoyalPrismara() + ")"));
            elements.add(new CustomText(825, 260, "Thalassar: " + stats.getRotmcBossThalassar() + " (" + stats.getRotmcRoyalThalassar() + ")"));
            elements.add(new CustomText(825, 280, "Osiris & Orion: " + stats.getRotmcBossDarkChampion() + " (" + stats.getRotmcRoyalDarkChampion() + ")"));
            elements.add(new CustomText(825, 300, "Golden Freddy: " + stats.getRotmcBossGoldenFreddy() + " (" + stats.getRotmcRoyalGoldenFreddy() + ")"));
            elements.add(new CustomText(825, 320, "Chronos: " + stats.getRotmcBossChronos() + " (" + stats.getRotmcRoyalChronos() + ")"));
            elements.add(new CustomText(825, 340, "Kurvaros: " + stats.getRotmcBossKurvaros() + " (" + stats.getRotmcRoyalKurvaros() + ")"));
            elements.add(new CustomText(825, 360, "Silex: " + stats.getRotmcBossSilex() + " (" + stats.getRotmcRoyalSilex() + ")"));
            elements.add(new CustomText(825, 380, "Loa: " + stats.getRotmcBossLoa() + " (" + stats.getRotmcRoyalLoa() + ")"));
            elements.add(new CustomText(825, 400, "Shadowflare: " + stats.getRotmcBossShadowflare() + " (" + stats.getRotmcRoyalShadowflare() + ")"));

            elements.add(new CustomText(600, 20, "Black bags:"));
            elements.add(new CustomText(600, 40, "Omnipotent: " + stats.getRotmcBossOmnipotent() + " (" + stats.getRotmcBloodshotOmnipotent() + ")"));
            elements.add(new CustomText(600, 60, "Chronos: " + stats.getRotmcBossChronos() + " (" + stats.getRotmcBloodshotChronos() + ")"));
            elements.add(new CustomText(600, 80, "Kurvaros: " + stats.getRotmcBossKurvaros() + " (" + stats.getRotmcBloodshotKurvaros() + ")"));
            elements.add(new CustomText(600, 100, "Silex: " + stats.getRotmcBossSilex() + " (" + stats.getRotmcBloodshotSilex() + ")"));
            elements.add(new CustomText(600, 120, "Shadowflare: " + stats.getRotmcBossShadowflare() + " (" + stats.getRotmcBloodshotShadowflare() + ")"));
            elements.add(new CustomText(600, 140, "Valerion: " + stats.getRotmcBossValerion() + " (" + stats.getRotmcBloodshotValerion() + ")"));
            elements.add(new CustomText(600, 160, "Nebula: " + stats.getRotmcBossNebula() + " (" + stats.getRotmcBloodshotNebula() + ")"));
            elements.add(new CustomText(600, 180, "Ophanim: " + stats.getRotmcBossOphanim() + " (" + stats.getRotmcBloodshotOphanim() + ")"));
            elements.add(new CustomText(600, 200, "Asmodeus: " + stats.getRotmcBossAsmodeus() + " (" + stats.getRotmcBloodshotAsmodeus() + ")"));
            elements.add(new CustomText(600, 220, "Seraphim: " + stats.getRotmcBossSeraphim() + " (" + stats.getRotmcBloodshotSeraphim() + ")"));


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
