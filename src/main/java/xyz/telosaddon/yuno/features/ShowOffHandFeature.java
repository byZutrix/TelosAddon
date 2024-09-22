package xyz.telosaddon.yuno.features;

import xyz.telosaddon.yuno.utils.Config;

public class ShowOffHandFeature extends ShowRangeFeature {
	public ShowOffHandFeature(Config config) {
		super(config, (inv -> inv.offHand.get(0)), "OffHand");
	}
}
