package com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus;

import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractLabelMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelAuxLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelAuxOnLabel;
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
import com.stereokrauts.stereoscope.webgui.Webclient;

public class FromBusLabelHandler implements IMessageHandler {
	private Webclient frontend;
	
	public FromBusLabelHandler(Webclient frontend) {
		this.frontend = frontend;
	}
	
	@Override
	public boolean handleMessage(IMessage msg) {
		final int channel = ((AbstractLabelMessage<?>) msg).getChannel() + 1;
		if (msg instanceof MsgChannelLevelLabel) {
			final MsgChannelLevelLabel msgChannelLevelChanged = (MsgChannelLevelLabel) msg;
			this.setChannelLevelLabel(channel, msgChannelLevelChanged.getAttachment());
			return true;
		} else if (msg instanceof MsgChannelAuxLevelLabel) {
			
			return true;
		} else if (msg instanceof MsgChannelAuxOnLabel) {
		
			return true;
		} else if (msg instanceof MsgInputPanLabel) {
			
			return true;
		} else if (msg instanceof MsgInputDynaAttackLabel) {
			
			return true;
		} else if (msg instanceof MsgInputDynaDecayReleaseLabel) {
			
			return true;
		} else if (msg instanceof MsgInputDynaGainLabel) {
			
			return true;
		} else if (msg instanceof MsgInputDynaHoldLabel) {
			
			return true;
		} else if (msg instanceof MsgInputDynaKneeLabel) {
			
			return true;
		} else if (msg instanceof MsgInputDynaRangeLabel) {
			
			return true;
		} else if (msg instanceof MsgInputDynaRatioLabel) {
			
			return true;
		} else if (msg instanceof MsgInputDynaThresholdLabel) {
			
			return true;
		} else if (msg instanceof MsgInputPeqFLabel) {
			
			return true;
		} else if (msg instanceof MsgInputPeqGLabel) {
			
			return true;
		} else if (msg instanceof MsgInputPeqQLabel) {
			
			return true;
		}
		return false;
	}

	private void setChannelLevelLabel(int channel, String attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "input/" + channel + "/levelLabel";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
	}

}
