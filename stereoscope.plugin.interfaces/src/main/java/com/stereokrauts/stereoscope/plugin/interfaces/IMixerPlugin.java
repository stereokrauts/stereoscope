package com.stereokrauts.stereoscope.plugin.interfaces;

import model.mixer.interfaces.IAmMixer;

import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;

public interface IMixerPlugin {
	IPersistentPluginConfiguration getPersistableConfiguration();
	IMessageReceiver getMessageEndpoint();
	String getPluginName();
	IAmMixer getMixer();
	void shutdown();
	void registerCommunicationAware(ICommunicationAware attendee);
	String getDisplayName();
}
