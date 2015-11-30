package com.stereokrauts.stereoscope.webgui.messaging.handler.toBus;

import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgOutputDelayChanged;
import com.stereokrauts.stereoscope.webgui.Webclient;
import com.stereokrauts.stereoscope.webgui.messaging.FrontendMessage;

public class DelayMsgHandler extends AbstractFrontendMsgHandler {
	private static final String[] LISTEN_ON = {
		"^" + OSC_PREFIX + "/output/.*/delay$"
	};
	
	public DelayMsgHandler(final Webclient frontend) {
		super(frontend);
	}

	@Override
	public final boolean handleMessage(final FrontendMessage msg) {
		LOG.info("I have a new message: " + msg);
		if (msg.getOscAddress().matches(".*/bus/.*")) {
			this.handleBusDelayMessage(msg);
		} else if (msg.getOscAddress().matches(".*/aux/.*")) {
			this.handleAuxDelayMessage(msg);
		} else if (msg.getOscAddress().matches(".*/omni/.*")) {
			this.handleOmniDelayMessage(msg);
		} else {
			return false;
		}
		return true;
	}
	
	private void handleBusDelayMessage(FrontendMessage msg) {
		final String[] splitted = msg.getOscAddress().split("/");
		final int busNumber = Integer.parseInt(splitted[4]) - 1;
		this.getFrontend().fireChange(new MsgBusMasterDelayChanged(busNumber, msg.getFloatValue()));
	}
	
	private void handleAuxDelayMessage(FrontendMessage msg) {
		final String[] splitted = msg.getOscAddress().split("/");
		final int auxNumber = Integer.parseInt(splitted[4]) - 1;
		this.getFrontend().fireChange(new MsgAuxMasterDelayChanged(auxNumber, msg.getFloatValue()));
	}
	
	private void handleOmniDelayMessage(FrontendMessage msg) {
		final String[] splitted = msg.getOscAddress().split("/");
		final int outputNr = Integer.parseInt(splitted[4]) - 1;
		this.getFrontend().fireChange(new MsgOutputDelayChanged(outputNr, msg.getFloatValue()));
	}

	@Override
	public String[] getInterestedAddresses() {
		return LISTEN_ON;
	}

}
