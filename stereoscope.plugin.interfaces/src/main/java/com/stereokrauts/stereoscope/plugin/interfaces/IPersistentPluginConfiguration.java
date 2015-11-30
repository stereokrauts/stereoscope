package com.stereokrauts.stereoscope.plugin.interfaces;

import model.beans.MixerConnectionBean;

/**
 * This is a variant of the "Memento"-Pattern and is used for mixer plugins
 * to get their internal state in a persistable form. Currently, only connection
 * parameters are implemented.
 * @author th
 *
 */
public interface IPersistentPluginConfiguration {
	MixerConnectionBean getConnectionBean();
}
