package com.stereokrauts.stereoscope.mixer.yamaha.ls9.midi;

import java.util.logging.Level;

import model.mixer.interfaces.IAmMixer;
import model.mixer.interfaces.IUnderstandMixer;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.util.FormatStringSysex;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.Ls9LabelDataProvider;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.Ls9Mixer;
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
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqFilterType;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqHPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqLPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqModeChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;



public final class Ls9MidiReceiver extends IUnderstandMixer {
	private static final float YAMAHA_FADER_STEPS = (1023);

	private static final SLogger logger = StereoscopeLogManager.getLogger("yamaha-ls9");

	private static final boolean DEBUG_FINEST = false;

	private final Ls9Mixer yammi;

	public Ls9MidiReceiver(final IAmMixer partner) {
		super(partner);
		this.yammi = (Ls9Mixer) partner;
	}




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

		if (DEBUG_FINEST) {
			logger.log(Level.FINEST, "CLEARMESSAGE: " + ByteStringConversion.toHex(sysexmessage));
		}



		/* From Yamaha Specification
		 * 
		 * Byte 0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 
		 * Msg  F0 43 1n 3E 12 01 01 14 00 00 cc cc dd dd dd dd dd F7
		 */
		/* CHANNEL NAME message is currently treated specially */
		if ((sysexmessage[4] == (byte) 0x12)
				&& (sysexmessage[5] == (byte) 0x01)
				&& (sysexmessage[6] == (byte) 0x01)
				&& (sysexmessage[7] == (byte) 0x14)
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

			logger.log(Level.FINER,"Received Channel " + chn + "'s Name: " + strName
					+ " -- " + name[0] + name[1] + name[2] + name[3]);

			this.yammi.fireChange(new MsgChannelNameChanged(chn, strName));
			return;
		}

		/* only use messages of appropriate length. */
		if (sysexmessage.length != 18) {
			return;
		}

		if (sysexmessage[6]  == 0x01 &&
				sysexmessage[7]  == 0x51 &&
				sysexmessage[8]  == 0x00 &&
				sysexmessage[9]  == 0x00 &&
				sysexmessage[10] == 0x00 &&
				sysexmessage[11] == 0x00) {
			// Mixer keeps requesting samplerate, didn't find out why
			// (probably just SM)
			if (this.yammi.getSamplerate() == 0) {
				// catch samplingrate 
				final FormatStringSysex srMsg = new FormatStringSysex("F0 43 10 3E 12 01" +
						"01 51 00 00 00 00" + 
						/* samplerate */ "{l5}" +
						/* STOP */ "F7");
				final Object[] srParameters = srMsg.parseMessage(sysexmessage);

				final int sampleRateNumber = (Integer) srParameters[0];
				if (this.yammi.getSamplerate() != sampleRateNumber) {
					this.yammi.setSamplerate(sampleRateNumber);
					this.yammi.setLabelMaker(new Ls9LabelDataProvider(this.yammi.getSamplerate()));
					// don't log until the samplerate is save
					logger.info("Samplerate set to :" + this.yammi.getSamplerate() + " Hz.");
				}
			}
		}

		final FormatStringSysex parser = new FormatStringSysex("F0 43 10 3E 12 01" +
				/* element */ "{s}" +
				/* parameter */ "{s}" +
				/* channel */ "{s}" + 
				/* value */ "{l5}" + 
				/* STOP */ "F7");

		final Object[] parameters = parser.parseMessage(sysexmessage);

		final short elementNo = ((Short)parameters[0]);
		final short parameter = ((Short)parameters[1]);
		final short channel = ((Short)parameters[2]);

		final long value = ((Long)parameters[3]);

