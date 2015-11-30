package com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus;

import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractGeqMessage;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandReset;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqFullReset;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqTypeChanged;
import com.stereokrauts.stereoscope.webgui.Webclient;

public class FromBusGeqHandler implements IMessageHandler {
	private Webclient frontend;
	
	public FromBusGeqHandler(Webclient frontend) {
		this.frontend = frontend;
	}

	@Override
	public boolean handleMessage(IMessage msg) {
		final int geqNumber = ((AbstractGeqMessage<?>) msg).getGeqNumber(); // NOPMD by th on 09.10.12 20:21
		if (geqNumber == this.frontend.getState().getCurrentGEQ()) {
			if (msg instanceof MsgGeqFullReset) {
				this.doGeqFullReset(geqNumber);
				return true;
			} else if (msg instanceof MsgGeqTypeChanged) {
				this.setCurrentGeqType((Boolean) msg.getAttachment());
				return true;
			} else if (msg instanceof MsgGeqBandLevelChanged) {
				final MsgGeqBandLevelChanged geqMsg = (MsgGeqBandLevelChanged) msg;
				final boolean rightChannel = geqMsg.isRightChannel();
				final int band = geqMsg.getBand();
				final float floatValue = geqMsg.getAttachment();
				this.setGeqBandLevel(geqNumber, rightChannel, band, floatValue);
				return true;
			} else if (msg instanceof MsgGeqBandReset) {
				final MsgGeqBandReset msgGeqBandReset = (MsgGeqBandReset) msg;
				final boolean rightChannel = msgGeqBandReset.isRightChannel();
				final int band = msgGeqBandReset.getBand();
				this.doGeqBandReset(geqNumber, rightChannel, band);
				return true;
			}
		}
		return false;
	}

	private void doGeqFullReset(int geqNumber) {
		final int bandCount = this.frontend.getState().getGeqBandCount();
		final float zeroValue = Webclient.CENTER_CONTROL_OFFSET; // NOPMD by th on 09.10.12 20:23
		String oscAddress = "";
		for (int i = 0; i < 2 * bandCount; i++) {
			if (i < bandCount) {
				oscAddress = Webclient.OSC_PREFIX
						+ "stateful/dsp/geq/band/" + (i + 1) + "/left/level";
			} else {
				oscAddress = Webclient.OSC_PREFIX
						+ "stateful/dsp/geq/band/" + (i - bandCount + 1)
						+ "/right/level";
			}
		}
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, zeroValue);
		
	}

	private void setCurrentGeqType(Boolean attachment) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "stateful/dsp/geq/isFlexEq15/visible";
		final float status = attachment ? 1.0f : 0.0f;
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, status);
		
	}

	private void setGeqBandLevel(int geqNumber, boolean rightChannel, int band,
			float floatValue) {
		String oscAddress = Webclient.OSC_PREFIX
				+ "stateful/dsp/geq/band/" + (band + 1)
				+ (rightChannel ? "/right/" : "/left/") + "level";
		final float level = (floatValue / Webclient.CENTER_CONTROL_SCALE_FACTOR) 
				+ Webclient.CENTER_CONTROL_OFFSET;
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, level);
		
	}

	private void doGeqBandReset(int geqNumber, boolean rightChannel, int band) {
		final float zeroValue = Webclient.CENTER_CONTROL_OFFSET; // NOPMD by th on 09.10.12 20:22
		final String oscAddress = Webclient.OSC_PREFIX
				+ "stateful/dsp/geq/band/" + (band + 1)
				+ (rightChannel ? "/right/" : "/left/") + "level";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, zeroValue);
		
	}
}
