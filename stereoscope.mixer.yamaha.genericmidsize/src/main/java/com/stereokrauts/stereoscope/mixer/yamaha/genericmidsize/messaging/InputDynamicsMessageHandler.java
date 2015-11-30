package com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.messaging;

import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.GenericMidsizeMixer;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeSysexParameter;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;
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
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaAttackLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaDecayReleaseLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaGainLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaHoldLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaKneeLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaRangeLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaRatioLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaThresholdLabel;

public class InputDynamicsMessageHandler extends HandlerForGenericMidsize implements IMessageHandler {
	public InputDynamicsMessageHandler(final GenericMidsizeMixer mixer) {
		super(mixer);
	}
	
	@Override
	public boolean handleMessage(final IMessage msg) {
		final int chn = ((AbstractInputDynamicsMessage<?>) msg).getChannel();
		final int dyna = ((AbstractInputDynamicsMessage<?>) msg).getProcessor();
		// set element byte (dyna1 or dyna2)
		final byte element = (dyna == 0) ? 
				GenericMidsizeSysexParameter.ELMT_INPUT_GATE :
					GenericMidsizeSysexParameter.ELMT_INPUT_COMP;
		if (msg instanceof MsgInputDynaOn) {
			final MsgInputDynaOn msgInputDynaOn = (MsgInputDynaOn) msg;
			this.mixer.getModifier().changedInputDynaOn(chn, element, msgInputDynaOn.getAttachment());
			return true;
		} else if (msg instanceof MsgInputDynaKeyIn) {
			// just gate
			final MsgInputDynaKeyIn msgInputDynaKeyIn = (MsgInputDynaKeyIn) msg;
			this.mixer.getModifier().changedInputDynaKeyIn(chn, msgInputDynaKeyIn.getAttachment());
			return true;
		} else if (msg instanceof MsgInputDynaAttack) {
			final MsgInputDynaAttack msgInputDynaAttack = (MsgInputDynaAttack) msg;
			this.mixer.getModifier().changedInputDynaAttack(chn, element, msgInputDynaAttack.getAttachment());
			final String label = this.mixer.getLabelMaker().getYamahaLabelDynaAttack(msgInputDynaAttack.getAttachment());
			this.mixer.fireChange(new MsgInputDynaAttackLabel(dyna, chn, label));
			return true;
		} else if (msg instanceof MsgInputDynaRange) {
			// just gate
			final MsgInputDynaRange msgInputDynaRange = (MsgInputDynaRange) msg;
			this.mixer.getModifier().changedInputDynaRange(chn, msgInputDynaRange.getAttachment());
			final String label = this.mixer.getLabelMaker().getYamahaLabelDynaRange(msgInputDynaRange.getAttachment());
			this.mixer.fireChange(new MsgInputDynaRangeLabel(dyna, chn, label));
			return true;
		} else if (msg instanceof MsgInputDynaHold) {
			// just gate
			final MsgInputDynaHold msgInputDynaHold = (MsgInputDynaHold) msg;
			this.mixer.getModifier().changedInputDynaHold(chn, msgInputDynaHold.getAttachment());
			final String label = this.mixer.getLabelMaker().getYamahaLabelDynaHold(msgInputDynaHold.getAttachment());
			this.mixer.fireChange(new MsgInputDynaHoldLabel(dyna, chn, label));
			return true;
		} else if (msg instanceof MsgInputDynaDecayRelease) {
			final MsgInputDynaDecayRelease msgInputDynaDecayRelease = (MsgInputDynaDecayRelease) msg;
			this.mixer.getModifier().changedInputDynaReleaseDecay(chn, element, msgInputDynaDecayRelease.getAttachment());
			final String label = this.mixer.getLabelMaker().getYamahaLabelDynaDecayRelease(msgInputDynaDecayRelease.getAttachment());
			this.mixer.fireChange(new MsgInputDynaDecayReleaseLabel(dyna, chn, label));
			return true;
		} else if (msg instanceof MsgInputDynaRatio) {
			// just compressor
			final MsgInputDynaRatio msgInputDynaRatio = (MsgInputDynaRatio) msg;
			this.mixer.getModifier().changedInputDynaRatio(chn, msgInputDynaRatio.getAttachment());
			final String label = this.mixer.getLabelMaker().getYamahaLabelDynaRatio(msgInputDynaRatio.getAttachment());
			this.mixer.fireChange(new MsgInputDynaRatioLabel(dyna, chn, label));
			return true;
		} else if (msg instanceof MsgInputDynaGain) {
			// just compressor
			final MsgInputDynaGain msgInputDynaGain = (MsgInputDynaGain) msg;
			this.mixer.getModifier().changedInputDynaGain(chn, msgInputDynaGain.getAttachment());
			final String label = this.mixer.getLabelMaker().getYamahaLabelDynaGain(msgInputDynaGain.getAttachment());
			this.mixer.fireChange(new MsgInputDynaGainLabel(dyna, chn, label));
			return true;
		} else if (msg instanceof MsgInputDynaKnee) {
			// just compressor
			final MsgInputDynaKnee msgInputDynaKnee = (MsgInputDynaKnee) msg;
			this.mixer.getModifier().changedInputDynaKnee(chn, msgInputDynaKnee.getAttachment());
			final String label = this.mixer.getLabelMaker().getYamahaLabelDynaKnee(msgInputDynaKnee.getAttachment());
			this.mixer.fireChange(new MsgInputDynaKneeLabel(dyna, chn, label));
			return true;
		} else if (msg instanceof MsgInputDynaThreshold) {
			final MsgInputDynaThreshold msgInputDynaThreshold = (MsgInputDynaThreshold) msg;
			this.mixer.getModifier().changedInputDynaThreshold(chn, element, msgInputDynaThreshold.getAttachment());
			final String label = this.mixer.getLabelMaker().getYamahaLabelDynaThreshold(msgInputDynaThreshold.getAttachment());
			this.mixer.fireChange(new MsgInputDynaThresholdLabel(dyna, chn, label));
			return true;
		}
		return false;
	}
}
