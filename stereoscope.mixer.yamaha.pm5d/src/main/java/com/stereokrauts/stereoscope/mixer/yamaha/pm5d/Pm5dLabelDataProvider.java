/**
 * 
 */
package com.stereokrauts.stereoscope.mixer.yamaha.pm5d;

import java.text.DecimalFormat;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;

/**
 * This class provides the label data that
 * can be used to send the actual value
 * of a mixer element to the surface element's
 * label.
 *  
 * @author Roland Jansen "jansen@stereokrauts.com"
 * @author Tobias Heide "theide@stereokrauts.com"
 *
 */
public class Pm5dLabelDataProvider {

	private static final SLogger LOGGER = StereoscopeLogManager.getLogger("yamaha-pm5d-labelmaker");
	
	private int sampleRate;
	
	// Data-strings, seperated by spaces
	private String labelLevelData;
	private String labelPeqQData;
	private String labelPeqFData;
	private String labelPeqFilterTypeData;
	private String labelRatioData;
	private String labelHoldData;
	private String labelDecayReleaseData;
	// arrays with single data values
	private String[] labelLevelTable;
	private String[] labelPeqQTable;
	private String[] labelPeqFTable;
	private String[] labelPeqFilterTypeTable;
	private String[] labelRangeTable;
	private String[] labelRatioTable;
	private String[] labelHoldTable;
	private String[] labelDecayReleaseTable;
	private String[] labelThresholdTable;
	
	public Pm5dLabelDataProvider(final int samplerate) {

		this.sampleRate = samplerate;
		
		/* JUST FOR TESTING
		 * sm doesn't have a samplerate
		 */
		if (this.sampleRate == 0) {
			this.sampleRate = 44100;
			LOGGER.warning("Didn't receive the samplerate from mixer, assuming 44.1 kHz.");
		}
		
		this.setYamahaLevel10DbTable();
		this.setYamahaDynamicRatio();
		this.setYamahaPeqF();
		this.setYamahaPeqQ();
		this.setYamahaPeqFilterType();
		
		this.setLabelRatioTable(this.labelRatioData.split(" "));
		this.setLabelPeqFTable(this.labelPeqFData.split(" "));
		this.setLabelPeqQTable(this.labelPeqQData.split(" "));
		this.setLabelPeqFilterType(this.labelPeqFilterTypeData.split(" "));
		
		
		this.setLableRangeTable();
		this.setLabelThresholdTable();
		
		this.setLabelLevelTable(this.labelLevelData.split(" "));
		
		// set tables according to samplerate
		if (this.sampleRate == 44100) {
			this.setYamahaDynamicDecayRelease44kHzGenericMidsizePm5d();
			this.setYamahaDynamicHold44kHz();
		} else if (this.sampleRate == 48000) {
			this.setYamahaDynamicDecayRelease48kHzGenericMidsizePm5d();
			this.setYamahaDynamicHold48kHzGenericMidsizePm5d();
		}
			
			this.setYamahaLevel10DbTable();
			this.setLabelDecayReleaseTable(this.labelDecayReleaseData.split(" "));
			this.setLabelHoldTable(this.labelHoldData.split(" "));
			this.setLabelLevelTable(this.labelLevelData.split(" "));
			
		
	}
		
	
	public Pm5dLabelDataProvider() {
		this(0);
	}


	private void setLabelLevelTable(final String[] labelLevelTable) {
		this.labelLevelTable = labelLevelTable;
	}


	private void setLabelPeqQTable(final String[] labelPeqQTable) {
		this.labelPeqQTable = labelPeqQTable;
	}


	private void setLabelPeqFTable(final String[] labelPeqFTable) {
		this.labelPeqFTable = labelPeqFTable;
	}

	private void setLabelPeqFilterType(final String[] labelPeqFilterTypeTable) {
		this.labelPeqFilterTypeTable = labelPeqFilterTypeTable;
	}

	private void setLabelRatioTable(final String[] labelRatioTable) {
		this.labelRatioTable = labelRatioTable;
	}


	private void setLabelHoldTable(final String[] labelHoldTable) {
		this.labelHoldTable = labelHoldTable;
	}


	public final void setLabelDecayReleaseTable(final String[] labelDecayReleaseTable) {
		this.labelDecayReleaseTable = labelDecayReleaseTable;
	}

	public final void setLableRangeTable() {
		this.labelRangeTable = new String[Pm5dSysexParameter.DYNAMICS_GATE_RANGE_VALUECOUNT];
		this.labelRangeTable[0] = "-inf";
		for (int i = 1; i < this.labelRangeTable.length; i++) {
			this.labelRangeTable[i] = (i - Pm5dSysexParameter.DYNAMICS_GATE_RANGE_VALUECOUNT + 1) + "dB";
		}
	}
	
