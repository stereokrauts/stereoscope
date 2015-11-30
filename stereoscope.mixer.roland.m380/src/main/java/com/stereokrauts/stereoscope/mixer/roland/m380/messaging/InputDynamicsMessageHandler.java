package com.stereokrauts.stereoscope.mixer.roland.m380.messaging;

import com.stereokrauts.stereoscope.mixer.roland.m380.M380Mixer;
import com.stereokrauts.stereoscope.mixer.roland.m380.midi.M380SysexParameter;
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


public class InputDynamicsMessageHandler extends HandlerForM380 implements
		IMessageHandler {

	public InputDynamicsMessageHandler(final M380Mixer mixer) {
		super(mixer);
	}

	
	/**
	 * This methods receives all dynamics related messages
	 * of the current input from the function handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	@Override
	public boolean handleMessage(final IMessage msg) {
		final int channel = ((AbstractInputDynamicsMessage<?>) msg).getChannel();
		final int dyna = ((AbstractInputDynamicsMessage<?>) msg).getProcessor();
		// separate dyna1 and dyna2
		if (dyna == 0) {
			if (msg instanceof MsgInputDynaOn) {
				final MsgInputDynaOn msgInputDynaOn = (MsgInputDynaOn) msg;
				final byte value = msgInputDynaOn.getAttachment() ? (byte) 1 : (byte) 0;
				this.mixer.getModifier().changedInputDynamicsParameterByte(channel,
						M380SysexParameter.CH_GATE_SWITCH, value);
				return true;
			} else if (msg instanceof MsgInputDynaKeyIn) {
				final MsgInputDynaKeyIn msgInputDynaKeyIn = (MsgInputDynaKeyIn) msg;
				final byte value = (byte) (msgInputDynaKeyIn.getAttachment() * 128);
				this.mixer.getModifier().changedInputDynamicsParameterByte(channel, M380SysexParameter.CH_GATE_KEY_IN, value);
			} else if (msg instanceof MsgInputDynaThreshold) {
				final MsgInputDynaThreshold msgInputDynaThreshold = (MsgInputDynaThreshold) msg;
				final short value = (short) ((msgInputDynaThreshold.getAttachment() * 800) - 800);
				this.mixer.getModifier().changedInputDynamicsParameterShort(channel,
						M380SysexParameter.CH_GATE_THRESHOLD, value);
				final String label = this.mixer.getLabelMaker().getGateThresholdLabel(msgInputDynaThreshold.getAttachment());
				this.mixer.fireChange(new MsgInputDynaThresholdLabel(dyna, channel, label));
				return true;
			} else if (msg instanceof MsgInputDynaRatio) {
				// works but is not supported in our TouchOSC Layout
				final MsgInputDynaRatio msgInputDynaRatio = (MsgInputDynaRatio) msg;
				final byte value = (byte) (msgInputDynaRatio.getAttachment() * 14);
				this.mixer.getModifier().changedInputDynamicsParameterByte(channel, M380SysexParameter.CH_GATE_RATIO, value);
				final String label = this.mixer.getLabelMaker().getReleaseHoldLabel(msgInputDynaRatio.getAttachment());
				this.mixer.fireChange(new MsgInputDynaRatioLabel(dyna, channel, label));
				return true;
			} else if (msg instanceof MsgInputDynaKnee) {
				// works but is not supported in our TouchOSC Layout
				final MsgInputDynaKnee msgInputDynaKnee = (MsgInputDynaKnee) msg;
				final byte value = (byte) (msgInputDynaKnee.getAttachment() * 9);
				this.mixer.getModifier().changedInputDynamicsParameterByte(channel, M380SysexParameter.CH_GATE_KNEE, value);
				final String label = this.mixer.getLabelMaker().getDynamicsKneeLabel(msgInputDynaKnee.getAttachment());
				this.mixer.fireChange(new MsgInputDynaKneeLabel(dyna, channel, label));
				return true;
			} else if (msg instanceof MsgInputDynaRange) {
				final MsgInputDynaRange msgInputDynaRange = (MsgInputDynaRange) msg;
				short shortValue = 0;
				if (msgInputDynaRange.getAttachment() == 0.0f) {
					shortValue = -8192;
				} else {
					shortValue =  (short) ((msgInputDynaRange.getAttachment() * 906) - 906);
				}
				this.mixer.getModifier().changedInputDynamicsParameterShort(channel, M380SysexParameter.CH_GATE_RANGE, shortValue);
				final String label = this.mixer.getLabelMaker().getGateRangeLabel(msgInputDynaRange.getAttachment());
				this.mixer.fireChange(new MsgInputDynaRangeLabel(dyna, channel, label));
				return true;
			} else if (msg instanceof MsgInputDynaAttack) {
				final MsgInputDynaAttack msgInputDynaAttack = (MsgInputDynaAttack) msg;
				final short value = (short) (msgInputDynaAttack.getAttachment() * 8000);
				this.mixer.getModifier().changedInputDynamicsParameterShort(channel, M380SysexParameter.CH_GATE_ATTACK, value);
				final String label = this.mixer.getLabelMaker().getAttackLabel(msgInputDynaAttack.getAttachment());
				this.mixer.fireChange(new MsgInputDynaAttackLabel(dyna, channel, label));
				return true;
			} else if (msg instanceof MsgInputDynaDecayRelease) {
				final MsgInputDynaDecayRelease msgInputDynaDecayRelease = (MsgInputDynaDecayRelease) msg;
				final short value = (short) (msgInputDynaDecayRelease.getAttachment() * 8000);
				this.mixer.getModifier().changedInputDynamicsParameterShort(channel, M380SysexParameter.CH_GATE_RELEASE, value);
				final String label = this.mixer.getLabelMaker().getReleaseHoldLabel(msgInputDynaDecayRelease.getAttachment());
				this.mixer.fireChange(new MsgInputDynaDecayReleaseLabel(dyna, channel, label));
				return true;
			} else if (msg instanceof MsgInputDynaHold) {
				final MsgInputDynaHold msgInputDynaHold = (MsgInputDynaHold) msg;
				final short value = (short) (msgInputDynaHold.getAttachment() * 8000);
				this.mixer.getModifier().changedInputDynamicsParameterShort(channel, M380SysexParameter.CH_GATE_HOLD, value);
				final String label = this.mixer.getLabelMaker().getReleaseHoldLabel(msgInputDynaHold.getAttachment());
				this.mixer.fireChange(new MsgInputDynaHoldLabel(dyna, channel, label));
				return true;
			}
			
		} else if (dyna ==1) {
			if (msg instanceof MsgInputDynaOn) {
				final MsgInputDynaOn msgInputDynaOn = (MsgInputDynaOn) msg;
				final byte value = msgInputDynaOn.getAttachment() ? (byte) 1 : (byte) 0;
				this.mixer.getModifier().changedInputDynamicsParameterByte(channel,
						M380SysexParameter.CH_COMP_SWITCH, value);
				return true;
			} else if (msg instanceof MsgInputDynaKeyIn) {
				final MsgInputDynaKeyIn msgInputDynaKeyIn = (MsgInputDynaKeyIn) msg;
				final byte value = (byte) (msgInputDynaKeyIn.getAttachment() * 127);
				this.mixer.getModifier().changedInputDynamicsParameterByte(channel, M380SysexParameter.CH_COMP_KEY_IN, value);
			} else if (msg instanceof MsgInputDynaThreshold) {
				final MsgInputDynaThreshold msgInputDynaThreshold = (MsgInputDynaThreshold) msg;
				final short value = (short) ((msgInputDynaThreshold.getAttachment() * 400) - 400);
				this.mixer.getModifier().changedInputDynamicsParameterShort(channel,
						M380SysexParameter.CH_COMP_THRESHOLD, value);
				final String label = this.mixer.getLabelMaker().getCompressorThresholdLabel(msgInputDynaThreshold.getAttachment());
				this.mixer.fireChange(new MsgInputDynaThresholdLabel(dyna, channel, label));
				return true;
			} else if (msg instanceof MsgInputDynaRatio) {
				final MsgInputDynaRatio msgInputDynaRatio = (MsgInputDynaRatio) msg;
				final byte value = (byte) (msgInputDynaRatio.getAttachment() * 13);
				this.mixer.getModifier().changedInputDynamicsParameterByte(channel, M380SysexParameter.CH_COMP_RATIO, value);
				final String label = this.mixer.getLabelMaker().getDynamicsRatioLabel(msgInputDynaRatio.getAttachment());
				this.mixer.fireChange(new MsgInputDynaRatioLabel(dyna, channel, label));
				return true;
			} else if (msg instanceof MsgInputDynaKnee) {
				final MsgInputDynaKnee msgInputDynaKnee = (MsgInputDynaKnee) msg;
				final byte value = (byte) (msgInputDynaKnee.getAttachment() * 9);
				this.mixer.getModifier().changedInputDynamicsParameterByte(channel, M380SysexParameter.CH_COMP_KNEE, value);
				final String label = this.mixer.getLabelMaker().getDynamicsKneeLabel(msgInputDynaKnee.getAttachment());
				this.mixer.fireChange(new MsgInputDynaKneeLabel(dyna, channel, label));
				return true;
			} else if (msg instanceof MsgInputDynaAttack) {
				final MsgInputDynaAttack msgInputDynaAttack = (MsgInputDynaAttack) msg;
				final short value = (short) (msgInputDynaAttack.getAttachment() * 8000);
				this.mixer.getModifier().changedInputDynamicsParameterShort(channel, M380SysexParameter.CH_COMP_ATTACK, value);
				final String label = this.mixer.getLabelMaker().getAttackLabel(msgInputDynaAttack.getAttachment());
				this.mixer.fireChange(new MsgInputDynaAttackLabel(dyna, channel, label));
				return true;
			} else if (msg instanceof MsgInputDynaDecayRelease) {
				final MsgInputDynaDecayRelease msgInputDynaDecayRelease = (MsgInputDynaDecayRelease) msg;
				final short value = (short) (msgInputDynaDecayRelease.getAttachment() * 8000);
				this.mixer.getModifier().changedInputDynamicsParameterShort(channel, M380SysexParameter.CH_COMP_RELEASE, value);
				final String label = this.mixer.getLabelMaker().getReleaseHoldLabel(msgInputDynaDecayRelease.getAttachment());
				this.mixer.fireChange(new MsgInputDynaDecayReleaseLabel(dyna, channel, label));
				return true;
			} else if (msg instanceof MsgInputDynaGain) {
				final MsgInputDynaGain msgInputDynaGain = (MsgInputDynaGain) msg;
				final short value = (short) ((msgInputDynaGain.getAttachment() * 800) - 400);
				this.mixer.getModifier().changedInputDynamicsParameterShort(channel, M380SysexParameter.CH_COMP_GAIN, value);
				final String label = this.mixer.getLabelMaker().getCompressorGainLabel(msgInputDynaGain.getAttachment());
				this.mixer.fireChange(new MsgInputDynaGainLabel(dyna, channel, label));
				return true;
			}
			
		}
		return false;
	}

}
