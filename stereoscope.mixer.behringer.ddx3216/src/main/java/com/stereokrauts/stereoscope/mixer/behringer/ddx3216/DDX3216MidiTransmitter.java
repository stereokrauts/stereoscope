package com.stereokrauts.stereoscope.mixer.behringer.ddx3216;


import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.lib.midi.util.FormatStringSysex;

final class DDX3216MidiTransmitter {
	ISendMidi midi;
	private final DDX3216Mixer mixer;

	DDX3216MidiTransmitter(final DDX3216Mixer mixer, final ISendMidi midi) {
		this.mixer = mixer;
		this.midi = midi;
	}


	// set to midichannel ignore (60)
	public void changeParameter(final byte parameter, final byte channel,
			final short data) {
		this.mixer.getCommunicationAware().receive();
		final FormatStringSysex fss = new FormatStringSysex(
				"F0 00 20 32 60 0B 20 01"
						+ "{b}"  /* channel */
						+ "{b}"  /* parameter */
						+ "{s}"  /* data */
						+ "F7"); /* STOP */

		final Object[] par = { new Byte(channel),
				new Byte(parameter),
				new Short(data)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	public void requestParameter(final byte parameter, final byte channel) {
		this.mixer.getCommunicationAware().receive();
		final FormatStringSysex fss = new FormatStringSysex(
				"F0 00 20 32 60 0B 60 01"
						+ "{b}"  /* channel */
						+ "{b}"  /* parameter */
						+ "F7"); /* STOP */
		final Object[] par = { new Byte(channel),
				new Byte(parameter)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}
}
