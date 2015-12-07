package model.protocol.osc.handler;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscMessageRelay;
import model.protocol.osc.OscObjectUtil;

import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAttack;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAutoOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaDecayRelease;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterFreq;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterQ;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterType;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaGain;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaHold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKeyIn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKnee;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaLeftSideChain;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaPair;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRange;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRatio;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaThreshold;

/**
 * This handler is for OSC messages that change the channel dynamics controls.
 */
public class OscInputChannelStripDynamicsMsgHandler extends	AbstractOscMessageHandler {
	private static final String[] LISTEN_ON = {
		"^" + OscMessageRelay.OSC_PREFIX + "/stateful/input/dynamics/(.*)",
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

		final int selectedInput = this.getSurface().getState().getCurrentInput();

		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");
		final int processorNr = Short.parseShort(splitted[5]) - 1;

		if (OscAddressUtil.stringify(msg.address()).matches(".*/dynaOn$")) {
			this.getSurface().fireChanged(new MsgInputDynaOn(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0)) == 1.0f));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/autoOn$")) {
			this.getSurface().fireChanged(new MsgInputDynaAutoOn(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0)) == 1.0f));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/leftSideChain$")) {
			this.getSurface().fireChanged(new MsgInputDynaLeftSideChain(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0)) == 1.0f));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/filterOn$")) {
			this.getSurface().fireChanged(new MsgInputDynaFilterOn(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0)) == 1.0f));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/pair$")) {
			this.getSurface().fireChanged(new MsgInputDynaPair(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0)) == 1.0f));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/attack$")) {
			this.getSurface().fireChanged(new MsgInputDynaAttack(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0))));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/(decay|release)$")) {
			this.getSurface().fireChanged(new MsgInputDynaDecayRelease(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0))));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/gain$")) {
			this.getSurface().fireChanged(new MsgInputDynaGain(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0))));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/hold$")) {
			this.getSurface().fireChanged(new MsgInputDynaHold(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0))));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/knee$")) {
			this.getSurface().fireChanged(new MsgInputDynaKnee(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0))));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/range$")) {
			this.getSurface().fireChanged(new MsgInputDynaRange(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0))));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/ratio$")) {
			this.getSurface().fireChanged(new MsgInputDynaRatio(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0))));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/threshold$")) {
			this.getSurface().fireChanged(new MsgInputDynaThreshold(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0))));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/keyIn$")) {
			this.getSurface().fireChanged(new MsgInputDynaKeyIn(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0))));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/filterType$")) {
			this.getSurface().fireChanged(new MsgInputDynaFilterType(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0))));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/filterFreq$")) {
			this.getSurface().fireChanged(new MsgInputDynaFilterFreq(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0))));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/filterQ$")) {
			this.getSurface().fireChanged(new MsgInputDynaFilterQ(processorNr, selectedInput, OscObjectUtil.toFloat(msg.get(0))));
		}

		return MessageHandleStatus.MESSAGE_HANDLED;
	}

}
