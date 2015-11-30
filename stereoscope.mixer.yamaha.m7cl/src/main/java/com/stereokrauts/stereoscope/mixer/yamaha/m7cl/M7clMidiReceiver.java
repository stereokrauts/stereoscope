package com.stereokrauts.stereoscope.mixer.yamaha.m7cl;

import java.util.logging.Level;

import model.mixer.interfaces.IAmMixer;
import model.mixer.interfaces.IUnderstandMixer;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.util.FormatStringSysex;
import com.stereokrauts.stereoscope.model.messaging.message.control.MsgSceneChange;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqTypeChanged;
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
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRange;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRatio;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaThreshold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelAuxLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelLevelLabel;
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
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgOutputDelayChanged;

public class M7clMidiReceiver extends IUnderstandMixer {
	private static final float YAMAHA_FADER_STEPS = (1023);
	private static final SLogger logger = StereoscopeLogManager.getLogger("yamaha-m7cl");

	public M7clMidiReceiver(final IAmMixer partner) {
		super(partner);
		this.yammi = (M7clMixer) partner;
	}

	private final M7clMixer yammi;


	/**
	 * This function gets called by the rwMidi-Library each time a sysex
	 * message is received over a Midi Port.
	 */
	@Override
	public void handleSysex(final byte[] sysexmessage) {

		/* only use messages of appropriate length. */
		if (sysexmessage.length < 16) {
			return;
		}

		this.yammi.newMessageFromMixer();


		/* From Yamaha Specification
		 * 
		 * Byte 0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 
		 * Msg  F0 43 1n 3E	11 01 01 13	00 00 cc cc	dd	dd	dd	dd	dd	F7

		 */
		/* CHANNEL NAME message is currently treated specially */
		if ((sysexmessage[4] == (byte) 0x11)
				&& (sysexmessage[5] == (byte) 0x01)
				&& (sysexmessage[6] == (byte) 0x01)
				&& (sysexmessage[7] == (byte) 0x13)
				&& (sysexmessage[8] == (byte) 0x00)
				&& (sysexmessage[9] == (byte) 0x00)) {

			final short chn = (short) ((sysexmessage[10] << 7) | (sysexmessage[11]));
			final byte name[] = { ' ', ' ', ' ', ' ' };

			name[0] = (byte) ((sysexmessage[13] >> 3) | (sysexmessage[12] << 4));
			name[1] = (byte) ((sysexmessage[14] >> 2) | (sysexmessage[13] << 5));
			name[2] = (byte) ((sysexmessage[15] >> 1) | (sysexmessage[14] << 6));
			name[3] = (byte) (sysexmessage[16] | (sysexmessage[15] << 7));

			final String strName = String.valueOf((char) name[0])
					+ String.valueOf((char) name[1])
					+ String.valueOf((char) name[2])
					+ String.valueOf((char) name[3]);

			logger.info("Received Channel " + chn + "'s Name: " + strName
					+ " -- " + name[0] + name[1] + name[2] + name[3]);

			this.yammi.fireChange(new MsgChannelNameChanged(chn, strName));
			return;
		}

		/* only use messages of appropriate length. */
		if (sysexmessage.length != 18) {
			return;
		}


		// M7clMixer keeps sending it's samplerate, didn't find out why.
		if (sysexmessage[6] == 0x01 && sysexmessage[7] == 0x4d && sysexmessage[8] == 0x00
				&& sysexmessage[9] == 0x00 && sysexmessage[10] == 0x00 && sysexmessage[11] == 0x00) {
			if (this.yammi.getSamplerate() == 0) {
				// catch samplingrate
				final FormatStringSysex srMsg = new FormatStringSysex(
						"F0 43 10 3E 11 01 01 4D 00 00 00 00"
								+ "{l5}" /* samplerate */
								+ "F7"); /* STOP */ 
				final Object[] srParameters = srMsg.parseMessage(sysexmessage);

				final int newSampleRate = (Integer) srParameters[0];
				if (this.yammi.getSamplerate() != newSampleRate) {
					this.yammi.setSamplerate(newSampleRate);
					this.yammi.labelMaker = new M7clLabelDataProvider(this.yammi.getSamplerate());
					// don't log until the samplerate is save
					logger.info("Samplerate set to :" + this.yammi.getSamplerate() + " Hz.");
				}
			}
		}

		final FormatStringSysex parser = new FormatStringSysex(
				"F0 43 10 3E 11 01"
						+ "{s}"  /* element */ 
						+ "{s}"  /* parameter */ 
						+ "{s}"  /* channel */ 
						+ "{l5}" /* value */ 
						+ "F7"); /* STOP */

		final Object[] parameters = parser.parseMessage(sysexmessage);

		final short elementNo = ((Short) parameters[0]);
		final short parameter = ((Short) parameters[1]);
		final short channel = ((Short) parameters[2]);

		final long value = ((Long) parameters[3]);

		try {
			if (elementNo == M7clMidiTransmitter.ELMT_LEVEL_MASTER) {
				/* master level */
				final float intValue = (value) / YAMAHA_FADER_STEPS;
				this.yammi.fireChange(new MsgMasterLevelChanged(intValue));
			} else if (elementNo == M7clMidiTransmitter.ELMT_LEVEL_AUX_MASTER) {
				/* AUX Master */
				final float intValue = (value) / YAMAHA_FADER_STEPS;
				this.yammi.fireChange(new MsgAuxMasterLevelChanged(channel, intValue));
			} else if (elementNo == M7clMidiTransmitter.ELMT_LEVEL_DCA) {
				/* DCA Master */
				final float intValue = (value) / YAMAHA_FADER_STEPS;
				this.yammi.fireChange(new MsgDcaLevelChanged(channel, intValue));
			} else if (elementNo == M7clMidiTransmitter.ELMT_LEVEL_CHANNEL) {
				/* channel Level */
				final float intValue = (value) / YAMAHA_FADER_STEPS;
				this.yammi.fireChange(new MsgChannelLevelChanged(channel, intValue));
				final String label = yammi.labelMaker.getYamahaLabelLevel10Db(intValue);
				this.yammi.fireChange(new MsgChannelLevelLabel(channel, 0, label));
			} else if ((elementNo == M7clMidiTransmitter.ELMT_AUXLEVEL_CHANNEL)) {
				if ((parameter - 2) % 3 == 0) {
					/* input aux send level */
					final float intValue = (value) / YAMAHA_FADER_STEPS;
					final int aux = (parameter - 2) / 3 - 1;
					this.yammi.fireChange(new MsgAuxSendChanged(channel, aux, intValue));
					final String label = yammi.labelMaker.getYamahaLabelLevel10Db(intValue);
					this.yammi.fireChange(new MsgChannelAuxLevelLabel(channel, aux, label));
				} else if (parameter % 3 == 0) {
					/* input aux send on */
					final int aux = parameter / 3 - 1;
					this.yammi.fireChange(new MsgAuxSendOnChanged(channel, aux, value == 1 ? true : false));
				}

			} else if (elementNo == M7clMidiTransmitter.ELMT_INPUT_ON) {
				this.yammi.fireChange(new MsgChannelOnChanged(channel, value == 1));
			} else if (elementNo == M7clMidiTransmitter.ELMT_INPUT_PAN) {
				//long normValue = value + 63;
				final float intValue = (float) (value) / 63;
				this.yammi.fireChange(new MsgInputPan(channel, intValue));
				final String label = this.yammi.labelMaker.getYamahaLabelPanning(intValue);
				this.yammi.fireChange(new MsgInputPanLabel(channel, 0, label));
			} else if (elementNo == M7clMidiTransmitter.ELMT_OMNI_OUTPUT && (parameter == M7clMidiTransmitter.PARAM_OUTPUT_DELAY)) {
				this.yammi.fireChange(new MsgOutputDelayChanged(channel, (float) (value) / (float) (100000)));
			} else if (elementNo == M7clMidiTransmitter.ELMT_RACK_MOUNT) {
				if (parameter == 0) {
					if (value == 2) {
						this.yammi.fireChange(new MsgGeqTypeChanged(channel, true));
					} else if (value == 1) {
						this.yammi.fireChange(new MsgGeqTypeChanged(channel, false));
					}
				}
			} else if (elementNo == M7clMidiTransmitter.ELMT_GRAPHICAL_EQ) {
				if (parameter == M7clMidiTransmitter.PARAM_GEQ_ISFLEXEQ) {
					this.yammi.fireChange(new MsgGeqTypeChanged(channel, value == 1 ? true : false));
					return;
				}
				final float floatValue = (float) (value) / M7clMidiTransmitter.GEQ_BAND_RANGE;
				if (parameter >= M7clMidiTransmitter.PARAM_GEQ_LEFT_FIRSTBAND
						&& parameter <= (M7clMidiTransmitter.PARAM_GEQ_LEFT_FIRSTBAND + 31)) {
					this.yammi.fireChange(new MsgGeqBandLevelChanged(channel, parameter - M7clMidiTransmitter.PARAM_GEQ_LEFT_FIRSTBAND, false,
							floatValue));
				} else if (parameter >= M7clMidiTransmitter.PARAM_GEQ_RIGHT_FIRSTBAND
						&& parameter <= (M7clMidiTransmitter.PARAM_GEQ_RIGHT_FIRSTBAND + 31)) { 
					this.yammi.fireChange(new MsgGeqBandLevelChanged(channel, parameter - M7clMidiTransmitter.PARAM_GEQ_RIGHT_FIRSTBAND, true,
							floatValue));
				}
			} else if (elementNo == M7clMidiTransmitter.ELMT_INPUT_PEQ) {
				if (parameter == M7clMidiTransmitter.PARAM_PEQ_MODE) {
					this.yammi.fireChange(new MsgInputPeqModeChanged(channel, (value == 1)));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_EQON) {
					this.yammi.fireChange(new MsgInputPeqOnChanged(channel, (value == 1)));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_HPFON) {
					this.yammi.fireChange(new MsgInputPeqHPFChanged(channel, (value == 1)));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_LPF0N) {
					this.yammi.fireChange(new MsgInputPeqLPFChanged(channel, (value == 1)));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_LOWQ) {
					final float intValue = (float) (value) / (float) (40);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqQ(intValue);
					this.yammi.fireChange(new MsgInputPeqQ(channel, M7clMidiTransmitter.PEQ_BAND1, intValue));
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, M7clMidiTransmitter.PEQ_BAND1, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_LOWF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqF(intValue);
					this.yammi.fireChange(new MsgInputPeqF(channel, M7clMidiTransmitter.PEQ_BAND1, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, M7clMidiTransmitter.PEQ_BAND1, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_LOWG) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, M7clMidiTransmitter.PEQ_BAND1, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, M7clMidiTransmitter.PEQ_BAND1, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_LOWMIDQ) {
					final float intValue = (float) (value) / (float) (40);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqQ(intValue);
					this.yammi.fireChange(new MsgInputPeqQ(channel, M7clMidiTransmitter.PEQ_BAND2, intValue));
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, M7clMidiTransmitter.PEQ_BAND2, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_LOWMIDF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqF(intValue);
					this.yammi.fireChange(new MsgInputPeqF(channel, M7clMidiTransmitter.PEQ_BAND2, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, M7clMidiTransmitter.PEQ_BAND2, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_LOWMIDG) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, M7clMidiTransmitter.PEQ_BAND2, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, M7clMidiTransmitter.PEQ_BAND2, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_HIMIDQ) {
					final float intValue = (float) (value) / (float) (40);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqQ(intValue);
					this.yammi.fireChange(new MsgInputPeqQ(channel, M7clMidiTransmitter.PEQ_BAND3, intValue));
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, M7clMidiTransmitter.PEQ_BAND3, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_HIMIDF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqF(intValue);
					this.yammi.fireChange(new MsgInputPeqF(channel, M7clMidiTransmitter.PEQ_BAND3, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, M7clMidiTransmitter.PEQ_BAND3, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_HIMIDG) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, M7clMidiTransmitter.PEQ_BAND3, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, M7clMidiTransmitter.PEQ_BAND3, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_HIQ) {
					final float intValue = (float) (value) / (float) (40);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqQ(intValue);
					this.yammi.fireChange(new MsgInputPeqQ(channel, M7clMidiTransmitter.PEQ_BAND4, intValue));
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, M7clMidiTransmitter.PEQ_BAND4, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_HIF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqF(intValue);
					this.yammi.fireChange(new MsgInputPeqF(channel, M7clMidiTransmitter.PEQ_BAND4, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, M7clMidiTransmitter.PEQ_BAND4, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_PEQ_HIG) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.labelMaker.getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, M7clMidiTransmitter.PEQ_BAND4, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, M7clMidiTransmitter.PEQ_BAND4, label));
				}
			} else if (elementNo == M7clMidiTransmitter.ELMT_INPUT_DYNA1) {
				// gate related messages
				if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_ATTACK) {
					final float intValue = (float) (value) / (float) (120);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaAttack(intValue);
					this.yammi.fireChange(new MsgInputDynaAttack(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaAttackLabel(0, channel, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_DECAY_RELEASE) {
					final float intValue = (float) (value) / (float) (159);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaDecayRelease(intValue);
					this.yammi.fireChange(new MsgInputDynaDecayRelease(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaDecayReleaseLabel(0, channel, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_HOLD) {
					final float intValue = (float) (value) / (float) (215);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaHold(intValue);
					this.yammi.fireChange(new MsgInputDynaHold(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaHoldLabel(0, channel, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_KEY_IN) {
					final float intValue = (float) (value) / (float) (2);
					this.yammi.fireChange(new MsgInputDynaKeyIn(0, channel, intValue));
					//} else if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_LINK) {
					// not implemented
				} else if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_ON) {
					this.yammi.fireChange(new MsgInputDynaOn(0, channel, (value == 1)));
				} else if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_RANGE) {
					// value<0: model.protocol.osc.handler.OscInputChannelStrip[...]
					final float pos = value + 70f;
					final float intValue = pos / 70;
					final String label = this.yammi.labelMaker.getYamahaLabelDynaRange(intValue);
					this.yammi.fireChange(new MsgInputDynaRange(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaRangeLabel(0, channel, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_THRESHOLD) {
					// value<0: model.protocol.osc.handler.OscInputChannelStrip[...]
					final long normValue = value + 720;
					final float intValue = (float) (normValue) / (float) (720);
					final String label = this.yammi.labelMaker.getYamahaLabelDyna1Threshold(intValue);
					this.yammi.fireChange(new MsgInputDynaThreshold(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaThresholdLabel(0, channel, label));
				}
			} else if (elementNo == M7clMidiTransmitter.ELMT_INPUT_DYNA2) {
				// compressor related messages
				if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_ATTACK) {
					final float intValue = (float) (value) / (float) (120);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaAttack(intValue);
					this.yammi.fireChange(new MsgInputDynaAttack(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaAttackLabel(1, channel, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_GAIN) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaGain(intValue);
					this.yammi.fireChange(new MsgInputDynaGain(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaGainLabel(1, channel, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_KNEE) {
					// knee-range in the sysex spec is 0-89 (but it's actually 0-5)
					final float intValue = (float) (value) / (float) (5);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaKnee(intValue);
					this.yammi.fireChange(new MsgInputDynaKnee(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaKneeLabel(1, channel, label));
					//} else if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_LINK) {
					// not implemented yet
				} else if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_ON) {
					this.yammi.fireChange(new MsgInputDynaOn(1, channel, (value == 1)));
				} else if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_RATIO) {
					final float intValue = (float) (value) / (float) (15);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaRatio(intValue);
					this.yammi.fireChange(new MsgInputDynaRatio(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaRatioLabel(1, channel, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_DECAY_RELEASE) {
					final float intValue = (float) (value) / (float) (159);
					final String label = this.yammi.labelMaker.getYamahaLabelDynaDecayRelease(intValue);
					this.yammi.fireChange(new MsgInputDynaDecayRelease(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaDecayReleaseLabel(1, channel, label));
				} else if (parameter == M7clMidiTransmitter.PARAM_INPUT_DYNA_THRESHOLD) {
					// value<0: model.protocol.osc.handler.OscInputChannelStrip[...]
					final long normValue = value + 540;
					final float intValue = (float) (normValue) / (float) (540);
					final String label = this.yammi.labelMaker.getYamahaLabelDyna2Threshold(intValue);
					this.yammi.fireChange(new MsgInputDynaThreshold(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaThresholdLabel(1, channel, label));					
				}

			} else if (elementNo == M7clMidiTransmitter.ELMT_SETUP_DIO) {
				// just catch the samplerate and do nothing to avoid an exception
			} else {
				throw new Exception("Unknown MIDI message: " + ByteStringConversion.toHex(sysexmessage));
			}
		} catch (final Exception e) {
			logger.log(Level.INFO, "Exception while parsing MIDI message", e);
		}
	}

	@Override
	public final void handleProgramChange(final int newProgram) {
		this.yammi.fireChange(new MsgSceneChange(newProgram, "unknown"));
	}
}
