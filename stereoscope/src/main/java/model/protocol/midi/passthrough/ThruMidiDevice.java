package model.protocol.midi.passthrough;

import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.IMidiInput;
import com.stereokrauts.lib.midi.api.IMidiOutput;

/**
 * This class implements all functions that would be neccessary to
 * forward MIDI messages from the Mixer to the StudioManager and
 * back.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public final class ThruMidiDevice implements MidiDevice {
	private static final SLogger LOG = StereoscopeLogManager.getLogger(ThruMidiDevice.class);

	/**
	 * The Input port of this class.
	 */
	private static IMidiInput thruInput;
	/**
	 * The Output port of this class.
	 */
	private static IMidiOutput thruOutput;

	/**
	 * Set the MIDI input device.
	 * @param in The new input device
	 */
	public static void setMidiInput(final IMidiInput in) {
		if (thruInput != null) {
			thruInput = in;
		} else {
			throw new IllegalStateException("There is an input set already.");
		}
	}

	/**
	 * Set the MIDI output device.
	 * @param out The new output device
	 */
	public static void setMidiOutput(final IMidiOutput out) {
		if (thruOutput != null) {
			thruOutput = out;
		} else {
			throw new IllegalStateException("There is an output set already.");
		}
	}




	/**
	 * This classes MIDI info object as required by the java sound api.
	 */
	private final Info info;
	/**
	 * Is this MIDI device currently used?
	 */
	private boolean isOpen = false;
	/**
	 * Create a new MIDI forwarder with the provided info object.
	 * @param myInfo Port description.
	 */

	public ThruMidiDevice(final MidiDevice.Info myInfo) {
		this.info = myInfo;
	}


	@Override
	public Info getDeviceInfo() {
		return this.info;
	}


	@Override
	public void open() throws MidiUnavailableException {
		LOG.info("open()");
		this.isOpen = true;
	}


	@Override
	public void close() {
		this.isOpen = false;
	}


	@Override
	public boolean isOpen() {
		return this.isOpen;
	}


	@Override
	public long getMicrosecondPosition() {
		return 0;
	}


	@Override
	public int getMaxReceivers() {
		return 0;
	}


	@Override
	public int getMaxTransmitters() {
		return 0;
	}


	@Override
	public Receiver getReceiver() throws MidiUnavailableException {
		return null;
	}


	@Override
	public List<Receiver> getReceivers() {
		return null;
	}


	@Override
	public Transmitter getTransmitter() throws MidiUnavailableException {
		return null;
	}


	@Override
	public List<Transmitter> getTransmitters() {
		return null;
	}

}
