package model.protocol.osc.handler;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscMessageRelay;

/**
 * This handler is for OSC messages that change the stateful
 * part of the surface.
 */
public class OscStateMsgHandler extends AbstractOscMessageHandler {
	private static final String[] LISTEN_ON = {
		"^" + OscMessageRelay.OSC_PREFIX + "/system/state/.*/changeTo/.*$"
	};


	@Override
	public final String[] getInterestedAddresses() {
		return LISTEN_ON;
	}


	@Override
	public final MessageHandleStatus handleOscMessage(final IOscMessage msg) {
		LOG.info("I have a new message: " + msg);

		if (this.isZMessage(msg)) {
			LOG.info("Message ignored - was z-Message");
			return MessageHandleStatus.MESSAGE_NOT_HANDLED;
		}

		if (OscAddressUtil.stringify(msg.address()).matches(".*/selectedAux/.*")) {
			this.handleAuxSelectionMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/selectedGeq/.*")) {
			this.handleGeqSelectionMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/selectedInput/.*")) {
			this.handleInputSelectionMessage(msg);
		} else {
			return MessageHandleStatus.MESSAGE_NOT_HANDLED;
		}
		return MessageHandleStatus.MESSAGE_HANDLED;
	}

	private void handleGeqSelectionMessage(final IOscMessage msg) {
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final byte eqNumber = Byte.parseByte(splitted[6]);
		this.getSurface().getState().setCurrentGEQ((byte) (eqNumber - 1));
		this.getSurface().updateClientViewGEQ();
	}

	private void handleAuxSelectionMessage(final IOscMessage msg) {
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final byte auxNumber = Byte.parseByte(splitted[6]);
		this.getSurface().getState().setCurrentAux((byte) (auxNumber - 1));
		this.getSurface().updateClientViewAux();
	}

	private void handleInputSelectionMessage(final IOscMessage msg) {
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final byte inputNumber = Byte.parseByte(splitted[6]);
		this.getSurface().getState().setCurrentInput((byte) (inputNumber - 1));
		this.getSurface().updateClientViewInput();
	}
}
