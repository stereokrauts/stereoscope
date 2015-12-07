package model.protocol.osc.handler;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscMessageRelay;
import model.protocol.osc.OscObjectUtil;

import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputGroups;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgOutputGroups;

/**
 * This handler is for OSC messages that change the group controls.
 */
public class OscGroupsMsgHandler extends AbstractOscMessageHandler {
	private static final String[] LISTEN_ON = {
		"^" + OscMessageRelay.OSC_PREFIX + "/input/groups/(.*)",
		"^" + OscMessageRelay.OSC_PREFIX + "/output/groups/(.*)",
	};



	@Override
	public final String[] getInterestedAddresses() {
		return LISTEN_ON;
	}


	@Override
	public final MessageHandleStatus handleOscMessage(final IOscMessage msg) {
		LOG.info("New message: " + msg);

		if (this.isZMessage(msg)) {
			LOG.info("Message ignored - was z-Message");
			return MessageHandleStatus.MESSAGE_NOT_HANDLED;
		}

		final String[] spliter = OscAddressUtil.stringify(msg.address()).split("/");
		final String subSystem = spliter[2];
		final String muteGroup = spliter[5];
		final int groupNumber =  Integer.parseInt(muteGroup);

		if (subSystem.matches("input")) {
			if (OscAddressUtil.stringify(msg.address()).matches(".*/mute/[0-9]$")) {
				this.getSurface().fireChanged(new MsgInputGroups(1, groupNumber, OscObjectUtil.toFloat(msg.get(0)) == 1.0f));
			} else {
				return MessageHandleStatus.MESSAGE_NOT_HANDLED;
			}
		} else if (subSystem.matches("output")) {
			if (OscAddressUtil.stringify(msg.address()).matches(".*/mute/[0-9]$")) {
				this.getSurface().fireChanged(new MsgOutputGroups(1, groupNumber, OscObjectUtil.toFloat(msg.get(0)) == 1.0f));
			}
		}

		return MessageHandleStatus.MESSAGE_HANDLED;
	}

}
