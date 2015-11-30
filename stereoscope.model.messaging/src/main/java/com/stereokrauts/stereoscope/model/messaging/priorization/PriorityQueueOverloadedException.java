package com.stereokrauts.stereoscope.model.messaging.priorization;

import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;

/**
 * This exception is thrown to the sender of a message when the message queue is
 * overloaded.
 * 
 * @author theide
 * 
 */
public class PriorityQueueOverloadedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final MessageWithSender message;

	public PriorityQueueOverloadedException(final MessageWithSender myMessage) {
		this.message = myMessage;

	}

	public final MessageWithSender getMessageWithSender() {
		return this.message;
	}
}
