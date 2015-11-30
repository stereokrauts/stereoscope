package com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus;

import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelAuxMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendOnChanged;
import com.stereokrauts.stereoscope.webgui.Webclient;

public class FromBusAuxChannelHandler implements IMessageHandler {
	private Webclient frontend;
	
	public FromBusAuxChannelHandler(final Webclient frontend) {
		this.frontend = frontend;
	}

	@Override
	public boolean handleMessage(final IMessage msg) {
		final int channel = ((AbstractChannelAuxMessage<?>) msg).getChannel();
		
		if (msg instanceof MsgAuxSendChanged) {
			final MsgAuxSendChanged msgAuxSendChanged = (MsgAuxSendChanged) msg;
			final int aux = msgAuxSendChanged.getAux();
			final float value = msgAuxSendChanged.getAttachment();
			this.setAuxSend(channel, aux, value);
			this.setAuxSendStateful(channel, aux, value);
			return true;
		} else if (msg instanceof MsgAuxSendOnChanged) {
			final MsgAuxSendOnChanged msgAuxSendOnChanged = (MsgAuxSendOnChanged) msg;
			final int aux = msgAuxSendOnChanged.getAux();
			final float value = msgAuxSendOnChanged.getAttachment() ? 1.0f : 0.0f;
			this.setAuxSendOn(channel, aux, value);
			this.setAuxSendOnStateful(channel, aux, value);
			return true;
		}
		return false;
	}

	private void setAuxSend(int channel, int aux, float value) {
		String oscAddress = Webclient.OSC_PREFIX 
				+ "input/" + (channel + 1) + "/toAux/" + (aux + 1) + "/level";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, value);
		
	}

	private void setAuxSendStateful(int channel, int aux, float value) {
		if (aux == this.frontend.getState().getCurrentAux()) {
			String oscAddress = Webclient.OSC_PREFIX 
					+ "stateful/aux/level/fromChannel/" + (channel + 1);
			this.frontend.getFrontendModifier().sendToFrontend(oscAddress, value);
		}
		
	}

	private void setAuxSendOn(int channel, int aux, float value) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "input/" + (channel + 1) + "/toAux/" + (aux + 1) + "/channelOn";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, value);
		
	}

	private void setAuxSendOnStateful(int channel, int aux, float value) {
		if (aux == this.frontend.getState().getCurrentAux()) {
			String oscAddress = Webclient.OSC_PREFIX 
					+ "stateful/aux/channelOn/fromChannel/" + (channel + 1);
			this.frontend.getFrontendModifier().sendToFrontend(oscAddress, value);
		}
		
	}
	
}
