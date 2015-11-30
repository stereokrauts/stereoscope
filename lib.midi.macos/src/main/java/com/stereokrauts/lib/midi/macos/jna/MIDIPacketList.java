package com.stereokrauts.lib.midi.macos.jna;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public final class MIDIPacketList extends Structure {
	public int numPackets;
	public MIDIPacket[] packet = new MIDIPacket[1];

	public MIDIPacketList() {
		super();
		setAlignType(ALIGN_NONE);
	}

	@Override
	protected List<?> getFieldOrder() {
		return Arrays.asList(new String[] { "numPackets", "packet" });
	}
}
