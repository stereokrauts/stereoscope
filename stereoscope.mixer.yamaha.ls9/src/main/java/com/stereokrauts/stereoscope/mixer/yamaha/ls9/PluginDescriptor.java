package com.stereokrauts.stereoscope.mixer.yamaha.ls9;

import com.stereokrauts.stereoscope.plugin.interfaces.AbstractMixerPluginDescriptor;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPluginBuilder;

public class PluginDescriptor extends AbstractMixerPluginDescriptor {
	static final String pluginName = "yamaha-ls9";
	static final String pluginVersion = "2";
	static final String displayName = "Yamaha LS9";

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
		return Ls9PluginBuilder.class;
	}

}
