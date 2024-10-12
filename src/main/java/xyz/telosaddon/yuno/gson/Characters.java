package xyz.telosaddon.yuno.gson;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Characters{

	@SerializedName("all")
	private List<AllItem> all;

	@SerializedName("slots")
	private int slots;

	@SerializedName("data")
	private Data data;

	@SerializedName("selected")
	private String selected;

	public List<AllItem> getAll(){
		return all;
	}

	public int getSlots(){
		return slots;
	}

	public Data getData(){
		return data;
	}

	public String getSelected(){
		return selected;
	}
}