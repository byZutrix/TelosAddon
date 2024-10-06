package xyz.telosaddon.yuno.utils.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.crash.CrashReport;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.utils.BossBarUtils;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
                configMap = new HashMap<>();
            }
        } else {
            TelosAddon.LOGGER.info("No config file found, resorting to defaults");
            configMap = new HashMap<>();
        }
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
        addDefault("NoFrontCameraFeatureEnabled", true);
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
        addDefault("ShowMainRangeInFirstPerson", true);
        addDefault("ShowOffHandRangeInFirstPerson", true);

        addDefault("ShowMainRangeFeatureColor", new Color(255, 0, 0).getRGB());
        addDefault("ShowOffHandRangeFeatureColor", new Color(0, 0, 255).getRGB());

        addDefault("ShowListedPrice", true);
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

        if (!tmpFile.exists()) {
            try {
                tmpFile.createNewFile();
            } catch (IOException ignore) {
                TelosAddon.LOGGER.log(Level.WARNING, "Encountered an issue when creating tmp config file.");
            }
        }

        if (overwriteBackup) {
            boolean deletedOldBackup = tmpFile.delete();
            boolean createdNewBackup = false;

            if (deletedOldBackup) {
                createdNewBackup = configFile.renameTo(tmpFile);
            }

            if (!createdNewBackup) {
                TelosAddon.LOGGER.log(Level.WARNING,"Could not backup config. Skipping save.");
                return;
            }
        }
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(configMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String key, Object value) {
        configMap.put(key, value);
        save();
    }

    public void remove(String key, Object value) {
        configMap.remove(key);
        save();
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
        save();
    }

    public void addLong(String key, long amount) {
        long value = getInteger(key);
        configMap.put(key, value + amount);
        save();
    }

    public void toggle(String key) {
        boolean value = getBoolean(key);
        value = !value;
        save();
    }

    @Override
    public String toString() {
        return "Config{" +
                "configMap=" + configMap +
                "}";
    }

}