	public final void setLabelThresholdTable() {
		this.labelThresholdTable = new String[Pm5dSysexParameter.DYNAMICS_GATE_THRESHOLD_VALUECOUNT];
		for (int i = 0; i < this.labelThresholdTable.length; i++) {
			final DecimalFormat df = new DecimalFormat("0.0");
			final String value = df.format(i / 10.0);
			this.labelThresholdTable[i] = (i == 0 ? "" : "-") + value + "dB";
		}
	}
	

	/** PUBLIC METHODS THAT RETURN LABELS FROM TABLES **/
	
	public final String getYamahaLabelLevel10Db(final float value) {
		return this.labelLevelTable[((int) (value * 1023))];
	}
	
	public final String getYamahaLabelPeqQ(final int value) {
		return this.labelPeqQTable[value + 4];
	}
	
	public final String getYamahaLabelPeqF(final int value) {
		return this.labelPeqFTable[value];
	}
	
	public final String getYamahaLabelFilterType(final int value) {
		return this.labelPeqFilterTypeTable[value];
	}
	
	public final String getYamahaLabelDynaRange(final float value) {
		return this.labelRangeTable[(int) (value * (Pm5dSysexParameter.DYNAMICS_GATE_RANGE_VALUECOUNT - 1))];
	}
	
	public final String getYamahaLabelDynaRatio(final float value) {
		return this.labelRatioTable[(int) (value * 15)];
	}

	public final String getYamahaLabelDynaType(final int proc, final float value) {
		String label = "";
		
		if (proc == 1) {
			if (value == 0) {
				label = "Dynamics1: Gate";
			} else {
				label = "Dynamics1: Ducking";
			}
		} else if (proc == 2) {
			final int type = (int) (value * 3);
			if (type == 0) {
				label = "Dynamics2: Compressor";
			} else if (type == 1) {
				label = "Dynamics2: Expander";
			} else if (type == 2) {
				label = "Dynamics2: Compander H";
			} else if (type == 3) {
				label = "Dynamics2: Compander S";
			}
		}
		
		return label;
	}
	
	public final String getYamahaLabelDynaHold(final float value) {
		return this.labelHoldTable[(int) (value * 215)];
	}
	
	public final String getYamahaLabelDynaDecayRelease(final float value) {
		return this.labelDecayReleaseTable[(int) (value * 159)];
	}
	

	public final String getYamahaLabelDynaThreshold(final float value) {
		return this.labelThresholdTable[(int) ((1.0-value) * (Pm5dSysexParameter.DYNAMICS_GATE_THRESHOLD_VALUECOUNT-1))];
	}
	
	/** PUBLIC METHODS THAT RETURN CALCULATED LABELS **/
	
	public final String getYamahaLabelPanning(final float value) {
		final int intValue = (int) (value * 63);
		if (intValue == 0) {
			return "Center";
		} else if (intValue < 0) {
			return "L" + -intValue;
		} else {
			return "R" + intValue;
		}
	}
	
	public final String getYamahaLabelPeqG(final float value) {
		final DecimalFormat df = new DecimalFormat("0.0");
		return df.format(value * 18) + "dB";
	}
	
	public final String getYamahaLabelDynaAttack(final float value) {
		final int intValue = (int) (value * 120);
		return intValue + "ms";
	}
	
	public final String getYamahaLabelDynaGain(final float value) {
		final float intValue = value * 18;
		final DecimalFormat df = new DecimalFormat("0.0");
		return df.format(intValue) + "dB";
	}
	
	public final String getYamahaLabelDynaKnee(final float value) {
		// Type Compander acts different, not handled yet
		final int intValue = (int) (value * 5);
		if (intValue == 0) {
			return "HARD";
		} else {
			return intValue + "";
		}
	}
	
		
	/** DATA TABLES **/
	
