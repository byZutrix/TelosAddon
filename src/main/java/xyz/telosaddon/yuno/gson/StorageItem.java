package xyz.telosaddon.yuno.gson;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StorageItem{

	@SerializedName("owner")
	private String owner;

	@SerializedName("identifier")
	private String identifier;

	@SerializedName("features")
	private List<Object> features;

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

	public String getOwner(){
		return owner;
	}

	public String getIdentifier(){
		return identifier;
	}

	public List<Object> getFeatures(){
		return features;
	}

	public String getId(){
		return id;
	}

	public String getType(){
		return type;
	}
}