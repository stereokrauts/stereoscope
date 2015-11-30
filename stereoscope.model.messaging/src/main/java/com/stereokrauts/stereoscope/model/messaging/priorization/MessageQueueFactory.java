package com.stereokrauts.stereoscope.model.messaging.priorization;

/**
 * This class exists to abstract the creation of an overriding message queue.
 * @author theide
 *
 */
public final class MessageQueueFactory {
	private MessageQueueFactory() { }
	
	public static IMessageQueue getMessageQueue(final String identifier) {
		if (identifier.equals("priority")) {
			return new PriorityMessageQueue();
		} else if (identifier.equals("overriding-priority")) {
			return new OverridingPriorityMessageQueue();
		} else {
			throw new IllegalArgumentException("Unknown identifier: " + identifier + ". Allowed are priority and overriding-priority.");
		}
	}
}
