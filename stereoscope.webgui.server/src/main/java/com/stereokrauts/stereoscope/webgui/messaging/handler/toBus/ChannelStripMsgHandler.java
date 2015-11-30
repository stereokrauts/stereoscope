package com.stereokrauts.stereoscope.webgui.messaging.handler.toBus;

import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputPan;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqF;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqFilterType;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqHPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqLPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqModeChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;
import com.stereokrauts.stereoscope.webgui.Webclient;
import com.stereokrauts.stereoscope.webgui.messaging.FrontendMessage;

public class ChannelStripMsgHandler extends AbstractFrontendMsgHandler {
	private static final String[] LISTEN_ON = {
		"^" + OSC_PREFIX + "/stateful/input/peq/(.*)",
		"^" + OSC_PREFIX + "/stateful/input/misc/(.*)",
	};
	
	public ChannelStripMsgHandler(Webclient frontend) {
		super(frontend);
	}
	
	@Override
	public boolean handleMessage(FrontendMessage msg) {
		LOG.info("New message from frontend: " + msg);
		
		final int chn = this.getFrontend().getState().getCurrentInput();
		final String[] splitted = msg.getOscAddress().split("/");
		final float value = msg.getFloatValue();
		
		if (msg.getOscAddress().matches(".*/peq/band/[0-9]/.*+(/z)?$")) {
			this.handleInputPeqBandMessage(chn, splitted, value);
		} else if (msg.getOscAddress().matches(".*/peq/band/[0-9]/type/*")) {
			this.handleFilterCharacteristicsMessage(chn, splitted, value);
		} else if (msg.getOscAddress().matches(".*/peq/mode$")) {
			this.getFrontend().fireChange(new MsgInputPeqModeChanged(chn, value == 1.0f));
		} else if (msg.getOscAddress().matches(".*/peq/peqOn$")) {
			this.getFrontend().fireChange(new MsgInputPeqOnChanged(chn, value == 1.0f));
		} else if (msg.getOscAddress().matches(".*/peq/hpfOn$")) {
			this.getFrontend().fireChange(new MsgInputPeqHPFChanged(chn, value == 1.0f));
		} else if (msg.getOscAddress().matches(".*/peq/lpfOn$")) {
			this.getFrontend().fireChange(new MsgInputPeqLPFChanged(chn, value == 1.0f));		
			// misc messages
		} else if (msg.getOscAddress().matches(".*/channelOn$")) {
			final int channel = this.getFrontend().getState().getCurrentInput();
			this.getFrontend().fireChange(new MsgChannelOnChanged(channel, value == 1.0f));
		} else if (msg.getOscAddress().matches(".*/level(/z)?$")) {
			this.handleInputGenericMessage(chn, "level", value);
		} else if (msg.getOscAddress().matches(".*/pan(/z)?$")) {
			this.handleInputGenericMessage(chn, "pan", value);
		} else {
			return false;
		}
		
		return true;
	}

	private void handleInputPeqBandMessage(int chn, String[] splitted,
			float value) {
		final int band = Integer.parseInt(splitted[6]) - 1;
		final String msgType = splitted[7];

		if (msgType.matches("q")) {
			this.getFrontend().fireChange(new MsgInputPeqQ(chn, band, value));
		} else if (msgType.matches("f")) {
			this.getFrontend().fireChange(new MsgInputPeqF(chn, band, value));
		} else if (msgType.matches("g")) {
			this.getFrontend().fireChange(new MsgInputPeqG(chn, band, this.convertToCenteredControl(value)));
		}
		
	}

	private void handleFilterCharacteristicsMessage(int chn, String[] splitted,
			float value) {
		final int band = Integer.parseInt(splitted[5]) - 1;
		final String filterType = splitted[6];
		int filterTypeNumber;

		if (filterType.matches("lowShelf") && value == 1.0f) {
			filterTypeNumber = 1;
		} else if (filterType.matches("hiShelf") && value == 1.0f) {
			filterTypeNumber = 2;
		} else 	if (filterType.matches("lpf") && value == 1.0f) {
			filterTypeNumber = 3;
		} else if (filterType.matches("hpf") && value == 1.0f) {
			filterTypeNumber = 4;
		} else {
			filterTypeNumber = 0; // parametric
		}
		this.getFrontend().fireChange(new MsgInputPeqFilterType(chn, band, filterTypeNumber));
		
	}

	private void handleInputGenericMessage(int chn, String msgType, float value) {
		if (msgType.matches("level")) {
			this.getFrontend().fireChange(new MsgChannelLevelChanged(chn, value));
		} else if (msgType.matches("pan")) {
			this.getFrontend().fireChange(new MsgInputPan(chn, this.convertToCenteredControl(value)));
		}
		
	}
	
	private float convertToCenteredControl(final float value) {
		return (value - Webclient.CENTER_CONTROL_OFFSET) * Webclient.CENTER_CONTROL_SCALE_FACTOR;
	}

	@Override
	public String[] getInterestedAddresses() {
		return LISTEN_ON;
	}

	

}
