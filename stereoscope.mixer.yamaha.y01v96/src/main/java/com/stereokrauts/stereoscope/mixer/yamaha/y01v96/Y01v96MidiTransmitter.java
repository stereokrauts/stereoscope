package com.stereokrauts.stereoscope.mixer.yamaha.y01v96;

import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.lib.midi.util.FormatStringSysex;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeMidiTransmitter;

/**
 * This class transmits MIDI data to the Yamaha 01v96.
 * @author th
 *
 */
final class Y01v96MidiTransmitter extends GenericMidsizeMidiTransmitter {
	private final Y01v96Mixer mixer;


	// just to implement the super constructor
	public Y01v96MidiTransmitter(final Y01v96Mixer mixer, final ISendMidi midi) {
		super(mixer, midi);
		this.mixer = mixer;
	}

	// can't be defined in GenericMidsizeMidiTransmitter
	// because of one different byte (0d)
	@Override
	public void requestChannelName(final byte channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e 0d 02 04 {b} {b} F7");
		for (byte i = 0; i < 4; i++) {
			final Object[] par = { new Byte(i), new Byte(channel) };
			this.midi.sendSysexData(fss.fillInMessage(par));
		}
	}


	// same as requestChannelName
	@Override
	public void requestSetupParameter(final byte element, final byte parameter,	final byte channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3E 0D 03 {b} {b} {b} F7");
		final Object[] par = { new Byte(element),
				new Byte(parameter),
				new Byte(channel)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	public void requestStereoChannelName(final byte stereoNumber) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e 0d 02 17 {b} {b} F7");
		for (byte i = 0; i < 4; i++) {
			final Object[] par = { new Byte(i), new Byte(stereoNumber) };
			this.midi.sendSysexData(fss.fillInMessage(par));
		}
	}
}
