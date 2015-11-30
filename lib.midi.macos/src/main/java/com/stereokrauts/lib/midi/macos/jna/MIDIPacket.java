package com.stereokrauts.lib.midi.macos.jna;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public final class MIDIPacket extends Structure {
	public long timeStamp;
	public short length;
	public byte[] data = new byte[256];

	@Override
	protected List<?> getFieldOrder() {
		return Arrays.asList(new String[] { "timeStamp", "length", "data" });
	}
}
