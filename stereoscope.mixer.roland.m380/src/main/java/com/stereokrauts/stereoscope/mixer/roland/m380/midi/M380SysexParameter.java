/**
 * 
 */
package com.stereokrauts.stereoscope.mixer.roland.m380.midi;

/**
 * @author jansen
 *
 */
public final class M380SysexParameter {

	// Address block: section parameter (1st byte)
	public final static byte ADDR_INPUT_BOARD = 0x00;
	public final static byte ADDR_OUTPUT_BOARD = 0x01;
	public final static byte ADDR_INPUT_PATCHBAY = 0x02;
	public final static byte ADDR_INPUT_CHANNEL = 0x03;
	public final static byte ADDR_TALKBACK_OSC = 0x04;
	public final static byte ADDR_AUX_CHANNEL = 0x05;
	public final static byte ADDR_MAIN_CHANNEL = 0x06;
	public final static byte ADDR_MONITOR = 0x07;
	public final static byte ADDR_MUTE_GROUP = 0x08;
	public final static byte ADDR_DCA_GROUP = 0x09;
	public final static byte ADDR_OUTPUT_PATCHBAY = 0x0A;
	public final static byte ADDR_EFFECTS = 0x0B;
	public final static byte ADDR_MATRIX_CHANNEL = 0x0E;
	public final static byte ADDR_TEMPO = 0x0F;
	public final static byte ADDR_USB_MEMORY_RECORDER = 0x0C;
	public final static byte ADDR_SYSTEM = 0x10;

	// Address block: input channel parameter (3rd byte)
	public final static byte INPUT_CHANNEL = 0x00;
	public final static byte INPUT_AUX = 0x01;
	public final static byte INPUT_MISC = 0x02; //not used

	// Address block: input channel parameter (4th byte)

	// input common parameter
	public final static byte CH_NAME1 = 0x00;
	public final static byte CH_NAME2 = 0x01;
	public final static byte CH_NAME3 = 0x02;
	public final static byte CH_NAME4 = 0x03;
	public final static byte CH_NAME5 = 0x04;
	public final static byte CH_NAME6 = 0x05;
	public final static byte CH_LINK = 0x08;
	public final static byte CH_MUTE = 0x0C;
	public final static byte CH_SOLO = 0x0D;
	public final static byte CH_LEVEL = 0x0E;
	public final static byte CH_PAN = 0x10;
	public final static byte CH_SWITCH = 0x11; //channel on/off

	// input dynamics parameter
	public final static byte CH_GATE_SWITCH = 0x30; //gate on/off
	public final static byte CH_GATE_KEY_IN = 0x31;
	public final static byte CH_GATE_MODE = 0x32; //expander, gate, ducking
	public final static byte CH_GATE_THRESHOLD = 0x33;
	public final static byte CH_GATE_RATIO = 0x35;
	public final static byte CH_GATE_KNEE = 0x36;
	public final static byte CH_GATE_RANGE = 0x37;
	public final static byte CH_GATE_ATTACK = 0x39;
	public final static byte CH_GATE_RELEASE = 0x3B;
	public final static byte CH_GATE_HOLD = 0x3D;

	public final static byte CH_COMP_SWITCH = 0x40;
	public final static byte CH_COMP_KEY_IN = 0x41;
	public final static byte CH_COMP_THRESHOLD = 0x42;
	public final static byte CH_COMP_RATIO = 0x44;
	public final static byte CH_COMP_KNEE = 0x45;
	public final static byte CH_COMP_ATTACK = 0x46;
	public final static byte CH_COMP_RELEASE = 0x48;
	public final static byte CH_COMP_GAIN = 0x4A;
	public final static byte CH_COMP_AUTOGAIN = 0x4C;

	// input peq parameter
	public final static byte CH_EQ_SWITCH = 0x50; //eq on/off
	public final static byte CH_EQ_ATT = 0x51; //attenuator (in case of overloading)
	public final static byte CH_EQ_LO_GAIN = 0x53;
	public final static byte CH_EQ_LO_FREQ = 0x55;
	public final static byte CH_EQ_LOMID_GAIN = 0x58;
	public final static byte CH_EQ_LOMID_FREQ = 0x5A;
	public final static byte CH_EQ_LOMID_Q = 0x5D;
	public final static byte CH_EQ_HIMID_GAIN = 0x5F;
	public final static byte CH_EQ_HIMID_FREQ = 0x61;
	public final static byte CH_EQ_HIMID_Q = 0x64;
	public final static byte CH_EQ_HI_GAIN = 0x66;
	public final static byte CH_EQ_HI_FREQ = 0x68;

	public final static int PEQ_BAND1 = 0;
	public final static int PEQ_BAND2 = 1;
	public final static int PEQ_BAND3 = 2;
	public final static int PEQ_BAND4 = 3;

	// input aux parameter
	// for further auxiliaries use: (8*aux-channel) + parameter-offset
	// e.g.: send-level for ch 5  : (8*4) + 2 = 34 (bzw. 0x22)
	public final static byte CH_AUX_SEND_SWITCH = 0x00;
	public final static byte CH_AUX_SEND_POSITION = 0x01;
	public final static byte CH_AUX_SEND_LEVEL = 0x02;
	public final static byte CH_AUX_SEND_PAN = 0x04;
	public final static byte CH_AUX_SEND_PAN_LINK = 0x05;

	// auxiliary channel section (3rd byte)
	public final static byte AUX_SECTION_GENERAL = 0x00;
	public final static byte AUX_SECTION_SEND = 0x02;

	// auxiliary parameter (4th byte)
	public final static byte AUX_FADER_LEVEL = 0x0E;

	public final static byte DCA_FADER_LEVEL = 0x0A;

	// main channel address 2nd byte
	public final static byte MAIN_CH_LEFT = 0x00;
	public final static byte MAIN_CH_RIGHT = 0x01;
	public final static byte MAIN_CH_CENTER = 0x02;
	// main channel address 3rd byte
	public final static byte MAIN_SECTION_GENERAL = 0x00;
	public final static byte MAIN_SECTION_AUX_SEND = 0x01;
	public final static byte MAIN_SECTION_MATRIX_SEND = 0x02;

	// main channel address 4th byte - parameter
	public final static byte MAIN_CH_LEVEL = 0x0E;

	// geq sections (3rd address byte)
	public final static byte GEQ_SECTION_GENERAL = 0x00;
	public final static byte GEQ_SECTION_MODE = 0x01;
	public final static byte GEQ_SECTION_PEQ = 0x02;
	public final static byte GEQ_SECTION_ANALYSER = 0x10;

	// 31 band geq
	public final static byte GEQ_BAND1 = 0x22;
	public final static byte GEQ_BAND31 = 0x5E;


}