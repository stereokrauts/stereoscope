package com.stereokrauts.stereoscope.plugin.model;

import com.stereokrauts.stereoscope.plugin.interfaces.AbstractMixerPluginDescriptor;
import com.stereokrauts.stereoscope.plugin.manager.MixerPluginManager;

public final class InternalMixerPluginResolver {
	public AbstractMixerPluginDescriptor resolve(final String pluginId) throws MixerPluginInitialisationException {
		try {
			return MixerPluginManager.getInstance().getDescriptorClassFor(pluginId);
		} catch (final Exception e) {
			throw new MixerPluginInitialisationException("Error creating plugin instance " + pluginId, e);
		}
	}
}
