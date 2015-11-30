package com.stereokrauts.stereoscope.mixer.behringer.ddx3216;

import model.mixer.interfaces.IAmMixer;
import model.mixer.interfaces.IUnderstandMixer;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.model.messaging.message.control.MsgSceneChange;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputPan;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelAuxLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPanLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqFLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqGLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqQLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqF;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;



public class DDX3216MidiReceiver extends IUnderstandMixer {
	private static final float BEHRINGER_FADER_STEPS = (1496);
	private static final SLogger LOG = StereoscopeLogManager.getLogger(DDX3216MidiReceiver.class);

	public DDX3216MidiReceiver(final IAmMixer partner) {
		super(partner);
		this.theMixer = (DDX3216Mixer) partner;
	}


	private final DDX3216Mixer theMixer;


	@Override
	public final void handleSysex(final byte[] sysexmessage) {
		if ((sysexmessage.length - 13) % 4 != 0) {
			return;
		}

		StereoscopeLogManager.getLogger(this.getClass()).finest("CLEARMESSAGE: " + ByteStringConversion.toHex(sysexmessage));

		this.theMixer.getCommunicationAware().transmit();

		final byte numberOfChanged = sysexmessage[7];

		for (int i = 0; i < numberOfChanged; i++) {
			final byte channel = sysexmessage[8 + i*4];
			final byte parameter = (sysexmessage[9 + i*4]);
			final short value = (short) ((sysexmessage[10 + i*4] << 7) | (sysexmessage[11 + i*4]));

			try {
				if (channel == DDX3216SysexParameter.SECTION_MASTER
						&& parameter == DDX3216SysexParameter.PARAM_LEVEL) {
					/* master level */
					final float intValue = (value) / BEHRINGER_FADER_STEPS;
					this.theMixer.fireChange(new MsgMasterLevelChanged(intValue));
				} else if (channel == DDX3216SysexParameter.SECTION_AUX1_MASTER
						&& parameter == DDX3216SysexParameter.PARAM_LEVEL) {
					/* AUX Master */
					final float intValue = (value) / BEHRINGER_FADER_STEPS;
					this.theMixer.fireChange(new MsgAuxMasterLevelChanged(0, intValue));
				} else if (channel == DDX3216SysexParameter.SECTION_AUX2_MASTER
						&& parameter == DDX3216SysexParameter.PARAM_LEVEL) {
					/* AUX Master */
					final float intValue = (value) / BEHRINGER_FADER_STEPS;
					this.theMixer.fireChange(new MsgAuxMasterLevelChanged(1, intValue));
				} else if (channel == DDX3216SysexParameter.SECTION_AUX3_MASTER
						&& parameter == DDX3216SysexParameter.PARAM_LEVEL) {
					/* AUX Master */
					final float intValue = (value) / BEHRINGER_FADER_STEPS;
					this.theMixer.fireChange(new MsgAuxMasterLevelChanged(2, intValue));
				} else if (channel == DDX3216SysexParameter.SECTION_AUX4_MASTER
						&& parameter == DDX3216SysexParameter.PARAM_LEVEL) {
					/* AUX Master */
					final float intValue = (value) / BEHRINGER_FADER_STEPS;
					this.theMixer.fireChange(new MsgAuxMasterLevelChanged(3, intValue));
				} else if (parameter == DDX3216SysexParameter.PARAM_BUS_LEVEL) {
					/* BUS Master */
					final float intValue = (value) / BEHRINGER_FADER_STEPS;
					this.theMixer.fireChange(new MsgBusMasterLevelChanged(channel, intValue));
				} else if (channel >= 0 && channel <= 31) {
					// input channel values
					this.handleInputChannelParameter(channel, parameter, value);
				}  else {
					throw new Exception();
				}
			} catch (final Exception e) {
				LOG.fine("Unknown: " + ByteStringConversion.toHex(sysexmessage));

			}
		}
	}

