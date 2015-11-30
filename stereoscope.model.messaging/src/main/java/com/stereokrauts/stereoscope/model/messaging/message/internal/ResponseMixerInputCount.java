package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalCountMessage;

public class ResponseMixerInputCount extends
	AbstractInternalCountMessage<Boolean> {

	public ResponseMixerInputCount(int inputs, Boolean attachment) {
			super(inputs, attachment);
	}

}
