package com.stereokrauts.stereoscope.mixer.roland.m380;

import com.stereokrauts.stereoscope.plugin.interfaces.AbstractMixerPluginDescriptor;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPluginBuilder;

public class PluginDescriptor extends AbstractMixerPluginDescriptor {
	final static String pluginName = "roland-m380";
	final static String pluginVersion = "1";
	final static String displayName = "Roland M-380";

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
		return M380PluginBuilder.class;
	}

}
