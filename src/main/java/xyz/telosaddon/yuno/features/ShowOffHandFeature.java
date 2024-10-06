package xyz.telosaddon.yuno.features;

import xyz.telosaddon.yuno.utils.config.Config;

public class ShowOffHandFeature extends ShowRangeFeature {
	public ShowOffHandFeature(Config config) {
		super(config, (inv -> inv.offHand.get(0)), "OffHand");
	}
}
