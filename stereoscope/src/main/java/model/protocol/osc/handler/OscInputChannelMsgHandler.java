package model.protocol.osc.handler;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscMessageRelay;
import model.protocol.osc.OscObjectUtil;

import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;

/**
 * This handler is for OSC messages that change the channel controls.
 */
public class OscInputChannelMsgHandler extends AbstractOscMessageHandler {
	private static final String[] LISTEN_ON = {
		"^" + OscMessageRelay.OSC_PREFIX + "/input/(.*)"
	};


	@Override
	public MessageHandleStatus handleOscMessage(final IOscMessage msg) {
		LOG.info("I have a new message: " + msg);
		if (OscAddressUtil.stringify(msg.address()).matches(".*/toAux/[0-9]+/level$")) {
			this.handleAuxSendLevelMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/toAux/[0-9]+/channelOn$")) {
			this.handleAuxSendOnMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/level(/z)?$")) {
			this.handleLevelMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/channelOn$")) {
			this.handleChannelOnMessage(msg);
		} else {
			return MessageHandleStatus.MESSAGE_NOT_HANDLED;
		}
		return MessageHandleStatus.MESSAGE_HANDLED;
	}

	private void handleAuxSendLevelMessage(final IOscMessage msg) {
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final int channel = Integer.parseInt(splitted[3]) - 1;
		final int aux = Integer.parseInt(splitted[5]) - 1;
		this.getSurface().fireChanged(new MsgAuxSendChanged(channel, aux,
				OscObjectUtil.toFloat(msg.get(0))));
	}

	private void handleAuxSendOnMessage(final IOscMessage msg) {
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final int channel = Integer.parseInt(splitted[3]) - 1;
		final int aux = Integer.parseInt(splitted[5]) - 1;
		this.getSurface().fireChanged(new MsgAuxSendOnChanged(channel, aux,
				OscObjectUtil.toBoolean(msg.get(0))));
	}

	private void handleChannelOnMessage(final IOscMessage msg) {
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final int channel = Integer.parseInt(splitted[3]) - 1;
		this.getSurface().fireChanged(new MsgChannelOnChanged(channel, OscObjectUtil.toFloat(msg.get(0)) == 1.0f));
	}

	private void handleLevelMessage(final IOscMessage msg) {
		LOG.info("Level message recognized: " + msg);

		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final int chn = Integer.parseInt(splitted[3]) - 1;

		final float receivedValue = OscObjectUtil.toFloat(msg.get(0));

		this.getSurface().fireChanged(new MsgChannelLevelChanged(chn, receivedValue));
	}


	@Override
	public final String[] getInterestedAddresses() {
		return LISTEN_ON;
	}
}
