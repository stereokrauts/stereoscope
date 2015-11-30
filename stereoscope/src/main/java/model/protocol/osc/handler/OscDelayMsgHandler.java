package model.protocol.osc.handler;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscMessageRelay;
import model.protocol.osc.OscObjectUtil;

import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgOutputDelayChanged;

/**
 * This handler is for OSC messages that change the delay controls.
 */
public class OscDelayMsgHandler extends AbstractOscMessageHandler {
	private static final String[] LISTEN_ON = {
		"^" + OscMessageRelay.OSC_PREFIX + "/output/.*/delay$"
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

		if (OscAddressUtil.stringify(msg.address()).matches(".*/bus/.*")) {
			this.handleBusDelayMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/aux/.*")) {
			this.handleAuxDelayMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/omni/.*")) {
			this.handleOmniDelayMessage(msg);
		} else {
			return MessageHandleStatus.MESSAGE_NOT_HANDLED;
		}
		return MessageHandleStatus.MESSAGE_HANDLED;
	}

	private void handleOmniDelayMessage(final IOscMessage msg) {
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final int outputNr = Integer.parseInt(splitted[4]) - 1;
		final float receivedValue = OscObjectUtil.toFloat(msg.get(0));
		this.getSurface().fireChanged(new MsgOutputDelayChanged(outputNr, receivedValue));
	}

	private void handleAuxDelayMessage(final IOscMessage msg) {
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final int auxNumber = Integer.parseInt(splitted[4]) - 1;
		final float receivedValue = OscObjectUtil.toFloat(msg.get(0));
		this.getSurface().fireChanged(new MsgAuxMasterDelayChanged(auxNumber, receivedValue));
	}

	private void handleBusDelayMessage(final IOscMessage msg) {
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final int busNumber = Integer.parseInt(splitted[4]) - 1;
		final float receivedValue = OscObjectUtil.toFloat(msg.get(0));
		this.getSurface().fireChanged(new MsgBusMasterDelayChanged(busNumber, receivedValue));
	}

}
