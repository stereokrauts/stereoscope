package com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus;

import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputPan;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqHPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqLPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqModeChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqOnChanged;
import com.stereokrauts.stereoscope.webgui.Webclient;

public class FromBusInputBasicHandler implements IMessageHandler {
	private Webclient frontend;
	
	public FromBusInputBasicHandler(Webclient frontend) {
		this.frontend = frontend;
	}
	
	@Override
	public boolean handleMessage(IMessage msg) {
		final int channel = ((AbstractInputMessage<?>) msg).getChannel();
		if (channel == this.frontend.getState().getCurrentInput()) {
			if (msg instanceof MsgInputPan) {
				final MsgInputPan msgInputPanChanged = (MsgInputPan) msg;
				this.setInputPan(channel, msgInputPanChanged.getAttachment());
				return true;
			} else if (msg instanceof MsgInputPeqOnChanged) {
				final MsgInputPeqOnChanged msgPeqOnChanged = (MsgInputPeqOnChanged) msg;
				this.setPeqOn(channel, msgPeqOnChanged.getAttachment());
				return true;
			} else if (msg instanceof MsgInputPeqModeChanged) {
				final MsgInputPeqModeChanged msgPeqModeChanged = (MsgInputPeqModeChanged) msg;
				this.setPeqMode(channel, msgPeqModeChanged.getAttachment());
				return true;
			} else if (msg instanceof MsgInputPeqLPFChanged) {
				final MsgInputPeqLPFChanged msgPeqLPFChanged = (MsgInputPeqLPFChanged) msg;
				this.setPeqLPFOn(channel, msgPeqLPFChanged.getAttachment());
				return true;
			} else if (msg instanceof MsgInputPeqHPFChanged) {
				final MsgInputPeqHPFChanged msgPeqHPFChanged = (MsgInputPeqHPFChanged) msg;
				this.setPeqHPFOn(channel, msgPeqHPFChanged.getAttachment());
				return true;
			} 
		}
		return false;
	}

	private void setInputPan(int channel, Float level) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "stateful/input/misc/pan";
		final float panLevel = (level / Webclient.CENTER_CONTROL_SCALE_FACTOR)
				+ Webclient.CENTER_CONTROL_OFFSET;
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, panLevel);

	}

	private void setPeqOn(int channel, Boolean attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "stateful/input/peq/peqOn";
		final float status = attachment ? 1.0f : 0.0f;
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, status);
		
	}

	private void setPeqMode(int channel, Boolean attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "stateful/input/peq/mode";
		final float status = attachment ? 1.0f : 0.0f;
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, status);
		
	}

	private void setPeqLPFOn(int channel, Boolean attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "stateful/input/peq/lpfOn";
		final float status = attachment ? 1.0f : 0.0f;
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, status);
	}

	private void setPeqHPFOn(int channel, Boolean attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "stateful/input/peq/hpfOn";
		final float status = attachment ? 1.0f : 0.0f;
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, status);
	}

}
