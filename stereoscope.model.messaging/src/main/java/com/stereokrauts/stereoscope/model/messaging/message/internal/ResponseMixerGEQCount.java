package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalCountMessage;

public class ResponseMixerGEQCount extends
	AbstractInternalCountMessage<Boolean> {

	public ResponseMixerGEQCount(int geqs, Boolean attachment) {
			super(geqs, attachment);
	}

}
