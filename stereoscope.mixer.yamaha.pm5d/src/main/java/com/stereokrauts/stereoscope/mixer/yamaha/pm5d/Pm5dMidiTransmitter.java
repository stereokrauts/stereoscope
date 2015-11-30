package com.stereokrauts.stereoscope.mixer.yamaha.pm5d;



import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.lib.midi.util.FormatStringSysex;

final class Pm5dMidiTransmitter {
	@SuppressWarnings("unused")
	private static final SLogger LOGGER = StereoscopeLogManager.getLogger("yamaha-pm5d");

	private ISendMidi midi;

	private final Pm5dMixer mixer;


	Pm5dMidiTransmitter(final Pm5dMixer pm5dMixer, final ISendMidi midi) {
		this.mixer = pm5dMixer;
		this.midi = midi;
	}

	public void changeParameter(final byte element, final byte parameter,
			final byte channel, final long data) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 10 3e   0F 01 {b}  {b} {b}   {l5}   F7");
		final Object[] par = { new Byte(element),
				new Byte(parameter),
				new Byte((byte) (channel + 1)), /* PM5D starts numbering at 1 */
				new Long(data)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	public void requestParameter(final byte element, final byte parameter,
			final byte channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e   0F 01 {b}  {b} {b}   F7");
		final Object[] par = { new Byte(element),
				new Byte(parameter),
				new Byte((byte) (channel + 1)), /* PM5D starts numbering at 1 */
		};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	public void requestSetupParameter(final byte element, final byte parameter,
			final byte channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e   0F 03 {b}  {b} {b}   F7");
		final Object[] par = { new Byte(element),
				new Byte(parameter),
				new Byte((byte) (channel + 1)), /* PM5D starts numbering at 1 */
		};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	public void requestChannelName(final byte channel) {
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 43 30 3e   0F 06 08 01 {b} F7");
		final Object[] par = { new Byte((byte) (channel + 1)) /* PM5D starts numbering at 1 */ };
		this.midi.sendSysexData(fss.fillInMessage(par));
	}
}
