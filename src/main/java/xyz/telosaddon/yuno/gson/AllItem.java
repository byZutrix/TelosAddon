package xyz.telosaddon.yuno.gson;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AllItem{

	@SerializedName("runes")
	private List<String> runes;

	@SerializedName("level")
	private int level;

	@SerializedName("created")
	private long created;

	@SerializedName("itemFilter")
	private ItemFilter itemFilter;

	@SerializedName("ruleset")
	private Ruleset ruleset;

	@SerializedName("type")
	private String type;

	@SerializedName("inventory")
	private Inventory inventory;

	@SerializedName("lastPlayed")
	private long lastPlayed;

	@SerializedName("previousLife")
	private Object previousLife;

	@SerializedName("highestFame")
	private int highestFame;

	@SerializedName("backpack")
	private Backpack backpack;

	@SerializedName("potions")
	private int potions;

	@SerializedName("playTime")
	private String playTime;

	@SerializedName("fame")
	private int fame;

	@SerializedName("uniqueId")
	private String uniqueId;

	@SerializedName("statistics")
	private Statistics statistics;

	public List<String> getRunes(){
		return runes;
	}

	public int getLevel(){
		return level;
	}

	public long getCreated(){
		return created;
	}

	public ItemFilter getItemFilter(){
		return itemFilter;
	}

	public Ruleset getRuleset(){
		return ruleset;
	}

	public String getType(){
		return type;
	}

	public Inventory getInventory(){
		return inventory;
	}

	public long getLastPlayed(){
		return lastPlayed;
	}

	public Object getPreviousLife(){
		return previousLife;
	}

	public int getHighestFame(){
		return highestFame;
	}

	public Backpack getBackpack(){
		return backpack;
	}

	public int getPotions(){
		return potions;
	}

	public String getPlayTime(){
		return playTime;
	}

	public int getFame(){
		return fame;
	}

	public String getUniqueId(){
		return uniqueId;
	}

	public Statistics getStatistics(){
		return statistics;
	}
}