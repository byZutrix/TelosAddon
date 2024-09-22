package xyz.telosaddon.yuno.features;

import xyz.telosaddon.yuno.utils.Config;

public abstract class AbstractFeature {

	private final Config config;
	private final String featureName;

	protected AbstractFeature(Config config, String featureName){
		this.config = config;
		this.featureName = featureName;
	}

	private String getEnabledKey(){
		return featureName + "Enabled";
	}

	protected String getFeatureName(){
		return this.featureName;
	}

	protected Config getConfig(){
		return this.config;
	}

	public boolean isEnabled() {
		return config.getBoolean(getEnabledKey());
	}

	public void disable(){
		this.config.set(getEnabledKey(), false);
	}

	public void enable(){
		this.config.set(getEnabledKey(), true);

	}

	public void toggle(){
		this.config.set(getEnabledKey(), !config.getBoolean(getEnabledKey()));

	}
}
