package com.stereokrauts.stereoscope.model.messaging;

import com.stereokrauts.stereoscope.model.messaging.api.IMessageSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

/**
 * This class wraps a message with a sender.
 * @author theide
 *
 */
public final class MessageWithSender implements IMessageWithSender {
	private final IMessageSender sender;
	private final AbstractMessage<?> message;

	public MessageWithSender(final IMessageSender source, final AbstractMessage<?> msg) {
		this.sender = source;
		this.message = msg;
	}
	
	@Override
	public IMessageSender getSender() {
		return this.sender;
	}

	@Override
	public AbstractMessage<?> getMessage() {
		return this.message;
	}
}
