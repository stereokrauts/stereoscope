package com.stereokrauts.lib.midi.macos;

import java.util.Arrays;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.IMidiInput;
import com.stereokrauts.lib.midi.api.IReactToMidi;
import com.stereokrauts.lib.midi.macos.jna.ECoreMidiErrorCodes;
import com.stereokrauts.lib.midi.macos.jna.MIDIPacketList;
import com.stereokrauts.lib.midi.macos.jna.MIDIClientRef;
import com.stereokrauts.lib.midi.macos.jna.MIDIEndpointRef;
import com.stereokrauts.lib.midi.macos.jna.MIDIPortRef;
import com.stereokrauts.lib.midi.macos.jna.MidiService.MIDIReadProc;
import com.stereokrauts.lib.midi.macos.jna.OSStatus;
import com.stereokrauts.lib.midi.macos.jna.util.CFStringRef;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public final class MacMidiInput implements IMidiInput {
	private static final SLogger LOGGER = StereoscopeLogManager.getLogger(MacMidiInput.class);

	private final MacMidiService midiService;
	private IReactToMidi handler;
	private final String displayName;
	private MIDIEndpointRef sourceEndpointRef;
	private MIDIClientRef clientRef;
	private final Object receiveLock = new Object();
	private MIDIPortRef midiPortRef;
	private MIDIReadProc midiInputReadProc; 

	public MacMidiInput(final MacMidiService midiService, final MacMidiEntity entity) {
		this.midiService = midiService;
		displayName = entity.getDisplayName();
		sourceEndpointRef = midiService.getService().MIDIEntityGetSource(entity.getReference(), 0);

		createMidiInputReadProc();
		
		final Memory clientRefMemory = new Memory(4);
		OSStatus status = midiService.getService().MIDIClientCreate(CFStringRef.CFSTR("Stereokrauts Stereosocpe"), null, null,
				clientRefMemory);
		
		checkStatus(status);
		clientRef = new MIDIClientRef();
		clientRef.setValue(clientRefMemory.getInt(0));
		
		final Memory mEndpointRef = new Memory(4);
		status = midiService.getService().MIDIDestinationCreate(clientRef, CFStringRef.CFSTR("Stereoscope Virtual MIDI Destination"), midiInputReadProc, Pointer.NULL, mEndpointRef);
		checkStatus(status);
		
	}

	@Override
	public void setHandler(final IReactToMidi handler) {
		if (this.handler != null) {
			throw new IllegalStateException("Only one handler is allowed!");
		}
		synchronized(receiveLock) {
			this.handler = handler;
			init();
		}
	}

	private void init() {
		
		OSStatus status = null;

		final Memory mPortRef = new Memory(4);
		status = midiService.getService().MIDIInputPortCreate(clientRef, CFStringRef.CFSTR("Stereoscope MIDI Input"), midiInputReadProc, 0, mPortRef);
		checkStatus(status);

		midiPortRef = new MIDIPortRef();
		midiPortRef.setValue(mPortRef.getInt(0));

		status = midiService.getService().MIDIPortConnectSource(midiPortRef, sourceEndpointRef, null);
		checkStatus(status);
	}

	private void createMidiInputReadProc() {
		midiInputReadProc = new MIDIReadProc() {
			@Override
			public void callback(final MIDIPacketList pktlist, final Void readProcRefCon, final Void srcConnRefCon) {
				LOGGER.finest("Packet count in this receive call: " + pktlist.numPackets);

				for (int i = 0; i < pktlist.numPackets; i++) {
					synchronized(receiveLock) {
						if (handler != null) {
							try {
								byte[] receivedBytes = Arrays.copyOf(pktlist.packet[i].data, pktlist.packet[i].length);
								handler.handleSysex(receivedBytes);
								LOGGER.finest("Received MIDI: " + ByteStringConversion.toHex(receivedBytes));
							} catch (ArrayIndexOutOfBoundsException e) {
								LOGGER.finest("MacOS gave us a MIDI message with wrong packet count.");
							}
						} else {
							throw new IllegalStateException("Received MIDI from CoreMIDI after the MIDI input was closed");
						}
					}
				}
			}
		};
	}

	@Override
	public void close() {
		handler = null;
		clientRef = null;
		midiInputReadProc = null;
		midiService.getService().MIDIPortDispose(midiPortRef);
	}

	@Override
	public String getName() {
		return displayName;
	}

	private void checkStatus(final OSStatus status) {
		if (!status.toMidiErrorCode().equals(ECoreMidiErrorCodes.kNoError)) {
			throw new RuntimeException("Could not open MIDI input: " + status.toMidiErrorCode().toString());
		}
	}

}
