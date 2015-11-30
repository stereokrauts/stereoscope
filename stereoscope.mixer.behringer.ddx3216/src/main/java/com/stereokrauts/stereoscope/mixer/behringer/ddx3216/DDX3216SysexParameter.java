/**
 * 
 */
package com.stereokrauts.stereoscope.mixer.behringer.ddx3216;

/**
 * @author jansen
 *
 */
public class DDX3216SysexParameter {


	/**
	 * these are constants which are named according
	 * to the ddx3216-sysex tables.
	 * the comment shows the according OSC-Name
	 */
	public static final byte SECTION_MASTER = 64;

	public static final byte SECTION_AUX1_MASTER = 48; 
	public static final byte SECTION_AUX2_MASTER = 49; 
	public static final byte SECTION_AUX3_MASTER = 50; 
	public static final byte SECTION_AUX4_MASTER = 51; 

	// channel parameters, level and mute are generic to all sections
	public static final byte PARAM_LEVEL = 1;
	public static final byte PARAM_MUTE = 2;
	public static final byte PARAM_PAN = 3;
	public static final byte PARAM_AUX1_LEVEL = 70;
	public static final byte PARAM_AUX2_LEVEL = 72;
	public static final byte PARAM_AUX3_LEVEL = 74;
	public static final byte PARAM_AUX4_LEVEL = 76;

	public static final byte PARAM_BUS_LEVEL = 6;


	// parametric eq generic parameter
	public static final byte PARAM_PEQ_EQON = 20; // equalizer on/off
	public static final byte PARAM_PEQ_BAND4_TYPE = 21; //0=Param/1=HC/2=HSh
	public static final byte PARAM_PEQ_BAND4_F = 22;
	public static final byte PARAM_PEQ_BAND4_G = 23;
	public static final byte PARAM_PEQ_BAND4_Q = 24;
	public static final byte PARAM_PEQ_BAND3_F = 26;
	public static final byte PARAM_PEQ_BAND3_G = 27;
	public static final byte PARAM_PEQ_BAND3_Q = 28;
	public static final byte PARAM_PEQ_BAND2_F = 30;
	public static final byte PARAM_PEQ_BAND2_G = 31;
	public static final byte PARAM_PEQ_BAND2_Q = 32;
	public static final byte PARAM_PEQ_BAND1_TYPE = 33;
	public static final byte PARAM_PEQ_BAND1_F = 34;
	public static final byte PARAM_PEQ_BAND1_G = 35;
	public static final byte PARAM_PEQ_BAND1_Q = 36;
	public static final byte PARAM_PEQ_HPF_ON = 37;
	public static final byte PARAM_PEQ_HPF_F = 38;

	public static final int PEQ_BAND1 = 0;
	public static final int PEQ_BAND2 = 1;
	public static final int PEQ_BAND3 = 2;
	public static final int PEQ_BAND4 = 3;
}
