/**
 * 
 */
package com.stereokrauts.stereoscope.mixer.roland.m380;

import java.text.DecimalFormat;

/**
 * This class provides the label data that
 * can be used to send the actual value
 * of a mixer element to the surface element's
 * label.
 *  
 * @author Roland Jansen "jansen@stereokrauts.com"
 *
 */
public class M380LabelDataProvider {
	
	//private static final SLogger logger = StereoscopeLogManager.getLogger("roland-m380");

	// arrays with single data values
	private final byte[][] labelChannelNameTable = new byte[48][6];    //6 chars on 48 channels
	private final String[] labelLevelTable = new String[1007];         //fader values (-Inf,-90.5,,,+10.0dB)
	private final String[] labelPanningTable = new String[127];        //panning values (L63,,,R63)
	private final String[] labelAttackTable = new String[8001];        //attack values (0.0,,,800.0ms)
	private final String[] labelGateRangeTable = new String[907];      //gate range (-Inf,-90.5,,,0.0dB)
	private final String[] labelGateThresholdTable = new String[801];  //gate threshold (-80.0,,,0.0dB)
	private final String[] labelCompThresholdTable = new String[401];  //compressor threshold (-40.0,,,0.0dB)
	private final String[] labelCompressorGainTable = new String[801]; //dynamics (comp) gain (-40.0,,,+40.0dB (a.gain=off)
	private final String[] labelRatioTable = new String[14];           //dynamics ratio (1:1 - inf:1)
	private final String[] labelKneeTable = new String[10];            //dynamics knee (hard - soft9)
	private final String[] labelPeqGain = new String[301];             //parametric eq gain (-15.0,,,+15.0dB)
	private final String[] labelPeqQuality = new String[1565];         //parametric eq q (0.36,,,16.00)
	private final String[] labelPeqFreq = new String[20001];           //parametric eq frequency (20Hz,,,20000Hz)
	
	public M380LabelDataProvider(final int samplerate) {

		this.setFaderLevelTable();
		this.setPanningLabelTable();
		this.setThresholdTable("gate");
		this.setThresholdTable("comp");
		this.setAttackLabelTable();
		this.setGateRangeTable();
		this.setCompressorGainTable();
		this.setDynamicsRatioTable();
		this.setPeqGainTable();
		this.setPeqQualityTable();
		this.setPeqFrequencyTable();
		this.setDynamicsKneeTable();
		
	}
		
	
	public M380LabelDataProvider() {
		this(0);
	}
	
	public String getChannelNameLabel(final int channel) {
		final String channelName =
				String.valueOf((char) this.labelChannelNameTable[channel][0])
				+ String.valueOf((char) this.labelChannelNameTable[channel][1])
				+ String.valueOf((char) this.labelChannelNameTable[channel][2])
				+ String.valueOf((char) this.labelChannelNameTable[channel][3]);
		return channelName;
	}


	public String getFaderLevelLabel(final float value) {
		return this.labelLevelTable[((int) (value * 1006))];
	}

	public String getPanningLabel(final float value) {
		return this.labelPanningTable[(int) (value * 63) + 63];
	}
	
	public String getAttackLabel(final float value) {
		return this.labelAttackTable[(int) (value * 8000)];
	}
	
	public String getReleaseHoldLabel(final float value) {
		return Integer.toString((int) (value * 8000)) + "ms";
	}
	
	public String getGateRangeLabel(final float value) {
		return this.labelGateRangeTable[(int) (value * 906)];
	}
	
	public String getGateThresholdLabel(final float value) {
		return this.labelGateThresholdTable[(int) (value * 800)];
	}
	
	public String getCompressorThresholdLabel(final float value) {
		return this.labelCompThresholdTable[(int) (value * 400)];
	}
	
	public String getCompressorGainLabel(final float value) {
		return this.labelCompressorGainTable[(int) (value * 800)];
	}
	
	public String getDynamicsRatioLabel(final float value) {
		return this.labelRatioTable[(int) (value * 13)];
	}
	
	public String getDynamicsKneeLabel(final float value) {
		return this.labelKneeTable[(int) (value * 9)];
	}
	
	public String getPeqGainLabel(final float value) {
		return this.labelPeqGain[(int) (value * 150) + 150];
	}
	
	public String getPeqQualityLabel(final float value) {
		return this.labelPeqQuality[(int) (value * 1564)];
	}
	
	public String getPeqFrequencyTable(final int value) {
		// this uses int values because it avoids another stack of calculations
		return this.labelPeqFreq[value];
	}
	
