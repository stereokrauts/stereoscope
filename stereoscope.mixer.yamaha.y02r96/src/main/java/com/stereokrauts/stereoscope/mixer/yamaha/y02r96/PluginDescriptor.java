package com.stereokrauts.stereoscope.mixer.yamaha.y02r96;

import com.stereokrauts.stereoscope.plugin.interfaces.AbstractMixerPluginDescriptor;

/**
 * This class holds the plugin metadata for the Yamaha 02R96 mixer plugin.
 * @author th
 *
 */
public final class PluginDescriptor extends AbstractMixerPluginDescriptor {
	private static final String PLUGIN_NAME = "yamaha-02r96";
	private static final String PLUGIN_VERSION = "2";

	@Override
	public String getName() {
		return PLUGIN_NAME;
	}

	@Override
	public String getVersion() {
		return PLUGIN_VERSION;
	}

	@Override
	public Class<Y02r96PluginBuilder> getPluginBuilder() {
		return Y02r96PluginBuilder.class;
	}

}
