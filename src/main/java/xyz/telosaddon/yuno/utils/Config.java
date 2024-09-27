package xyz.telosaddon.yuno.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Integer.class, new IntegerSerializer())
            .registerTypeAdapter(Double.class, new DoubleSerializer())
            .registerTypeAdapter(Long.class, new LongSerializer())
            .setPrettyPrinting()
            .create();
    private final File configFile;

    private Map<String, Object> configMap;

    public Config() {
        Path path = FabricLoader.getInstance().getConfigDir();
        this.configFile = new File(path.toFile(), "telosaddon.json");
        this.configMap = new HashMap<>();
    }
    public void load() {
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                configMap = GSON.fromJson(reader, type);
            } catch (IOException e) {
                e.printStackTrace();
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

        addDefault("ShowMainRangeFeatureEnabled", true);
        addDefault("ShowOffHandRangeFeatureEnabled", false);
        addDefault("ShowMainRangeFeatureHeight", 0.5);
        addDefault("ShowOffHandRangeFeatureHeight", 0.5);

        addDefault("ShowMainRangeFeatureColor", new Color(255, 0, 0).getRGB());
        addDefault("ShowOffHandRangeFeatureColor", new Color(0, 0, 255).getRGB());
        save();

    }

    public void addDefault(String key, Object value) {
        if(!configMap.containsKey(key)) {
            configMap.put(key, value);
        }
    }

    public void save() {
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