		try {
			if (elementNo == Ls9SysexParameter.ELMT_MASTER_LEVEL) {
				/* master level */
				final float intValue = (value) / YAMAHA_FADER_STEPS;
				this.yammi.fireChange(new MsgMasterLevelChanged(intValue));
			} else if (elementNo == Ls9SysexParameter.ELMT_AUX_MASTER_LEVEL) {
				/* AUX Master */
				final float intValue = (value) / YAMAHA_FADER_STEPS;
				this.yammi.fireChange(new MsgAuxMasterLevelChanged(channel, intValue));
			} else if (elementNo == Ls9SysexParameter.ELMT_INPUT_LEVEL) {
				/* channel Level */
				final float intValue = (value) / YAMAHA_FADER_STEPS;
				this.yammi.fireChange(new MsgChannelLevelChanged(channel, intValue));
				final String label = this.yammi.getLabelMaker().getYamahaLabelLevel10Db(intValue);
				this.yammi.fireChange(new MsgChannelLevelLabel(channel, 0, label));
			} else if (elementNo == Ls9SysexParameter.ELMT_INPUT_AUX_SEND) {
				if ((parameter - 2) % 3 == 0) {
					/* aux send level */
					final float intValue = (value) / YAMAHA_FADER_STEPS;
					final int aux = (parameter - 2) / 3 - 1;
					this.yammi.fireChange(new MsgAuxSendChanged(channel, aux, intValue));
					final String label = this.yammi.getLabelMaker().getYamahaLabelLevel10Db(intValue);
					this.yammi.fireChange(new MsgChannelAuxLevelLabel(channel, aux, label));
				} else if (parameter % 3 == 0) {
					/* aux send on */
					final int aux = parameter / 3 - 1;
					this.yammi.fireChange(new MsgAuxSendOnChanged(channel, aux, value == 1 ? true : false));
				}
			} else if (elementNo == Ls9SysexParameter.ELMT_INPUT_ON) {
				this.yammi.fireChange(new MsgChannelOnChanged(channel, value == 1 ? true : false));
			} else if (elementNo == Ls9SysexParameter.ELMT_INPUT_PAN) {
				//long normValue = value + 63;
				final float intValue = (float) (value) / 63;
				this.yammi.fireChange(new MsgInputPan(channel, intValue));
				final String label = this.yammi.getLabelMaker().getYamahaLabelPanning(intValue);
				this.yammi.fireChange(new MsgInputPanLabel(channel, 0, label));
			} else if (elementNo == Ls9SysexParameter.ELMT_OMNI_OUTPUT && (parameter == Ls9SysexParameter.PARAM_OUTPUT_DELAY)) {
				this.yammi.fireChange(new MsgAuxMasterDelayChanged(channel, (float)(value)/(float)(100000)));
			} else if (elementNo == Ls9SysexParameter.ELMT_RACK_MOUNT) {
				if (parameter == 0) {
					if (value == 2) {
						this.yammi.fireChange(new MsgGeqTypeChanged(channel, true));
					} else if (value == 1) {
						this.yammi.fireChange(new MsgGeqTypeChanged(channel, false));
					}
				}
			} else if (elementNo == Ls9SysexParameter.ELMT_GRAPHICAL_EQ) {
				if (parameter == Ls9SysexParameter.PARAM_GEQ_ISFLEXEQ) {
					this.yammi.fireChange(new MsgGeqTypeChanged(channel, value == 1 ? true : false));
				}
				final float floatValue = (float)(value) / Ls9SysexParameter.GEQ_BAND_RANGE;
				if (parameter >= Ls9SysexParameter.PARAM_GEQ_LEFT_FIRSTBAND
						&& parameter <= (Ls9SysexParameter.PARAM_GEQ_LEFT_FIRSTBAND+31)) {
					this.yammi.fireChange(new MsgGeqBandLevelChanged(channel, parameter - Ls9SysexParameter.PARAM_GEQ_LEFT_FIRSTBAND, false,
							floatValue));
				} else if (parameter >= Ls9SysexParameter.PARAM_GEQ_RIGHT_FIRSTBAND
						&& parameter <= (Ls9SysexParameter.PARAM_GEQ_RIGHT_FIRSTBAND+31)) { 
					this.yammi.fireChange(new MsgGeqBandLevelChanged(channel, parameter - Ls9SysexParameter.PARAM_GEQ_RIGHT_FIRSTBAND, true,
							floatValue));
				}
			} else if (elementNo == Ls9SysexParameter.ELMT_INPUT_PEQ) {
				if (parameter == Ls9SysexParameter.PARAM_PEQ_MODE) {
					this.yammi.fireChange(new MsgInputPeqModeChanged(channel, (value == 1)));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_EQON) {
					this.yammi.fireChange(new MsgInputPeqOnChanged(channel, (value == 1)));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_HPFON) {
					this.yammi.fireChange(new MsgInputPeqHPFChanged(channel, (value == 1)));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_LPF0N) {
					this.yammi.fireChange(new MsgInputPeqLPFChanged(channel, (value == 1)));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_LOWQ) {

					String label = "";
					// separate filter type from quality
					if (value > 40) {
						label = this.yammi.getLabelMaker().getYamahaLabelFilterType((int) value - 41);
						this.yammi.fireChange(new MsgInputPeqQ(channel, Ls9SysexParameter.PEQ_BAND1, (int) value - 41));
					} else {
						label = this.yammi.getLabelMaker().getYamahaLabelPeqQ((int) value);
						final float toOscValue = (float) (value) / (float) (40);
						this.yammi.fireChange(new MsgInputPeqQ(channel, Ls9SysexParameter.PEQ_BAND1, toOscValue));
					}
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, Ls9SysexParameter.PEQ_BAND1, label));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_LOWF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqF((int) value);
					this.yammi.fireChange(new MsgInputPeqF(channel, Ls9SysexParameter.PEQ_BAND1, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, Ls9SysexParameter.PEQ_BAND1, label));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_LOWG) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, Ls9SysexParameter.PEQ_BAND1, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, Ls9SysexParameter.PEQ_BAND1, label));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_LOWMIDQ) {
					final float intValue = (float) (value) / (float) (40);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqQ((int) value);
					this.yammi.fireChange(new MsgInputPeqQ(channel, Ls9SysexParameter.PEQ_BAND2, intValue));
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, Ls9SysexParameter.PEQ_BAND2, label));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_LOWMIDF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqF((int) value);
					this.yammi.fireChange(new MsgInputPeqF(channel, Ls9SysexParameter.PEQ_BAND2, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, Ls9SysexParameter.PEQ_BAND2, label));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_LOWMIDG) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, Ls9SysexParameter.PEQ_BAND2, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, Ls9SysexParameter.PEQ_BAND2, label));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_HIMIDQ) {
					final float intValue = (float) (value) / (float) (40);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqQ((int) value);
					this.yammi.fireChange(new MsgInputPeqQ(channel, Ls9SysexParameter.PEQ_BAND3, intValue));
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, Ls9SysexParameter.PEQ_BAND3, label));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_HIMIDF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqF((int) value);
					this.yammi.fireChange(new MsgInputPeqF(channel, Ls9SysexParameter.PEQ_BAND3, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, Ls9SysexParameter.PEQ_BAND3, label));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_HIMIDG) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, Ls9SysexParameter.PEQ_BAND3, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, Ls9SysexParameter.PEQ_BAND3, label));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_HIQ) {

					String label = "";
					// separate filter type from quality
					if (value > 40) {
						label = this.yammi.getLabelMaker().getYamahaLabelFilterType((int) value - 41);
						this.yammi.fireChange(new MsgInputPeqFilterType(channel, Ls9SysexParameter.PEQ_BAND4, (int) value - 41));
					} else {
						label = this.yammi.getLabelMaker().getYamahaLabelPeqQ((int) value);
						final float toOscValue = (float) (value) / (float) (40);
						this.yammi.fireChange(new MsgInputPeqQ(channel, Ls9SysexParameter.PEQ_BAND4, toOscValue));
					}
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, Ls9SysexParameter.PEQ_BAND4, label));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_HIF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqF((int) value);
					this.yammi.fireChange(new MsgInputPeqF(channel, Ls9SysexParameter.PEQ_BAND4, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, 4, label));
				} else if (parameter == Ls9SysexParameter.PARAM_PEQ_HIG) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, Ls9SysexParameter.PEQ_BAND4, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, Ls9SysexParameter.PEQ_BAND4, label));

				} 
			} else if (elementNo == Ls9SysexParameter.ELMT_INPUT_DYNA1) {
				// gate related messages
				if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_ATTACK) {
					final float intValue = (float) (value) / (float) (120);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaAttack(intValue);
					this.yammi.fireChange(new MsgInputDynaAttack(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaAttackLabel(0, channel, label));
				} else if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_DECAY_RELEASE) {
					final float intValue = (float) (value) / (float) (159);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaDecayRelease(intValue);
					this.yammi.fireChange(new MsgInputDynaDecayRelease(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaDecayReleaseLabel(0, channel, label));
				} else if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_HOLD) {
					final float intValue = (float) (value) / (float) (215);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaHold(intValue);
					this.yammi.fireChange(new MsgInputDynaHold(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaHoldLabel(0, channel, label));
				} else if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_KEY_IN) {
					final float intValue = (float) (value) / (float) (2);
					this.yammi.fireChange(new MsgInputDynaKeyIn(0, channel, intValue));
					//} else if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_LINK) {
					// not implemented
				} else if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_ON) {
					this.yammi.fireChange(new MsgInputDynaOn(0, channel, (value == 1)));
				} else if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_RANGE) {
					// value<0: model.protocol.osc.handler.OscInputChannelStrip[...]
					final float pos = value + 70f;
					final float intValue = pos / (70);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaRange(intValue);
					this.yammi.fireChange(new MsgInputDynaRange(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaRangeLabel(0, channel, label));
				} else if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_THRESHOLD) {
					// value<0: model.protocol.osc.handler.OscInputChannelStrip[...]
					final long normValue = value + 540;
					final float intValue = (float) (normValue) / (float) (540);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaThreshold(intValue);
					this.yammi.fireChange(new MsgInputDynaThreshold(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaThresholdLabel(0, channel, label));
				}
			} else if (elementNo == Ls9SysexParameter.ELMT_INPUT_DYNA2) {
				// compressor related messages
				if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_ATTACK) {
					final float intValue = (float) (value) / (float) (120);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaAttack(intValue);
					this.yammi.fireChange(new MsgInputDynaAttack(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaAttackLabel(1, channel, label));
				} else if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_GAIN) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaGain(intValue);
					this.yammi.fireChange(new MsgInputDynaGain(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaGainLabel(1, channel, label));
				} else if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_KNEE) {
					// knee-range in the sysex spec is 0-89 (but it's actually 0-5)
					final float intValue = (float) (value) / (float) (5);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaKnee(intValue);
					this.yammi.fireChange(new MsgInputDynaKnee(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaKneeLabel(1, channel, label));
					//} else if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_LINK) {
					// not implemented yet
				} else if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_ON) {
					this.yammi.fireChange(new MsgInputDynaOn(1, channel, (value ==1)));
				} else if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_RATIO) {
					final float intValue = (float) (value) / (float) (15);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaRatio(intValue);
					this.yammi.fireChange(new MsgInputDynaRatio(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaRatioLabel(1, channel, label));
				} else if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_DECAY_RELEASE) {
					final float intValue = (float) (value) / (float) (159);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaDecayRelease(intValue);
					this.yammi.fireChange(new MsgInputDynaDecayRelease(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaDecayReleaseLabel(1, channel, label));
				} else if (parameter == Ls9SysexParameter.PARAM_INPUT_DYNA_THRESHOLD) {
					// value<0: model.protocol.osc.handler.OscInputChannelStrip[...]
					final long normValue = value + 540;
					final float intValue = (float) (normValue) / (float) (540);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaThreshold(intValue);
					this.yammi.fireChange(new MsgInputDynaThreshold(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaThresholdLabel(1, channel, label));
				}

			} else if (elementNo == Ls9SysexParameter.ELMT_SETUP_DIO) {
				// just catch the samplerate and do nothing to avoid an exception
			} else {
				throw new Exception("Unknown MIDI message: " + ByteStringConversion.toHex(sysexmessage));
			}
		} catch (final Exception e) {
			logger.log(Level.INFO, "Exception while parsing MIDI message", e);
		}
	}



	@Override
	public void handleProgramChange(final int newProgram) {
		this.yammi.fireChange(new MsgSceneChange(newProgram, "unknown"));
	}
}
