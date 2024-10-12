package xyz.telosaddon.yuno.gson;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Backpack{

	@SerializedName("contents")
	private List<ContentsItem> contents;

	public List<ContentsItem> getContents(){
		return contents;
	}
}