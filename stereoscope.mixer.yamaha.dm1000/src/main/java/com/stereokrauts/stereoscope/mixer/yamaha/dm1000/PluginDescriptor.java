package com.stereokrauts.stereoscope.mixer.yamaha.dm1000;

import com.stereokrauts.stereoscope.plugin.interfaces.AbstractMixerPluginDescriptor;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPluginBuilder;

public class PluginDescriptor extends AbstractMixerPluginDescriptor {
	final static String pluginName = "yamaha-dm1000";
	final static String pluginVersion = "1";
	final static String displayName = "Yamaha DM1000";
	
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
		return DM1000PluginBuilder.class;
	}

}
