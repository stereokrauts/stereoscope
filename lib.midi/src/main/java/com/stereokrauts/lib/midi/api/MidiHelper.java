package com.stereokrauts.lib.midi.api;

import java.util.List;

/**
 * This class provides utility functions for the MIDI subsystem.
 * @author th
 *
 */
public final class MidiHelper {
	private MidiHelper() { }
	
	public static <T extends IMidiPort> T getPortFromList(final List<T> inputs, final String inputPortName) {
		for (final T port : inputs) {
			if (port.getName().equals(inputPortName)) {
				return port;
			}
		}
		throw new IllegalArgumentException("MIDI Port is not registered in system: " + inputPortName + " - available ports are " + inputs);
	}
}
