package com.stereokrauts.stereoscope.plugin.interfaces;

public interface IMixerCommunicationVisualisator {

	/**
	 * A mixer has sent a new message to stereoscope.
	 */
	public abstract void newMessageFromMixer();

	/**
	 * Stereoscope sends a message to the mixer.
	 */
	public abstract void newMessageToMixer();

}