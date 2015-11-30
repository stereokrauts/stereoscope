package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalCountMessage;

public class ResponseMixerMatrixBusCount extends
	AbstractInternalCountMessage<Boolean> {

	public ResponseMixerMatrixBusCount(int busses, Boolean attachment) {
			super(busses, attachment);
	}

}
