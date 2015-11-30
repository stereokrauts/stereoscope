package com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi;


import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.lib.midi.util.FormatStringSysex;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.GenericMidsizeMixer;

public class GenericMidsizeMidiTransmitter {
	protected ISendMidi midi;
	private static final SLogger LOG = StereoscopeLogManager.getLogger(GenericMidsizeMidiTransmitter.class);
	private final GenericMidsizeMixer partner;

	public GenericMidsizeMidiTransmitter(final GenericMidsizeMixer partner, final ISendMidi midi) {
		this.partner = partner;
		this.midi = midi;
	}

	public void changeParameter(final byte element, final byte parameter,
			final byte channel, final int data) {
		LOG.finest("Setting Parameter on mixer: element=" + element + ", parameter=" + parameter + ", channel=" + channel + ", data=" + data);
		partner.getCommunicationAware().receive();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 10 3e 7f 01 {b} {b} {b} {i} F7");
		final Object[] par = { new Byte(element),
				new Byte(parameter),
				new Byte(channel),
				new Integer(data)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	public void requestParameter(final byte element, final byte parameter,
			final byte channel) {
		LOG.finest("Requesting Parameter from mixer: element=" + element + ", parameter=" + parameter + ", channel=" + channel);
		partner.getCommunicationAware().receive();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e 7f 01 {b} {b} {b} F7");
		final Object[] par = { new Byte(element),
				new Byte(parameter),
				new Byte(channel)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	// has to be overridden by plugin because of one unequal byte
	public void requestChannelName(final byte channel) {
		throw new UnsupportedOperationException("This method must be overridden by the mixer plugin.");
	}

	// has to be overridden by plugin because of one unequal byte
	public void requestSetupParameter(final byte element, final byte parameter,
			final byte channel) {
		throw new UnsupportedOperationException("This method must be overridden by the mixer plugin.");
	}

	// this can be used for all keys
	public void requestKey(final byte parameter, final byte channel) {
		LOG.finest("Requesting key from mixer: parameter=" + parameter + ", channel=" + channel);
		final FormatStringSysex fss = new FormatStringSysex("F0 43 10 3E 0D 20 00 {b} {b} F7");
		final Object[] par = { new Byte(parameter),
				new Byte(channel)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	public void changeGeqParameter(final byte element, final byte parameter, final byte channel,
			final int data) {
		LOG.finest("Changing GEQ parameter on mixer: element=" + element + ", parameter=" + parameter + ", channel=" + channel + ", data=" + data);
		partner.getCommunicationAware().receive();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 10 3e 7f 01 {b} {b} {b} {-i} F7");
		final Object[] par = { new Byte(element),
				new Byte(parameter),
				new Byte(channel),
				new Integer(data)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}
}
