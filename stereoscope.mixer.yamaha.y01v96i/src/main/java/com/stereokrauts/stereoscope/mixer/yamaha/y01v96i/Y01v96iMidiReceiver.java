package com.stereokrauts.stereoscope.mixer.yamaha.y01v96i;

import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeMidiReceiver;

import model.mixer.interfaces.IAmMixer;

/**
 * This class receives MIDI data from the Yamaha 01v96.
 * @author th
 *
 */
public class Y01v96iMidiReceiver extends GenericMidsizeMidiReceiver {

	public Y01v96iMidiReceiver(final IAmMixer partner) {
		super(partner);
		//this.yammi = (Y01v96iMixer) partner;
	}

}