	private void setYamahaLevel10DbTable() {
		this.labelLevelData = "-InfdB -138.00dB -135.00dB -132.00dB -129.00dB -126.00dB "
				+ "-123.00dB -120.00dB -117.00dB -114.00dB -111.00dB -108.00dB -105.00dB -102.00dB -99.00dB "
				+ "-96.00dB -95.00dB -94.00dB -93.00dB -92.00dB -91.00dB -90.00dB -89.00dB -88.00dB -87.00dB "
				+ "-86.00dB -85.00dB -84.00dB -83.00dB -82.00dB -81.00dB -80.00dB -79.00dB -78.00dB -77.80dB "
				+ "-77.60dB -77.40dB -77.20dB -77.00dB -76.80dB -76.60dB -76.40dB -76.20dB -76.00dB -75.80dB "
				+ "-75.60dB -75.40dB -75.20dB -75.00dB -74.80dB -74.60dB -74.40dB -74.20dB -74.00dB -73.80dB "
				+ "-73.60dB -73.40dB -73.20dB -73.00dB -72.80dB -72.60dB -72.40dB -72.20dB -72.00dB -71.80dB "
				+ "-71.60dB -71.40dB -71.20dB -71.00dB -70.80dB -70.60dB -70.40dB -70.20dB -70.00dB -69.80dB "
				+ "-69.60dB -69.40dB -69.20dB -69.00dB -68.80dB -68.60dB -68.40dB -68.20dB -68.00dB -67.80dB "
				+ "-67.60dB -67.40dB -67.20dB -67.00dB -66.80dB -66.60dB -66.40dB -66.20dB -66.00dB -65.80dB "
				+ "-65.60dB -65.40dB -65.20dB -65.00dB -64.80dB -64.60dB -64.40dB -64.20dB -64.00dB -63.80dB "
				+ "-63.60dB -63.40dB -63.20dB -63.00dB -62.80dB -62.60dB -62.40dB -62.20dB -62.00dB -61.80dB "
				+ "-61.60dB -61.40dB -61.20dB -61.00dB -60.80dB -60.60dB -60.40dB -60.20dB -60.00dB -59.80dB "
				+ "-59.60dB -59.40dB -59.20dB -59.00dB -58.80dB -58.60dB -58.40dB -58.20dB -58.00dB -57.80dB "
				+ "-57.60dB -57.40dB -57.20dB -57.00dB -56.80dB -56.60dB -56.40dB -56.20dB -56.00dB -55.80dB "
				+ "-55.60dB -55.40dB -55.20dB -55.00dB -54.80dB -54.60dB -54.40dB -54.20dB -54.00dB -53.80dB "
				+ "-53.60dB -53.40dB -53.20dB -53.00dB -52.80dB -52.60dB -52.40dB -52.20dB -52.00dB -51.80dB "
				+ "-51.60dB -51.40dB -51.20dB -51.00dB -50.80dB -50.60dB -50.40dB -50.20dB -50.00dB -49.80dB "
				+ "-49.60dB -49.40dB -49.20dB -49.00dB -48.80dB -48.60dB -48.40dB -48.20dB -48.00dB -47.80dB "
				+ "-47.60dB -47.40dB -47.20dB -47.00dB -46.80dB -46.60dB -46.40dB -46.20dB -46.00dB -45.80dB "
				+ "-45.60dB -45.40dB -45.20dB -45.00dB -44.80dB -44.60dB -44.40dB -44.20dB -44.00dB -43.80dB "
				+ "-43.60dB -43.40dB -43.20dB -43.00dB -42.80dB -42.60dB -42.40dB -42.20dB -42.00dB -41.80dB "
				+ "-41.60dB -41.40dB -41.20dB -41.00dB -40.80dB -40.60dB -40.40dB -40.20dB -40.00dB -39.90dB "
				+ "-39.80dB -39.70dB -39.60dB -39.50dB -39.40dB -39.30dB -39.20dB -39.10dB -39.00dB -38.90dB "
				+ "-38.80dB -38.70dB -38.60dB -38.50dB -38.40dB -38.30dB -38.20dB -38.10dB -38.00dB -37.90dB "
				+ "-37.80dB -37.70dB -37.60dB -37.50dB -37.40dB -37.30dB -37.20dB -37.10dB -37.00dB -36.90dB "
				+ "-36.80dB -36.70dB -36.60dB -36.50dB -36.40dB -36.30dB -36.20dB -36.10dB -36.00dB -35.90dB "
				+ "-35.80dB -35.70dB -35.60dB -35.50dB -35.40dB -35.30dB -35.20dB -35.10dB -35.00dB -34.90dB "
				+ "-34.80dB -34.70dB -34.60dB -34.50dB -34.40dB -34.30dB -34.20dB -34.10dB -34.00dB -33.90dB "
				+ "-33.80dB -33.70dB -33.60dB -33.50dB -33.40dB -33.30dB -33.20dB -33.10dB -33.00dB -32.90dB "
				+ "-32.80dB -32.70dB -32.60dB -32.50dB -32.40dB -32.30dB -32.20dB -32.10dB -32.00dB -31.90dB "
				+ "-31.80dB -31.70dB -31.60dB -31.50dB -31.40dB -31.30dB -31.20dB -31.10dB -31.00dB -30.90dB "
				+ "-30.80dB -30.70dB -30.60dB -30.50dB -30.40dB -30.30dB -30.20dB -30.10dB -30.00dB -29.90dB "
				+ "-29.80dB -29.70dB -29.60dB -29.50dB -29.40dB -29.30dB -29.20dB -29.10dB -29.00dB -28.90dB "
				+ "-28.80dB -28.70dB -28.60dB -28.50dB -28.40dB -28.30dB -28.20dB -28.10dB -28.00dB -27.90dB "
				+ "-27.80dB -27.70dB -27.60dB -27.50dB -27.40dB -27.30dB -27.20dB -27.10dB -27.00dB -26.90dB "
				+ "-26.80dB -26.70dB -26.60dB -26.50dB -26.40dB -26.30dB -26.20dB -26.10dB -26.00dB -25.90dB "
				+ "-25.80dB -25.70dB -25.60dB -25.50dB -25.40dB -25.30dB -25.20dB -25.10dB -25.00dB -24.90dB "
				+ "-24.80dB -24.70dB -24.60dB -24.50dB -24.40dB -24.30dB -24.20dB -24.10dB -24.00dB -23.90dB "
				+ "-23.80dB -23.70dB -23.60dB -23.50dB -23.40dB -23.30dB -23.20dB -23.10dB -23.00dB -22.90dB "
				+ "-22.80dB -22.70dB -22.60dB -22.50dB -22.40dB -22.30dB -22.20dB -22.10dB -22.00dB -21.90dB "
				+ "-21.80dB -21.70dB -21.60dB -21.50dB -21.40dB -21.30dB -21.20dB -21.10dB -21.00dB -20.90dB "
				+ "-20.80dB -20.70dB -20.60dB -20.50dB -20.40dB -20.30dB -20.20dB -20.10dB -20.00dB -19.95dB "
				+ "-19.90dB -19.85dB -19.80dB -19.75dB -19.70dB -19.65dB -19.60dB -19.55dB -19.50dB -19.45dB "
				+ "-19.40dB -19.35dB -19.30dB -19.25dB -19.20dB -19.15dB -19.10dB -19.05dB -19.00dB -18.95dB "
				+ "-18.90dB -18.85dB -18.80dB -18.75dB -18.70dB -18.65dB -18.60dB -18.55dB -18.50dB -18.45dB "
				+ "-18.40dB -18.35dB -18.30dB -18.25dB -18.20dB -18.15dB -18.10dB -18.05dB -18.00dB -17.95dB "
				+ "-17.90dB -17.85dB -17.80dB -17.75dB -17.70dB -17.65dB -17.60dB -17.55dB -17.50dB -17.45dB "
				+ "-17.40dB -17.35dB -17.30dB -17.25dB -17.20dB -17.15dB -17.10dB -17.05dB -17.00dB -16.95dB "
				+ "-16.90dB -16.85dB -16.80dB -16.75dB -16.70dB -16.65dB -16.60dB -16.55dB -16.50dB -16.45dB "
				+ "-16.40dB -16.35dB -16.30dB -16.25dB -16.20dB -16.15dB -16.10dB -16.05dB -16.00dB -15.95dB "
				+ "-15.90dB -15.85dB -15.80dB -15.75dB -15.70dB -15.65dB -15.60dB -15.55dB -15.50dB -15.45dB "
				+ "-15.40dB -15.35dB -15.30dB -15.25dB -15.20dB -15.15dB -15.10dB -15.05dB -15.00dB -14.95dB "
				+ "-14.90dB -14.85dB -14.80dB -14.75dB -14.70dB -14.65dB -14.60dB -14.55dB -14.50dB -14.45dB "
				+ "-14.40dB -14.35dB -14.30dB -14.25dB -14.20dB -14.15dB -14.10dB -14.05dB -14.00dB -13.95dB "
				+ "-13.90dB -13.85dB -13.80dB -13.75dB -13.70dB -13.65dB -13.60dB -13.55dB -13.50dB -13.45dB "
				+ "-13.40dB -13.35dB -13.30dB -13.25dB -13.20dB -13.15dB -13.10dB -13.05dB -13.00dB -12.95dB "
				+ "-12.90dB -12.85dB -12.80dB -12.75dB -12.70dB -12.65dB -12.60dB -12.55dB -12.50dB -12.45dB "
				+ "-12.40dB -12.35dB -12.30dB -12.25dB -12.20dB -12.15dB -12.10dB -12.05dB -12.00dB -11.95dB "
				+ "-11.90dB -11.85dB -11.80dB -11.75dB -11.70dB -11.65dB -11.60dB -11.55dB -11.50dB -11.45dB "
				+ "-11.40dB -11.35dB -11.30dB -11.25dB -11.20dB -11.15dB -11.10dB -11.05dB -11.00dB -10.95dB "
				+ "-10.90dB -10.85dB -10.80dB -10.75dB -10.70dB -10.65dB -10.60dB -10.55dB -10.50dB -10.45dB "
				+ "-10.40dB -10.35dB -10.30dB -10.25dB -10.20dB -10.15dB -10.10dB -10.05dB -10.00dB -9.95dB "
				+ "-9.90dB -9.85dB -9.80dB -9.75dB -9.70dB -9.65dB -9.60dB -9.55dB -9.50dB -9.45dB -9.40dB "
				+ "-9.35dB -9.30dB -9.25dB -9.20dB -9.15dB -9.10dB -9.05dB -9.00dB -8.95dB -8.90dB -8.85dB "
				+ "-8.80dB -8.75dB -8.70dB -8.65dB -8.60dB -8.55dB -8.50dB -8.45dB -8.40dB -8.35dB -8.30dB "
				+ "-8.25dB -8.20dB -8.15dB -8.10dB -8.05dB -8.00dB -7.95dB -7.90dB -7.85dB -7.80dB -7.75dB "
				+ "-7.70dB -7.65dB -7.60dB -7.55dB -7.50dB -7.45dB -7.40dB -7.35dB -7.30dB -7.25dB -7.20dB "
				+ "-7.15dB -7.10dB -7.05dB -7.00dB -6.95dB -6.90dB -6.85dB -6.80dB -6.75dB -6.70dB -6.65dB "
				+ "-6.60dB -6.55dB -6.50dB -6.45dB -6.40dB -6.35dB -6.30dB -6.25dB -6.20dB -6.15dB -6.10dB "
				+ "-6.05dB -6.00dB -5.95dB -5.90dB -5.85dB -5.80dB -5.75dB -5.70dB -5.65dB -5.60dB -5.55dB "
				+ "-5.50dB -5.45dB -5.40dB -5.35dB -5.30dB -5.25dB -5.20dB -5.15dB -5.10dB -5.05dB -5.00dB "
				+ "-4.95dB -4.90dB -4.85dB -4.80dB -4.75dB -4.70dB -4.65dB -4.60dB -4.55dB -4.50dB -4.45dB "
				+ "-4.40dB -4.35dB -4.30dB -4.25dB -4.20dB -4.15dB -4.10dB -4.05dB -4.00dB -3.95dB -3.90dB "
				+ "-3.85dB -3.80dB -3.75dB -3.70dB -3.65dB -3.60dB -3.55dB -3.50dB -3.45dB -3.40dB -3.35dB "
				+ "-3.30dB -3.25dB -3.20dB -3.15dB -3.10dB -3.05dB -3.00dB -2.95dB -2.90dB -2.85dB -2.80dB "
				+ "-2.75dB -2.70dB -2.65dB -2.60dB -2.55dB -2.50dB -2.45dB -2.40dB -2.35dB -2.30dB -2.25dB "
				+ "-2.20dB -2.15dB -2.10dB -2.05dB -2.00dB -1.95dB -1.90dB -1.85dB -1.80dB -1.75dB -1.70dB "
				+ "-1.65dB -1.60dB -1.55dB -1.50dB -1.45dB -1.40dB -1.35dB -1.30dB -1.25dB -1.20dB -1.15dB "
				+ "-1.10dB -1.05dB -1.00dB -0.95dB -0.90dB -0.85dB -0.80dB -0.75dB -0.70dB -0.65dB -0.60dB "
				+ "-0.55dB -0.50dB -0.45dB -0.40dB -0.35dB -0.30dB -0.25dB -0.20dB -0.15dB -0.10dB -0.05dB "
				+ "0.00dB 0.05dB 0.10dB 0.15dB 0.20dB 0.25dB 0.30dB 0.35dB 0.40dB 0.45dB 0.50dB 0.55dB 0.60dB "
				+ "0.65dB 0.70dB 0.75dB 0.80dB 0.85dB 0.90dB 0.95dB 1.00dB 1.05dB 1.10dB 1.15dB 1.20dB 1.25dB "
				+ "1.30dB 1.35dB 1.40dB 1.45dB 1.50dB 1.55dB 1.60dB 1.65dB 1.70dB 1.75dB 1.80dB 1.85dB 1.90dB "
				+ "1.95dB 2.00dB 2.05dB 2.10dB 2.15dB 2.20dB 2.25dB 2.30dB 2.35dB 2.40dB 2.45dB 2.50dB 2.55dB "
				+ "2.60dB 2.65dB 2.70dB 2.75dB 2.80dB 2.85dB 2.90dB 2.95dB 3.00dB 3.05dB 3.10dB 3.15dB 3.20dB "
				+ "3.25dB 3.30dB 3.35dB 3.40dB 3.45dB 3.50dB 3.55dB 3.60dB 3.65dB 3.70dB 3.75dB 3.80dB 3.85dB "
				+ "3.90dB 3.95dB 4.00dB 4.05dB 4.10dB 4.15dB 4.20dB 4.25dB 4.30dB 4.35dB 4.40dB 4.45dB 4.50dB "
				+ "4.55dB 4.60dB 4.65dB 4.70dB 4.75dB 4.80dB 4.85dB 4.90dB 4.95dB 5.00dB 5.05dB 5.10dB 5.15dB "
				+ "5.20dB 5.25dB 5.30dB 5.35dB 5.40dB 5.45dB 5.50dB 5.55dB 5.60dB 5.65dB 5.70dB 5.75dB 5.80dB "
				+ "5.85dB 5.90dB 5.95dB 6.00dB 6.05dB 6.10dB 6.15dB 6.20dB 6.25dB 6.30dB 6.35dB 6.40dB 6.45dB "
				+ "6.50dB 6.55dB 6.60dB 6.65dB 6.70dB 6.75dB 6.80dB 6.85dB 6.90dB 6.95dB 7.00dB 7.05dB 7.10dB "
				+ "7.15dB 7.20dB 7.25dB 7.30dB 7.35dB 7.40dB 7.45dB 7.50dB 7.55dB 7.60dB 7.65dB 7.70dB 7.75dB "
				+ "7.80dB 7.85dB 7.90dB 7.95dB 8.00dB 8.05dB 8.10dB 8.15dB 8.20dB 8.25dB 8.30dB 8.35dB 8.40dB "
				+ "8.45dB 8.50dB 8.55dB 8.60dB 8.65dB 8.70dB 8.75dB 8.80dB 8.85dB 8.90dB 8.95dB 9.00dB 9.05dB "
				+ "9.10dB 9.15dB 9.20dB 9.25dB 9.30dB 9.35dB 9.40dB 9.45dB 9.50dB 9.55dB 9.60dB 9.65dB 9.70dB "
				+ "9.75dB 9.80dB 9.85dB 9.90dB 9.95dB 10.00dB";
	}

