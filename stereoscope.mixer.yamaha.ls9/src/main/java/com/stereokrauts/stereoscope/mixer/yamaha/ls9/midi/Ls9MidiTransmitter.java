package com.stereokrauts.stereoscope.mixer.yamaha.ls9.midi;


import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.lib.midi.util.FormatStringSysex;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.Ls9Mixer;

public final class Ls9MidiTransmitter {
	private ISendMidi midi;
	private final Ls9Mixer mixer;



	public Ls9MidiTransmitter(final Ls9Mixer mixer, final ISendMidi midi) {
		this.mixer = mixer;
		this.midi = midi;
	}

	public void changeParameter(final short element, final short parameter,
			final short channel, final long data) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 10 3e   12 01 {s}   {s} {s}   {l5}   F7");
		final Object[] par = { new Short(element),
				new Short(parameter),
				new Short(channel),
				new Long(data)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	public void requestParameter(final short element, final short parameter,
			final short channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e   12 01 {s}   {s} {s}   F7");
		final Object[] par = { new Short(element),
				new Short(parameter),
				new Short(channel) };
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	// needed for samplerate
	public void requestSetupParameter(final byte element, final short parameter,
			final short channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e   12 01  01 {b}   {s} {s}   F7");
		final Object[] par = { new Byte(element),
				new Short(parameter),
				new Short(channel) };
		this.midi.sendSysexData(fss.fillInMessage(par));
	}


	public void requestChannelName(final short channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e   12 01 01 14   00 00   {s} F7");
		final Object[] par = { new Short(channel) };
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	//!!!UNTESTED!!! LS9 not verified
	// this can be used for all keys
	public void requestKey(final byte parameter, final byte channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 10 3E 0D 20 00 {b} {b} F7");
		final Object[] par = { new Byte(parameter),
				new Byte(channel)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}
}
