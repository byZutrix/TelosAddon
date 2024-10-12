package xyz.telosaddon.yuno.gson;

import com.google.gson.annotations.SerializedName;

public class Boost{

	@SerializedName("loot")
	private float loot;

	@SerializedName("fame")
	private float fame;

	public float getLoot(){
		return loot;
	}

	public float getFame(){
		return fame;
	}
}