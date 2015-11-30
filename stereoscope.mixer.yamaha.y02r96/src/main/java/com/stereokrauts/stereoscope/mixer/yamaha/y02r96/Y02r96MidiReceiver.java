package com.stereokrauts.stereoscope.mixer.yamaha.y02r96;

import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeMidiReceiver;

import model.mixer.interfaces.IAmMixer;

/**
 * This class receives MIDI data from the Yamaha 02R96 mixer.
 * @author th
 *
 */
public class Y02r96MidiReceiver extends GenericMidsizeMidiReceiver {
	public Y02r96MidiReceiver(final IAmMixer partner) {
		super(partner);
	}
}
