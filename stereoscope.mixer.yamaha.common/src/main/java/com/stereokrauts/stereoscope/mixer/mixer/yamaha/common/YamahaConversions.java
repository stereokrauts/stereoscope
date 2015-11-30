package com.stereokrauts.stereoscope.mixer.mixer.yamaha.common;

public final class YamahaConversions {
	private static final float YAMAHA_FADER_STEPS = (1023);

	public static Float faderSteps(final Integer value) {
		return new Float((float) value / YAMAHA_FADER_STEPS);
	}
	
	public static Integer faderSteps(final Float value) {
		return new Integer((int) (value * YAMAHA_FADER_STEPS));
	}
}
