package com.stereokrauts.stereoscope.mixer.roland.m380.messaging;

import com.stereokrauts.stereoscope.mixer.roland.m380.M380Mixer;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.mixerglobal.MsgDcaLevelChanged;

public final class DcaMessageHandler extends HandlerForM380  implements IMessageHandler {
	public DcaMessageHandler(final M380Mixer mixer) {
		super(mixer);
	}

	@Override
	public boolean handleMessage(final IMessage msg) {
		final MsgDcaLevelChanged dcamsg = ((MsgDcaLevelChanged) msg);
		this.mixer.getModifier().changedDcaMaster(dcamsg.getDcaNumber(), dcamsg.getAttachment());
		return true;
	}

}
