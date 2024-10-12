package xyz.telosaddon.yuno.gson;

import com.google.gson.annotations.SerializedName;

public class Mastery{

	@SerializedName("realm:samurai")
	private boolean realmSamurai;

	public boolean isRealmSamurai(){
		return realmSamurai;
	}
}