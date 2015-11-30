package com.stereokrauts.lib.midi.support;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.IMidiInput;
import com.stereokrauts.lib.midi.api.IReactToMidi;

/**
 * This class handles midi messages that are being received through the
 * java sound system.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public final class MidiInput implements IMidiInput {
	/**
	 * Central instance of the logging subsystem for this subsystem.
	 */
	private static final SLogger LOGGER = StereoscopeLogManager.getLogger("midi");

	/**
	 * The Transmitter (the class that sends the MIDI messages to this
	 * subsystem).
	 */
	private final Transmitter trans;
	/**
	 * The class to which received MIDI messages are forwarded.
	 */
	private IReactToMidi reactor;
	/**
	 * The associated MIDI device.
	 */
	private final MidiDevice myDevice;
	/**
	 * The internal object handling MIDI messages sent by the java sound
	 * system.
	 */
	private InternalReceiver myInternalReceiver;
	/**
	 * Do we currently have an open connection?
	 */
	private boolean isOpen = false;
	
	/**
	 * This internal class is responsible to translate between the
	 * Java Sound API and it's requirements and the stereoscope internal
	 * MIDI subsystem.
	 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
	 *
	 */
	private final class InternalReceiver implements Receiver {
		private static final int LOWER_FOUR_BITS = 0x0F;
		private static final int UPPER_TWO_BITS = 0xC0;
		/**
		 * The magic first byte of a SYSEX message.
		 */
		private static final int SYSEX_MAGIC_FIRSTBYTE = 0xF0;

		@Override
		public void send(final MidiMessage message, final long timeStamp) {
			LOGGER.finest("MIDI received: "
					+ ByteStringConversion.toHex(message.getMessage()));
			final byte[] m = message.getMessage();
			if (m[0] == (byte) SYSEX_MAGIC_FIRSTBYTE) {
				MidiInput.this.reactor.handleSysex(m);
			} else if ((m[0] ^ (byte) UPPER_TWO_BITS) <= LOWER_FOUR_BITS) {
				final int newProgram = (m[1]);
				LOGGER.fine("PROGRAM Change to " + newProgram);
				MidiInput.this.reactor.handleProgramChange(newProgram);
			}
		}

		@Override
		public void close() {
			MidiInput.this.trans.close();
		}
	}
	
	/**
	 * Registers this object on the given MIDI device.
	 * @param d The device to receive messages from.
	 * @throws MidiUnavailableException Any error from the
	 * 		java sound API MIDI subsystem.
	 */
	public MidiInput(final MidiDevice d) throws MidiUnavailableException {
		this.trans = d.getTransmitter();
		this.myDevice = d;
		this.isOpen = true;
	}
	
	/* (non-Javadoc)
	 * @see com.stereokrauts.lib.midi.standard.IMidiInput#setHandler(com.stereokrauts.lib.midi.standard.IReactToMidi)
	 */
	@Override
	public void setHandler(final IReactToMidi handler) {
		this.reactor = handler;
		this.myInternalReceiver = new InternalReceiver();
		this.trans.setReceiver(this.myInternalReceiver);
	}

	/* (non-Javadoc)
	 * @see com.stereokrauts.lib.midi.standard.IMidiInput#close()
	 */
	@Override
	public void close() {
		if (this.isOpen) {
			this.myInternalReceiver.close();
			//trans.close();
			if (this.myDevice.isOpen()) {
				this.myDevice.close();
			}
			this.isOpen = false;
		}
	}

	@Override
	public String getName() {
		return this.myDevice.getDeviceInfo().getName();
	}
}
