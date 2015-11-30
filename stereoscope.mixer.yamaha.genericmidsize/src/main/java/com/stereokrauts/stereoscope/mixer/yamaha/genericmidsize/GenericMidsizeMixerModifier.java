package com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize;

import java.util.logging.Level;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeMidiTransmitter;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeSysexParameter;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqFLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqGLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqQLabel;

public class GenericMidsizeMixerModifier {
	private static final int YAMAHA_FADER_STEPS = 1023;

	private static final SLogger logger = StereoscopeLogManager.getLogger(GenericMidsizeMixerModifier.class);

	private final GenericMidsizeMidiTransmitter sysex;
	private final GenericMidsizeMixer mixer;

	private static boolean DEBUG_FINEST = false;

	public GenericMidsizeMixerModifier(final GenericMidsizeMixer mixer, final GenericMidsizeMidiTransmitter mysysex) {
		this.mixer = mixer;
		this.sysex = mysysex;
	}

	public void changedMasterLevel(final float level) {
		final short value = (short) (level * 1023);
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_STEREO_FADER, (byte) 0, (byte) 0, value);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET MASTER value=" + value);
		}
	}

	public void changedAuxMaster(final int aux, final float level) {
		final short value = (short) (level * 1023);
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_AUX_FADER, (byte) 0, (byte) aux, value);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET AUXMASTER aux=" + aux + ", value=" + value);
		}
	}

	public void changedChannelLevel(final int channel, final float level) {
		final short value = (short) (level * 1023);
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_FADER, (byte) 0, (byte) channel, value);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET channel=" + channel + ", value=" + value);
		}
	}

	public void changedChannelAuxLevel(final int channel, final int aux, final float level) {
		final short value = (short) (level * 1023);
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_AUX, (byte) ((aux + 1) * 3 - 1),
				(byte) channel, value);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET channel=" + channel + ", aux=" + aux + ", value=" + value);
		}
	}

	public void changedChannelAuxSendOn(final int channel, final int aux, final boolean status) {
		final byte value = status ? (byte) 1 : (byte) 0;
		this.sysex
		.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_AUX, (byte) (aux * 3), (byte) channel, value);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET AUXSENDON channel=" + channel + ", value=" + value);
		}
	}

	public void changedChannelOnButton(final int channel, final boolean status) {
		final byte value = status ? (byte) 1 : (byte) 0;
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_ON, (byte) 0, (byte) channel, value);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET CHON channel=" + channel + ", value=" + value);
		}
	}

	public void changedInputPan(final int channel, final float value) {
		final int panning = (int) (value * 63);
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PAN, (byte) 0, (byte) channel, panning);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET INPUTPAN channel=" + channel + ", value=" + panning);
		}
	}

	public void changedPeqBandG(final int channel, final int band, final Float attachment) {

		byte parameter = 0;
		final int value = (int) (attachment * 180);
		final String label = this.mixer.getLabelMaker().getYamahaLabelPeqG(attachment);

		if (band == GenericMidsizeSysexParameter.PEQ_LOW_BAND) {
			parameter = GenericMidsizeSysexParameter.PARAM_PEQ_LOWG;
		} else if (band == GenericMidsizeSysexParameter.PEQ_LOWMID_BAND) {
			parameter = GenericMidsizeSysexParameter.PARAM_PEQ_LOWMIDG;
		} else if (band == GenericMidsizeSysexParameter.PEQ_HIMID_BAND) {
			parameter = GenericMidsizeSysexParameter.PARAM_PEQ_HIMIDG;
		} else if (band == GenericMidsizeSysexParameter.PEQ_HI_BAND) {
			parameter = GenericMidsizeSysexParameter.PARAM_PEQ_HIG;
		}

		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET PEQ_G channel=" + channel + ", band=" + band + ", value=" + value);
		}
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PEQ, parameter, (byte) channel, value);
		this.mixer.fireChange(new MsgInputPeqGLabel(channel, band, label));
	}

	public void changedPeqBandF(final int chn, final int band, final Float attachment) {

		byte parameter = 0;
		final int value = (int) (attachment * 119) + 5;
		final String label = this.mixer.getLabelMaker().getYamahaLabelPeqF(value);

		if (band == GenericMidsizeSysexParameter.PEQ_LOW_BAND) {
			parameter = GenericMidsizeSysexParameter.PARAM_PEQ_LOWF;
		} else if (band == GenericMidsizeSysexParameter.PEQ_LOWMID_BAND) {
			parameter = GenericMidsizeSysexParameter.PARAM_PEQ_LOWMIDF;
		} else if (band == GenericMidsizeSysexParameter.PEQ_HIMID_BAND) {
			parameter = GenericMidsizeSysexParameter.PARAM_PEQ_HIMIDF;
		} else if (band == GenericMidsizeSysexParameter.PEQ_HI_BAND) {
			parameter = GenericMidsizeSysexParameter.PARAM_PEQ_HIF;
		}

		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET PEQ_F channel=" + parameter + ", band=" + band + ", value=" + value);
		}
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PEQ, parameter, (byte) chn, value);
		this.mixer.fireChange(new MsgInputPeqFLabel(chn, band, label));
	}

	public void changedPeqBandQ(final int chn, final int band, final Float attachment) {

		byte parameter = 0;
		final int value = (int) (attachment * 40);
		final String label = this.mixer.getLabelMaker().getYamahaLabelPeqQ(value);

		if (band == GenericMidsizeSysexParameter.PEQ_LOW_BAND) {
			parameter = GenericMidsizeSysexParameter.PARAM_PEQ_LOWQ;
		} else if (band == GenericMidsizeSysexParameter.PEQ_LOWMID_BAND) {
			parameter = GenericMidsizeSysexParameter.PARAM_PEQ_LOWMIDQ;
		} else if (band == GenericMidsizeSysexParameter.PEQ_HIMID_BAND) {
			parameter = GenericMidsizeSysexParameter.PARAM_PEQ_HIMIDQ;
		} else if (band == GenericMidsizeSysexParameter.PEQ_HI_BAND) {
			parameter = GenericMidsizeSysexParameter.PARAM_PEQ_HIQ;
		}

		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET PEQ_Q channel=" + parameter + ", band=" + band + ", value=" + value);
		}
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PEQ, parameter, (byte) chn, value);
		this.mixer.fireChange(new MsgInputPeqQLabel(chn, band, label));
	}

	public void changedPeqBandFilterType(final int chn, final int band, final int attachment) {

		byte parameter = 0;
		final int mixerValue = attachment + 41;
		final String label = this.mixer.getLabelMaker().getYamahaLabelPeqQ(attachment);

		if (band == GenericMidsizeSysexParameter.PEQ_LOW_BAND) {
			parameter = GenericMidsizeSysexParameter.PARAM_PEQ_LOWQ;
		} else if (band == GenericMidsizeSysexParameter.PEQ_HI_BAND) {
			parameter = GenericMidsizeSysexParameter.PARAM_PEQ_HIQ;
		}

		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET PEQ FILTER TYPE channel=" + parameter + ", band=" + band + ", value="
					+ mixerValue);
		}
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PEQ, parameter, (byte) chn, mixerValue);
		this.mixer.fireChange(new MsgInputPeqQLabel(chn, band, label));
	}

	public void changedPeqMode(final int chn, final Boolean attachment) {
		final byte mode = attachment ? (byte) 1 : (byte) 0;
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PEQ,
				GenericMidsizeSysexParameter.PARAM_PEQ_MODE, (byte) chn, mode);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET PEQ_MODE channel=" + chn + ", value=" + mode);
		}
	}

	public void changedPeqHPFOn(final int chn, final Boolean attachment) {
		final byte mode = attachment ? (byte) 1 : (byte) 0;
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PEQ,
				GenericMidsizeSysexParameter.PARAM_PEQ_HPFON, (byte) chn, mode);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET PEQ_HPF_ON channel=" + chn + ", value=" + mode);
		}
	}

	public void changedPeqLPFOn(final int chn, final Boolean attachment) {
		final byte mode = attachment ? (byte) 1 : (byte) 0;
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PEQ,
				GenericMidsizeSysexParameter.PARAM_PEQ_LPF0N, (byte) chn, mode);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET PEQ_LPF_ON channel=" + chn + ", value=" + mode);
		}
	}

	public void changedPeqOn(final int chn, final Boolean attachment) {
		final byte mode = attachment ? (byte) 1 : (byte) 0;
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PEQ,
				GenericMidsizeSysexParameter.PARAM_PEQ_EQON, (byte) chn, mode);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET PEQ_ON channel=" + chn + ", value=" + mode);
		}
	}

	public void changedInputDynaOn(final int chn, final byte element, final Boolean value) {
		final byte mode = value ? (byte) 1 : (byte) 0;
		if (element == GenericMidsizeSysexParameter.ELMT_INPUT_GATE) {
			this.sysex.changeParameter(element, GenericMidsizeSysexParameter.PARAM_INPUT_GATE_ON, (byte) chn, mode);
		} else {
			this.sysex.changeParameter(element, GenericMidsizeSysexParameter.PARAM_INPUT_COMP_ON, (byte) chn, mode);
		}
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET INPUT_DYNA_ON channel=" + chn + "processor=" + element + ", value="
					+ mode);
		}
	}

	public void changedInputDynaType(final int chn, final byte element, final float value) {
		int type = 0;
		if (element == GenericMidsizeSysexParameter.ELMT_INPUT_GATE) {
			type = (int) (value * 5);
			this.sysex.changeParameter(element, GenericMidsizeSysexParameter.PARAM_INPUT_GATE_TYPE, (byte) chn, type);
		} else {
			type = (int) (value * 3);
			this.sysex.changeParameter(element, GenericMidsizeSysexParameter.PARAM_INPUT_COMP_TYPE, (byte) chn, type);
		}
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET INPUT_DYNA_TYPE channel=" + chn + ", processor=" + element + ", value="
					+ type);
		}
	}

	public void changedInputDynaKeyIn(final int chn, final float value) {
		// only Gate/Ducking, no check needed
		final int key = (int) (value * 2);
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_GATE,
				GenericMidsizeSysexParameter.PARAM_INPUT_GATE_KEY_IN, (byte) chn, key);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET INPUT_DYNA_KEYIN channel=" + chn + "value=" + key);
		}
	}

	public void changedInputDynaAttack(final int chn, final byte element, final float value) {
		final int attack = (int) (value * 120);
		if (element == GenericMidsizeSysexParameter.ELMT_INPUT_GATE) {
			this.sysex.changeParameter(element, GenericMidsizeSysexParameter.PARAM_INPUT_GATE_ATTACK, (byte) chn,
					attack);
		} else {
			this.sysex.changeParameter(element, GenericMidsizeSysexParameter.PARAM_INPUT_COMP_ATTACK, (byte) chn,
					attack);
		}
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET INPUT_DYNA_ATTACK channel=" + chn + ", processor=" + element + ", value="
					+ attack);
		}
	}

	public void changedInputDynaRange(final int chn, final float value) {
		// only Gate/Ducking, no check needed
		// value<0: model.protocol.osc.handler.OscInputChannelStrip[...]
		final int range = (int) (value * 70) - (70);
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_GATE,
				GenericMidsizeSysexParameter.PARAM_INPUT_GATE_RANGE, (byte) chn, range);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET INPUT_DYNA_RANGE channel=" + chn + ", value=" + range);
		}
	}

	public void changedInputDynaHold(final int chn, final float value) {
		// Hold is only available for Gate/Ducking, no check needed
		final int hold = (int) (value * 215);
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_GATE,
				GenericMidsizeSysexParameter.PARAM_INPUT_GATE_HOLD, (byte) chn, hold);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET INPUT_DYNA_HOLD channel=" + chn + ", value=" + hold);
		}
	}

	public void changedInputDynaReleaseDecay(final int chn, final byte element, final float value) {
		// Decay is for gates, release for compressors/expanders
		final int dr = (int) (value * 159);
		if (element == GenericMidsizeSysexParameter.ELMT_INPUT_GATE) {
			this.sysex.changeParameter(element, GenericMidsizeSysexParameter.PARAM_INPUT_GATE_DECAY, (byte) chn, dr);
		} else {
			this.sysex.changeParameter(element, GenericMidsizeSysexParameter.PARAM_INPUT_COMP_RELEASE, (byte) chn, dr);
		}
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET INPUT_DYNA_REL/DEC channel=" + chn + ", value=" + dr);
		}
	}

	public void changedInputDynaRatio(final int chn, final float value) {
		// Ratio is only available for Compressor/Expander, no check needed
		final int ratio = (int) (value * 15);
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_COMP,
				GenericMidsizeSysexParameter.PARAM_INPUT_COMP_RATIO, (byte) chn, ratio);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET INPUT_DYNA_RATIO channel=" + chn + ", value=" + ratio);
		}
	}

	public void changedInputDynaGain(final int chn, final float value) {
		// Gain is only available for Compressor/Expander, no check needed
		final int gain = (int) (value * 180);
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_COMP,
				GenericMidsizeSysexParameter.PARAM_INPUT_COMP_GAIN, (byte) chn, gain);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET INPUT_DYNA_GAIN channel=" + chn + ", value=" + gain);
		}
	}

	public void changedInputDynaKnee(final int chn, final float value) {
		// only Compressor/Expander, no check needed
		// knee-range in the sysex spec is 0-89 (but it's actually 0-5)
		final int knee = (int) (value * 5);
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_COMP,
				GenericMidsizeSysexParameter.PARAM_INPUT_COMP_KNEE, (byte) chn, knee);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET INPUT_DYNA_KNEE channel=" + chn + ", value=" + knee);
		}
	}

	public void changedInputDynaThreshold(final int chn, final byte element, final float value) {
		// value<0
		final int threshold = (int) (value * 540) - (540);
		if (element == GenericMidsizeSysexParameter.ELMT_INPUT_GATE) {
			this.sysex.changeParameter(element, GenericMidsizeSysexParameter.PARAM_INPUT_GATE_THRESHOLD, (byte) chn,
					threshold);
		} else {
			this.sysex.changeParameter(element, GenericMidsizeSysexParameter.PARAM_INPUT_COMP_THRESHOLD, (byte) chn,
					threshold);
		}
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET INPUT_DYNA_THRESHOLD channel=" + chn + ", value=" + threshold);
		}
	}

	public void changedAuxDelayTime(final int aux, final float delayTime) {
		final int delaySamples = Math.min(this.mixer.getMaxOutputDelaySamples(), (int) (delayTime * this.mixer
				.getSamplerate().getValue()));
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_AUX_DELAY, (byte) 1, (byte) aux,
				(short) delaySamples);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET AUX_DELAY_TIME aux=" + aux + ", samples=" + delaySamples);
		}
	}

	public void changedBusDelayTime(final int busNumber, final float delayTime) {
		final int delaySamples = Math.min(this.mixer.getMaxOutputDelaySamples(), (int) (delayTime * this.mixer
				.getSamplerate().getValue()));
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_BUS_DELAY, (byte) 1, (byte) busNumber,
				delaySamples);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET BUS_DELAY_TIME bus=" + busNumber + ", samples=" + delaySamples);
		}
	}

	public void changedBusMaster(final int busNumber, final float level) {
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_BUS_FADER, (byte) 0, (byte) busNumber,
				(int) (level * YAMAHA_FADER_STEPS));
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET BUS_MASTER bus=" + busNumber + ", level=" + level);
		}
	}

	public void changedInputGroup(final byte group, final boolean status) {
		final int value = status ? (int) 1 : (int) 0;
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_INPUT_GROUP, group, (byte) 01, value);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET INPUT_GROUP group=" + group + ", value=" + value);
		}
	}

	public void changedOutputGroup(final byte group, final boolean status) {
		final int value = status ? (int) 1 : (int) 0;
		this.sysex.changeParameter(GenericMidsizeSysexParameter.ELMT_OUTPUT_GROUP, group, (byte) 00, value);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET OUTPUT_GROUP group=" + group + ", value=" + value);
		}
	}
}
