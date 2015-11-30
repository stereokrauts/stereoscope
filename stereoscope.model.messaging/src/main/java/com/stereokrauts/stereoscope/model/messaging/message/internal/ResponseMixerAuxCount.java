package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalCountMessage;

public class ResponseMixerAuxCount extends
	AbstractInternalCountMessage<Boolean> {

	public ResponseMixerAuxCount(int auxiliaries, Boolean attachment) {
			super(auxiliaries, attachment);
	}

}
