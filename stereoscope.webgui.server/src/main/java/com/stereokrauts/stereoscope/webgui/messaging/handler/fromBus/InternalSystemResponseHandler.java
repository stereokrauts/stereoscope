package com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus;

import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerGEQCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerAuxCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerGEQCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerGEQIsFlexEQ;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerInputCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerIsMixerWithGraphicalEQ;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerMatrixBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerName;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerOutputCount;
import com.stereokrauts.stereoscope.webgui.Webclient;

public class InternalSystemResponseHandler implements IMessageHandler {
	private Webclient frontend;
	
	public InternalSystemResponseHandler(final Webclient frontend) {
		this.frontend = frontend;
	}

	@Override
	public boolean handleMessage(IMessage msg) {
		if (msg instanceof ResponseMixerInputCount) {
			this.frontend.getState().setInputCount(((ResponseMixerInputCount) msg).getCount());
			return true;
		} else if (msg instanceof ResponseMixerAuxCount) {
			this.frontend.getState().setAuxCount(((ResponseMixerAuxCount) msg).getCount());
			return true;
		} else if (msg instanceof ResponseMixerBusCount) {
			this.frontend.getState().setBusCount(((ResponseMixerBusCount) msg).getCount());
			return true;
		} else if (msg instanceof ResponseMixerMatrixBusCount) {
			this.frontend.getState().setMatrixCount(((ResponseMixerMatrixBusCount) msg).getCount());
		} else if (msg instanceof ResponseMixerOutputCount) {
			this.frontend.getState().setOutputCount(((ResponseMixerOutputCount) msg).getCount());
			return true;
		} else if (msg instanceof ResponseMixerGEQCount) {
			this.frontend.getState().setGeqCount(((ResponseMixerGEQCount) msg).getCount());
			return true;
		} else if (msg instanceof ResponseMixerGEQIsFlexEQ) {
			this.frontend.getState().setFlexEq((boolean) msg.getAttachment());
			return true;
		} else if (msg instanceof ResponseMixerIsMixerWithGraphicalEQ) {
			boolean mixerWithGEQ = (boolean) msg.getAttachment();
			this.frontend.getState().setMixerWithGraphicalEQ(mixerWithGEQ);
			if (mixerWithGEQ == true) {
				frontend.fireChange(new RequestMixerGEQCount(true));
			}
			return true;
		} else if (msg instanceof ResponseMixerName) {
			String name = ((ResponseMixerName) msg).getName();
			this.frontend.getState().setName(name);
			final String oscAddress = Webclient.OSC_PREFIX
					+ "system/response/mixerProperties";
			this.frontend.getFrontendModifier().sendToFrontend(oscAddress, true);
			return true;
		}
		return false;
	}

}
