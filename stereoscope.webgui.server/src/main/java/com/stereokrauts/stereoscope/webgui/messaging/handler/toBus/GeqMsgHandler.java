package com.stereokrauts.stereoscope.webgui.messaging.handler.toBus;

import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandReset;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqFullReset;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerGEQAllBandLevel;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerGEQSingleBandLevel;
import com.stereokrauts.stereoscope.webgui.Webclient;
import com.stereokrauts.stereoscope.webgui.messaging.FrontendMessage;

public class GeqMsgHandler extends AbstractFrontendMsgHandler {
	private static final String[] LISTEN_ON = {
		"^" + OSC_PREFIX + "/dsp/geq/(.*)",
		"^" + OSC_PREFIX + "/stateful/dsp/geq/(.*)",
	};
	
	public GeqMsgHandler(Webclient frontend) {
		super(frontend);
	}
	
	@Override
	public boolean handleMessage(FrontendMessage msg) {
		LOG.info("New message from frontend: " + msg);
		if (this.getFrontend().getState().isMixerWithGraphicalEQ()) {
			final String[] splitted = msg.getOscAddress().split("/");
			if (msg.getOscAddress().matches(".*/stateful/.*/band/[0-9]+/.*/level$")) {
				this.handleBandLevelMessage(msg, splitted);
			} else if (msg.getOscAddress().matches(".*/stateful/.*/band/[0-9]+/.*/reset$")) {
				this.handleBandResetMessage(msg, splitted);
			} else if (msg.getOscAddress().matches(".*/stateful/.*/resetGeq$")) {
				this.handleGeqFullReset(msg);
			} else {
				return false;
			}
			return true;
		}
		return false;
	}

	private void handleBandLevelMessage(FrontendMessage msg, String[] splitted) {
		final boolean isRight = splitted[7].equals("right");
		final int bandNumber = Integer.parseInt(splitted[6]);

		final float level = (msg.getFloatValue() - Webclient.CENTER_CONTROL_OFFSET) * Webclient.CENTER_CONTROL_SCALE_FACTOR;
		this.getFrontend().fireChange(new MsgGeqBandLevelChanged(this.getFrontend().getState().getCurrentGEQ(), bandNumber - 1, isRight, level));
		
	}

	private void handleBandResetMessage(FrontendMessage msg, String[] splitted) {
		final int geq = this.getFrontend().getState().getCurrentGEQ();
		final boolean isRight = splitted[7].equals("right");
		final int bandNumber = Integer.parseInt(splitted[6]);
		
		this.getFrontend().fireChange(new MsgGeqBandReset((short) geq, bandNumber - 1, isRight, null));
		this.getFrontend().fireChange(new RequestMixerGEQSingleBandLevel(geq, bandNumber, isRight));
		
	}

	private void handleGeqFullReset(FrontendMessage msg) {
		int currentGeq = this.getFrontend().getState().getCurrentGEQ();
		this.getFrontend().fireChange(new MsgGeqFullReset((short) currentGeq, null));
		this.getFrontend().fireChange(new RequestMixerGEQAllBandLevel(currentGeq, true));
	}

	@Override
	public String[] getInterestedAddresses() {
		return LISTEN_ON;
	}

}
