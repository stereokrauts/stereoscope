package com.stereokrauts.stereoscope.plugin.interfaces;

/**
 * This class must be provided by all Plugins and named
 * PluginDescriptor. It must return reasonable values which
 * describe the plugins behaviour.
 * 
 * @author Tobias Heide <tobi@s-hei.de>
 */
public abstract class AbstractMixerPluginDescriptor {
	/** @return returns a unique name */
	public abstract String getName();
	/** @return returns the version number of the plugin */
	public abstract String getVersion();
	
	public abstract Class<? extends IMixerPluginBuilder> getPluginBuilder();
}
