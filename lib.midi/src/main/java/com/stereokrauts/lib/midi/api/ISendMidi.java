package com.stereokrauts.lib.midi.api;

/**
 * This interface provides a function to send sysex
 * data over a MIDI line (which is probably connected
 * to the mixer).
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public interface ISendMidi {

	/** This function can be called to send MIDI data
	 * to the mixer.
	 * @param sysexdata The sysex data that shall be sent.
	 */
	void sendSysexData(byte[] sysexdata);

	/** This function sends a active sensing message to
	 * the mixer.
	 */
	void activeSensing();
}
