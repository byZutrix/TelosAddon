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
    private static final long CACHE_LIFETIME = 56750; // 1 minute (in milliseconds)
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
                if (Objects.equals(character.getUniqueId(), active)) {
                    elements.add(new CustomText(8, 180 + y_offset, "§lClass: " + character.getType().substring(6)));
                } else {
                    elements.add(new CustomText(8, 180 + y_offset, "Class: " + character.getType().substring(6)));
                }
                elements.add(new CustomText(108, 180 + y_offset, "Level: " + character.getLevel()));
                elements.add(new CustomText(178, 180 + y_offset, "Fame: " + character.getFame()));
                elements.add(new CustomText(258, 180 + y_offset, "Mode: " + character.getRuleset().getType()));

                y_offset += 20;
            }

            Statistics stats = playerData.getData().getStatistics();

            // shadow lands
            elements.add(new CustomText(425, 20, "Shadow Lands:"));
            elements.add(new CustomText(425, 40, "Reaper: " + stats.getRotmcBossReaper() + " (" + stats.getRotmcRoyalReaper() + "/§4" + stats.getRotmcBloodshotReaper() + "§r)"));
            elements.add(new CustomText(425, 60, "Warden: " + stats.getRotmcBossWarden() + " (" + stats.getRotmcRoyalWarden() + "/§4" + stats.getRotmcBloodshotWarden() + "§r)"));
            elements.add(new CustomText(425, 80, "Herald: " + stats.getRotmcBossHerald() + " (" + stats.getRotmcRoyalHerald() + "/§4" + stats.getRotmcBloodshotHerald() + "§r)"));
            elements.add(new CustomText(425, 100, "Defender: " + stats.getRotmcBossDefender() + " (" + stats.getRotmcRoyalDefender() + "/§4" + stats.getRotmcBloodshotDefender() + "§r)"));

            // world bosses
            elements.add(new CustomText(550, 20, "World Bosses:"));
            elements.add(new CustomText(550, 40, "Tidol: " + stats.getRotmcBossTidol() + " (" + stats.getRotmcRoyalTidol() + "§r/§e" + stats.getRotmcCompanionTidol() + "§r)"));
            elements.add(new CustomText(550, 60, "Lotil: " + stats.getRotmcBossLotil() + " (" + stats.getRotmcRoyalLotil() + "§r/§e" + stats.getRotmcCompanionLotil() + "§r)"));
            elements.add(new CustomText(550, 80, "Valus: " + stats.getRotmcBossValus() + " (" + stats.getRotmcRoyalValus() + "§r/§e" + stats.getRotmcCompanionValus() + "§r)"));
            elements.add(new CustomText(550, 100, "Chungus: " + stats.getRotmcBossChungus() + " (" + stats.getRotmcRoyalChungus() + "§r/§e" + stats.getRotmcCompanionChungus() + "§r)"));
            elements.add(new CustomText(550, 120, "Astaroth: " + stats.getRotmcBossAstaroth() + " (" + stats.getRotmcRoyalAstaroth() + "§r/§e" + stats.getRotmcCompanionAstaroth() + "§r)"));
            elements.add(new CustomText(550, 140, "Glumi: " + stats.getRotmcBossGlumi() + " (" + stats.getRotmcRoyalGlumi() + "§r/§e" + stats.getRotmcCompanionGlumi() + "§r)"));
            elements.add(new CustomText(550, 160, "Anubis: " + stats.getRotmcBossAnubis() + " (" + stats.getRotmcRoyalAnubis() + "§r/§e" + stats.getRotmcCompanionAnubis() + "§r)"));
            elements.add(new CustomText(550, 180, "Oozul: " + stats.getRotmcBossOozul() + " (" + stats.getRotmcRoyalOozul() + "§r/§e" + stats.getRotmcCompanionOozul() + "§r)"));
            elements.add(new CustomText(550, 200, "Illarius: " + stats.getRotmcBossIllarius() + " (" + stats.getRotmcRoyalIllarius() + "§r/§e" + stats.getRotmcCompanionIllarius() + "§r)"));
            elements.add(new CustomText(550, 220, "Freddy: " + stats.getRotmcBossFreddy() + " (" + stats.getRotmcRoyalFreddy() + "§r/§e" + stats.getRotmcCompanionFreddy() + "§r)"));

            // world dungeons
            elements.add(new CustomText(675, 20, "World Dungeons:"));
            elements.add(new CustomText(675, 40, "Omnipotent: " + stats.getRotmcBossOmnipotent() + " (" + stats.getRotmcRoyalOmnipotent() + "/§4" + stats.getRotmcBloodshotOmnipotent() + "§r)"));
            elements.add(new CustomText(675, 60, "Prismara: " + stats.getRotmcBossPrismara() + " (" + stats.getRotmcRoyalPrismara() + ")"));
            elements.add(new CustomText(675, 80, "Kurvaros: " + stats.getRotmcBossKurvaros() + " (" + stats.getRotmcRoyalKurvaros() + "/§4" + stats.getRotmcBloodshotKurvaros() + "§r)"));
            elements.add(new CustomText(675, 100, "Thalassar: " + stats.getRotmcBossThalassar() + " (" + stats.getRotmcRoyalThalassar() + ")"));
            elements.add(new CustomText(675, 120, "Chronos: " + stats.getRotmcBossChronos() + " (" + stats.getRotmcRoyalChronos() + "/§4" + stats.getRotmcBloodshotChronos() + "§r)"));
            elements.add(new CustomText(675, 140, "Shadowflare: " + stats.getRotmcBossShadowflare() + " (" + stats.getRotmcRoyalShadowflare() + "/§4" + stats.getRotmcBloodshotShadowflare() + "§r)"));
            elements.add(new CustomText(675, 160, "Golden Freddy: " + stats.getRotmcBossGoldenFreddy() + " (" + stats.getRotmcRoyalGoldenFreddy() + ")"));
            elements.add(new CustomText(675, 180, "Loa: " + stats.getRotmcBossLoa() + " (" + stats.getRotmcRoyalLoa() + ")"));
            elements.add(new CustomText(675, 200, "Silex: " + stats.getRotmcBossSilex() + " (" + stats.getRotmcRoyalSilex() + "/§4" + stats.getRotmcBloodshotSilex() + "§r)"));
            elements.add(new CustomText(675, 220, "Onyx: " + stats.getRotmcBossOnyx() + " (" + "/§4" + stats.getRotmcBloodshotOnyx() + "§r)"));

            // dungeon bosses
            elements.add(new CustomText(800, 20, "Dungeon Bosses:"));
            elements.add(new CustomText(800, 40, "Jones: " + stats.getRotmcBossJones() + " (§2" + stats.getRotmcIrradiatedJones() + "§r/§e" + stats.getRotmcCompanionJones() + "§r)"));
            elements.add(new CustomText(800, 60, "Miraj: " + stats.getRotmcBossMiraj() + " (§2" + stats.getRotmcIrradiatedMiraj() + "§r/§e" + stats.getRotmcCompanionMiraj() + "§r)"));
            elements.add(new CustomText(800, 80, "Choji: " + stats.getRotmcBossChoji() + " (§2" + stats.getRotmcIrradiatedChoji() + "§r/§e" + stats.getRotmcCompanionChoji() + "§r)"));
            elements.add(new CustomText(800, 100, "Drayruk: " + stats.getRotmcBossDrayruk() + " (§2" + stats.getRotmcIrradiatedDrayruk() + "§r/§e" + stats.getRotmcCompanionDrayruk() + "§r)"));
            elements.add(new CustomText(800, 120, "Zhum: " + stats.getRotmcBossZhum() + " (§2" + stats.getRotmcIrradiatedZhum() + "§r/§e" + stats.getRotmcCompanionZhum() + "§r)"));
            elements.add(new CustomText(800, 140, "Flora: " + stats.getRotmcBossFlora() + " (§2" + stats.getRotmcIrradiatedFlora() + "§r/§e" + stats.getRotmcCompanionFlora() + "§r)"));
            elements.add(new CustomText(800, 160, "Malfas: " + stats.getRotmcBossMalfas() + " (" + stats.getRotmcRoyalMalfas() + "§r/§e" + stats.getRotmcCompanionMalfas() + "§r)"));
            elements.add(new CustomText(800, 180, "Ashenclaw: " + stats.getRotmcBossAshenclaw() + " (" + stats.getRotmcRoyalAshenclaw() + "§r/§e" + stats.getRotmcCompanionAshenclaw() + "§r)"));
            elements.add(new CustomText(800, 200, "Heptavius: " + stats.getRotmcBossHeptavius() + " (" + stats.getRotmcRoyalHeptavius() + "§r/§e" + stats.getRotmcCompanionHeptavius() + "§r)"));
            elements.add(new CustomText(800, 220, "Corvack: " + stats.getRotmcBossCorvack() + " (" + stats.getRotmcRoyalCorvack() + "§r/§e" + stats.getRotmcCompanionCorvack() + "§r)"));
            elements.add(new CustomText(800, 240, "Magnus: " + stats.getRotmcBossMagnus() + " (" + stats.getRotmcRoyalMagnus() + "§r/§e" + stats.getRotmcCompanionMagnus() + "§r)"));
            elements.add(new CustomText(800, 260, "Pyro: " + stats.getRotmcBossPyro() + " (" + stats.getRotmcRoyalPyro() + "§r/§e" + stats.getRotmcCompanionPyro() + "§r)"));
            elements.add(new CustomText(800, 280, "Cryo: " + stats.getRotmcBossCryo() + " (" + stats.getRotmcRoyalCryo() + "§r/§e" + stats.getRotmcCompanionCryo() + "§r)"));
            elements.add(new CustomText(800, 300, "Arctic Colossus: " + stats.getRotmcBossArcticColossus() + " (" + stats.getRotmcRoyalArcticColossus() + "§r/§e" + stats.getRotmcCompanionArcticColossus() + "§r)"));
            elements.add(new CustomText(800, 320, "Frozen Goliath: " + stats.getRotmcBossOog() + " (" + stats.getRotmcRoyalOog() + "§r/§e" + stats.getRotmcCompanionArcticColossus() + "§r)"));
            elements.add(new CustomText(800, 340, "Osiris & Orion: " + stats.getRotmcBossDarkChampion() + " (" + stats.getRotmcRoyalDarkChampion() + ")"));
            elements.add(new CustomText(800, 360, "Valerion: " + stats.getRotmcBossValerion() + " (§4" + stats.getRotmcBloodshotValerion() + "§r/§e" + stats.getRotmcCompanionValerion() + "§r)"));
            elements.add(new CustomText(800, 380, "Nebula: " + stats.getRotmcBossNebula() + " (§4" + stats.getRotmcBloodshotNebula() + "§r)"));
            elements.add(new CustomText(800, 400, "Ophanim: " + stats.getRotmcBossOphanim() + " (§4" + stats.getRotmcBloodshotOphanim() + "§r)"));
            elements.add(new CustomText(800, 420, "Asmodeus: " + stats.getRotmcBossAsmodeus() + " (§4" + stats.getRotmcBloodshotAsmodeus() + "§r)"));
            elements.add(new CustomText(800, 440, "Seraphim: " + stats.getRotmcBossSeraphim() + " (§4" + stats.getRotmcBloodshotSeraphim() + "§r)"));




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
