package com.stereokrauts.stereoscope.plugin.interfaces;


/**
 * This interface is to be implemented by a mixer plugin.
 * @author theide
 *
 */
public interface IMixerPluginBuilder {
	IMixerPlugin buildPlugin(AbstractApplicationContext ctx) throws Exception;
	IMixerPlugin buildPlugin(AbstractApplicationContext ctx, IPersistentPluginConfiguration configuration) throws Exception;
}
