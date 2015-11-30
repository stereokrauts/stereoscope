package com.stereokrauts.stereoscope.plugin.guihelper;

import java.net.URL;

/**
 * This class denotes an association between mixer
 * plugins and their provided images.
 * @author th
 *
 */
public final class MixerPluginImageAssociation {
	private String pluginName;
	private String pluginId;
	private URL image;

	public MixerPluginImageAssociation(final String pluginId, final String pluginName, final URL image) {
		this.pluginId = pluginId;
		this.pluginName = pluginName;
		this.image = image;
	}

	public String getPluginName() {
		return this.pluginName;
	}
	public void setPluginName(final String pluginName) {
		this.pluginName = pluginName;
	}
	public String getPluginId() {
		return this.pluginId;
	}
	public void setPluginId(final String pluginId) {
		this.pluginId = pluginId;
	}
	public URL getImage() {
		return this.image;
	}
	public void setImage(final URL image) {
		this.image = image;
	}
}
