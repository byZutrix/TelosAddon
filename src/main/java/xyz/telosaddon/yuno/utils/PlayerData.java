package xyz.telosaddon.yuno.utils;

import com.google.gson.annotations.SerializedName;

public class PlayerData {
    public Player player;
    public Data data;

    public static class Player {
        public String name;
        public String id;
    }

    public static class Data {
        @SerializedName("uniqueId")
        public String uniqueId;
        @SerializedName("username")
        public String username;
        @SerializedName("lastPlayed")
        public long lastPlayed;
        @SerializedName("rank")
        public String rank;
        @SerializedName("balance")
        public int balance;
    }
}
