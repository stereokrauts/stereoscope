/**
 * 
 */
package com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi;

/**
 * @author jansen
 *
 */
public class GenericMidsizeSysexParameter {

	/**
	 * these are constants which are named according
	 * to the yamaha-sysex tables.
	 * the comment shows the according OSC-Name
	 */
	public final static byte ELMT_INPUT_FADER = 0x1C; //LevelChannel
	public final static byte ELMT_STEREO_FADER = 0x4F; //LevelMaster
	public final static byte ELMT_AUX_FADER = 0x39; //LevelAuxMaster
	public final static byte ELMT_AUX_DELAY = 0x3D; //AuxDelay
	public final static byte ELMT_INPUT_AUX = 0x23; //AuxlevelChannel
	public final static byte ELMT_INPUT_ON = 0x1A; //Channel On Button
	public final static byte ELMT_INPUT_PAN = 0x1B; //pan
	public final static byte ELMT_BUS_DELAY = 0x2F; //BusDelay
	public final static byte ELMT_BUS_FADER = 0x2B; //BusFader
	
	
	public final static byte ELMT_INPUT_GATE = 0x1E; //Input Gate
	public final static byte ELMT_INPUT_COMP = 0x1F; //Input Compressor
	public final static byte ELMT_INPUT_PEQ = 0x20; //Input PEQ
	
	public final static byte ELMT_SETUP_DIO = 0x23;
	public final static byte PARAM_DIO_CLOCKMASTER = 0; // Clock Source of Mixer
	
	public final static byte ELMT_SETUP_MIDI_RX = 0x4E; // Midi Receive Channel
	public final static byte ELMT_SETUP_MIDI_TX = 0X4F; // Midi Transmit Channel
	public final static byte ELMT_SETUP_MIDI_TX_ENABLE = 0x55; // Midi Functions Transmit Enable/Disable
	public final static byte ELMT_SETUP_MIDI_RX_ENABLE = 0x56; // Midi Functions Transmit Enable/Disable
	
	// input/output groups (fader, mute, dynamics, eq)
	public final static byte ELMT_INPUT_GROUP = 26;
	public final static byte ELMT_OUTPUT_GROUP = 16;
	
	// channel strip eq parameter
	public final static byte PARAM_PEQ_MODE = 0x00; //switches between EQ TypeI/II
	public final static byte PARAM_PEQ_LOWQ = 0x01; //low band quality
	public final static byte PARAM_PEQ_LOWF = 0x02; //low band frequency
	public final static byte PARAM_PEQ_LOWG = 0x03; // low band gain
	public final static byte PARAM_PEQ_HPFON = 0x04; // high pass filter on/off
	public final static byte PARAM_PEQ_LOWMIDQ = 0x05; // low mid band quality
	public final static byte PARAM_PEQ_LOWMIDF = 0x06; // low mid band frequency
	public final static byte PARAM_PEQ_LOWMIDG = 0x07; // low mid band gain
	public final static byte PARAM_PEQ_HIMIDQ = 0x08; // high mid band quality
	public final static byte PARAM_PEQ_HIMIDF = 0x09; // high mid band frequency
	public final static byte PARAM_PEQ_HIMIDG = 0x0A; //high mid band gain
	public final static byte PARAM_PEQ_HIQ = 0x0B; // high band quality
	public final static byte PARAM_PEQ_HIF = 0x0C; // high band frequency
	public final static byte PARAM_PEQ_HIG = 0x0D; // high band gain
	public final static byte PARAM_PEQ_LPF0N = 0x0E; // low pass filter on/off
	public final static byte PARAM_PEQ_EQON = 0x0F; // equalizer on/off
	// eq-band numbers. They're often used so they're defined here
	public final static int PEQ_LOW_BAND = 0;
	public final static int PEQ_LOWMID_BAND = 1;
	public final static int PEQ_HIMID_BAND = 2;
	public final static int PEQ_HI_BAND = 3;
	
	// channel strip gate parameter
	public final static byte PARAM_INPUT_GATE_ON = 0x00; // gate on/off
	public final static byte PARAM_INPUT_GATE_LINK = 0x01; // link on/off
	public final static byte PARAM_INPUT_GATE_KEY_IN = 0x02; // can be self/channel/aux
	public final static byte PARAM_INPUT_GATE_KEY_AUX = 0x03;
	public final static byte PARAM_INPUT_GATE_KEY_CH = 0x04;
	public final static byte PARAM_INPUT_GATE_TYPE = 0x05; // normal gate or ducking
	public final static byte PARAM_INPUT_GATE_ATTACK = 0x06;
	public final static byte PARAM_INPUT_GATE_RANGE = 0x07;
	public final static byte PARAM_INPUT_GATE_HOLD = 0x08;
	public final static byte PARAM_INPUT_GATE_DECAY = 0x09;
	public final static byte PARAM_INPUT_GATE_THRESHOLD = 0x0A;
	
	// channel strip compressor parameter
	public final static byte PARAM_INPUT_COMP_LOC = 0x00; //pre eq, post eq, post fader
	public final static byte PARAM_INPUT_COMP_ON = 0x01; // compressor on/off
	public final static byte PARAM_INPUT_COMP_LINK = 0x02; // link on/off
	public final static byte PARAM_INPUT_COMP_TYPE = 0x03; // compressor, expander, compander h, compander s
	public final static byte PARAM_INPUT_COMP_ATTACK = 0x04;
	public final static byte PARAM_INPUT_COMP_RELEASE = 0x05;
	public final static byte PARAM_INPUT_COMP_RATIO  = 0x06;
	public final static byte PARAM_INPUT_COMP_GAIN = 0x07;
	public final static byte PARAM_INPUT_COMP_KNEE = 0x08;
	public final static byte PARAM_INPUT_COMP_THRESHOLD = 0x09;
	
	
	//buttons
	public final static byte USER_DEFINE_KEYS = 0x31; //user defined buttons
	

	
}
