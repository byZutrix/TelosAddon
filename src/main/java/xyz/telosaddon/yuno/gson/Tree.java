package xyz.telosaddon.yuno.gson;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Tree{

	@SerializedName("children")
	private List<Object> children;

	@SerializedName("id")
	private String id;

	public List<Object> getChildren(){
		return children;
	}

	public String getId(){
		return id;
	}
}