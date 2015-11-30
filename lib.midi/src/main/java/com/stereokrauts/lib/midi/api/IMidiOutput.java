package com.stereokrauts.lib.midi.api;

import javax.sound.midi.InvalidMidiDataException;

public interface IMidiOutput {

	/**
	 * Send a sysex messages to the device.
	 * @param message The message to send
	 * @throws InvalidMidiDataException Any error from the java sound api.
	 */
	void sendSysexMessage(final byte[] message)
			throws InvalidMidiDataException;

	/**
	 * Sends an active sensing MIDI message to the device.
	 */
	void sendActiveSensing();

	/**
	 * Releases all resources owned by this object.
	 */
	void close();

	String getName();
}