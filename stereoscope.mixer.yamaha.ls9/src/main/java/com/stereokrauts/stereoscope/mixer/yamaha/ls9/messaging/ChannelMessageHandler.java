	package com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging;

import com.stereokrauts.stereoscope.mixer.yamaha.ls9.Ls9Mixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelLevelLabel;


public class ChannelMessageHandler extends HandlerForLs9 implements IMessageHandler {
	public ChannelMessageHandler(final Ls9Mixer mixer) {
		super(mixer);
	}

	@Override
	public final boolean handleMessage(final IMessage msg) {
		final int chn = ((AbstractChannelMessage<?>) msg).getChannel();
		if (msg instanceof MsgChannelLevelChanged) {
			final MsgChannelLevelChanged msgChannelLevelChanged = (MsgChannelLevelChanged) msg;
			this.mixer.getModifier().changedChannelLevel(chn, msgChannelLevelChanged.getAttachment());
			final String label = this.mixer.getLabelMaker().getYamahaLabelLevel10Db(msgChannelLevelChanged.getAttachment());
			this.mixer.fireChange(new MsgChannelLevelLabel(chn, 0, label));
			return true;
		} else if (msg instanceof MsgChannelOnChanged) {
			final MsgChannelOnChanged msgChannelOnChanged = (MsgChannelOnChanged) msg;
			this.mixer.getModifier().changedChannelOnButton(chn, msgChannelOnChanged.getAttachment());
			return true;
		}
		return false;
	}
}
