package com.stereokrauts.stereoscope.webgui.messaging.handler.toBus;

import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;
import com.stereokrauts.stereoscope.webgui.Webclient;
import com.stereokrauts.stereoscope.webgui.messaging.FrontendMessage;

public class OutputMsgHandler extends AbstractFrontendMsgHandler {
	private static final String[] LISTEN_ON = {
		"^" + OSC_PREFIX + "/output/(.*)"
	};
	
	public OutputMsgHandler(Webclient frontend) {
		super(frontend);
	}
	
	@Override
	public boolean handleMessage(FrontendMessage msg) {
		LOG.info("New message from frontend: " + msg);
		if (msg.getOscAddress().matches(".*/master/level$")) {
			this.handleMasterLevelMessage(msg);
			return true;
		} else if (msg.getOscAddress().matches(".*/aux/[0-9]+/level$")) {
			this.handleAuxLevelMessage(msg);
			return true;
		} else if (msg.getOscAddress().matches(".*/bus/[0-9]+/level$")) {
			this.handleBusLevelMessage(msg);
			return true;
		}
		return false;
	}



	private void handleMasterLevelMessage(FrontendMessage msg) {
		this.getFrontend().fireChange(new MsgMasterLevelChanged(msg.getFloatValue()));
	}

	private void handleAuxLevelMessage(FrontendMessage msg) {
		final String[] splitted = msg.getOscAddress().split("/");
		final byte auxNumber = (byte) (Byte.parseByte(splitted[4]) - 1);
		this.getFrontend().fireChange(new MsgAuxMasterLevelChanged(auxNumber, msg.getFloatValue()));
	}

	private void handleBusLevelMessage(FrontendMessage msg) {
		final String[] splitted = msg.getOscAddress().split("/");
		final byte busNumber = (byte) (Byte.parseByte(splitted[4]) - 1);
		this.getFrontend().fireChange(new MsgBusMasterLevelChanged(busNumber, msg.getFloatValue()));
	}

	@Override
	public String[] getInterestedAddresses() {
		return LISTEN_ON;
	}

	

}
