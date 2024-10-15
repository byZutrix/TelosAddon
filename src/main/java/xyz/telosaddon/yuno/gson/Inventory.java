package xyz.telosaddon.yuno.gson;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Inventory{

	@SerializedName("armor")
	private List<ArmorItem> armor;

	@SerializedName("selectedSlot")
	private int selectedSlot;

	@SerializedName("extra")
	private List<ExtraItem> extra;

	@SerializedName("storage")
	private List<StorageItem> storage;

	public List<ArmorItem> getArmor(){
		return armor;
	}

	public int getSelectedSlot(){
		return selectedSlot;
	}

	public List<ExtraItem> getExtra(){
		return extra;
	}

	public List<StorageItem> getStorage(){
		return storage;
	}
}