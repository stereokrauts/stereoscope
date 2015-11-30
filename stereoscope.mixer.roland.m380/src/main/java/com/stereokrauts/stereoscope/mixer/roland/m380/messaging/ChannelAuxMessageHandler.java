package com.stereokrauts.stereoscope.mixer.roland.m380.messaging;

import com.stereokrauts.stereoscope.mixer.roland.m380.M380Mixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelAuxLevelLabel;


public class ChannelAuxMessageHandler extends HandlerForM380 implements
		IMessageHandler {

	public ChannelAuxMessageHandler(final M380Mixer mixer) {
		super(mixer);
	}

	/**
	 * This methods receives all channel aux related messages from the function
	 * handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	@Override
	public final boolean handleMessage(final IMessage msg) {
		if (msg instanceof MsgAuxSendChanged) {
			final MsgAuxSendChanged msgAuxSendChanged = (MsgAuxSendChanged) msg;
			final int channel = msgAuxSendChanged.getChannel();
			final int aux = msgAuxSendChanged.getAux();
			final float value = msgAuxSendChanged.getAttachment();
			this.mixer.getModifier().changedChannelAuxLevel(channel, aux, value);
			final String label = this.mixer.getLabelMaker().getFaderLevelLabel(value);
			this.mixer.fireChange(new MsgChannelAuxLevelLabel(channel, aux, label));
			return true;
		} else if (msg instanceof MsgAuxSendOnChanged) {
			final MsgAuxSendOnChanged msgAuxSendOnChanged = (MsgAuxSendOnChanged) msg;
			this.mixer.getModifier().changedChannelAuxOn(msgAuxSendOnChanged.getChannel(),
					msgAuxSendOnChanged.getAux(),
					msgAuxSendOnChanged.getAttachment());
		}
		return false;
	}

}
