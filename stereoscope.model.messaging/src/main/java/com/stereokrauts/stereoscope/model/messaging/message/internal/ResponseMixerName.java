package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

public class ResponseMixerName extends
	AbstractInternalMessage<Boolean> {
	private final String name;
	
	public ResponseMixerName(String name, Boolean attachment) {
			super(attachment);
			this.name = name;
	}

	public String getName() {
		return name;
	}

}
