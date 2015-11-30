package com.stereokrauts.stereoscope.model.messaging.annotations;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

public @interface StereoscopeMessageHandler {
	Class<? extends AbstractMessage<?>> value();
}
