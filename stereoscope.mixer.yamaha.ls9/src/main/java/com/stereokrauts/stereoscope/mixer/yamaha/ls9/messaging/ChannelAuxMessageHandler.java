package com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging;

import com.stereokrauts.stereoscope.mixer.yamaha.ls9.Ls9Mixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelAuxLevelLabel;


public class ChannelAuxMessageHandler extends HandlerForLs9 implements
		IMessageHandler {

	public ChannelAuxMessageHandler(final Ls9Mixer mixer) {
		super(mixer);
	}

	/**
	 * This methods receives all channel aux related messages from
	 * the function handleNotification.
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
			final String label = this.mixer.getLabelMaker().getYamahaLabelLevel10Db(value);
			this.mixer.fireChange(new MsgChannelAuxLevelLabel(channel, aux, label));
			return true;
		} else if (msg instanceof MsgAuxSendOnChanged) {
			final MsgAuxSendOnChanged msgAuxSendOnChanged = (MsgAuxSendOnChanged) msg;
			this.mixer.getModifier().changedChannelAuxOn(msgAuxSendOnChanged.getChannel(),
					msgAuxSendOnChanged.getAux(),
					msgAuxSendOnChanged.getAttachment());
			return true;
		}
		return false;
	}

}
