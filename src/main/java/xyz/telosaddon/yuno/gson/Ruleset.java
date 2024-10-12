package xyz.telosaddon.yuno.gson;

import com.google.gson.annotations.SerializedName;

public class Ruleset{

	@SerializedName("type")
	private String type;

	public String getType(){
		return type;
	}
}