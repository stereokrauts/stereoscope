package model.protocol.osc.handler;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscMessageRelay;
import model.protocol.osc.OscObjectUtil;

import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;

/**
 * This handler is for OSC messages that change the output controls.
 */
public class OscOutputMsgHandler extends AbstractOscMessageHandler {
	private static final String[] LISTEN_ON = {
		"^" + OscMessageRelay.OSC_PREFIX + "/output/(.*)"
	};


	@Override
	public final String[] getInterestedAddresses() {
		return LISTEN_ON;
	}


	@Override
	public final MessageHandleStatus handleOscMessage(final IOscMessage msg) {
		LOG.info("I have a new message: " + msg);
		if (OscAddressUtil.stringify(msg.address()).matches(".*/master/level$")) {
			this.handleMasterLevelMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/aux/[0-9]+/level$")) {
			this.handleAuxLevelMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/bus/[0-9]+/level$")) {
			this.handleBusLevelMessage(msg);
		} else {
			return MessageHandleStatus.MESSAGE_NOT_HANDLED;
		}
		return MessageHandleStatus.MESSAGE_HANDLED;
	}

	private void handleBusLevelMessage(final IOscMessage msg) {
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final byte busNumber = (byte) (Byte.parseByte(splitted[4]) - 1);
		this.getSurface().fireChanged(new MsgBusMasterLevelChanged(busNumber, OscObjectUtil.toFloat(msg.get(0))));
	}


	private void handleAuxLevelMessage(final IOscMessage msg) {
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final byte auxNumber = (byte) (Byte.parseByte(splitted[4]) - 1);
		this.getSurface().fireChanged(new MsgAuxMasterLevelChanged(auxNumber, OscObjectUtil.toFloat(msg.get(0))));
	}

	private void handleMasterLevelMessage(final IOscMessage msg) {
		this.getSurface().fireChanged(new MsgMasterLevelChanged(OscObjectUtil.toFloat(msg.get(0))));
	}

}
