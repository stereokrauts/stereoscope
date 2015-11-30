package com.stereokrauts.stereoscope.model.messaging.message.mixerglobal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

public final class MsgDcaLevelChanged extends AbstractMessage<Float> {
	private final int dcaNumber;

	public MsgDcaLevelChanged(final int dcaNumber, final Float attachmentValue) {
		super(attachmentValue);
		this.dcaNumber = dcaNumber;
	}

	public int getDcaNumber() {
		return dcaNumber;
	}
}