	private void setYamahaPeqQ() {
		this.labelPeqQData = "16.0 14.0 12.5 11.0 10.0 9.0 8.0 7.0 6.3 5.6 5.0 4.5 4.0 3.5 3.2 2.8 2.5 2.2 2.0 1.8 1.6 1.4 1.2 "
				+ "1.1 1.0 0.90 0.80 0.70 0.63 0.56 0.50 0,45 0.40 0.35 0.32 0.28 0.25 0.22 0.20 0.18 0.16 "
				+ "0.14 0.12 0.11 0.10";
	}
	
	private void setYamahaPeqFilterType() {
		this.labelPeqFilterTypeData = "lowShelf hiShelf LPF HPF";
	}
	
	private void setYamahaPeqF() {
		this.labelPeqFData = "16.0Hz 17.0Hz 18.0Hz 19.0Hz 20.0Hz 21.2Hz 22.4Hz 23.6Hz 25.0Hz 26.5Hz 28.0Hz "
				+ "30.0Hz 31.5Hz 33.5Hz 35.5Hz 37.5Hz 40.0Hz 42.5Hz 45.0Hz 47.5Hz 50.0Hz 53.0Hz 56.0Hz 60.0Hz "
				+ "63.0Hz 67.0Hz 71.0Hz 75.0Hz 80.0Hz 85.0Hz 90.0Hz 95.0Hz 100Hz 106Hz 112Hz 118Hz 125Hz 132Hz "
				+ "140Hz 150Hz 160Hz 170Hz 180Hz 190Hz 200Hz 212Hz 224Hz 236Hz 250Hz 265Hz 280Hz 300Hz 315Hz "
				+ "335Hz 355Hz 375Hz 400Hz 425Hz 450Hz 475Hz 500Hz 530Hz 560Hz 600Hz 630Hz 670Hz 710Hz 750Hz "
				+ "800Hz 850Hz 900Hz 950Hz 1.00kHz 1.06kHz 1.12kHz 1.18kHz 1.25kHz 1.32kHz 1.40kHz 1.50kHz 1.60kHz "
				+ "1.70kHz 1.80kHz 1.90kHz 2.00kHz 2.12kHz 2.24kHz 2.36kHz 2.50kHz 2.65kHz 2.80kHz 3.00kHz 3.15kHz "
				+ "3.35kHz 3.55kHz 3.75kHz 4.00kHz 4.25kHz 4.50kHz 4.75kHz 5.00kHz 5.30kHz 5.60kHz 6.00kHz 6.30kHz "
				+ "6.70kHz 7.10kHz 7.50kHz 8.00kHz 8.50kHz 9.00kHz 9.50kHz 10.0kHz 10.6kHz 11.2kHz 11.8kHz 12.5kHz "
				+ "13.2kHz 14.0kHz 15.0kHz 16.0kHz 17.0kHz 18.0kHz 19.0kHz 20.0kHz 21.2kHz 22.4kHz 23.6kHz";
	}
	
