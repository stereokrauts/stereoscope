package com.stereokrauts.lib.midi.util;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.midi.api.ISendMidi;


public final class DummyMidiSink implements ISendMidi {
	private byte[] lastSentSysex;

	@Override
	public void sendSysexData(final byte[] sysexdata) {
		this.lastSentSysex = sysexdata;
	}

	@Override
	public void activeSensing() {
		/* ignored */
	}

	public String getLastSentSysex() {
		return ByteStringConversion.toHex(this.lastSentSysex);
	}

	public void clear() {
		lastSentSysex = new byte[0];
	}

}
