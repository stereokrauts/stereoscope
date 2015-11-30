package model.protocol.osc.handler;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscMessageRelay;
import model.protocol.osc.OscObjectUtil;

import com.stereokrauts.stereoscope.model.messaging.message.mixerglobal.MsgDcaLevelChanged;

/**
 * This handler is for OSC messages that change the output controls.
 */
public class OscMixerGlobalMsgHandler extends AbstractOscMessageHandler {
	private static final String[] LISTEN_ON = {
		"^" + OscMessageRelay.OSC_PREFIX + "/global/(.*)"
	};


	@Override
	public final String[] getInterestedAddresses() {
		return LISTEN_ON;
	}


	@Override
	public final MessageHandleStatus handleOscMessage(final IOscMessage msg) {
		LOG.info("I have a new message: " + msg);
		if (OscAddressUtil.stringify(msg.address()).matches(".*/dca/level$")) {
			this.handleDcaLevelMessage(msg);
		} else {
			return MessageHandleStatus.MESSAGE_NOT_HANDLED;
		}
		return MessageHandleStatus.MESSAGE_HANDLED;
	}

	private void handleDcaLevelMessage(final IOscMessage msg) {
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final byte dcaNumber = (byte) (Byte.parseByte(splitted[4]) - 1);
		this.getSurface().fireChanged(new MsgDcaLevelChanged(dcaNumber, OscObjectUtil.toFloat(msg.get(0))));
	}

}
