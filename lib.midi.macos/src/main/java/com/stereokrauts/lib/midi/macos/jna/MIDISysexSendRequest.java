package com.stereokrauts.lib.midi.macos.jna;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class MIDISysexSendRequest extends Structure {
	public interface MIDICompletionProc extends Callback {
		void callback(final MIDISysexSendRequest request);
	}

	public MIDISysexSendRequest() {
	}

	// / Cast data at given memory location (pointer + offset) as an existing
	// Pair struct
	public MIDISysexSendRequest(final Pointer pointer, final int offset) {
		super();
		useMemory(pointer, offset);
		read();
	}

	public MIDIEndpointRef destination;
	public Pointer data;
	public int bytesToSend;
	public boolean complete;
	public byte[] reserved = new byte[3];
	public MIDICompletionProc completionProc;
	public Pointer completionRefCon;

	@Override
	protected List<?> getFieldOrder() {
		return Arrays.asList(new String[] { "destination", "data", "bytesToSend", "complete", "reserved", "completionProc",
				"completionRefCon" });
	}

}
