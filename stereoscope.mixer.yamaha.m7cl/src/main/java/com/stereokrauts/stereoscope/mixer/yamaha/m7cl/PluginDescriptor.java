package com.stereokrauts.stereoscope.mixer.yamaha.m7cl;

import com.stereokrauts.stereoscope.plugin.interfaces.AbstractMixerPluginDescriptor;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPluginBuilder;

public final class PluginDescriptor extends AbstractMixerPluginDescriptor {
	final static String pluginName = "yamaha-m7cl";
	final static String pluginVersion = "2";
	final static String displayName = "Yamaha M7CL";

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
		return M7clPluginBuilder.class;
	}

}
