package model.protocol.osc.handler;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscMessageRelay;
import model.protocol.osc.OscObjectUtil;

import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;

/**
 * This handler is for OSC messages that change the auxiliary send controls.
 */
public class OscOutputAuxMsgHandler extends AbstractOscMessageHandler {
	private static final String[] LISTEN_ON = {
		"^" + OscMessageRelay.OSC_PREFIX + "/output/aux/(.*)",
		"^" + OscMessageRelay.OSC_PREFIX + "/stateful/aux/(.*)",
	};


	@Override
	public final String[] getInterestedAddresses() {
		return LISTEN_ON;
	}


	@Override
	public MessageHandleStatus handleOscMessage(final IOscMessage msg) {
		LOG.info("I have a new message: " + msg);
		if (OscAddressUtil.stringify(msg.address()).matches(".*/stateful/.*/level/fromChannel/[0-9]+(/z)?$")) {
			this.handleLevelMessage(msg);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/stateful/aux/level$")) {
			this.handleMasterLevelMessage(msg);
		} else {
			return MessageHandleStatus.MESSAGE_NOT_HANDLED;
		}
		return MessageHandleStatus.MESSAGE_HANDLED;
	}

	private void handleMasterLevelMessage(final IOscMessage msg) {
		this.getSurface().fireChanged(new MsgAuxMasterLevelChanged(this.getSurface().getState().getCurrentAux(), OscObjectUtil.toFloat(msg.get(0))));
	}

	private void handleLevelMessage(final IOscMessage msg) {
		LOG.info("Level message recognized: " + msg);

		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final int chn = Integer.parseInt(splitted[6]) - 1;

		final int aux = this.getSurface().getState().getCurrentAux();
		final float value = OscObjectUtil.toFloat(msg.get(0));

		this.getSurface().fireChanged(new MsgAuxSendChanged(chn, aux, value));
	}



}
