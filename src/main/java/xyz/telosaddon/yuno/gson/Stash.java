package xyz.telosaddon.yuno.gson;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Stash{

	@SerializedName("size")
	private int size;

	@SerializedName("contents")
	private List<List<ContentsItemItem>> contents;

	public int getSize(){
		return size;
	}

	public List<List<ContentsItemItem>> getContents(){
		return contents;
	}
}