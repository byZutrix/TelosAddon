package xyz.telosaddon.yuno.gson;

import com.google.gson.annotations.SerializedName;

public class Guild{

	@SerializedName("guild")
	private String guild;

	public String getGuild(){
		return guild;
	}
}