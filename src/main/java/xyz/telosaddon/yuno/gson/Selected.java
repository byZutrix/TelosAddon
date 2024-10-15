package xyz.telosaddon.yuno.gson;

import com.google.gson.annotations.SerializedName;

public class Selected{

	@SerializedName("mount")
	private String mount;

	@SerializedName("pet")
	private String pet;

	public String getMount(){
		return mount;
	}

	public String getPet(){
		return pet;
	}
}