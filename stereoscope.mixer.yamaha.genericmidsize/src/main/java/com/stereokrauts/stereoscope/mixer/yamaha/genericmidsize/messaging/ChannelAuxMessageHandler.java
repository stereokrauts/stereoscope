package com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.messaging;

import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.GenericMidsizeMixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelAuxLevelLabel;

public class ChannelAuxMessageHandler extends HandlerForGenericMidsize implements IMessageHandler {
	public ChannelAuxMessageHandler(final GenericMidsizeMixer mixer) {
		super(mixer);
	}

	@Override
	public boolean handleMessage(final IMessage msg) {
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
			this.mixer.getModifier().changedChannelAuxSendOn(msgAuxSendOnChanged.getChannel(),
					msgAuxSendOnChanged.getAux(),
					msgAuxSendOnChanged.getAttachment());
			return true;
		} else {
			return false;
		}
	}
	

}
