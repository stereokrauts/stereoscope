package model.protocol.osc.handler;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscConstants;
import model.protocol.osc.OscMessageRelay;
import model.protocol.osc.OscObjectUtil;

import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputPan;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqF;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqFilterType;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqHPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqLPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqModeChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;

/**
 * This handler is for OSC messages that change the channel strip controls.
 */
public class OscInputChannelStripMsgHandler extends AbstractOscMessageHandler {
	private static final String[] LISTEN_ON = {
		"^" + OscMessageRelay.OSC_PREFIX + "/stateful/input/peq/(.*)",
		"^" + OscMessageRelay.OSC_PREFIX + "/stateful/input/misc/(.*)",
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

		final int chn = this.getSurface().getState().getCurrentInput();
		final float value = OscObjectUtil.toFloat(msg.get(0));
		final String[] splitted = OscAddressUtil.stringify(msg.address()).split("/");


		if (OscAddressUtil.stringify(msg.address()).matches(".*/peq/band/[0-9]/.*+(/z)?$")) {
			this.handleInputPeqBandMessage(chn, splitted, value);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/peq/band/[0-9]/type/*")) {
			this.handleFilterCharacteristicsMessage(chn, splitted, value);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/peq/mode$")) {
			this.getSurface().fireChanged(new MsgInputPeqModeChanged(chn, OscObjectUtil.toFloat(msg.get(0)) == 1.0f));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/peq/peqOn$")) {
			this.getSurface().fireChanged(new MsgInputPeqOnChanged(chn, OscObjectUtil.toFloat(msg.get(0)) == 1.0f));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/peq/hpfOn$")) {
			this.getSurface().fireChanged(new MsgInputPeqHPFChanged(chn, OscObjectUtil.toFloat(msg.get(0)) == 1.0f));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/peq/lpfOn$")) {
			this.getSurface().fireChanged(new MsgInputPeqLPFChanged(chn, OscObjectUtil.toFloat(msg.get(0)) == 1.0f));		
			// misc messages
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/channelOn$")) {
			final int channel = this.getSurface().getState().getCurrentInput();
			this.getSurface().fireChanged(new MsgChannelOnChanged(channel, OscObjectUtil.toFloat(msg.get(0)) == 1.0f));
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/level(/z)?$")) {
			this.handleInputGenericMessage(chn, "level", value);
		} else if (OscAddressUtil.stringify(msg.address()).matches(".*/pan(/z)?$")) {
			this.handleInputGenericMessage(chn, "pan", value);
		} else {
			return MessageHandleStatus.MESSAGE_NOT_HANDLED;
		}
		return MessageHandleStatus.MESSAGE_HANDLED;
	}

	private void handleFilterCharacteristicsMessage(final int chn, final String[] splitted, final float value) {

		final int band = Integer.parseInt(splitted[5]) - 1;

		final String filterType = splitted[6];
		int filterTypeNumber;

		if (filterType.matches("lowShelf") && value == 1.0f) {
			filterTypeNumber = 1;
		} else if (filterType.matches("hiShelf") && value == 1.0f) {
			filterTypeNumber = 2;
		} else 	if (filterType.matches("lpf") && value == 1.0f) {
			filterTypeNumber = 3;
		} else if (filterType.matches("hpf") && value == 1.0f) {
			filterTypeNumber = 4;
		} else {
			filterTypeNumber = 0; // parametric
		}

		this.getSurface().fireChanged(new MsgInputPeqFilterType(chn, band, filterTypeNumber));
	}

	private void handleInputPeqBandMessage(final int chn, final String[] splitted, final float value) {

		final int band = Integer.parseInt(splitted[6]) - 1;
		final String msgType = splitted[7];

		if (msgType.matches("q")) {
			this.getSurface().fireChanged(new MsgInputPeqQ(chn, band, value));
		} else if (msgType.matches("f")) {
			this.getSurface().fireChanged(new MsgInputPeqF(chn, band, value));
		} else if (msgType.matches("g")) {
			this.getSurface().fireChanged(new MsgInputPeqG(chn, band, this.convertToCenteredControl(value)));
		}
	}

	private void handleInputGenericMessage(final int chn, final String msgType, final float value) {
		if (msgType.matches("level")) {
			this.getSurface().fireChanged(new MsgChannelLevelChanged(chn, value));
		} else if (msgType.matches("pan")) {
			this.getSurface().fireChanged(new MsgInputPan(chn, this.convertToCenteredControl(value)));
		}
	}

	private float convertToCenteredControl(final float value) {
		return (value - OscConstants.CENTER_CONTROL_OFFSET) * OscConstants.CENTER_CONTROL_SCALE_FACTOR;
	}

	public final void setCurrentElementValues(final int idx, final float value) {
		//this.currentElementValues[idx] = value;

		/*
		 * this is needed as a snapping stub
		 */
	}



}
