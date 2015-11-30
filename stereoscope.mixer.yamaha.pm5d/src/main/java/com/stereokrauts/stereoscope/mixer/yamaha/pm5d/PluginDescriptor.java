package com.stereokrauts.stereoscope.mixer.yamaha.pm5d;

import com.stereokrauts.stereoscope.plugin.interfaces.AbstractMixerPluginDescriptor;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPluginBuilder;

public class PluginDescriptor extends AbstractMixerPluginDescriptor {
	final static String pluginName = "yamaha-pm5d";
	final static String pluginVersion = "2";
	final static String displayName = "Yamaha PM5D";

	@Override
	public String getName() {
		return pluginName;
	}

	@Override
	public String getVersion() {
		return pluginVersion;
	}

	@Override
	public Class<? extends IMixerPluginBuilder> getPluginBuilder() {
		return Pm5dPluginBuilder.class;
	}
}
