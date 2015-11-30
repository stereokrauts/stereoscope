package com.stereokrauts.lib.midi.standard;

import java.util.ArrayList;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.IMidiInput;
import com.stereokrauts.lib.midi.api.IMidiInputPort;
import com.stereokrauts.lib.midi.api.IMidiOutput;
import com.stereokrauts.lib.midi.api.IMidiOutputPort;
import com.stereokrauts.lib.midi.api.IMidiSubsystem;
import com.stereokrauts.lib.midi.api.MidiException;
import com.stereokrauts.lib.midi.support.MidiInput;
import com.stereokrauts.lib.midi.support.MidiOutput;

/**
 * The main entry point into the midi subsystem of stereoscope.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 */
public final class MidiSubsystem implements IMidiSubsystem {
	/**
	 * The logger for the midi subsystem.
	 */
	private static final SLogger LOGGER = StereoscopeLogManager.getLogger("midi");

	/**
	 * @return a list of all available midi output ports on this computer.
	 */
	@Override
	public MidiOutputPort[] getOutputPorts() {
        final MidiDevice.Info[] devs = MidiSystem.getMidiDeviceInfo();
        final ArrayList<MidiOutputPort> rv = new ArrayList<>();
        for (int i = 0; i < devs.length; i++) {
        	try {
				if (MidiSystem.getMidiDevice(devs[i]).getMaxReceivers() == -1) {
					rv.add(new MidiOutputPort(devs[i].getName(), devs[i]));
				}
			} catch (final Exception e) {
				LOGGER.info("Midi Port unavailable (in): " + e.toString());
				/* ignore unavailable ports */
			}
        }
        return rv.toArray(new MidiOutputPort[rv.size()]);
	}
	
	/**
	 * @return a list of all available midi input ports on this computer.
	 */
	@Override
	public MidiInputPort[] getInputPorts() {
        final MidiDevice.Info[] devs = MidiSystem.getMidiDeviceInfo();
        final ArrayList<MidiInputPort> rv = new ArrayList<>();
        for (int i = 0; i < devs.length; i++) {
        	try {
				if (MidiSystem.getMidiDevice(devs[i]).getMaxTransmitters()
																		== -1) {
					rv.add(new MidiInputPort(devs[i].getName(), devs[i]));
				}
			} catch (final Exception e) {
				LOGGER.info("Midi Port unavailable (out): " + e.toString());
				/* ignore unavailable ports */
			}
        }
        return rv.toArray(new MidiInputPort[0]);
	}
	
	/**
	 * @param p a port that has previously been returned by getOutputPorts
	 * @return The corresponding object of the stereoscope midi subsystem
	 * @throws MidiUnavailableException Any error of the java sound api.
	 */
	@Override
	public synchronized IMidiOutput getMidiOutput(final IMidiOutputPort p)
	throws MidiException {
		try {
			final MidiDevice d = MidiSystem.getMidiDevice(p.getInfo());
			if (!d.isOpen()) {
				d.open();
			}
			return new MidiOutput(d);
		} catch (final MidiUnavailableException e) {
			throw new MidiException(e);
		}
	}
	
	/**
	 * @param p a port that has previously been returned by getInputPorts
	 * @return The corresponding object of the stereoscope midi subsystem
	 * @throws MidiUnavailableException Any error of the java sound api.
	 */
	@Override
	public synchronized IMidiInput getMidiInput(final IMidiInputPort p)
	throws MidiException {
		try {
			final MidiDevice d = MidiSystem.getMidiDevice(p.getInfo());
			if (!d.isOpen()) {
				d.open();
			}
			return new MidiInput(d);
		} catch (final MidiUnavailableException e) {
			throw new MidiException(e);
		}
	}
}
