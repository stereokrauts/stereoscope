package com.stereokrauts.stereoscope.webgui.messaging.handler.toBus;

import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.webgui.Webclient;
import com.stereokrauts.stereoscope.webgui.messaging.FrontendMessage;

public class OutputAuxMsgHandler extends AbstractFrontendMsgHandler {
	private static final String[] LISTEN_ON = {
		"^" + OSC_PREFIX + "/output/aux/(.*)",
		"^" + OSC_PREFIX + "/stateful/aux/(.*)",
	};
	
	public OutputAuxMsgHandler(Webclient frontend) {
		super(frontend);
	}
	
	@Override
	public boolean handleMessage(FrontendMessage msg) {
		LOG.info("New message from frontend: " + msg);
		if (msg.getOscAddress().matches(".*/stateful/.*/level/fromChannel/[0-9]+(/z)?$")) {
			this.handleLevelMessage(msg);
		} else if (msg.getOscAddress().matches(".*/stateful/aux/level$")) {
			this.handleMasterLevelMessage(msg);
		} else {
			return false;
		}
		return true;
	}

	private void handleMasterLevelMessage(FrontendMessage msg) {
		this.getFrontend().fireChange(
				new MsgAuxMasterLevelChanged(this.getFrontend().getState().getCurrentAux(),
						msg.getFloatValue()));
		
	}

	private void handleLevelMessage(FrontendMessage msg) {
		final String[] splitted = msg.getOscAddress().split("/");
		final int chn = Integer.parseInt(splitted[6]) - 1;

		final int aux = this.getFrontend().getState().getCurrentAux();
		final float value = msg.getFloatValue();

		this.getFrontend().fireChange(new MsgAuxSendChanged(chn, aux, value));
		
	}

	@Override
	public String[] getInterestedAddresses() {
		return LISTEN_ON;
	}

	

}
