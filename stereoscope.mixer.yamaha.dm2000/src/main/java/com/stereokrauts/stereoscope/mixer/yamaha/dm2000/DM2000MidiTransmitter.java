package com.stereokrauts.stereoscope.mixer.yamaha.dm2000;

import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.lib.midi.util.FormatStringSysex;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeMidiTransmitter;


final class DM2000MidiTransmitter extends GenericMidsizeMidiTransmitter {
	private final DM2000Mixer mixer;

	public DM2000MidiTransmitter (final DM2000Mixer mixer, final ISendMidi midi) {
		super(mixer, midi);
		this.mixer = mixer;
	}

	// dm2k has geq
	static final int GEQ_BAND_RANGE = 150;
	public final static short ELMT_GRAPHICAL_EQ = 0x57;
	public final static short PARAM_GEQ_LEFT_FIRSTBAND = 0x04;
	//public final static short PARAM_GEQ_RIGHT_FIRSTBAND = 0x28;


	// can't be defined in GenericMidsizeMidiTransmitter
	// because of one different byte (06)
	@Override
	public void requestChannelName(final byte channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e 06 02 04 {b} {b} F7");
		for (byte i = 0; i < 4; i++) {
			final Object[] par = { new Byte(i), new Byte(channel) };
			this.midi.sendSysexData(fss.fillInMessage(par));
		}
	}

	// same as requestChannelName
	@Override
	public void requestSetupParameter(final byte element, final byte parameter,
			final byte channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3E 06 03 {b} {b} {b} F7");
		final Object[] par = { new Byte(element),
				new Byte(parameter),
				new Byte(channel)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

}
