package com.stereokrauts.lib.midi.api;

/**
 * This interface is to be implemented by a class that is responsible
 * for handling MIDI messages from the java sound system.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public interface IReactToMidi {
	/**
	 * This function is being called by the MIDI Subsystem any
	 * time a sysex message is received (e.g. from the mixer) which
	 * indicates, that the state of the mixer has changed.
	 * @param message The raw message sent by the mixer.
	 */
	void handleSysex(byte[] message);
	
	void handleProgramChange(int newProgram);
}