	private void setYamahaDynamicRatio() {
		this.labelRatioData = "1:1 1.1:1 1.3:1 1.5:1 1.7:1 2:1 2.5:1 3:1 3.5:1 4:1 5:1 6:1 8:1 10:1 20:1 Inf:1";
	}
	
	private void setYamahaDynamicHold44kHz() {
		this.labelHoldData = "0.02ms 0.05ms 0.07ms 0.09ms 0.11ms 0.14ms 0.16ms 0.18ms 0.20ms 0.23ms "
				+ "0.25ms 0.27ms 0.29ms 0.32ms 0.34ms 0.36ms 0.39ms 0.41ms 0.43ms 0.45ms 0.48ms 0.50ms "
				+ "0.52ms 0.54ms 0.57ms 0.59ms 0.61ms 0.63ms 0.66ms 0.68ms 0.70ms 0.73ms 0.75ms 0.79ms "
				+ "0.84ms 0.88ms 0.93ms 0.98ms 1.02ms 1.07ms 1.11ms 1.16ms 1.20ms 1.25ms 1.29ms 1.34ms "
				+ "1.38ms 1.43ms 1.47ms 1.56ms 1.66ms 1.75ms 1.84ms 1.93ms 2.02ms 2.11ms 2.20ms 2.29ms "
				+ "2.38ms 2.47ms 2.56ms 2.65ms 2.74ms 2.83ms 2.93ms 3.11ms 3.29ms 3.47ms 3.65ms 3.83ms "
				+ "4.01ms 4.20ms 4.38ms 4.56ms 4.74ms 4.92ms 5.10ms 5.28ms 5.46ms 5.65ms 5.83ms 6.19ms "
				+ "6.55ms 6.92ms 7.28ms 7.64ms 8.00ms 8.37ms 8.73ms 9.09ms 9.46ms 9.82ms 10.1ms 10.5ms "
				+ "10.9ms 11.2ms 11.6ms 12.3ms 13.0ms 13.8ms 14.5ms 15.2ms 15.9ms 16.7ms 17.4ms 18.1ms "
				+ "18.8ms 19.6ms 20.3ms 21.0ms 21.7ms 22.5ms 23.2ms 24.6ms 26.1ms 27.6ms 29.0ms 30.5ms "
				+ "31.9ms 33.4ms 34.8ms 36.3ms 37.7ms 39.2ms 40.6ms 42.1ms 43.5ms 45.0ms 46.4ms 49.3ms "
				+ "52.2ms 55.1ms 58.0ms 60.9ms 63.8ms 66.7ms 69.6ms 72.5ms 75.4ms 78.3ms 81.2ms 84.2ms "
				+ "87.1ms 90.0ms 92.9ms 98.7ms 104ms 110ms 116ms 121ms 127ms 133ms 139ms 145ms 150ms "
				+ "156ms 162ms 168ms 174ms 179ms 185ms 197ms 209ms 220ms 232ms 243ms 255ms 267ms 278ms "
				+ "290ms 301ms 313ms 325ms 336ms 348ms 359ms 371ms 394ms 417ms 441ms 464ms 487ms 510ms "
				+ "534ms 557ms 580ms 603ms 626ms 650ms 673ms 696ms 719ms 743ms 789ms 835ms 882ms 928ms "
				+ "975ms 1.02s 1.06s 1.11s 1.16s 1.20s 1.25s 1.30s 1.34s 1.39s 1.43s 1.48s 1.57s 1.67s "
				+ "1.76s 1.85s 1.95s 2.04s 2.13s";
	}
	
