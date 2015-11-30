package model.mixer.interfaces;

public enum SampleRate {
	R_UNDEFINED(0),
	R_44100(44100),
	R_48000(48000),
	R_88200(88200),
	R_96000(96000);

	private final int value;

	private SampleRate(final int samplesPerSecond) {
		this.value = samplesPerSecond;

	}

	public int getValue() {
		return value;
	}
}
