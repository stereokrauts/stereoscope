package com.stereokrauts.stereoscope.plugin.model;

import com.stereokrauts.stereoscope.plugin.interfaces.AbstractApplicationContext;
import com.stereokrauts.stereoscope.plugin.interfaces.AbstractMixerPluginDescriptor;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPlugin;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPluginBuilder;
import com.stereokrauts.stereoscope.plugin.interfaces.IPersistentPluginConfiguration;

public final class InternalMixerPluginBuilder {

	public static IMixerPlugin build(final String pluginId, final AbstractApplicationContext ctx, final IPersistentPluginConfiguration configuration) throws MixerPluginInitialisationException {
		try {
			final AbstractMixerPluginDescriptor desc = (new InternalMixerPluginResolver()).resolve(pluginId);
			final IMixerPluginBuilder builder = desc.getPluginBuilder().newInstance();
			return builder.buildPlugin(ctx, configuration);
		} catch (final Exception e) {
			throw new MixerPluginInitialisationException("Error creating plugin instance " + pluginId, e);
		}
	}
	
	public static IMixerPlugin build(final String pluginId, final AbstractApplicationContext ctx) throws MixerPluginInitialisationException {
		try {
			final AbstractMixerPluginDescriptor desc = (new InternalMixerPluginResolver()).resolve(pluginId);
			final IMixerPluginBuilder builder = desc.getPluginBuilder().newInstance();
			return builder.buildPlugin(ctx);
		} catch (final Exception e) {
			throw new MixerPluginInitialisationException("Error creating plugin instance " + pluginId + ": " + e.getMessage(), e);
		}
	}
}