	public final void handleInputChannelParameter(final byte channel, final byte parameter, final short value) {
		if (parameter == DDX3216SysexParameter.PARAM_LEVEL) {
			/* channel Level */
			final float intValue = (value) / BEHRINGER_FADER_STEPS;
			this.theMixer.fireChange(new MsgChannelLevelChanged(channel, intValue));
			final String label = this.theMixer.labelMaker.getLabelLevel(intValue);
			this.theMixer.fireChange(new MsgChannelLevelLabel(channel, 0, label));
		} else if (channel >= 0 && channel <= 31
				&& parameter == DDX3216SysexParameter.PARAM_AUX1_LEVEL) {
			final float intValue = (value) / BEHRINGER_FADER_STEPS;
			final String label = this.theMixer.labelMaker.getLabelLevel(intValue);
			this.theMixer.fireChange(new MsgAuxSendChanged(channel, 0, intValue));
			this.theMixer.fireChange(new MsgChannelAuxLevelLabel(channel, 0, label));
		} else if (channel >= 0 && channel <= 31
				&& parameter == DDX3216SysexParameter.PARAM_AUX2_LEVEL) {
			final float intValue = (value) / BEHRINGER_FADER_STEPS;
			final String label = this.theMixer.labelMaker.getLabelLevel(intValue);
			this.theMixer.fireChange(new MsgAuxSendChanged(channel, 1, intValue));
			this.theMixer.fireChange(new MsgChannelAuxLevelLabel(channel, 1, label));
		} else if (channel >= 0 && channel <= 31
				&& parameter == DDX3216SysexParameter.PARAM_AUX3_LEVEL) {
			final float intValue = (value) / BEHRINGER_FADER_STEPS;
			final String label = this.theMixer.labelMaker.getLabelLevel(intValue);
			this.theMixer.fireChange(new MsgAuxSendChanged(channel, 2, intValue));
			this.theMixer.fireChange(new MsgChannelAuxLevelLabel(channel, 2, label));
		} else if (channel >= 0 && channel <= 31
				&& parameter == DDX3216SysexParameter.PARAM_AUX4_LEVEL) {
			final float intValue = (value) / BEHRINGER_FADER_STEPS;
			final String label = this.theMixer.labelMaker.getLabelLevel(intValue);
			this.theMixer.fireChange(new MsgAuxSendChanged(channel, 3, intValue));
			this.theMixer.fireChange(new MsgChannelAuxLevelLabel(channel, 3, label));
		} else if (channel >= 0 && channel <= 31
				&& parameter == DDX3216SysexParameter.PARAM_MUTE) {
			this.theMixer.fireChange(new MsgChannelOnChanged(channel, value == 1 ? true : false));
		} else if (channel >= 0 && channel <= 31
				&& parameter == DDX3216SysexParameter.PARAM_PAN) {
			final float intValue = (float) (value) / 30 -1;
			this.theMixer.fireChange(new MsgInputPan(channel, intValue));
			final String label = this.theMixer.labelMaker.getLabelPan(intValue);
			this.theMixer.fireChange(new MsgInputPanLabel(channel, 0, label));
			/* PARAMETRIC EQUALIZER */
		} else if (parameter == DDX3216SysexParameter.PARAM_PEQ_EQON) {
			this.theMixer.fireChange(new MsgInputPeqOnChanged(channel, (value == 1)));
		} else if (parameter == DDX3216SysexParameter.PARAM_PEQ_HPF_ON) {
			this.theMixer.fireChange(new MsgInputPeqOnChanged(channel, (value == 1)));
		} else if (parameter == DDX3216SysexParameter.PARAM_PEQ_BAND1_Q) {
			final float intValue = (float) (value) / (float) (40);
			final String label = this.theMixer.labelMaker.getLabelPeqQ(intValue);
			this.theMixer.fireChange(new MsgInputPeqQ(channel, DDX3216SysexParameter.PEQ_BAND1, intValue));
			this.theMixer.fireChange(new MsgInputPeqQLabel(channel, DDX3216SysexParameter.PEQ_BAND1, label));
		} else if (parameter == DDX3216SysexParameter.PARAM_PEQ_BAND1_F) {
			final float intValue = (float) (value) / (float) (159);
			final String label = this.theMixer.labelMaker.getLabelPeqF(intValue);
			this.theMixer.fireChange(new MsgInputPeqF(channel, DDX3216SysexParameter.PEQ_BAND1, intValue));
			this.theMixer.fireChange(new MsgInputPeqFLabel(channel, DDX3216SysexParameter.PEQ_BAND1, label));
		} else if (parameter == DDX3216SysexParameter.PARAM_PEQ_BAND1_G) {
			float intValue = (float) (value) / (float) (72);
			intValue = (intValue - 0.5f) * 2;
			final String label = this.theMixer.labelMaker.getLabelPeqG(intValue);
			this.theMixer.fireChange(new MsgInputPeqG(channel, DDX3216SysexParameter.PEQ_BAND1, intValue));
			this.theMixer.fireChange(new MsgInputPeqGLabel(channel, DDX3216SysexParameter.PEQ_BAND1, label));
		} else if (parameter == DDX3216SysexParameter.PARAM_PEQ_BAND2_Q) {
			final float intValue = (float) (value) / (float) (40);
			final String label = this.theMixer.labelMaker.getLabelPeqQ(intValue);
			this.theMixer.fireChange(new MsgInputPeqQ(channel, DDX3216SysexParameter.PEQ_BAND2, intValue));
			this.theMixer.fireChange(new MsgInputPeqQLabel(channel, DDX3216SysexParameter.PEQ_BAND2, label));
		} else if (parameter == DDX3216SysexParameter.PARAM_PEQ_BAND2_F) {
			final float intValue = (float) (value) / (float) (159);
			final String label = this.theMixer.labelMaker.getLabelPeqF(intValue);
			this.theMixer.fireChange(new MsgInputPeqF(channel, DDX3216SysexParameter.PEQ_BAND2, intValue));
			this.theMixer.fireChange(new MsgInputPeqFLabel(channel, DDX3216SysexParameter.PEQ_BAND2, label));
		} else if (parameter == DDX3216SysexParameter.PARAM_PEQ_BAND2_G) {
			float intValue = (float) (value) / (float) (72);
			intValue = (intValue - 0.5f) * 2;
			final String label = this.theMixer.labelMaker.getLabelPeqG(intValue);
			this.theMixer.fireChange(new MsgInputPeqG(channel, DDX3216SysexParameter.PEQ_BAND2, intValue));
			this.theMixer.fireChange(new MsgInputPeqGLabel(channel, DDX3216SysexParameter.PEQ_BAND2, label));
		} else if (parameter == DDX3216SysexParameter.PARAM_PEQ_BAND3_Q) {
			final float intValue = (float) (value) / (float) (40);
			final String label = this.theMixer.labelMaker.getLabelPeqQ(intValue);
			this.theMixer.fireChange(new MsgInputPeqQ(channel, DDX3216SysexParameter.PEQ_BAND3, intValue));
			this.theMixer.fireChange(new MsgInputPeqQLabel(channel, DDX3216SysexParameter.PEQ_BAND3, label));
		} else if (parameter == DDX3216SysexParameter.PARAM_PEQ_BAND3_F) {
			final float intValue = (float) (value) / (float) (159);
			final String label = this.theMixer.labelMaker.getLabelPeqF(intValue);
			this.theMixer.fireChange(new MsgInputPeqF(channel, DDX3216SysexParameter.PEQ_BAND3, intValue));
			this.theMixer.fireChange(new MsgInputPeqFLabel(channel, DDX3216SysexParameter.PEQ_BAND3, label));
		} else if (parameter == DDX3216SysexParameter.PARAM_PEQ_BAND3_G) {
			float intValue = (float) (value) / (float) (72);
			intValue = (intValue - 0.5f) * 2;
			final String label = this.theMixer.labelMaker.getLabelPeqG(intValue);
			this.theMixer.fireChange(new MsgInputPeqG(channel, DDX3216SysexParameter.PEQ_BAND3, intValue));
			this.theMixer.fireChange(new MsgInputPeqGLabel(channel, DDX3216SysexParameter.PEQ_BAND3, label));
		} else if (parameter == DDX3216SysexParameter.PARAM_PEQ_BAND4_Q) {
			final float intValue = (float) (value) / (float) (40);
			final String label = this.theMixer.labelMaker.getLabelPeqQ(intValue);
			this.theMixer.fireChange(new MsgInputPeqQ(channel, DDX3216SysexParameter.PEQ_BAND4, intValue));
			this.theMixer.fireChange(new MsgInputPeqQLabel(channel, DDX3216SysexParameter.PEQ_BAND4, label));
		} else if (parameter == DDX3216SysexParameter.PARAM_PEQ_BAND4_F) {
			final float intValue = (float) (value) / (float) (159);
			final String label = this.theMixer.labelMaker.getLabelPeqF(intValue);
			this.theMixer.fireChange(new MsgInputPeqF(channel, DDX3216SysexParameter.PEQ_BAND4, intValue));
			this.theMixer.fireChange(new MsgInputPeqFLabel(channel, DDX3216SysexParameter.PEQ_BAND4, label));
		} else if (parameter == DDX3216SysexParameter.PARAM_PEQ_BAND4_G) {
			float intValue = (float) (value) / (float) (72);
			intValue = (intValue - 0.5f) * 2;
			final String label = this.theMixer.labelMaker.getLabelPeqG(intValue);
			this.theMixer.fireChange(new MsgInputPeqG(channel, DDX3216SysexParameter.PEQ_BAND4, intValue));
			this.theMixer.fireChange(new MsgInputPeqGLabel(channel, DDX3216SysexParameter.PEQ_BAND4, label));
		}
	}

	@Override
	public final void handleProgramChange(final int newProgram) {
		this.theMixer.fireChange(new MsgSceneChange(newProgram, "<unknown>"));
	}
}
