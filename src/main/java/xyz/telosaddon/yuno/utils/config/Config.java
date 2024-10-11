package xyz.telosaddon.yuno.utils.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import xyz.telosaddon.yuno.TelosAddon;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class Config {

    private final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Integer.class, new IntegerSerializer())
            .registerTypeAdapter(Double.class, new DoubleSerializer())
            .registerTypeAdapter(Long.class, new LongSerializer())
            .setPrettyPrinting()
            .create();
    private final File configFile;

    /**
     * Backup file in case the main file becomes corrupted
     */
    private final File tmpFile;
    private Map<String, Object> configMap;

    public Config() {
        Path path = FabricLoader.getInstance().getConfigDir();
        this.configFile = new File(path.toFile(), "telosaddon.json");
        this.configMap = new HashMap<>();
        this.tmpFile = new File(path.toFile(), "telosaddon.tmp");
    }

    /**
     * Loads the config from the local json file
     */
    public void load() {
        this.load(false);
    }

    private void load(boolean fromBackup) {
        TelosAddon.LOGGER.info( "Attempting to load config" + (fromBackup ? " from backup..." : "..."));
        configMap = new HashMap<>();
        File file = fromBackup ? tmpFile : configFile;
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {

                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                configMap = GSON.fromJson(reader, type);
            } catch (Exception e) {
                TelosAddon.LOGGER.severe("Error loading config!, resorting to defaults");
                e.printStackTrace();
                if (e instanceof JsonSyntaxException && !fromBackup) {
                    // JsonSyntaxException indicates the main config was corrupted, so load from backup
                    this.load(true);
                    return;
                }
            }
        } else {
            TelosAddon.LOGGER.info("No config or backup file found, resorting to defaults");
        }
        if (configMap == null) configMap = new HashMap<>(); // case both files are blank/corrupted


        loadDefaults();
        save();
    }

    public void loadDefaults(){
        addDefault("GreenBags", 0);
        addDefault("GoldBags", 0);
        addDefault("WhiteBags", 0);
        addDefault("BlackBags", 0);
        addDefault("XMasBags", 0);
        addDefault("Crosses", 0);
        addDefault("Relics", 0);
        addDefault("TotalRuns", 0);
        addDefault("NoWhiteRuns", 0);
        addDefault("TotalPlaytime", 0L);

        addDefault("NewGamma", 15000.0D);
        addDefault("NormalGamma", 1.0D);

        addDefault("MenuColor", new Color(255, 215, 0).getRGB());
        addDefault("BorderColor", new Color(255, 255, 255, 150).getRGB());
        addDefault("FillColor", new Color(0, 0, 0, 100).getRGB());

        addDefault("InfoX", 4);
        addDefault("InfoY", 4);

        addDefault("BagX", -1);
        addDefault("BagY", 60);

        addDefault("GreenSetting", false);
        addDefault("GoldSetting", false);
        addDefault("WhiteSetting", false);
        addDefault("BlackSetting", false);
        addDefault("XMasSetting", false);
        addDefault("CrossSetting", false);
        addDefault("RelicSetting", false);
        addDefault("TotalRunSetting", false);
        addDefault("NoWhiteRunSetting", false);
        addDefault("LifetimeSetting", false);


        addDefault("SwingSetting", false);
        addDefault("GammaSetting", false);
        addDefault("FPSSetting", false);
        addDefault("PingSetting", false);
        addDefault("PlaytimeSetting", false);
        addDefault("SpawnBossesSetting", false);
        addDefault("SoundSetting", false);
        addDefault("Font", "Default");

        addDefault("DiscordRPCSetting", true);
        addDefault("DiscordDefaultStatusMessage", " ~Just chillin'");
        addDefault("RPCShowLocationSetting", true);
        addDefault("RPCShowFightingSetting", false);

        addDefault("SwingIfNoCooldown", false);
        addDefault("ShowMainRangeFeatureEnabled", true);
        addDefault("ShowOffHandRangeFeatureEnabled", false);
        addDefault("ShowMainRangeFeatureHeight", 0.5);
        addDefault("ShowOffHandRangeFeatureHeight", 0.5);

        addDefault("ShowMainRangeFeatureColor", new Color(255, 0, 0).getRGB());
        addDefault("ShowOffHandRangeFeatureColor", new Color(0, 0, 255).getRGB());
    }

    public void addDefault(String key, Object value) {
        if(!configMap.containsKey(key)) {
            //TelosAddon.LOGGER.info("Config key (" + key + ") does not have a value. Using default");
            configMap.put(key, value);
        }
    }

    public void save() {
        this.save(true);
    }

    /**
     * Saves config to the local json file and the previous version becomes a backup .tmp file
     */

    private void save(boolean overwriteBackup) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            synchronized (this) { // lets see if this works, we can try reentrantlock if not
                try {
                    ensureConfigFileExists();
                    ensureTmpFileExists();
                    if (overwriteBackup && !handleBackup()) {
                        TelosAddon.LOGGER.log(Level.WARNING, "Backup failed; skipping config save.");
                        return; // Early exit if backup fails
                    }
                    saveConfig();
                } catch (IOException e) {
                    TelosAddon.LOGGER.log(Level.WARNING, "An error occurred while saving the config: ", e);
                }
            }
        });
    }

    private void ensureTmpFileExists() throws IOException {
        if (!tmpFile.exists()) {
            if (!tmpFile.createNewFile()) {
                throw new IOException("Failed to create temporary backup file.");
            }
        }
    }

    private void ensureConfigFileExists() throws IOException {
        if (!configFile.exists()) {
            if (!configFile.createNewFile()) {
                throw new IOException("Failed to create temporary config file.");
            }
        }
    }

    private boolean handleBackup() {
        if (tmpFile.exists() && !tmpFile.delete()) {
            TelosAddon.LOGGER.log(Level.WARNING, "Could not delete old backup file.");
            return false; // Backup process failed
        }
        return configFile.renameTo(tmpFile); // Attempt to create a new backup
    }

    private void saveConfig() throws IOException {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(configMap, writer);
            TelosAddon.LOGGER.log(Level.INFO, "Config saved successfully");
        }
    }

    public void set(String key, Object value) {
        configMap.put(key, value);
    }

    public Optional<Object> get(String key) {
        return Optional.ofNullable(configMap.get(key));
    }

    public Double getDouble(String key) {
        Object value = configMap.get(key);
        if(value instanceof Double)
            return (Double) value;
        throw new ClassCastException("Value is not a Double");
    }

    public int getInteger(String key) {
        Object value = configMap.get(key);
        if(value instanceof Number)
            return ((Number) value).intValue();
        throw new ClassCastException("Value is not an Int");
    }

    public Boolean getBoolean(String key) {
        Object value = configMap.get(key);
        if(value instanceof Boolean)
            return (Boolean) value;
        throw new ClassCastException("Value is not a Boolean");
    }

    public String getString(String key) {
        Object value = configMap.get(key);
        if(value instanceof String)
            return (String) value;
        throw new ClassCastException("Value is not a String");
    }

    public void addInt(String key, int amount) {
        int value = getInteger(key);
        configMap.put(key, value + amount);
    }

    public void addLong(String key, long amount) {
        long value = getInteger(key);
        configMap.put(key, value + amount);
    }

    @Override
    public String toString() {
        return "Config{" +
                "configMap=" + configMap +
                "}";
    }

}
