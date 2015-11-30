package com.stereokrauts.stereoscope.mixer.yamaha.dm1000;


import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.lib.midi.util.FormatStringSysex;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeMidiTransmitter;

final class DM1000MidiTransmitter extends GenericMidsizeMidiTransmitter {

	private final DM1000Mixer mixer;

	DM1000MidiTransmitter (final DM1000Mixer mixer, final ISendMidi midi) {
		super(mixer, midi);
		this.mixer = mixer;
	}
	// can't be defined in GenericMidsizeMidiTransmitter
	// because of one different byte (0c)
	@Override
	public void requestChannelName(final byte channel) {
		this.mixer.getCommunicationAware().receive();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e 0c 02 04 {b} {b} F7");
		for (byte i = 0; i < 4; i++) {
			final Object[] par = { new Byte(i), new Byte(channel) };
			this.midi.sendSysexData(fss.fillInMessage(par));
		}
	}

	// same as requestChannelName
	@Override
	public void requestSetupParameter(final byte element, final byte parameter,
			final byte channel) {
		this.mixer.getCommunicationAware().receive();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3E 0C 03 {b} {b} {b} F7");
		final Object[] par = { new Byte(element),
				new Byte(parameter),
				new Byte(channel)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}
}
