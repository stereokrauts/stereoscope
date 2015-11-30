package com.stereokrauts.stereoscope.mixer.yamaha.dm2000;

import java.util.logging.Level;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.GenericMidsizeMixerModifier;

public class DM2000MixerModifier extends GenericMidsizeMixerModifier {
	private static final SLogger logger = StereoscopeLogManager.getLogger("yamaha-generic-midsize-modifier");

	private static boolean DEBUG_FINEST = false;

	private final DM2000MidiTransmitter sysex;

	public DM2000MixerModifier(final DM2000Mixer mixer, final DM2000MidiTransmitter mysysex) {
		super(mixer, mysysex);
		this.sysex = mysysex;
	}

	public void changedGeqBandLevel(final short eqNumber, final int band, final float level) {
		final int value = (int) (level * DM2000MidiTransmitter.GEQ_BAND_RANGE);
		System.out.println(value);
		this.sysex.changeGeqParameter((byte) DM2000MidiTransmitter.ELMT_GRAPHICAL_EQ,
				(byte) (DM2000MidiTransmitter.PARAM_GEQ_LEFT_FIRSTBAND + band), (byte) eqNumber, value);

		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET GEQ_BAND_LEVEL Eq-no=" + eqNumber + ", band=" + band + ", value=" + value);
		}
	}

	public void changedGeqFullReset(final short eqNumber) {
		for (int i = 0; i < 31; i++) {
			this.changedGeqBandLevel(eqNumber, i, 0.0f);
		}
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "FULL RESET GEQ=" + eqNumber);
		}

	}

	public void changedGeqBandReset(final short eqNumber, final int band) {
		this.sysex.changeGeqParameter((byte) DM2000MidiTransmitter.ELMT_GRAPHICAL_EQ,
				(byte) (DM2000MidiTransmitter.PARAM_GEQ_LEFT_FIRSTBAND + band), (byte) eqNumber, 0);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "BAND RESET GEQ=" + eqNumber + ", band=" + band);
		}
	}

}
