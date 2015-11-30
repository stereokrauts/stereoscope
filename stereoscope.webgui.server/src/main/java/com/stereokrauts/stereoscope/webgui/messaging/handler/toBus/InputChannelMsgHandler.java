package com.stereokrauts.stereoscope.webgui.messaging.handler.toBus;

import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.webgui.Webclient;
import com.stereokrauts.stereoscope.webgui.messaging.FrontendMessage;

/**
 * This handler is for messages that change the channel controls.
 */
public class InputChannelMsgHandler extends AbstractFrontendMsgHandler {
	private static final String[] LISTEN_ON = {
		"^" + OSC_PREFIX + "/input/(.*)"
	};
	private static final boolean LOG_DEBUG = false;

	public InputChannelMsgHandler(final Webclient frontend) {
		super(frontend);
	}

	@Override
	public final boolean handleMessage(final FrontendMessage msg) {
		if (LOG_DEBUG) {
			LOG.info("New message from frontend: " + msg.getOscAddress());
		}
		if (msg.getOscAddress().matches(".*/toAux/[0-9]+/level$")) {
			this.handleAuxSendLevelMessage(msg);
		} else if (msg.getOscAddress().matches(".*/toAux/[0-9]+/channelOn$")) {
			this.handleAuxSendOnMessage(msg);
		} else if (msg.getOscAddress().matches(".*/level(/z)?$")) {
			this.handleLevelMessage(msg);
		} else if (msg.getOscAddress().matches(".*/channelOn$")) {
			this.handleChannelOnMessage(msg);
		} else {
			return false;
		}
		return true;
	}

	private void handleAuxSendLevelMessage(final FrontendMessage msg) {
		final String[] splitted = msg.getOscAddress().split("/");
		final int channel = Integer.parseInt(splitted[3]) - 1;
		final int aux = Integer.parseInt(splitted[5]) - 1;
		this.getFrontend().fireChange(new MsgAuxSendChanged(channel, aux,
				msg.getFloatValue()));
	}

	private void handleAuxSendOnMessage(final FrontendMessage msg) {
		final String[] splitted = msg.getOscAddress().split("/");
		final int channel = Integer.parseInt(splitted[3]) - 1;
		final int aux = Integer.parseInt(splitted[5]) - 1;
		if (msg.getMsgType() == "boolean") {
			this.getFrontend().fireChange(new MsgAuxSendOnChanged(channel, aux,
					msg.getBooleanValue()));
		} else {
			LOG.error("Wrong message type: " + msg.getMsgType()
					+ ". Should be 'boolean'.");
		}
	}

	private void handleChannelOnMessage(final FrontendMessage msg) {
		final String[] splitted = msg.getOscAddress().split("/");
		final int channel = Integer.parseInt(splitted[3]) - 1;
		if (msg.getMsgType() == "boolean") {
			this.getFrontend().fireChange(new MsgChannelOnChanged(channel,
					msg.getBooleanValue()));
		} else {
			LOG.error("Wrong message type: " + msg.getMsgType()
					+ ". Should be 'boolean'.");
		}
	}

	private void handleLevelMessage(final FrontendMessage msg) {
		final String[] splitted = msg.getOscAddress().split("/");
		final int channel = Integer.parseInt(splitted[3]) - 1;
		final float receivedValue = msg.getFloatValue();
		this.getFrontend().fireChange(new MsgChannelLevelChanged(channel, receivedValue));
	}

	@Override
	public final String[] getInterestedAddresses() {
		return LISTEN_ON;
	}


}
