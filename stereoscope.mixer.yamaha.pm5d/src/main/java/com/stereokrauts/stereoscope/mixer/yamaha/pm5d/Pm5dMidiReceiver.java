package com.stereokrauts.stereoscope.mixer.yamaha.pm5d;

import java.util.logging.Level;

import model.mixer.interfaces.IAmMixer;
import model.mixer.interfaces.IUnderstandMixer;
import model.mixer.interfaces.SampleRate;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.util.FormatStringSysex;
import com.stereokrauts.stereoscope.model.messaging.message.control.MsgSceneChange;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelNameChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputPan;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAttack;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaDecayRelease;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaGain;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaHold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKeyIn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKnee;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaPair;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRange;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRatio;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaThreshold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelAuxLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaAttackLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaDecayReleaseLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaGainLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaHoldLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaKneeLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaRangeLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaRatioLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaThresholdLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPanLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqFLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqGLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqQLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqF;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqHPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqLPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqModeChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;
import com.stereokrauts.stereoscope.model.messaging.message.mixerglobal.MsgDcaLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;

public class Pm5dMidiReceiver extends IUnderstandMixer {
	private static final SLogger LOGGER = StereoscopeLogManager.getLogger("yamaha-pm5d");


	public Pm5dMidiReceiver(final IAmMixer partner) {
		super(partner);
		this.yammi = (Pm5dMixer) partner;
	}


	private final Pm5dMixer yammi;



