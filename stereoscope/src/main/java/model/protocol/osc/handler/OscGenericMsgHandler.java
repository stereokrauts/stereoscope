package model.protocol.osc.handler;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscMessageRelay;
import model.protocol.osc.OscObjectUtil;

/**
 * This handler is for OSC messages of generic nature.
 */
public class OscGenericMsgHandler extends AbstractOscMessageHandler {
	private static final String[] LISTEN_ON = {
		"^" +  "/PageAux",
		"^" +  "/PageChannels",
		"^" +  "/PageGEQ",
		"^" +  "/PageInput",
		"^" + OscMessageRelay.OSC_PREFIX + "/system/resync"
	};


	@Override
	public final String[] getInterestedAddresses() {
		return LISTEN_ON;
	}

	/** 
	 * This function handles the resync, wether the TouchOSC page
	 * changes or a resync button is pressed.
	 */

	@Override
	public final MessageHandleStatus handleOscMessage(final IOscMessage msg) {
		LOG.info("I have a new message: " + msg);

		if (this.isZMessage(msg)) {
			LOG.info("Message ignored - was z-Message");
			return MessageHandleStatus.MESSAGE_NOT_HANDLED;
		}

		if (OscAddressUtil.stringify(msg.address()).matches(".*/PageAux[12]$")) {
			this.handlePageAuxMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/PageChannels[12]$")) {
			this.handlePageChannelsMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/PageGEQ$")) {
			this.handlePageGEQMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/PageInput$")) {
			this.handlePageInputMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/PageDelay$")) {
			this.handlePageDelayMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/resync$")) {
			this.handleResyncMessage(msg);
		} else {
			return MessageHandleStatus.MESSAGE_NOT_HANDLED;
		}
		return MessageHandleStatus.MESSAGE_HANDLED;
	}

	private void handleResyncMessage(final IOscMessage msg) {
		if (!this.isZMessage(msg) && OscObjectUtil.toFloat(msg.get(0)) == 1.0) {
			this.getSurface().updateClientViewChannels();
			this.getSurface().updateClientViewChannelNames();
			this.getSurface().updateClientViewAux();
			this.getSurface().updateClientViewGEQ();
			this.getSurface().updateClientViewDelay();
			this.getSurface().updateClientViewInput();
		}
	}

	private void handlePageGEQMessage(final IOscMessage msg) {
		if (!this.isZMessage(msg)) {
			this.getSurface().updateClientViewGEQ();
		}
	}

	private void handlePageChannelsMessage(final IOscMessage msg) {
		if (!this.isZMessage(msg)) {
			this.getSurface().updateClientViewChannels();
		}
	}

	private void handlePageAuxMessage(final IOscMessage msg) {
		if (!this.isZMessage(msg)) {
			this.getSurface().updateClientViewAux();
		}
	}

	private void handlePageInputMessage(final IOscMessage msg) {
		if (!this.isZMessage(msg)) {
			this.getSurface().updateClientViewInput();
		}
	}

	private void handlePageDelayMessage(final IOscMessage msg) {
		if (!this.isZMessage(msg)) {
			this.getSurface().updateClientViewDelay();
		}
	}
}
