package com.stereokrauts.stereoscope.mixer.yamaha.pm5d;

final class Pm5dSysexParameter {

	public static final byte ELMT_MASTER_LEVEL = 0x60;
	public static final byte ELMT_AUX_MASTER_LEVEL = 0x3F;
	public static final byte ELMT_INPUT_LEVEL = 0x28;
	public static final byte ELMT_INPUT_AUX_SEND = 0x37;
	public static final byte ELMT_GRAPHICAL_EQ = 0x74;
	public static final byte ELMT_INPUT_ON = 0x26;
	public static final byte ELMT_INPUT_PAN = 0x27; //pan
	public static final byte ELMT_AUX_DELAY = 0x45;
	public static final byte ELMT_LEVEL_DCA = 0x70;
	public static final byte ELMT_MATRIX_DELAY = 0x58;

	public static final byte ELMT_INPUT_DYN1 = 0x2B; //Input Dynamics1
	public static final byte ELMT_INPUT_DYN2 = 0x2E; //Input Dynamics2
	public static final byte ELMT_INPUT_PEQ = 0x31; //Input PEQ

	public static final byte ELMT_SETUP_DIO = 0x2F;
	public static final byte PARAM_DIO_CLOCKMASTER = 0; // Clock Source of Mixer

	//ACHTUNG mehrere Filtertypen, default +-15dB
	public static final int GEQ_BAND_RANGE = 150;
	public static final int DYNAMICS_COMP_THRESHOLD_VALUECOUNT = 541; // Count of possible threshold values


	public static final int DYNAMICS_GATE_THRESHOLD_VALUECOUNT = 721; // Count of possible threshold values
	public static final int DYNAMICS_GATE_RANGE_VALUECOUNT = 71;


	public static final byte PARAM_GEQ_ON = 0x02; // geq on button
	public static final byte PARAM_GEQ_FLAT = 0x03; // geq flat (reset)
	public static final byte PARAM_GEQ_FIRSTBAND = 0x05;
	//public final static short PARAM_GEQ_RIGHT_FIRSTBAND = 0x28;
	public static final byte PARAM_AUX_DELAY_TIME = 0x03;
	public static final byte PARAM_MATRIX_DELAY_TIME = 0x03;

	// parametric eq generic parameter - nearly identical to LS9/M7CL (param+1)
	public static final byte PARAM_PEQ_MODE = 0x02; //switches between EQ TypeI/II
	public static final byte PARAM_PEQ_LOWQ = 0x09; //low band quality
	public static final byte PARAM_PEQ_LOWF = 0x0A; //low band frequency
	public static final byte PARAM_PEQ_LOWG = 0x0B; // low band gain
	public static final byte PARAM_PEQ_HPFON = 0x06; // high pass filter on/off
	public static final byte PARAM_PEQ_LOWMIDQ = 0x0D; // low mid band quality
	public static final byte PARAM_PEQ_LOWMIDF = 0x0E; // low mid band frequency
	public static final byte PARAM_PEQ_LOWMIDG = 0x0F; // low mid band gain
	public static final byte PARAM_PEQ_HIMIDQ = 0x11; // high mid band quality
	public static final byte PARAM_PEQ_HIMIDF = 0x12; // high mid band frequency
	public static final byte PARAM_PEQ_HIMIDG = 0x13; //high mid band gain
	public static final byte PARAM_PEQ_HIQ = 0x15; // high band quality
	public static final byte PARAM_PEQ_HIF = 0x16; // high band frequency
	public static final byte PARAM_PEQ_HIG = 0x17; // high band gain
	public static final byte PARAM_PEQ_LPF0N = 0x07; // low pass filter on/off
	public static final byte PARAM_PEQ_EQON = 0x03; // equalizer on/off
	// parametric eq additional parameter (mixer specific)
	public static final byte PARAM_PEQ_LOW_BYPASS = 0x08;
	public static final byte PARAM_PEQ_LOWMID_BYPASS = 0x0C;
	public static final byte PARAM_PEQ_HIMID_BYPASS = 0x10;
	public static final byte PARAM_PEQ_HI_BYPASS = 0x14;
	public static final byte PARAM_PEQ_FLAT = 0x01;
	public static final byte PARAM_PEQ_EQ1_TYPE = 0x04; //low band characteristics
	public static final byte PARAM_PEQ_EQ2_TYPE = 0x05; //high band characteristics

	public static final int PEQ_BAND1 = 0;
	public static final int PEQ_BAND2 = 1;
	public static final int PEQ_BAND3 = 2;
	public static final int PEQ_BAND4 = 3;

	// channel strip gate parameter
	public static final byte PARAM_INPUT_GATE_ON = 0x01; // gate on/off
	public static final byte PARAM_INPUT_GATE_LINK = 0x02; // link on/off
	public static final byte PARAM_INPUT_GATE_KEY_IN = 0x03; // can be self/channel/aux
	public static final byte PARAM_INPUT_GATE_TYPE = 0x08; // normal gate or ducking
	public static final byte PARAM_INPUT_GATE_ATTACK = 0x09;
	public static final byte PARAM_INPUT_GATE_RANGE = 0x0A;
	public static final byte PARAM_INPUT_GATE_HOLD = 0x0B;
	public static final byte PARAM_INPUT_GATE_DECAY = 0x0C;
	public static final byte PARAM_INPUT_GATE_THRESHOLD = 0x0D;

	// channel strip compressor parameter
	public static final byte PARAM_INPUT_COMP_ON = 0x01; // compressor on/off
	public static final byte PARAM_INPUT_COMP_LINK = 0x02; // link on/off
	public static final byte PARAM_INPUT_COMP_TYPE = 0x04; // compressor, expander, compander h, compander s
	public static final byte PARAM_INPUT_COMP_ATTACK = 0x05;
	public static final byte PARAM_INPUT_COMP_RELEASE = 0x06;
	public static final byte PARAM_INPUT_COMP_RATIO  = 0x07;
	public static final byte PARAM_INPUT_COMP_GAIN = 0x08;
	public static final byte PARAM_INPUT_COMP_KNEE = 0x09;
	public static final byte PARAM_INPUT_COMP_THRESHOLD = 0x0A;
	public static final byte PARAM_INPUT_PAN = 0x02;
}