	/**
	 * This function gets called by the rwMidi-Library each time a sysex
	 * message is received over a Midi Port.
	 */
	@Override
	public void handleSysex(final byte[] sysexmessage) {

		LOGGER.finest("CLEARMESSAGE: " + ByteStringConversion.toHex(sysexmessage));

		/* only use messages of appropriate length. */
		if (sysexmessage.length != 15) {
			return;
		}

		this.yammi.newMessageFromMixer();


		/* From Yamaha Specification
		 * 
		 * Byte 0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 
		 * Msg  F0 43 1n 3E	0F 06 08 01	cc dd dd dd	dd dd F7

		 */
		/* CHANNEL NAME message is currently treated specially */
		if ((sysexmessage[4] == (byte) 0x0F)
				&& (sysexmessage[5] == (byte) 0x06)
				&& (sysexmessage[6] == (byte) 0x08)
				&& (sysexmessage[7] == (byte) 0x01)) {

			final byte chn = (byte) (sysexmessage[8] - 1);
			final byte[] name = { ' ', ' ', ' ', ' ' };

			name[0] = (byte) ((sysexmessage[10] >> 3) | (sysexmessage[9] << 4));
			name[1] = (byte) ((sysexmessage[11] >> 2) | (sysexmessage[10] << 5));
			name[2] = (byte) ((sysexmessage[12] >> 1) | (sysexmessage[11] << 6));
			name[3] = (byte) (sysexmessage[13] | (sysexmessage[12] << 7));

			final String strName = String.valueOf((char) name[0])
					+ String.valueOf((char) name[1])
					+ String.valueOf((char) name[2])
					+ String.valueOf((char) name[3]);

			LOGGER.info("Received Channel " + chn + "'s Name: " + strName
					+ " -- " + name[0] + name[1] + name[2] + name[3]);

			this.yammi.fireChange(new MsgChannelNameChanged(chn, strName));
			return;
		}

		// Mixer keeps requesting samplerate, didn't find out why
		// (probably just SM)
		if (sysexmessage[5] == 0x03
				&& sysexmessage[6] == 0x23
				&& sysexmessage[7] == 0x00
				&& sysexmessage[8] == 0x00) {
			// catch samplingrate
			final FormatStringSysex srMsg = new FormatStringSysex(
					"F0 43 10 3E"
							+ "{b}"         /* mixer id */
							+ "03 23 00 00" /* fixed bytes */
							+ "{i}"         /* samplerate */
							+ "F7");        /* STOP */

			final Object[] srParameters = srMsg.parseMessage(sysexmessage);

			if (((Byte) srParameters[0] == (byte) 0x06)
					|| ((Byte) srParameters[0] == (byte) 0x0B)
					|| ((Byte) srParameters[0] == (byte) 0x0C)
					|| ((Byte) srParameters[0] == (byte) 0x0D)) {

				final int sampleRateNumber = (Integer) srParameters[1];
				final SampleRate sampleRate = YamahaSampleRateAdapter.adapt(sampleRateNumber);
				if (this.yammi.getSamplerate() != sampleRate) {
					this.yammi.setSamplerate(sampleRateNumber);
					this.yammi.labelMaker = new Pm5dLabelDataProvider(sampleRateNumber);
					// don't log until the samplerate is save
					LOGGER.info("Samplerate set to :" + this.yammi.getSamplerate() + " Hz.");
				}
			}

		}		
		/* F0 43 10 3E 0F 01 27 01 01 00 00 00 04 21 F7 */
		final FormatStringSysex parser = new FormatStringSysex(
				"F0 43 10 3E 0F 01"
						+ "{b}"   /* element */
						+ "{b}"   /* parameter */
						+ "{b}"   /* channel */
						+ "{-l5}" /* value */
						+  "F7"); /* STOP */

		final Object[] parameters = parser.parseMessage(sysexmessage);

		final byte elementNo = ((Byte) parameters[0]);
		final byte parameter = ((Byte) parameters[1]);
		final byte channel = (byte) (((Byte) parameters[2]) - 1); /* PM5D starts numbering at 1, but internally we start at 0 */

		final long value = ((Long) parameters[3]);

		try {
			if (elementNo == Pm5dSysexParameter.ELMT_MASTER_LEVEL) {
				/* master level */
				final float intValue = (float) (value) / (float) (1023);
				this.yammi.fireChange(new MsgMasterLevelChanged(intValue));
			} else if (elementNo == Pm5dSysexParameter.ELMT_AUX_MASTER_LEVEL) {
				/* AUX Master */
				final float intValue = (float) (value) / (float) (1023);
				this.yammi.fireChange(new MsgAuxMasterLevelChanged(channel, intValue));
			} else if (elementNo == Pm5dSysexParameter.ELMT_LEVEL_DCA) {
				/* DCA FAder */
				final float intValue = (float) (value) / (float) (1023);
				this.yammi.fireChange(new MsgDcaLevelChanged(channel, intValue));
			} else if (elementNo == Pm5dSysexParameter.ELMT_INPUT_LEVEL) {
				/* channel Level */
				final float intValue = (float) (value) / (float) (1023);
				this.yammi.fireChange(new MsgChannelLevelChanged(channel, intValue));
			} else if ((elementNo == Pm5dSysexParameter.ELMT_INPUT_AUX_SEND)) {
				if ((parameter - 3) % 3 == 0) {
					/* input aux send level */
					final float intValue = (float) (value) / (float) (1023);
					final int intAux = (parameter - 3)/3 - 1;
					final String label = this.yammi.labelMaker.getYamahaLabelLevel10Db(intValue);
					this.yammi.fireChange(new MsgAuxSendChanged(channel, intAux, intValue));
					this.yammi.fireChange(new MsgChannelAuxLevelLabel(channel, intAux, label));
				} else if (parameter % 3 == 0) {
					/* input aux send on */
					final int aux = parameter/3 - 1;
					this.yammi.fireChange(new MsgAuxSendOnChanged(channel, aux, value == 1 ? true : false));
				}
			} else if (elementNo == Pm5dSysexParameter.ELMT_INPUT_ON) {
				LOGGER.fine("Received INPUT_ON message: v=" + value);
				this.yammi.fireChange(new MsgChannelOnChanged(channel, value == 1));
			} else if (elementNo == Pm5dSysexParameter.ELMT_INPUT_PAN) {
				//long normValue = value + 63;
				final float intValue = (float) (value) / 63;
				this.yammi.fireChange(new MsgInputPan(channel, intValue));
				final String label = this.yammi.labelMaker.getYamahaLabelPanning(intValue);
				this.yammi.fireChange(new MsgInputPanLabel(channel, 0, label));
			} else if (elementNo == Pm5dSysexParameter.ELMT_AUX_DELAY && (parameter == Pm5dSysexParameter.PARAM_AUX_DELAY_TIME)) {
				this.yammi.fireChange(new MsgAuxMasterDelayChanged(channel, (float) (value)/(float)(100000)));
			} else if (elementNo == Pm5dSysexParameter.ELMT_MATRIX_DELAY && (parameter == Pm5dSysexParameter.PARAM_MATRIX_DELAY_TIME)) {
				this.yammi.fireChange(new MsgBusMasterDelayChanged(channel, (float) (value)/(float)(100000)));
			} else if (elementNo == Pm5dSysexParameter.ELMT_GRAPHICAL_EQ) {
				LOGGER.fine("Received GEQ message: nr=" + channel + ", band=" + parameter + ", v=" + value);
				final float floatValue = (float) (value) / Pm5dSysexParameter.GEQ_BAND_RANGE;
				this.yammi.fireChangeGeqBandLevel(channel, false, parameter - Pm5dSysexParameter.PARAM_GEQ_FIRSTBAND, floatValue);
			} else if (elementNo == Pm5dSysexParameter.ELMT_INPUT_PEQ) {
				LOGGER.fine("PEQ Message: param=" + parameter + ", value=" + value);
				if (parameter == Pm5dSysexParameter.PARAM_PEQ_MODE) {
					this.yammi.fireChange(new MsgInputPeqModeChanged(channel, (value == 1)));
				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_EQON) {
					this.yammi.fireChange(new MsgInputPeqOnChanged(channel, (value == 1)));
				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_HPFON) {
					this.yammi.fireChange(new MsgInputPeqHPFChanged(channel, (value == 1)));
				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_LPF0N) {
					this.yammi.fireChange(new MsgInputPeqLPFChanged(channel, (value == 1)));

				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_LOWQ) {
					final float intValue = (float) (value + 4) / (float) (44);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqQ((int) value);
					this.yammi.fireChange(new MsgInputPeqQ(channel, Pm5dSysexParameter.PEQ_BAND1, intValue));
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, Pm5dSysexParameter.PEQ_BAND1, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_LOWF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqF((int) value);
					this.yammi.fireChange(new MsgInputPeqF(channel, Pm5dSysexParameter.PEQ_BAND1, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, Pm5dSysexParameter.PEQ_BAND1, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_LOWG) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, Pm5dSysexParameter.PEQ_BAND1, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, Pm5dSysexParameter.PEQ_BAND1, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_LOWMIDQ) {
					final float intValue = (float) (value + 4) / (float) (44);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqQ((int) value);
					this.yammi.fireChange(new MsgInputPeqQ(channel, Pm5dSysexParameter.PEQ_BAND2, intValue));
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, Pm5dSysexParameter.PEQ_BAND2, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_LOWMIDF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqF((int) value);
					this.yammi.fireChange(new MsgInputPeqF(channel, Pm5dSysexParameter.PEQ_BAND2, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, Pm5dSysexParameter.PEQ_BAND2, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_LOWMIDG) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, Pm5dSysexParameter.PEQ_BAND2, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, Pm5dSysexParameter.PEQ_BAND2, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_HIMIDQ) {
					final float intValue = (float) (value + 4) / (float) (44);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqQ((int) value);
					this.yammi.fireChange(new MsgInputPeqQ(channel, Pm5dSysexParameter.PEQ_BAND3, intValue));
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, Pm5dSysexParameter.PEQ_BAND3, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_HIMIDF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqF((int) value);
					this.yammi.fireChange(new MsgInputPeqF(channel, Pm5dSysexParameter.PEQ_BAND3, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, Pm5dSysexParameter.PEQ_BAND3, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_HIMIDG) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, Pm5dSysexParameter.PEQ_BAND3, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, Pm5dSysexParameter.PEQ_BAND3, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_HIQ) {
					final float intValue = (float) (value + 4) / (float) (44);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqQ((int) value);
					this.yammi.fireChange(new MsgInputPeqQ(channel, Pm5dSysexParameter.PEQ_BAND4, intValue));
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, Pm5dSysexParameter.PEQ_BAND4, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_HIF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqF((int) value);
					this.yammi.fireChange(new MsgInputPeqF(channel, Pm5dSysexParameter.PEQ_BAND4, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, Pm5dSysexParameter.PEQ_BAND4, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_PEQ_HIG) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, Pm5dSysexParameter.PEQ_BAND4, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, Pm5dSysexParameter.PEQ_BAND4, label));
				}
			} else if (elementNo == Pm5dSysexParameter.ELMT_INPUT_PAN) {
				if (parameter == Pm5dSysexParameter.PARAM_INPUT_PAN) {
					long normValue = (int) value;
					if (value > 100) {
						normValue = value - 268435456;
					}
					final float intValue = (float) (normValue) / 63;
					this.yammi.fireChange(new MsgInputPan(channel, intValue));
				}
			} else if (elementNo == Pm5dSysexParameter.ELMT_INPUT_DYN1) {
				LOGGER.fine("DYN1 Message: param=" + parameter + ", value=" + value);
				// gate related messages
				if (parameter == Pm5dSysexParameter.PARAM_INPUT_GATE_ATTACK) {
					final float intValue = (float) (value) / (float) (120);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaAttack(intValue);
					this.yammi.fireChange(new MsgInputDynaAttack(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaAttackLabel(0, channel, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_INPUT_GATE_DECAY) {
					final float intValue = (float) (value) / (float) (159);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaDecayRelease(intValue);
					this.yammi.fireChange(new MsgInputDynaDecayRelease(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaDecayReleaseLabel(0, channel, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_INPUT_GATE_HOLD) {
					final float intValue = (float) (value) / (float) (215);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaHold(intValue);
					this.yammi.fireChange(new MsgInputDynaHold(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaHoldLabel(0, channel, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_INPUT_GATE_KEY_IN) {
					final float intValue = (float) (value) / (float) (2);
					this.yammi.fireChange(new MsgInputDynaKeyIn(0, channel, intValue));
				} else if (parameter == Pm5dSysexParameter.PARAM_INPUT_GATE_LINK) {
					this.yammi.fireChange(new MsgInputDynaPair(0, channel, (value == 1)));
				} else if (parameter == Pm5dSysexParameter.PARAM_INPUT_GATE_ON) {
					this.yammi.fireChange(new MsgInputDynaOn(0, channel, (value == 1)));
				} else if (parameter == Pm5dSysexParameter.PARAM_INPUT_GATE_RANGE) {
					final float normalized = ((float) Pm5dSysexParameter.DYNAMICS_GATE_RANGE_VALUECOUNT + value - 1)/(Pm5dSysexParameter.DYNAMICS_GATE_RANGE_VALUECOUNT - 1);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaRange(normalized);
					this.yammi.fireChange(new MsgInputDynaRange(0, channel, normalized));
					this.yammi.fireChange(new MsgInputDynaRangeLabel(0, channel, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_INPUT_GATE_THRESHOLD) {
					final float normalized = ((float) Pm5dSysexParameter.DYNAMICS_GATE_THRESHOLD_VALUECOUNT + value - 1)/(Pm5dSysexParameter.DYNAMICS_GATE_THRESHOLD_VALUECOUNT - 1);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaThreshold(normalized);
					this.yammi.fireChange(new MsgInputDynaThreshold(0, channel, normalized));
					this.yammi.fireChange(new MsgInputDynaThresholdLabel(0, channel, label));
				}
			} else if (elementNo == Pm5dSysexParameter.ELMT_INPUT_DYN2) {
				LOGGER.fine("DYN2 Message: param=" + parameter + ", value=" + value);
				// compressor related messages
				if (parameter == Pm5dSysexParameter.PARAM_INPUT_COMP_ATTACK) {
					final float intValue = (float) (value) / (float) (120);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaAttack(intValue);
					this.yammi.fireChange(new MsgInputDynaAttack(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaAttackLabel(1, channel, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_INPUT_COMP_GAIN) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaGain(intValue);
					this.yammi.fireChange(new MsgInputDynaGain(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaGainLabel(1, channel, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_INPUT_COMP_KNEE) {
					// knee-range in the sysex spec is 0-89 (but it's actually 0-5)
					final float intValue = (float) (value) / (float) (5);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaKnee(intValue);
					this.yammi.fireChange(new MsgInputDynaKnee(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaKneeLabel(1, channel, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_INPUT_COMP_LINK) {
					this.yammi.fireChange(new MsgInputDynaPair(1, channel, (value == 1)));
				} else if (parameter == Pm5dSysexParameter.PARAM_INPUT_COMP_ON) {
					this.yammi.fireChange(new MsgInputDynaOn(1, channel, (value == 1)));
				} else if (parameter == Pm5dSysexParameter.PARAM_INPUT_COMP_RATIO) {
					final float intValue = (float) (value) / (float) (15);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaRatio(intValue);
					this.yammi.fireChange(new MsgInputDynaRatio(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaRatioLabel(1, channel, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_INPUT_COMP_RELEASE) {
					final float intValue = (float) (value) / (float) (159);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaDecayRelease(intValue);
					this.yammi.fireChange(new MsgInputDynaDecayRelease(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaDecayReleaseLabel(1, channel, label));
				} else if (parameter == Pm5dSysexParameter.PARAM_INPUT_COMP_THRESHOLD) {
					final float normalized = ((float) Pm5dSysexParameter.DYNAMICS_COMP_THRESHOLD_VALUECOUNT + value - 1)/(Pm5dSysexParameter.DYNAMICS_COMP_THRESHOLD_VALUECOUNT - 1);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaThreshold(normalized);
					this.yammi.fireChange(new MsgInputDynaThreshold(1, channel, normalized));
					this.yammi.fireChange(new MsgInputDynaThresholdLabel(1, channel, label));
				}
			} else if (elementNo == Pm5dSysexParameter.ELMT_SETUP_DIO) {
				// just catch the samplerate and do nothing to avoid an exception
			} else {
				throw new Exception("Unknown MIDI message: " + ByteStringConversion.toHex(sysexmessage));
			}
		} catch (final Exception e) {
			LOGGER.log(Level.INFO, "Exception while parsing MIDI message", e);
		}
	}

	@Override
	public final void handleProgramChange(final int newProgram) {
		this.yammi.fireChange(new MsgSceneChange(newProgram, "unknown"));
	}
}
