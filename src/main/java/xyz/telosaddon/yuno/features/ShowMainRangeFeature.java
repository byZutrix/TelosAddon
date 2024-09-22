package xyz.telosaddon.yuno.features;

import net.minecraft.entity.player.PlayerInventory;
import xyz.telosaddon.yuno.utils.Config;

public class ShowMainRangeFeature extends ShowRangeFeature {

	public ShowMainRangeFeature(Config config) {
		super(config, PlayerInventory::getMainHandStack, "Main");
	}
}
