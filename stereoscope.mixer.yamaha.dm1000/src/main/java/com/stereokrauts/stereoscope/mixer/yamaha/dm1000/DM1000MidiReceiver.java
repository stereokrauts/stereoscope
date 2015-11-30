package com.stereokrauts.stereoscope.mixer.yamaha.dm1000;

import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeMidiReceiver;

import model.mixer.interfaces.IAmMixer;

public class DM1000MidiReceiver extends GenericMidsizeMidiReceiver {
	public DM1000MidiReceiver(final IAmMixer partner) {
		super(partner);
	}

}