	private void setYamahaDynamicHold48kHzGenericMidsizePm5d() {
		this.labelHoldData = "0.02ms 0.04ms 0.06ms 0.08ms 0.10ms 0.13ms 0.15ms 0.17ms 0.19ms 0.21ms 0.23ms "
				+ "0.25ms 0.27ms 0.29ms 0.31ms 0.33ms 0.35ms 0.38ms 0.40ms 0.42ms 0.44ms 0.46ms 0.48ms 0.50ms "
				+ "0.52ms 0.54ms 0.56ms 0.58ms 0.60ms 0.63ms 0.65ms 0.67ms 0.69ms 0.73ms 0.77ms 0.81ms 0.85ms "
				+ "0.90ms 0.94ms 0.98ms 1.02ms 1.06ms 1.10ms 1.15ms 1.19ms 1.23ms 1.27ms 1.31ms 1.35ms 1.44ms "
				+ "1.52ms 1.60ms 1.69ms 1.77ms 1.85ms 1.94ms 2.02ms 2.10ms 2.19ms 2.27ms 2.35ms 2.44ms 2.52ms "
				+ "2.60ms 2.69ms 2.85ms 3.02ms 3.19ms 3.35ms 3.52ms 3.69ms 3.85ms 4.02ms 4.19ms 4.35ms 4.52ms "
				+ "4.69ms 4.85ms 5.02ms 5.19ms 5.35ms 5.69ms 6.02ms 6.35ms 6.69ms 7.02ms 7.35ms 7.69ms 8.02ms "
				+ "8.35ms 8.69ms 9.02ms 9.35ms 9.69ms 10.0ms 10.3ms 10.6ms 11.3ms 12.0ms 12.6ms 13.3ms 14.0ms "
				+ "14.6ms 15.3ms 16.0ms 16.6ms 17.3ms 18.0ms 18.6ms 19.3ms 20.0ms 20.6ms 21.3ms 22.6ms 24.0ms "
				+ "25.3ms 26.6ms 28.0ms 29.3ms 30.6ms 32.0ms 33.3ms 34.6ms 36.0ms 37.3ms 38.6ms 40.0ms 41.3ms "
				+ "42.6ms 45.3ms 48.0ms 50.6ms 53.3ms 56.0ms 58.6ms 61.3ms 64.0ms 66.6ms 69.3ms 72.0ms 74.6ms "
				+ "77.3ms 80.0ms 82.6ms 85.3ms 90.6ms 96.0ms 101ms 106ms 112ms 117ms 122ms 128ms 133ms 138ms "
				+ "144ms 149ms 154ms 160ms 165ms 170ms 181ms 192ms 202ms 213ms 224ms 234ms 245ms 256ms 266ms "
				+ "277ms 288ms 298ms 309ms 320ms 330ms 341ms 362ms 384ms 405ms 426ms 448ms 469ms 490ms 512ms "
				+ "533ms 554ms 576ms 597ms 618ms 640ms 661ms 682ms 725ms 768ms 810ms 853ms 896ms 938ms 981ms "
				+ "1.02s 1.06s 1.10s 1.15s 1.19s 1.23s 1.28s 1.32s 1.36s 1.45s 1.53s 1.62s 1.70s 1.79s 1.87s "
				+ "1.96s";
	}
	
