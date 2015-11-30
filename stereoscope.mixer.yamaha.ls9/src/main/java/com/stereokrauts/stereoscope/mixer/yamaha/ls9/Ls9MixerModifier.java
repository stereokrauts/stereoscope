package com.stereokrauts.stereoscope.mixer.yamaha.ls9;

import java.util.logging.Level;


import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.midi.Ls9MidiTransmitter;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.midi.Ls9SysexParameter;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqFLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqGLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqQLabel;

public class Ls9MixerModifier {
	private static final SLogger logger = StereoscopeLogManager.getLogger("yamaha-ls9-modifier");

	private static final boolean DEBUG_FINEST = false;

	private final Ls9MidiTransmitter sysex;
	private final Ls9Mixer mixer;

	public Ls9MixerModifier(final Ls9Mixer mixer, final Ls9MidiTransmitter mysysex)
	{
		this.mixer = mixer;
		this.sysex = mysysex;
	}

	public final void changedAuxMaster(final int aux, final float level) {
		final short value = (short) (level * 1023);
		this.sysex.changeParameter(Ls9SysexParameter.ELMT_AUX_MASTER_LEVEL, (short) 0, (short) aux, value);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET AUXMASTER aux=" + aux + ", value=" + value);
		}
	}

	public final void changedChannelAuxLevel(final int channel, final int aux,
			final float level) {

		final short value = (short) (level * 1023);
		this.sysex.changeParameter(Ls9SysexParameter.ELMT_INPUT_AUX_SEND, (short) ((aux + 1) * 3 + 2),
				(short) channel, value);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "AUXSET channel=" + channel + ", aux=" + aux
					+ ", value=" + value);
		}
	}

	public final void changedChannelAuxOn(final int channel, final int aux,
			final boolean status) {
		final short value = status ? (short) 1 : (short) 0;
		this.sysex.changeParameter(Ls9SysexParameter.ELMT_INPUT_AUX_SEND, (short) ((aux*3) + 3),
				(short) channel, value);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "AUXSENDONSET channel=" + channel + ", aux=" + aux
					+ ", value=" + value);
		}
	}

	public final void changedChannelLevel(final int channel, final float level) {
		final short value = (short) (level * 1023);
		this.sysex.changeParameter(Ls9SysexParameter.ELMT_INPUT_LEVEL, (short) 0, (short) channel, value);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET channel=" + channel + ", value=" + value);
		}
	}	

	public final void changedChannelOnButton(final int channel, final boolean status) {
		this.sysex.changeParameter(Ls9SysexParameter.ELMT_INPUT_ON, 
				(short) 0, (short) channel, status ? (short) 1 : (short) 0);
	}

	public final void changedGeqBandLevel(final short eqNumber, final boolean rightChannel, final int band, final float level) {
		if (!rightChannel) {
			// left band selected
			this.sysex.changeParameter(Ls9SysexParameter.ELMT_GRAPHICAL_EQ,
					(short) (Ls9SysexParameter.PARAM_GEQ_LEFT_FIRSTBAND + band), 
					eqNumber, (long) (level*Ls9SysexParameter.GEQ_BAND_RANGE));
		} else {
			this.sysex.changeParameter(Ls9SysexParameter.ELMT_GRAPHICAL_EQ,
					(short) (Ls9SysexParameter.PARAM_GEQ_RIGHT_FIRSTBAND + band), 
					eqNumber, (long) (level*Ls9SysexParameter.GEQ_BAND_RANGE));
		}
	}

	public final void changedGeqBandReset(final short eqNumber, final boolean rightChannel, final int band) {
		this.changedGeqBandLevel(eqNumber, rightChannel, band, 0.0f);
	}

	public final void changedGeqFullReset(final short eqNumber) {
		final float zeroValue = 0;
		boolean channel;
		for (int i = 0; i < 62; i++) {
			if (i < 31) {
				channel = false;
				this.changedGeqBandLevel(eqNumber, channel, i, zeroValue);
			} else {
				channel = false;
				this.changedGeqBandLevel(eqNumber, channel, i, zeroValue);
			}
		}
	}

	public final void changedInputDynaAttack(final int chn, final byte element, final float value) {
		final int attack = (int) (value * 120);
		this.sysex.changeParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_ATTACK,
				(byte) chn, attack);
	}

	public final void changedInputDynaGain(final int chn, final byte element, final float value) {
		// only Compressor/Expander
		final int gain = (int) (value * 180);
		this.sysex.changeParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_GAIN,
				(byte) chn, gain);

	}

	public final void changedInputDynaHold(final int chn, final byte element, final float value) {
		// only Gate/Ducking, not used in Dyna2 (just dummy)
		final int hold = (int) (value * 215);
		this.sysex.changeParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_HOLD,
				(byte) chn, hold);

	}

	public final void changedInputDynaKeyIn(final int chn, final byte element, final float value) {
		// only Gate/Ducking, no check needed
		final int key = (int) (value * 13);
		this.sysex.changeParameter(element, 
				Ls9SysexParameter.PARAM_INPUT_DYNA_KEY_IN,
				(byte) chn, key);

	}

	public final void changedInputDynaKnee(final int chn, final byte element, final float value) {
		// only Compressor/Expander
		// knee-range in the sysex spec is 0-89 (but most likely 0-5)
		final int knee = (int) (value * 5);
		this.sysex.changeParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_KNEE,
				(byte) chn, knee);

	}

	public final void changedInputDynaOn(final int chn, final byte element,
			final Boolean value) {
		this.sysex.changeParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_ON,
				(byte) chn, value ? (byte) 1 : (byte) 0);
	}

	public final void changedInputDynaRange(final int chn, final byte element, final float value) {
		// only Gate/Ducking, not used in Dyna2 (just a dummy)
		// value<0: model.protocol.osc.handler.OscInputChannelStrip[...]
		final int range = (int) (value * 70) - (70);
		this.sysex.changeParameter(element, 
				Ls9SysexParameter.PARAM_INPUT_DYNA_RANGE,
				(byte) chn, range);

	}

	public final void changedInputDynaRatio(final int chn, final byte element, final float value) {
		// only Compressor/Expander
		final int ratio = (int) (value * 15);
		this.sysex.changeParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_RATIO,
				(byte) chn, ratio);

	}

	public final void changedInputDynaReleaseDecay(final int chn, final byte element, final float value) {
		// Decay is for gates, release for compressors/expanders
		final int dr = (int) (value * 159);
		this.sysex.changeParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_DECAY_RELEASE,
				(byte) chn, dr);
	}

	public final void changedInputDynaThreshold(final int chn, final byte element, final float value) {
		// value<0: model.protocol.osc.handler.OscInputChannelStrip[...]
		final int threshold = (int) (value * 540) - (540);
		this.sysex.changeParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_THRESHOLD,
				(byte) chn, threshold);
	}

	public final void changedInputDynaType(final int chn, final byte element,
			final float value) {
		final int type = (int) (value * 6);
		this.sysex.changeParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_TYPE,
				(byte) chn, type);
	}

	public final void changedInputGroup(final byte element, final int group, final boolean status) {
		this.sysex.changeParameter(element, (short) group, (short) 00,
				status ? (byte) 1 : (byte) 0);
	}

	public final void changedInputPan(final int chn, final float value) {
		final int panning = (int) (value * 63);
		this.sysex.changeParameter(Ls9SysexParameter.ELMT_INPUT_PAN,
				Ls9SysexParameter.PARAM_INPUT_PAN, (byte) chn, panning);
	}

	public final void changedMasterLevel(final float level) {
		final short value = (short) (level * 1023);
		this.sysex.changeParameter(Ls9SysexParameter.ELMT_MASTER_LEVEL, (short) 0, (short) 0, value);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET MASTER value=" + value);
		}
	}

	public final void changedOutputDelayTime(final int outputNumber, final float delayTime) {
		final int delayMillisec = Math.min(this.mixer.getMaxOutputDelayMilliSec(),
				(int) (delayTime * 1000));

		this.sysex.changeParameter(Ls9SysexParameter.ELMT_OMNI_OUTPUT, Ls9SysexParameter.PARAM_OUTPUT_DELAY, (short) outputNumber,
				delayMillisec * 100);
	}

	public final void changedPeqBandQ(final int chn, final int band, final Float attachment) {

		byte parameter = 0;
		final int value = (int) (attachment * 40);
		final String label = this.mixer.getLabelMaker().getYamahaLabelPeqQ(value);

		if (band == Ls9SysexParameter.PEQ_BAND1) {
			parameter = Ls9SysexParameter.PARAM_PEQ_LOWQ;
		} else if (band == Ls9SysexParameter.PEQ_BAND2) {
			parameter = Ls9SysexParameter.PARAM_PEQ_LOWMIDQ;
		} else if (band == Ls9SysexParameter.PEQ_BAND3) {
			parameter = Ls9SysexParameter.PARAM_PEQ_HIMIDQ;
		} else if (band == Ls9SysexParameter.PEQ_BAND4) {
			parameter = Ls9SysexParameter.PARAM_PEQ_HIQ;
		}

		this.sysex.changeParameter(Ls9SysexParameter.ELMT_INPUT_PEQ,
				parameter, (byte) chn, value);
		this.mixer.fireChange(new MsgInputPeqQLabel(chn, band, label));

	}

	public final void changedPeqBandF(final int chn, final int band, final Float attachment) {

		byte parameter = 0;
		final int value = (int) (attachment * 119) + 5;
		final String label = this.mixer.getLabelMaker().getYamahaLabelPeqF(value);

		if (band == Ls9SysexParameter.PEQ_BAND1) {
			parameter = Ls9SysexParameter.PARAM_PEQ_LOWF;
		} else if (band == Ls9SysexParameter.PEQ_BAND2) {
			parameter = Ls9SysexParameter.PARAM_PEQ_LOWMIDF;
		} else if (band == Ls9SysexParameter.PEQ_BAND3) {
			parameter = Ls9SysexParameter.PARAM_PEQ_HIMIDF;
		} else if (band == Ls9SysexParameter.PEQ_BAND4) {
			parameter = Ls9SysexParameter.PARAM_PEQ_HIF;
		}

		this.sysex.changeParameter(Ls9SysexParameter.ELMT_INPUT_PEQ,
				parameter, (byte) chn, value);
		this.mixer.fireChange(new MsgInputPeqFLabel(chn, band, label));

	}

	public final void changedPeqBandG(final int chn, final int band, final Float attachment) {

		byte parameter = 0;
		final int value = (int) (attachment * 180);
		final String label = this.mixer.getLabelMaker().getYamahaLabelPeqG(attachment);

		if (band == Ls9SysexParameter.PEQ_BAND1) {
			parameter = Ls9SysexParameter.PARAM_PEQ_LOWG;
		} else if (band == Ls9SysexParameter.PEQ_BAND2) {
			parameter = Ls9SysexParameter.PARAM_PEQ_LOWMIDG;
		} else if (band == Ls9SysexParameter.PEQ_BAND3) {
			parameter = Ls9SysexParameter.PARAM_PEQ_HIMIDG;
		} else if (band == Ls9SysexParameter.PEQ_BAND4) {
			parameter = Ls9SysexParameter.PARAM_PEQ_HIG;
		}

		this.sysex.changeParameter(Ls9SysexParameter.ELMT_INPUT_PEQ,
				parameter, (byte) chn, value);
		this.mixer.fireChange(new MsgInputPeqGLabel(chn, band, label));

	}

	public final void changedPeqHPFOn(final int chn, final Boolean attachment) {
		this.sysex.changeParameter(Ls9SysexParameter.ELMT_INPUT_PEQ, 
				Ls9SysexParameter.PARAM_PEQ_HPFON, (byte) chn, attachment ? (byte) 1 : (byte) 0);

	}

	public final void changedPeqLPFOn(final int chn, final Boolean attachment) {
		this.sysex.changeParameter(Ls9SysexParameter.ELMT_INPUT_PEQ, 
				Ls9SysexParameter.PARAM_PEQ_LPF0N, (byte) chn, attachment ? (byte) 1 : (byte) 0);

	}

	public final void changedPeqMode(final int chn, final Boolean attachment) {
		this.sysex.changeParameter(Ls9SysexParameter.ELMT_INPUT_PEQ, 
				Ls9SysexParameter.PARAM_PEQ_MODE, (byte) chn, attachment ? (byte) 1 : (byte) 0);

	}

	public final void changedPeqOn(final int chn, final Boolean attachment) {
		this.sysex.changeParameter(Ls9SysexParameter.ELMT_INPUT_PEQ, 
				Ls9SysexParameter.PARAM_PEQ_EQON, (byte) chn, attachment ? (byte) 1 : (byte) 0);		
	}

}