	public void setChannelNameChar(final int channel, final byte position, final byte chNameChar) {
		this.labelChannelNameTable[channel][position] = chNameChar;
	}
	
	private void setFaderLevelTable() {
		final DecimalFormat df = new DecimalFormat("0.0");
		for (int i=-906; i<=100; i++) {
			final int normalized = i + 906;
			if (i == -906) {
				this.labelLevelTable[normalized] = "-Inf";
			} else {
				this.labelLevelTable[normalized] = df.format((float) (i + 1) / 10) + "dB";
			}
		}
		
	}

	public void setPanningLabelTable() {
		for (int i=-63; i<=63; i++) {
			final int normalized = i + 63;
			if (i < 0) {
				this.labelPanningTable[normalized] = i + "L";
			} else if (i == 0) {
				this.labelPanningTable[normalized] = Integer.toString(i);
			} else {
				this.labelPanningTable[normalized] = i + "R";
			}
		}
	}

	public void setAttackLabelTable() {
		final DecimalFormat df = new DecimalFormat("0.0");
		for (int i=0; i<=8000; i++) {
			this.labelAttackTable[i] = df.format((float) i / 10) + "ms";
		}
	}

	public void setThresholdTable(final String processor) {
		final DecimalFormat df = new DecimalFormat("0.0");
		if (processor.matches("gate")) {
			for (int i=0; i<=800; i++) {
				this.labelGateThresholdTable[i] = df.format((float) (i - 800) / 10) + "dB";
			}
		} else if (processor.matches("comp")) {
			for (int i=0; i<=400; i++) {
				this.labelCompThresholdTable[i] = df.format((float) (i - 400) / 10) + "dB";
			}
		}

	}
	
	private void setGateRangeTable() {
		//same as faders but different value range
		final DecimalFormat df = new DecimalFormat("0.0");
		for (int i=-906; i<=0; i++) {
			final int normalized = i + 906;
			if (i == -906) {
				this.labelGateRangeTable[normalized] = "-Inf";
			} else {
				this.labelGateRangeTable[normalized] = df.format((float) (i + 1) / 10) + "dB";
			}
		}
	}

	public void setCompressorGainTable() {
		final DecimalFormat df = new DecimalFormat("0.0");
		for (int i=0; i<=800; i++) {
			this.labelCompressorGainTable[i] = df.format((float) (i - 400) / 10) + "dB";
		}
	}

	public void setPeqGainTable() {
		final DecimalFormat df = new DecimalFormat("0.0");
		for (int i=0; i<=300; i++) {
			this.labelPeqGain[i] = df.format((float) (i - 150) / 10) + "dB";
		}
	}

	public void setPeqQualityTable() {
		final DecimalFormat df = new DecimalFormat("0.00");
		for (int i=0; i<=1564; i++) {
			this.labelPeqQuality[i] = df.format((float) (i + 36) / 100);
		}
	}
	
	public void setPeqFrequencyTable() {
		final DecimalFormat df = new DecimalFormat("0.00");
		for (int i=0; i<=20000; i++) {
			if (i <= 999) {
				this.labelPeqFreq[i] = Integer.toString(i) + "Hz";
			} else {
				this.labelPeqFreq[i] = df.format((float) (i) / 1000) + "kHz";
			}
		}
	}
	
	public void setDynamicsRatioTable() {
		this.labelRatioTable[0] = "1.00:1";
		this.labelRatioTable[1] = "1.12:1";
		this.labelRatioTable[2] = "1.25:1";
		this.labelRatioTable[3] = "1.40:1";
		this.labelRatioTable[4] = "1.60:1";
		this.labelRatioTable[5] = "1.80:1";
		this.labelRatioTable[6] = "2.00:1";
		this.labelRatioTable[7] = "2.50:1";
		this.labelRatioTable[8] = "3.20:1";
		this.labelRatioTable[9] = "4.00:1";
		this.labelRatioTable[10] = "5.60:1";
		this.labelRatioTable[11] = "8.00:1";
		this.labelRatioTable[12] = "16.00:1";
		this.labelRatioTable[13] = "INF:1";
	}
	
	public void setDynamicsKneeTable() {
		this.labelKneeTable[0] = "Hard";
		this.labelKneeTable[1] = "Soft1";
		this.labelKneeTable[2] = "Soft2";
		this.labelKneeTable[3] = "Soft3";
		this.labelKneeTable[4] = "Soft4";
		this.labelKneeTable[5] = "Soft5";
		this.labelKneeTable[6] = "Soft6";
		this.labelKneeTable[7] = "Soft7";
		this.labelKneeTable[8] = "Soft8";
		this.labelKneeTable[9] = "Soft9";
	}

}
