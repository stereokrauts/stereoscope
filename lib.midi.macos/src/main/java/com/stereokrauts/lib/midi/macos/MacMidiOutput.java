package com.stereokrauts.lib.midi.macos;

import java.util.Collection;

import javax.sound.midi.InvalidMidiDataException;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.IMidiOutput;
import com.stereokrauts.lib.midi.macos.jna.ECoreMidiErrorCodes;
import com.stereokrauts.lib.midi.macos.jna.MIDISysexSendRequest;
import com.stereokrauts.lib.midi.macos.jna.MIDIEndpointRef;
import com.stereokrauts.lib.midi.macos.jna.OSStatus;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public final class MacMidiOutput implements IMidiOutput {
	private static final SLogger LOGGER = StereoscopeLogManager.getLogger(MacMidiOutput.class);

	private static final int MIDI_SEND_BUFFER_SIZE = 1 * 1000 * 1000;

	private final MacMidiService midiService;
	private MIDIEndpointRef endPointRef;
	private final String entityName;
	@SuppressWarnings("unchecked")
	private final Collection<MIDISysexSendRequest> requests = new CircularFifoBuffer(MIDI_SEND_BUFFER_SIZE);

	public MacMidiOutput(final MacMidiService midiService, final MacMidiEntity entity) {
		this.midiService = midiService;
		entityName = entity.getDisplayName();

		endPointRef = midiService.getService().MIDIEntityGetDestination(entity.getReference(), 0);
	}

	@Override
	public void sendSysexMessage(final byte[] message) throws InvalidMidiDataException {
		final MIDISysexSendRequest request = createSendRequest(message);
		keepSendRequest(request);

		final OSStatus status = midiService.getService().MIDISendSysex(request);
		if (!status.toMidiErrorCode().equals(ECoreMidiErrorCodes.kNoError)) {
			LOGGER.error("Returned " + status + ": " + status.toMidiErrorCode().name());
		}
	}

	private void keepSendRequest(final MIDISysexSendRequest request) {
		requests.add(request);
	}

	private MIDISysexSendRequest createSendRequest(final byte[] sysexData) {
		final MIDISysexSendRequest request = new MIDISysexSendRequest();

		request.destination = endPointRef;
		final Pointer sysexDataMemory = new Memory(sysexData.length);
		sysexDataMemory.write(0, sysexData, 0, sysexData.length);
		request.data = sysexDataMemory;
		request.bytesToSend = sysexData.length;
		request.complete = false;
		request.completionProc = null;
		return request;
	}

	@Override
	public void sendActiveSensing() {

	}

	@Override
	public void close() {
	}

	@Override
	public String getName() {
		return entityName;
	}

}
