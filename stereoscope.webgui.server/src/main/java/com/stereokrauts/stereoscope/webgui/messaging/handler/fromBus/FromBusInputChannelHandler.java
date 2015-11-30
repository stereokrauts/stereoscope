	package com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus;

import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelNameChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.webgui.Webclient;


public class FromBusInputChannelHandler implements IMessageHandler {
	private Webclient frontend;

	public FromBusInputChannelHandler(final Webclient frontend) {
		this.frontend = frontend;
	}

	@Override
	public final boolean handleMessage(final IMessage msg) {
		final int channel = ((AbstractChannelMessage<?>) msg).getChannel();
		if (msg instanceof MsgChannelLevelChanged) {
			final MsgChannelLevelChanged msgChannelLevelChanged = (MsgChannelLevelChanged) msg;
			this.setChannelLevel(channel, msgChannelLevelChanged.getAttachment());
			return true;
		} else if (msg instanceof MsgChannelOnChanged) {
			final MsgChannelOnChanged msgChannelOnChanged = (MsgChannelOnChanged) msg;
			this.setChannelOn(channel, msgChannelOnChanged.getAttachment());
			return true;
		} else if (msg instanceof MsgChannelNameChanged) {
			final MsgChannelNameChanged msgChannelNameChanged = (MsgChannelNameChanged) msg;
			this.setChannelName(channel, msgChannelNameChanged.getAttachment());
			return true;
		}
		return false;
	}

	private void setChannelLevel(int channel, Float attachment) {
		final String oscAddress = Webclient.OSC_PREFIX + "input/" + (channel + 1) + "/level";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}

	private void setChannelOn(int channel, Boolean attachment) {
		final String oscAddress = Webclient.OSC_PREFIX + "input/" + (channel + 1) + "/channelOn";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}
	
	private void setChannelName(int channel, String attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "input/" + (channel + 1) + "/label";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, attachment);
		
	}
}