	private void setYamahaDynamicDecayRelease44kHzGenericMidsizePm5d() {
		this.labelDecayReleaseData = "6ms 12ms 17ms 23ms 29ms 35ms 41ms 46ms 52ms 58ms 64ms 70ms 75ms 81ms "
				+ "87ms 93ms 99ms 104ms 110ms 116ms 122ms 128ms 133ms 139ms 145ms 151ms 157ms 163ms 168ms 174ms "
				+ "180ms 186ms 192ms 203ms 215ms 226ms 238ms 250ms 261ms 273ms 284m s 296m s 308ms 319ms 331ms "
				+ "342ms 354ms 366ms 377ms 400ms 424ms 447ms 470ms 493ms 517ms 540m s 563m s 586ms 609ms 633ms "
				+ "656ms 679ms 702ms 725ms 749ms 795ms 842ms 888ms 934ms 981ms 1.02s 1.07s 1.12s 1.16s 1.21s "
				+ "1.25s 1.30s 1.35s 1.39s 1.44s 1.49s 1.58s 1.67s 1.77s 1.86s 1.95s 2.04s 2.14s 2.23s 2.32s "
				+ "2.42s 2.51s 2.60s 2.69s 2.79s 2.88s 2.97s 3.16s 3.34s 3.53s 3.72s 3.90s 4.09s 4.27s 4.46s "
				+ "4.64s 4.83s 5.02s 5.20s 5.39s 5.57s 5.76s 5.94s 6.32s 6.69s 7.06s 7.43s 7.80s 8.17s 8.54s "
				+ "8.92s 9.29s 9.66s 10.0s 10.4s 10.7s 11.1s 11.5s 11.8s 12.6s 13.3s 14.1s 14.8s 15.6s 16.3s "
				+ "17.0s 17.8s 18.5s 19.3s 20.0s 20.8s 21.5s 22.2s 23.0s 23.7s 25.2s 26.7s 28.2s 29.7s 31.2s "
				+ "32.6s 34.1s 35.6s 37.1s 38.6s 40.1s 41.6s 43.0s 44.5s 46.0s";
	}
	
