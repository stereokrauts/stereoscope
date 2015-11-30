package com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi;


import java.util.logging.Level;

import model.mixer.interfaces.IAmMixer;
import model.mixer.interfaces.IUnderstandMixer;
import model.mixer.interfaces.SampleRate;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.util.FormatStringSysex;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.GenericMidsizeLabelDataProvider;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.GenericMidsizeMixer;
import com.stereokrauts.stereoscope.model.messaging.message.control.MsgSceneChange;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelNameChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputGroups;
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
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.labels.MsgBusMasterLevelLabel;

public abstract class GenericMidsizeMidiReceiver extends IUnderstandMixer {
	private static final float YAMAHA_FADER_STEPS = (1023);

	private static final SLogger logger = StereoscopeLogManager.getLogger("yamaha-generic-midsize");

	private static final boolean DEBUG_FINEST = false;

	protected GenericMidsizeMixer yammi;


	public GenericMidsizeMidiReceiver(final IAmMixer partner) {
		super(partner);
		this.yammi = (GenericMidsizeMixer) partner;
	}


	@Override
	public void handleSysex(final byte[] sysexmessage) {
		if (sysexmessage.length != 14) {
			return;
		}


		if (DEBUG_FINEST) {
			logger.log(Level.FINEST, "CLEARMESSAGE: " + ByteStringConversion.toHex(sysexmessage));
		}
		yammi.getCommunicationAware().transmit();

		if ((sysexmessage[4] == (byte) 0x06)         // DM2000
				|| (sysexmessage[4] == (byte) 0x0B)  // 02R96 
				|| (sysexmessage[4] == (byte) 0x0C)  // DM1000
				|| (sysexmessage[4] == (byte) 0x0D)  // 01V96
				|| (sysexmessage[4] == (byte) 0x1A)  // 01V96i
				&& (sysexmessage[5] == (byte) 0x02)
				&& ( (sysexmessage[6] == (byte) 0x04)
						|| (sysexmessage[6] == (byte) 0x17))) {
			byte chn = sysexmessage[8];
			if (sysexmessage[6] == (byte)0x17 ) {
				chn *= 2;
				chn += 32;
			}
			final byte pos = sysexmessage[7];
			final char name = (char) sysexmessage[12];
			logger.log(Level.FINE, String.format("CH %d - Received Char %s at pos %d ", chn, name, pos));
			// length-check to avoid an 'OutOfBoundsException' when the layer changes
			if (pos <= 3) { 
				this.yammi.updateChannelName(chn, pos, name);
				this.yammi.fireChange(new MsgChannelNameChanged(chn, this.yammi.getInternalChannelName(chn)));
			}
			return;
		}

		if (sysexmessage[5] == 0x03 &&
				sysexmessage[6] == 0x23 &&
				sysexmessage[7] == 0x00 &&
				sysexmessage[8] == 0x00) {
			// Mixer keeps requesting samplerate, didn't find out why
			// (probably just SM)
			if (this.yammi.getSamplerate() == SampleRate.R_UNDEFINED) {
				// catch samplingrate
				final FormatStringSysex srMsg = new FormatStringSysex("F0 43 10 3E" +
						/* mixer id */ "{b}" +
						/* fixed bytes */ "03 23 00 00" +
						/* samplerate */ "{i}" +
						/* STOP */ "F7");

				final Object[] srParameters = srMsg.parseMessage(sysexmessage);

				if (((Byte)srParameters[0] == (byte) 0x06)
						|| ((Byte)srParameters[0] == (byte) 0x0B)
						|| ((Byte)srParameters[0] == (byte) 0x0C)
						|| ((Byte)srParameters[0] == (byte) 0x0D)) {

					final int sampleRateNumber = (Integer)srParameters[1];
					final SampleRate sr = YamahaSampleRateAdapter.adapt(sampleRateNumber);
					if (this.yammi.getSamplerate() != sr) {
						this.yammi.setSamplerate(sampleRateNumber);
						this.yammi.setLabelMaker(new GenericMidsizeLabelDataProvider(this.yammi.getSamplerate()));
						// log until the samplerate is save
						logger.log(Level.INFO, "Samplerate set to :" + this.yammi.getSamplerate() + " Hz.");
					}
				}
			}
		}

		final FormatStringSysex parser = new FormatStringSysex("F0 43 10 3E 7F 01" +
				/* element */ "{b}" +
				/* parameter */ "{b}" +
				/* channel */ "{b}" + 
				/* value */ "{-i}" + 
				/* STOP */ "F7");

		final Object[] parameters = parser.parseMessage(sysexmessage);

		final byte elementNo = ((Byte)parameters[0]);
		final byte parameter = ((Byte)parameters[1]);
		final byte channel = ((Byte)parameters[2]);
		final int value = ((Integer)parameters[3]);



		try {
			if (elementNo == GenericMidsizeSysexParameter.ELMT_STEREO_FADER) {
				/* master level */
				final float intValue = (value) / YAMAHA_FADER_STEPS;
				this.yammi.fireChange(new MsgMasterLevelChanged(intValue));
			} else if (elementNo == GenericMidsizeSysexParameter.ELMT_AUX_FADER) {
				/* AUX Master */
				final float intValue = (value) / YAMAHA_FADER_STEPS;
				this.yammi.fireChange(new MsgAuxMasterLevelChanged(channel, intValue));
			} else if (elementNo == GenericMidsizeSysexParameter.ELMT_BUS_FADER) {
				/* BUS Master */
				final float intValue = (value) / YAMAHA_FADER_STEPS;
				this.yammi.fireChange(new MsgBusMasterLevelChanged(channel, intValue));
				final String label = this.yammi.getLabelMaker().getYamahaLabelLevel10Db(intValue);
				this.yammi.fireChange(new MsgBusMasterLevelLabel(channel, label));
			} else if (elementNo == GenericMidsizeSysexParameter.ELMT_INPUT_FADER  && (parameter == 0)) {
				/* channel Level */
				final float intValue = (value) / YAMAHA_FADER_STEPS;
				final String label = this.yammi.getLabelMaker().getYamahaLabelLevel10Db(intValue);
				this.yammi.fireChange(new MsgChannelLevelChanged(channel, intValue));
				this.yammi.fireChange(new MsgChannelLevelLabel(channel, 0, label));
			} else if (elementNo == GenericMidsizeSysexParameter.ELMT_INPUT_AUX) {
				if ((parameter + 1) % 3 == 0) {
					/* AUX Send level */
					final byte aux = (byte) ((parameter + 1) / 3 - 1);
					final float intValue = (value) / YAMAHA_FADER_STEPS;
					final String label = this.yammi.getLabelMaker().getYamahaLabelLevel10Db(intValue);
					this.yammi.fireChange(new MsgAuxSendChanged(channel, aux, intValue));
					this.yammi.fireChange(new MsgChannelAuxLevelLabel(channel, aux, label));
				} else if ((parameter + 1) % 3 == 1) {
					/* AUX SendOn (Mute) */ 
					final byte aux = (byte) ((parameter + 3) / 3 - 1);
					this.yammi.fireChange(new MsgAuxSendOnChanged(channel, aux, (value == 1)));
				}
			} else if (elementNo == GenericMidsizeSysexParameter.ELMT_INPUT_ON) {
				this.yammi.fireChange(new MsgChannelOnChanged(channel, (value == 1)));
			} else if (elementNo == GenericMidsizeSysexParameter.ELMT_INPUT_PAN) {
				int normValue = value;
				if (value > 100) {
					normValue = value - 268435456;
				}
				final float intValue = (float) (normValue) / 63;
				final String label = this.yammi.getLabelMaker().getYamahaLabelPanning(intValue);
				this.yammi.fireChange(new MsgInputPan(channel, intValue));
				this.yammi.fireChange(new MsgInputPanLabel(channel, 0, label));
			} else if (elementNo == GenericMidsizeSysexParameter.ELMT_AUX_DELAY && (parameter == 1)) {
				this.yammi.fireChange(new MsgAuxMasterDelayChanged(channel, (value)/(float)(this.yammi.getSamplerate().getValue())));
			} else if (elementNo == GenericMidsizeSysexParameter.ELMT_BUS_DELAY && (parameter == 1)) {
				this.yammi.fireChange(new MsgBusMasterDelayChanged(channel, (value)/(float)(this.yammi.getSamplerate().getValue())));
			} else if (elementNo == GenericMidsizeSysexParameter.ELMT_INPUT_PEQ) {
				if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_MODE) {
					this.yammi.fireChange(new MsgInputPeqModeChanged(channel, (value == 1)));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_EQON) {
					this.yammi.fireChange(new MsgInputPeqOnChanged(channel, (value == 1)));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_HPFON) {
					this.yammi.fireChange(new MsgInputPeqHPFChanged(channel, (value == 1)));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_LPF0N) {
					this.yammi.fireChange(new MsgInputPeqLPFChanged(channel, (value == 1)));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_LOWQ) {

					String label = "";
					// separate filter type from quality
					if (value > 40) {
						label = this.yammi.getLabelMaker().getYamahaLabelFilterType(value - 41);
						this.yammi.fireChange(new MsgInputPeqFilterType(channel, GenericMidsizeSysexParameter.PEQ_LOW_BAND, value - 41));
					} else {
						label = this.yammi.getLabelMaker().getYamahaLabelPeqQ(value);
						final float toOscValue = (float) (value) / (float) (40);
						this.yammi.fireChange(new MsgInputPeqQ(channel, GenericMidsizeSysexParameter.PEQ_LOW_BAND, toOscValue));
					}
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, GenericMidsizeSysexParameter.PEQ_LOW_BAND, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_LOWF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqF(value);
					this.yammi.fireChange(new MsgInputPeqF(channel, GenericMidsizeSysexParameter.PEQ_LOW_BAND, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, GenericMidsizeSysexParameter.PEQ_LOW_BAND, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_LOWG) {
					int normValue = value;
					if (value > 200) {
						normValue = value - 268435456;
					}
					final float intValue = (float) (normValue) / (float) (180);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, GenericMidsizeSysexParameter.PEQ_LOW_BAND, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, GenericMidsizeSysexParameter.PEQ_LOW_BAND, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_LOWMIDQ) {
					final float intValue = (float) (value) / (float) (40);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqQ(value);
					this.yammi.fireChange(new MsgInputPeqQ(channel, GenericMidsizeSysexParameter.PEQ_LOWMID_BAND, intValue));
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, GenericMidsizeSysexParameter.PEQ_LOWMID_BAND, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_LOWMIDF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqF(value);
					this.yammi.fireChange(new MsgInputPeqF(channel, GenericMidsizeSysexParameter.PEQ_LOWMID_BAND, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, GenericMidsizeSysexParameter.PEQ_LOWMID_BAND, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_LOWMIDG) {
					int normValue = value;
					if (value > 200) {
						normValue = value - 268435456;
					}
					final float intValue = (float) (normValue) / (float) (180);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, GenericMidsizeSysexParameter.PEQ_LOWMID_BAND, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, GenericMidsizeSysexParameter.PEQ_LOWMID_BAND, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_HIMIDQ) {
					final float intValue = (float) (value) / (float) (40);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqQ(value);
					this.yammi.fireChange(new MsgInputPeqQ(channel, GenericMidsizeSysexParameter.PEQ_HIMID_BAND, intValue));
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, GenericMidsizeSysexParameter.PEQ_HIMID_BAND, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_HIMIDF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqF(value);
					this.yammi.fireChange(new MsgInputPeqF(channel, GenericMidsizeSysexParameter.PEQ_HIMID_BAND, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, GenericMidsizeSysexParameter.PEQ_HIMID_BAND, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_HIMIDG) {
					int normValue = value;
					if (value > 200) {
						normValue = value - 268435456;
					}
					final float intValue = (float) (normValue) / (float) (180);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, GenericMidsizeSysexParameter.PEQ_HIMID_BAND, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, GenericMidsizeSysexParameter.PEQ_HIMID_BAND, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_HIQ) {

					String label = "";
					// separate filter type from quality
					if (value > 40) {
						label = this.yammi.getLabelMaker().getYamahaLabelFilterType(value - 41);
						this.yammi.fireChange(new MsgInputPeqFilterType(channel, GenericMidsizeSysexParameter.PEQ_HI_BAND, value - 41));
					} else {
						label = this.yammi.getLabelMaker().getYamahaLabelPeqQ(value);
						final float toOscValue = (float) (value) / (float) (40);
						this.yammi.fireChange(new MsgInputPeqQ(channel, GenericMidsizeSysexParameter.PEQ_HI_BAND, toOscValue));
					}
					this.yammi.fireChange(new MsgInputPeqQLabel(channel, GenericMidsizeSysexParameter.PEQ_HI_BAND, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_HIF) {
					final float intValue = (float) (value - 5) / (float) (119);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqF(value);
					this.yammi.fireChange(new MsgInputPeqF(channel, GenericMidsizeSysexParameter.PEQ_HI_BAND, intValue));
					this.yammi.fireChange(new MsgInputPeqFLabel(channel, GenericMidsizeSysexParameter.PEQ_HI_BAND, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_PEQ_HIG) {
					// normValue is actually "(byte)(value & 0xFF)" (first byte of integer)
					int normValue = value;
					if (value > 200) {
						normValue = value - 268435456;
					}
					final float intValue = (float) (normValue) / (float) (180);
					final String label = this.yammi.getLabelMaker().getYamahaLabelPeqG(intValue);
					this.yammi.fireChange(new MsgInputPeqG(channel, GenericMidsizeSysexParameter.PEQ_HI_BAND, intValue));
					this.yammi.fireChange(new MsgInputPeqGLabel(channel, GenericMidsizeSysexParameter.PEQ_HI_BAND, label));
				}
			} else if (elementNo == GenericMidsizeSysexParameter.ELMT_INPUT_GATE) {
				// gate related messages
				if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_GATE_ATTACK) {
					final float intValue = (float) (value) / (float) (120);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaAttack(intValue);
					this.yammi.fireChange(new MsgInputDynaAttack(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaAttackLabel(0, channel, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_GATE_DECAY) {
					final float intValue = (float) (value) / (float) (159);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaDecayRelease(intValue);
					this.yammi.fireChange(new MsgInputDynaDecayRelease(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaDecayReleaseLabel(0, channel, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_GATE_HOLD) {
					final float intValue = (float) (value) / (float) (215);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaHold(intValue);
					this.yammi.fireChange(new MsgInputDynaHold(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaHoldLabel(0, channel, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_GATE_KEY_AUX) {
					/** not implemented yet **/
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_GATE_KEY_CH) {
					/** not implemented yet **/
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_GATE_KEY_IN) {
					final float intValue = (float) (value) / (float) (2);
					this.yammi.fireChange(new MsgInputDynaKeyIn(0, channel, intValue));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_GATE_LINK) {
					this.yammi.fireChange(new MsgInputDynaPair(0, channel, (value == 1)));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_GATE_ON) {
					this.yammi.fireChange(new MsgInputDynaOn(0, channel, (value == 1)));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_GATE_RANGE) {
					final float intValue = 1.0f + ((float) (value) / (float) (70));
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaRange(intValue);
					this.yammi.fireChange(new MsgInputDynaRange(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaRangeLabel(0, channel, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_GATE_THRESHOLD) {
					final float intValue = 1.0f + ((float) (value) / (float) (540));
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaThreshold(intValue);
					this.yammi.fireChange(new MsgInputDynaThreshold(0, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaThresholdLabel(0, channel, label));
				}
			} else if (elementNo == GenericMidsizeSysexParameter.ELMT_INPUT_COMP) {
				// compressor related messages
				if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_COMP_ATTACK) {
					final float intValue = (float) (value) / (float) (120);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaAttack(intValue);
					this.yammi.fireChange(new MsgInputDynaAttack(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaAttackLabel(1, channel, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_COMP_GAIN) {
					final float intValue = (float) (value) / (float) (180);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaGain(intValue);
					this.yammi.fireChange(new MsgInputDynaGain(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaGainLabel(1, channel, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_COMP_KNEE) {
					// knee-range in the sysex spec is 0-89 (but it's actually 0-5)
					final float intValue = (float) (value) / (float) (5);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaKnee(intValue);
					this.yammi.fireChange(new MsgInputDynaKnee(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaKneeLabel(1, channel, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_COMP_LINK) {
					this.yammi.fireChange(new MsgInputDynaPair(1, channel, (value == 1)));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_COMP_LOC) {
					/** not implemented yet **/
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_COMP_ON) {
					this.yammi.fireChange(new MsgInputDynaOn(1, channel, (value ==1)));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_COMP_RATIO) {
					final float intValue = (float) (value) / (float) (15);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaRatio(intValue);
					this.yammi.fireChange(new MsgInputDynaRatio(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaRatioLabel(1, channel, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_COMP_RELEASE) {
					final float intValue = (float) (value) / (float) (159);
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaDecayRelease(intValue);
					this.yammi.fireChange(new MsgInputDynaDecayRelease(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaDecayReleaseLabel(1, channel, label));
				} else if (parameter == GenericMidsizeSysexParameter.PARAM_INPUT_COMP_THRESHOLD) {
					final float intValue = 1.0f + ((float) (value) / (float) (540));
					final String label = this.yammi.getLabelMaker().getYamahaLabelDynaThreshold(intValue);
					this.yammi.fireChange(new MsgInputDynaThreshold(1, channel, intValue));
					this.yammi.fireChange(new MsgInputDynaThresholdLabel(1, channel, label));
				}

				// input mute groups
			} else if (elementNo == GenericMidsizeSysexParameter.ELMT_INPUT_GROUP) {
				int group = parameter;
				// check for mute group
				if (parameter >= 0x08 && parameter <= 0x0F) {
					group -= 7;
					this.yammi.fireChange(new MsgInputGroups(1, group, (value == 1)));
				}
				// output groups	
			} else if (elementNo == GenericMidsizeSysexParameter.ELMT_OUTPUT_GROUP) {
				int group = parameter;
				// check for mute group
				if (parameter >= 0x04 && parameter <= 0x07) {
					group -= 3;
					this.yammi.fireChange(new MsgInputGroups(1, group, (value == 1)));
				}

			} else if (elementNo == GenericMidsizeSysexParameter.ELMT_SETUP_DIO) {
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
