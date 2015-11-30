package com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging;

import com.stereokrauts.stereoscope.mixer.yamaha.ls9.Ls9Mixer;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.midi.Ls9SysexParameter;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAttack;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAutoOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaDecayRelease;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterFreq;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterQ;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterType;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaGain;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaHold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKeyIn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKnee;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaLeftSideChain;
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


public class InputDynamicsMessageHandler extends HandlerForLs9 implements
		IMessageHandler {

	public InputDynamicsMessageHandler(final Ls9Mixer mixer) {
		super(mixer);
	}

	
	/**
	 * This methods receives all dynamics related messages
	 * of the current input from the function handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	@Override
	public final boolean handleMessage(final IMessage msg) {
		final int chn = ((AbstractInputDynamicsMessage<?>) msg).getChannel();
		final int dyna = ((AbstractInputDynamicsMessage<?>) msg).getProcessor();
		// set element byte (dyna1 or dyna2)
		final byte element = (dyna == 0)
				? Ls9SysexParameter.ELMT_INPUT_DYNA1
					: Ls9SysexParameter.ELMT_INPUT_DYNA2;
			
		if (msg instanceof MsgInputDynaOn) {
			final MsgInputDynaOn msgInputDynaOn = (MsgInputDynaOn) msg;
			this.mixer.getModifier().changedInputDynaOn(chn, element, msgInputDynaOn.getAttachment());
			return true;
		} else if (msg instanceof MsgInputDynaAutoOn) {
			// not implemented yet
			return false;
		} else if (msg instanceof MsgInputDynaLeftSideChain) {
			// not implemented yet
			return false;
		} else if (msg instanceof MsgInputDynaKeyIn) {
			final MsgInputDynaKeyIn msgInputDynaKeyIn = (MsgInputDynaKeyIn) msg;
			this.mixer.getModifier().changedInputDynaKeyIn(chn, element, msgInputDynaKeyIn.getAttachment());
		} else if (msg instanceof MsgInputDynaFilterOn) {
			// not implemented yet
			return false;
		} else if (msg instanceof MsgInputDynaFilterType) {
			// not implemented yet
			return false;
		} else if (msg instanceof MsgInputDynaFilterFreq) {
			// not implemented yet
			return false;
		} else if (msg instanceof MsgInputDynaFilterQ) {
			// not implemented yet
			return false;
		} else if (msg instanceof MsgInputDynaAttack) {
			final MsgInputDynaAttack msgInputDynaAttack = (MsgInputDynaAttack) msg;
			this.mixer.getModifier().changedInputDynaAttack(chn, element, msgInputDynaAttack.getAttachment());
			final String label = this.mixer.getLabelMaker().getYamahaLabelDynaAttack(msgInputDynaAttack.getAttachment());
			this.mixer.fireChange(new MsgInputDynaAttackLabel(dyna, chn, label));
			return true;
		} else if (msg instanceof MsgInputDynaRange) {
			final MsgInputDynaRange msgInputDynaRange = (MsgInputDynaRange) msg;
			this.mixer.getModifier().changedInputDynaRange(chn, element, msgInputDynaRange.getAttachment());
			final String label = this.mixer.getLabelMaker().getYamahaLabelDynaRange(msgInputDynaRange.getAttachment());
			this.mixer.fireChange(new MsgInputDynaRangeLabel(dyna, chn, label));
			return true;
		} else if (msg instanceof MsgInputDynaHold) {
			final MsgInputDynaHold msgInputDynaHold = (MsgInputDynaHold) msg;
			this.mixer.getModifier().changedInputDynaHold(chn, element, msgInputDynaHold.getAttachment());
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
			final MsgInputDynaRatio msgInputDynaRatio = (MsgInputDynaRatio) msg;
			this.mixer.getModifier().changedInputDynaRatio(chn, element, msgInputDynaRatio.getAttachment());
			final String label = this.mixer.getLabelMaker().getYamahaLabelDynaRatio(msgInputDynaRatio.getAttachment());
			this.mixer.fireChange(new MsgInputDynaRatioLabel(dyna, chn, label));
			return true;
		} else if (msg instanceof MsgInputDynaGain) {
			final MsgInputDynaGain msgInputDynaGain = (MsgInputDynaGain) msg;
			this.mixer.getModifier().changedInputDynaGain(chn, element, msgInputDynaGain.getAttachment());
			final String label = this.mixer.getLabelMaker().getYamahaLabelDynaGain(msgInputDynaGain.getAttachment());
			this.mixer.fireChange(new MsgInputDynaGainLabel(dyna, chn, label));
			return true;
		} else if (msg instanceof MsgInputDynaKnee) {
			final MsgInputDynaKnee msgInputDynaKnee = (MsgInputDynaKnee) msg;
			this.mixer.getModifier().changedInputDynaKnee(chn, element, msgInputDynaKnee.getAttachment());
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
