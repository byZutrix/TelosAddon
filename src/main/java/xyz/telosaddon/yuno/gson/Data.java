package xyz.telosaddon.yuno.gson;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{
	@SerializedName("sticker")
	private String sticker;

	@SerializedName("lastPlayed")
	private long lastPlayed;

	@SerializedName("guild")
	private Guild guild;

	@SerializedName("characters")
	private Characters characters;

	@SerializedName("balance")
	private int balance;

	@SerializedName("companions")
	private Companions companions;

	@SerializedName("rank")
	private String rank;

	@SerializedName("boost")
	private Boost boost;

	@SerializedName("playTime")
	private String playTime;

	@SerializedName("glow")
	private String glow;

	@SerializedName("uniqueId")
	private String uniqueId;

	@SerializedName("rewards")
	private List<Object> rewards;

	@SerializedName("username")
	private String username;

	@SerializedName("currentServer")
	private String currentServer;

	@SerializedName("stash")
	private Stash stash;

	@SerializedName("statistics")
	private Statistics statistics;

	@SerializedName("mastery")
	private Mastery mastery;

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

	@SerializedName("previousLife")
	private Object previousLife;

	@SerializedName("highestFame")
	private int highestFame;

	@SerializedName("backpack")
	private Object backpack;

	@SerializedName("potions")
	private int potions;

	@SerializedName("fame")
	private int fame;

	public String getSticker(){
		return sticker;
	}

	public long getLastPlayed(){
		return lastPlayed;
	}

	public Guild getGuild(){
		return guild;
	}

	public Characters getCharacters(){
		return characters;
	}

	public int getBalance(){
		return balance;
	}

	public Companions getCompanions(){
		return companions;
	}

	public String getRank(){
		return rank;
	}

	public Boost getBoost(){
		return boost;
	}

	public String getPlayTime(){
		return playTime;
	}

	public String getGlow(){
		return glow;
	}

	public String getUniqueId(){
		return uniqueId;
	}

	public List<Object> getRewards(){
		return rewards;
	}

	public String getUsername(){
		return username;
	}

	public String getCurrentServer(){
		return currentServer;
	}

	public Stash getStash(){
		return stash;
	}

	public Statistics getStatistics(){
		return statistics;
	}

	public Mastery getMastery(){
		return mastery;
	}

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

	public Object getPreviousLife(){
		return previousLife;
	}

	public int getHighestFame(){
		return highestFame;
	}

	public Object getBackpack(){
		return backpack;
	}

	public int getPotions(){
		return potions;
	}

	public int getFame(){
		return fame;
	}
}