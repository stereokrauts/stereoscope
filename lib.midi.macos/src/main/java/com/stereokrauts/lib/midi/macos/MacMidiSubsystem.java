package com.stereokrauts.lib.midi.macos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;

import javax.sound.midi.MidiUnavailableException;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.IMidiInput;
import com.stereokrauts.lib.midi.api.IMidiInputPort;
import com.stereokrauts.lib.midi.api.IMidiOutput;
import com.stereokrauts.lib.midi.api.IMidiOutputPort;
import com.stereokrauts.lib.midi.api.IMidiSubsystem;
import com.stereokrauts.lib.midi.api.MidiException;

/**
 * The main entry point into the midi subsystem of stereoscope.
 * 
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 */
public final class MacMidiSubsystem implements IMidiSubsystem {
	private MacMidiService midiService;

	public MacMidiSubsystem() {
		try {
			midiService = new MacMidiService();
		} catch (Exception e) {
			SLogger logger = StereoscopeLogManager.getLogger(getClass());
			String errorMessage = "Could not initialize Mac MIDI subsystem";
			logger.log(Level.INFO, errorMessage, e);
			throw new IllegalStateException(errorMessage, e);
		}
	}

	/**
	 * @return a list of all available midi output ports on this computer.
	 */
	@Override
	public IMidiOutputPort[] getOutputPorts() {
		final ArrayList<MacMidiPort> rv = new ArrayList<MacMidiPort>();

		for (final MacMidiDevice device : midiService.getDevices()) {
			if (device.isOnline()) {
				for (final MacMidiEntity entity : midiService.getEntities(device)) {
					if (entity.isOutputPort()) {
						rv.add(new MacMidiOutputPort(entity));
					}
				}
			}
		}
		
		Collections.sort(rv, getMidiPortComparator());

		return rv.toArray(new IMidiOutputPort[rv.size()]);
	}

	/**
	 * @return a list of all available midi input ports on this computer.
	 */
	@Override
	public IMidiInputPort[] getInputPorts() {
		final ArrayList<MacMidiPort> rv = new ArrayList<MacMidiPort>();

		for (final MacMidiDevice device : midiService.getDevices()) {
			if (device.isOnline()) {
				for (final MacMidiEntity entity : midiService.getEntities(device)) {
					if (entity.isInputPort()) {
						rv.add(new MacMidiInputPort(entity));
					}
				}
			}
		}
		
		Collections.sort(rv, getMidiPortComparator());
		
		return rv.toArray(new IMidiInputPort[rv.size()]);
	}

	/**
	 * @param p
	 *            a port that has previously been returned by getOutputPorts
	 * @return The corresponding object of the stereoscope midi subsystem
	 * @throws MidiUnavailableException
	 *             Any error of the java sound api.
	 */
	@Override
	public synchronized IMidiOutput getMidiOutput(final IMidiOutputPort p) throws MidiException {
		if (p instanceof MacMidiPort) {
			final MacMidiPort macMidiPort = (MacMidiPort) p;
			if (macMidiPort.getEntity().isOutputPort()) {
				final MacMidiOutput macMidiOutput = new MacMidiOutput(midiService, macMidiPort.getEntity());
				return macMidiOutput;
			}
		}
		throw new IllegalArgumentException("The provided IMidiPort is not suitable for the MAC midi abstraction or is not an output port: "
				+ p);
	}

	/**
	 * @param p
	 *            a port that has previously been returned by getInputPorts
	 * @return The corresponding object of the stereoscope midi subsystem
	 * @throws MidiUnavailableException
	 *             Any error of the java sound api.
	 */
	@Override
	public synchronized IMidiInput getMidiInput(final IMidiInputPort p) throws MidiException {
		if (p instanceof MacMidiPort) {
			final MacMidiPort macMidiPort = (MacMidiPort) p;
			if (macMidiPort.getEntity().isInputPort()) {
				final MacMidiInput macMidiOutput = new MacMidiInput(midiService, macMidiPort.getEntity());
				return macMidiOutput;
			}
		}
		throw new IllegalArgumentException("The provided IMidiPort is not suitable for the MAC midi abstraction or is not an output port: "
				+ p);
	}
	
	private Comparator<? super MacMidiPort> getMidiPortComparator() {
		return new Comparator<MacMidiPort>() {
			@Override
			public int compare(MacMidiPort o1, MacMidiPort o2) {
				return o1.getName().compareTo(o2.getName());
			}
		};
	}
}
