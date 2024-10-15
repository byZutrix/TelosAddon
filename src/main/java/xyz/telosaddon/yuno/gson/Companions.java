package xyz.telosaddon.yuno.gson;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Companions{

	@SerializedName("selected")
	private Selected selected;

	@SerializedName("unlocked")
	private List<String> unlocked;

	public Selected getSelected(){
		return selected;
	}

	public List<String> getUnlocked(){
		return unlocked;
	}
}