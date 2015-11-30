package com.stereokrauts.lib.midi.standard;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import com.stereokrauts.lib.midi.api.IMidiPort;


/**
 * This class is the stereoscope internal representation of a MIDI device,
 * it is related to an java sound api MIDI device.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public abstract class MidiPort implements IMidiPort {
	/**
	 * The textual name of the device.
	 */
	private final String name;
	/**
	 * The corresponding java sound api Info object.
	 */
	private final MidiDevice.Info info;
	
	/**
	 * Create a new device.
	 * @param myName The name of the device
	 * @param myInfo The Info object of the sound api.
	 */
	public MidiPort(final String myName, final Info myInfo) {
		this.name = myName;
		this.info = myInfo;
	}
	
	/**
	 * @return Get the name of the device.
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return Get the Info object of the device.
	 */
	@Override
	public MidiDevice.Info getInfo() {
		return this.info;
	}
	
	/**
	 * @return get a string representation of the object.
	 */
	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		final MidiPort other = (MidiPort) obj;
		if (this.name == null
				|| other.name != null
				|| !this.name.equals(other.name)) {
				return false;
		}
		return true;
	}
	
	/**
	 * Returns a Midi Input Port object given it's name.
	 * @param midiInputPortName The name to search for
	 * @return The midi object
	 * @throws MidiUnavailableException Any java sound api error.
	 */
	public static IMidiPort getInputByName(final String midiInputPortName)
	throws MidiUnavailableException {
        final MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
        	if (MidiSystem.getMidiDevice(infos[i]).getMaxTransmitters() != 0) {
	        	if (infos[i].getName().equals(midiInputPortName)) {
	        		return new MidiInputPort(midiInputPortName, infos[i]);
	        	}
        	}
        }
        return null;
	}

	/**
	 * Returns a Midi Output Port object given it's name.
	 * @param midiOutputPortName The name to search for
	 * @return The midi object
	 * @throws MidiUnavailableException Any java sound api error.
	 */
	public static IMidiPort getOutputByName(final String midiOutputPortName)
	throws MidiUnavailableException {
        final MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
        	if (MidiSystem.getMidiDevice(infos[i]).getMaxReceivers() != 0) {
	        	if (infos[i].getName().equals(midiOutputPortName)) {
	        		return new MidiOutputPort(midiOutputPortName, infos[i]);
	        	}
        	}
        }
        return null;
	}
}
