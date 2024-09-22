package xyz.telosaddon.yuno.utils;

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
import java.util.logging.Logger;

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

    /**
     * Loads config settings from the local json file
     * @param fromBackup Whether to load specifically from the backup file
     */
    private void load(boolean fromBackup) {
        File file = fromBackup ? tmpFile : configFile;
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                configMap = GSON.fromJson(reader, type);
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();

                if (e instanceof JsonSyntaxException) {
                    // JsonSyntaxException indicates the file was corrupted, so load from backup
                    this.load(true);
                    return;
                }
            }
        } else {
            configMap = new HashMap<>();
        }

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

        // Don't overwrite backup with main json file when pulling from backup
        save(!fromBackup);
    }

    public void addDefault(String key, Object value) {
        if(!configMap.containsKey(key)) {
            configMap.put(key, value);
        }
    }

    /**
     * Saves config to the local json file and backs up the existing config
     */
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
                TelosAddon.LOGGER.warning("Encountered an issue when creating tmp config file.");
            }
        }

        if (overwriteBackup) {
            boolean deletedOldBackup = tmpFile.delete();
            boolean createdNewBackup = false;

            if (deletedOldBackup) {
                createdNewBackup = configFile.renameTo(tmpFile);
            }

            if (!createdNewBackup) {
                TelosAddon.LOGGER.warning("Could not backup config. Skipping save.");
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

    public Object get(String key) {
        if(configMap.get(key) != null)
            return configMap.get(key);
        throw new ClassCastException("Could not find Object!");
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