	private void setYamahaDynamicDecayRelease48kHzGenericMidsizePm5d() {
		this.labelDecayReleaseData = "5ms 11ms 16ms 21ms 27ms 32ms 37ms 43ms 48ms 53ms 59ms 64ms 69ms 75ms 80ms 85ms 91ms 96ms "
				+ "101ms 107ms 112ms 117ms 123ms 128ms 133ms 139ms 144ms 149ms 155ms 160ms 165ms 171ms 176ms 187ms "
				+ "197ms 208ms 219ms 229ms 240ms 251ms 261ms 272ms 283ms 293ms 304ms 315ms 325ms 336ms 347ms 368ms "
				+ "389ms 411ms 432ms 453ms 475ms 496ms 517ms 539ms 560ms 581ms 603ms 624ms 645ms 667ms 688ms 730ms "
				+ "773ms 816ms 858ms 901ms 944ms 986ms 1.02s 1.07s 1.11s 1.15s 1.20s 1.24s 1.28s 1.32s 1.37s 1.45s "
				+ "1.54s 1.62s 1.71s 1.79s 1.88s 1.96s 2.05s 2.13s 2.22s 2.30s 2.39s 2.47s 2.56s 2.65s 2.73s 2.90s "
				+ "3.07s 3.24s 3.41s 3.58s 3.75s 3.93s 4.10s 4.27s 4.44s 4.61s 4.78s 4.95s 5.12s 5.29s 5.46s 5.80s "
				+ "6.14s 6.48s 6.83s 7.17s 7.51s 7.85s 8.19s 8.53s 8.87s 9.21s 9.56s 9.90s 10.2s 10.5s 10.9s 11.6s "
				+ "12.2s 12.9s 13.6s 14.3s 15.0s 15.7s 16.3s 17.0s 17.7s 18.4s 19.1s 19.7s 20.4s 21.1s 21.8s 23.2s "
				+ "24.5s 25.9s 27.3s 28.6s 30.0s 31.4s 32.7s 34.1s 35.4s 36.8s 38.2s 39.5s 40.9s 42.3s";
	}
	

}
