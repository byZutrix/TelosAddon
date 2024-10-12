package xyz.telosaddon.yuno.utils;

import java.util.HashMap;
import java.util.Map;

public class PlayerDataCache {
    private static class CacheEntry {
        String data;
        long timestamp;

        CacheEntry(String data) {
            this.data = data;
            this.timestamp = System.currentTimeMillis(); // Capture current time
        }
    }

    private final Map<String, CacheEntry> cache = new HashMap<>();
    private final long cacheLifetime; // Cache lifetime in milliseconds

    public PlayerDataCache(long cacheLifetime) {
        this.cacheLifetime = cacheLifetime;
    }

    // Method to get cached data if it is still valid
    public String get(String playerName) {
        CacheEntry entry = cache.get(playerName);
        if (entry != null && isValid(entry.timestamp)) {
            return entry.data;
        }
        return null; // Return null if not valid or not found
    }

    // Method to cache data
    public void put(String playerName, String data) {
        cache.put(playerName, new CacheEntry(data));
    }

    // Check if the cache entry is still valid based on the lifetime
    private boolean isValid(long timestamp) {
        return System.currentTimeMillis() - timestamp < cacheLifetime;
    }

    // Optional: Method to clear cache (if needed)
    public void clear() {
        cache.clear();
    }
}
