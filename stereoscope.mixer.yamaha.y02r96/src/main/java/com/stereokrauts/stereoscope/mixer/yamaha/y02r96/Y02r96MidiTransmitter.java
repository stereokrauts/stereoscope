package com.stereokrauts.stereoscope.mixer.yamaha.y02r96;


import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.lib.midi.util.FormatStringSysex;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeMidiTransmitter;

/**
 * This class transmits MIDI data to the Yamaha 02R96.
 * @author th
 *
 */
final class Y02r96MidiTransmitter extends GenericMidsizeMidiTransmitter {

	private final Y02r96Mixer mixer;

	public Y02r96MidiTransmitter(final Y02r96Mixer y02r96Mixer, final ISendMidi midi) {
		super(y02r96Mixer, midi);
		this.mixer = y02r96Mixer;

	}

	// can't be defined in GenericMidsizeMidiTransmitter
	// because of one different byte (0b)
	@Override
	public void requestChannelName(final byte channel) {
		this.mixer.getCommunicationAware().receive();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e 0b 02 04 {b} {b} F7");
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
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3E 0B 03 {b} {b} {b} F7");
		final Object[] par = { new Byte(element),
				new Byte(parameter),
				new Byte(channel)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}
}
