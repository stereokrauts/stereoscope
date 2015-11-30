package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalCountMessage;

public class ResponseMixerBusCount extends
	AbstractInternalCountMessage<Boolean> {

	public ResponseMixerBusCount(int busses, Boolean attachment) {
			super(busses, attachment);
	}

}
