package com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi;

import model.mixer.interfaces.SampleRate;

public final class YamahaSampleRateAdapter {

	public static SampleRate adapt(final int sampleRateNumber) {
		switch (sampleRateNumber) {
		case 0:
			return SampleRate.R_44100;
		case 1:
			return SampleRate.R_48000;
		case 2:
			return SampleRate.R_88200;
		case 3:
			return SampleRate.R_96000;
		default:
			return SampleRate.R_UNDEFINED;
		}
	}

}
