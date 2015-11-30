package com.stereokrauts.lib.midi.macos.jna;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Memory;
import com.sun.jna.Structure;

public class MIDINotification extends Structure {
	public int messageID;
	public int messageSize;
	public Memory optionalData;

	@Override
	protected List<?> getFieldOrder() {
		return Arrays.asList(new String[] { "messageID", "messageSize", "optionalData" });
	}
}
