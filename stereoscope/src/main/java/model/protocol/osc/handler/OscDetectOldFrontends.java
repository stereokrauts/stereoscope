package model.protocol.osc.handler;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;

/**
 * This handler exists to detect outdated OSC frontends on the users ipad.
 */
public final class OscDetectOldFrontends extends AbstractOscMessageHandler {


	@Override
	public String[] getInterestedAddresses() {
		return new String[] {
				"^/toMix/.*$",
				"^/channelOn/.*$",
				"^/master/.*$",
				"^/toAux/.*$",
				"^/selectedGEQ/.*$",
				"^/chooseAux/.*$"
		};
	}


	@Override
	public MessageHandleStatus handleOscMessage(final IOscMessage msg) {
		final String errmsg = "Detected outdated OSC message namespace. Please update your frontend! " + OscAddressUtil.stringify(msg.address());
		LOG.severe(errmsg);
		return MessageHandleStatus.MESSAGE_HANDLED; /* discard this message as being processed */
	}

}
