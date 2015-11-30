package com.stereokrauts.stereoscope.mixer.yamaha.m7cl;


import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.lib.midi.util.FormatStringSysex;

final class M7clMidiTransmitter {
	private final ISendMidi midi;

	static final int GEQ_BAND_RANGE = 150;

	public static final short ELMT_LEVEL_MASTER = 0x6C;
	public static final short ELMT_LEVEL_AUX_MASTER = 0x4D;
	public static final short ELMT_LEVEL_CHANNEL = 0x32;
	public static final short ELMT_AUXLEVEL_CHANNEL = 0x42;
	public static final short ELMT_LEVEL_DCA = 0x79;

	public static final short ELMT_RACK_MOUNT = 0x7D;
	public static final short ELMT_GRAPHICAL_EQ = 0x7E;
	public static final short ELMT_INPUT_ON = 0x30; //Channel On Button
	public static final short ELMT_INPUT_PAN = 0x31; //pan

	// busses missing
	public static final byte ELMT_INPUT_DYNA1 = 0x36; //Input Dynamics1
	public static final byte ELMT_INPUT_DYNA2 = 0x39; //Input Dynamics2
	public static final byte ELMT_INPUT_PEQ = 0x3C; //Input PEQ
	public static final byte ELMT_INPUT_MUTE_GROUP = 0x3E; //Input Mute

	public static final byte ELMT_SETUP_DIO = 0x4D;
	public static final byte PARAM_DIO_CLOCKMASTER = 0; // Clock Source of Mixer

	public static final short ELMT_OMNI_OUTPUT = 0x1A;

	public static final short PARAM_INPUT_PAN = 0x01; // pan mode=0x00

	public static final short PARAM_GEQ_ISFLEXEQ = 0x00;
	public static final short PARAM_GEQ_LEFT_FIRSTBAND = 0x09;
	public static final short PARAM_GEQ_RIGHT_FIRSTBAND = 0x28;

	public static final short PARAM_OUTPUT_DELAY = 0x02;

	public static final byte CHANNEL_SOLO_KEYS = 0x01; //solo buttons
	public static final byte USER_DEFINE_KEYS = 0x31; //user defined buttons

	// parametric eq generic parameter - identical for LS9/M7CL
	public static final byte PARAM_PEQ_MODE = 0x01; //switches between EQ TypeI/II
	public static final byte PARAM_PEQ_LOWQ = 0x08; //low band quality
	public static final byte PARAM_PEQ_LOWF = 0x09; //low band frequency
	public static final byte PARAM_PEQ_LOWG = 0x0A; // low band gain
	public static final byte PARAM_PEQ_HPFON = 0x05; // high pass filter on/off
	public static final byte PARAM_PEQ_LOWMIDQ = 0x0C; // low mid band quality
	public static final byte PARAM_PEQ_LOWMIDF = 0x0D; // low mid band frequency
	public static final byte PARAM_PEQ_LOWMIDG = 0x0E; // low mid band gain
	public static final byte PARAM_PEQ_HIMIDQ = 0x10; // high mid band quality
	public static final byte PARAM_PEQ_HIMIDF = 0x11; // high mid band frequency
	public static final byte PARAM_PEQ_HIMIDG = 0x12; //high mid band gain
	public static final byte PARAM_PEQ_HIQ = 0x14; // high band quality
	public static final byte PARAM_PEQ_HIF = 0x15; // high band frequency
	public static final byte PARAM_PEQ_HIG = 0x16; // high band gain
	public static final byte PARAM_PEQ_LPF0N = 0x06; // low pass filter on/off
	public static final byte PARAM_PEQ_EQON = 0x02; // equalizer on/off
	// parametric eq additional parameter (mixer specific)
	public static final byte PARAM_PEQ_LOW_BYPASS = 0x07;
	public static final byte PARAM_PEQ_LOWMID_BYPASS = 0x0B;
	public static final byte PARAM_PEQ_HIMID_BYPASS = 0x0F;
	public static final byte PARAM_PEQ_HI_BYPASS = 0x13;
	public static final byte PARAM_PEQ_FLAT = 0x00;
	public static final byte PARAM_PEQ_EQ1_TYPE = 0x03;
	public static final byte PARAM_PEQ_EQ2_TYPE = 0x04;

	public static final int PEQ_BAND1 = 0;
	public static final int PEQ_BAND2 = 1;
	public static final int PEQ_BAND3 = 2;
	public static final int PEQ_BAND4 = 3;

	// input dynamics parameter. appears to dynamics 1 and 2.
	// completely different from generic midsize
	public static final byte PARAM_INPUT_DYNA_ON = 0x00;
	public static final byte PARAM_INPUT_DYNA_TYPE = 0x01;
	public static final byte PARAM_INPUT_DYNA_AUTO_ON = 0x02;
	public static final byte PARAM_INPUT_DYNA_LEFT_SIDE_CHAIN = 0x03;
	public static final byte PARAM_INPUT_DYNA_KEY_IN = 0x04;
	public static final byte PARAM_INPUT_DYNA_FILTER_ON = 0x05;
	public static final byte PARAM_INPUT_DYNA_FILTER_TYPE = 0x06;
	public static final byte PARAM_INPUT_DYNA_FILTER_FREQ = 0x07;
	public static final byte PARAM_INPUT_DYNA_FILTER_Q = 0x08;
	public static final byte PARAM_INPUT_DYNA_ATTACK = 0x09;
	public static final byte PARAM_INPUT_DYNA_RANGE = 0x0A;
	public static final byte PARAM_INPUT_DYNA_HOLD = 0x0B;
	public static final byte PARAM_INPUT_DYNA_DECAY_RELEASE = 0x0C;
	public static final byte PARAM_INPUT_DYNA_RATIO = 0x0D;
	public static final byte PARAM_INPUT_DYNA_GAIN = 0x0E;
	public static final byte PARAM_INPUT_DYNA_KNEE = 0x0F;
	public static final byte PARAM_INPUT_DYNA_THRESHOLD = 0x10;

	private final M7clMixer mixer;


	M7clMidiTransmitter(final M7clMixer mixer, final ISendMidi midi) {
		this.mixer = mixer;
		this.midi = midi;
	}

	public void changeParameter(final short element, final short parameter,
			final short channel, final long data) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 10 3e   11 01 {s}  {s} {s}   {l5}   F7");
		final Object[] par = { new Short(element),
				new Short(parameter),
				new Short(channel),
				new Long(data)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	public void requestParameter(final short element, final short parameter,
			final short channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e   11 01 {s}  {s} {s}   F7");
		final Object[] par = { new Short(element),
				new Short(parameter),
				new Short(channel) };
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	public void requestSetupParameter(final byte element, final short parameter,
			final short channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e   11 01 01 {b}  {s} {s}   F7");
		final Object[] par = { new Byte(element),
				new Short(parameter),
				new Short(channel) };
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	public void requestChannelName(final short channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e   11 01 01 13   00 00   {s} F7");
		final Object[] par = { new Short(channel) };
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	// !!!UNTESTED!!! m7cl not verified
	// this can be used for all keys
	public void requestKey(final byte parameter, final byte channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 10 3E 0D 20 00 {b} {b} F7");
		final Object[] par = { new Byte(parameter),
				new Byte(channel)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}
}
