package com.stereokrauts.stereoscope.mixer.yamaha.y01v96i;

import com.stereokrauts.stereoscope.plugin.interfaces.AbstractMixerPluginDescriptor;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPluginBuilder;

/**
 * This class describes the plugin metadata.
 * @author th
 *
 */
public final class PluginDescriptor extends AbstractMixerPluginDescriptor {
	private static final String PLUGIN_NAME = "yamaha-01v96i";
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
	public Class<? extends IMixerPluginBuilder> getPluginBuilder() {
		return Y01v96iPluginBuilder.class;
	}

}
