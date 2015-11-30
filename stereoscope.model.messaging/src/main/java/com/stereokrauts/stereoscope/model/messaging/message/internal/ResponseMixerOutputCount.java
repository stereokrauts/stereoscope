package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalCountMessage;

public class ResponseMixerOutputCount extends
	AbstractInternalCountMessage<Boolean> {

	public ResponseMixerOutputCount(int outputs, Boolean attachment) {
			super(outputs, attachment);
	}

}
