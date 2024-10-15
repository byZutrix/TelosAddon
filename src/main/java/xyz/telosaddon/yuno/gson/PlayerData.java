package xyz.telosaddon.yuno.gson;

import com.google.gson.annotations.SerializedName;

public class PlayerData{

	@SerializedName("data")
	private Data data;

	@SerializedName("player")
	private Player player;

	public Data getData(){
		return data;
	}

	public Player getPlayer(){
		return player;
	}
}