/**
 * 
 */
package com.stereokrauts.stereoscope.mixer.behringer.ddx3216;

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
public class DDX3216LabelDataProvider {
	
	//private static final SLogger logger = StereoscopeLogManager.getLogger("yamaha-generic-midsize");
	
	private String[] labelLevelTable;
	private String[] labelPeqQTable;
	private String[] labelPeqFTable;
	private String[] labelPeqGTable;

	
	public DDX3216LabelDataProvider() {

		this.setLabelLevelTable();
		this.setLabelPeqFTable();
		this.setLabelPeqQTable();
		this.setLabelPeqGTable();
	}
		
	
	private void setLabelLevelTable() {
		final DecimalFormat df = new DecimalFormat("0.00");
		this.labelLevelTable = new String[1473];
		
		for (int i = 0; i <= 1472; i++) {
			final float intValue = (float) (i) / 16 - 80;
			this.labelLevelTable[i] = df.format(intValue) + "dB";
		}
	}

	private void setLabelPeqQTable() {
		final DecimalFormat df = new DecimalFormat("0.00");
		this.labelPeqQTable = new String[41];
		
		for (int i = 0; i <= 40; i++) {
			final double intValue = 0.1 * Math.pow(100, (i)/40d);
			this.labelPeqQTable[i] = df.format(intValue);
		}
	}

	private void setLabelPeqFTable() {
		final DecimalFormat df = new DecimalFormat("0.00");
		this.labelPeqFTable = new String[160];
		
		for (int i = 0; i <= 159; i++) {
			final double intValue = 20 * Math.pow(1000, (i)/ 159d);
			this.labelPeqFTable[i] = df.format(intValue) + "Hz";
		}
	}
	
	private void setLabelPeqGTable() {
		final DecimalFormat df = new DecimalFormat();
		this.labelPeqGTable = new String[73];
		
		for (int i = 0; i <= 72; i++) {
			final float intValue = (float) (i)/2 -18;
			this.labelPeqGTable[i] = df.format(intValue) + "dB";
		}
	}


	/* PUBLIC METHODS THAT RETURN LABELS */
	
	public final String getLabelLevel(final float value) {
		return this.labelLevelTable[((int) (value * 1472))];
	}
	
	public final String getLabelPan(final float value) {
		final int intValue = (int) (value * 30);
		if (intValue == 0) {
			return "Center";
		} else if (intValue < 0) {
			return "L" + -intValue;
		} else {
			return "R" + intValue;
		}
	}
	
	public final String getLabelPeqQ(final float value) {
		return this.labelPeqQTable[((int) (value * 40))];
	}
	
	public final String getLabelPeqF(final float value) {
		return this.labelPeqFTable[(int) (value * 159)];
	}
	
	public final String getLabelPeqG(final float value) {
		final float intValue = value / 2 + 0.5f;
		return this.labelPeqGTable[(int) (intValue * 72)];
	}

}	
		
