package xyz.telosaddon.yuno.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComponentsItem{

	@SerializedName("allowNormal")
	private boolean allowNormal;

	@SerializedName("type")
	private String type;

	@SerializedName("allowGreater")
	private boolean allowGreater;

	@SerializedName("whitelist")
	private List<String> whitelist;

	@SerializedName("cutoff")
	private int cutoff;

	public boolean isAllowNormal(){
		return allowNormal;
	}

	public String getType(){
		return type;
	}

	public boolean isAllowGreater(){
		return allowGreater;
	}

	public List<String> getWhitelist(){
		return whitelist;
	}

	public int getCutoff(){
		return cutoff;
	}
}