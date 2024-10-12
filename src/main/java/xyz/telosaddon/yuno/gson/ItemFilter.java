package xyz.telosaddon.yuno.gson;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ItemFilter{

	@SerializedName("components")
	private List<ComponentsItem> components;

	public List<ComponentsItem> getComponents(){
		return components;
	}
}