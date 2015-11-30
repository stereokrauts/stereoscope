package com.stereokrauts.lib.midi.api;

public interface IMidiInput {

	/**
	 * Sets the class to which the MIDI messages should be forwarded.
	 * @param handler the class responsible for handling the MIDI messages.
	 */
	void setHandler(final IReactToMidi handler);

	/**
	 * Release all resources owned by this class.
	 */
	void close();

	String getName();

}