package com.stereokrauts.stereoscope.mixer.behringer.ddx3216;

import com.stereokrauts.stereoscope.plugin.interfaces.AbstractMixerPluginDescriptor;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPluginBuilder;

public class PluginDescriptor extends AbstractMixerPluginDescriptor {
	final static String pluginName = "behringer-ddx3216";
	final static String pluginVersion = "1";
	final static String displayName = "Behringer DDX3216";
	
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
		return DDX3216PluginBuilder.class;
	}

}
