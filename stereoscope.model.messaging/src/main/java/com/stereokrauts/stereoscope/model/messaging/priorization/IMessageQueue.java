package com.stereokrauts.stereoscope.model.messaging.priorization;

import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;

/**
 * This interface is to be implemented by all message queues that are used
 * for message dispatching of MessageWithSender objects.
 * @author theide
 *
 */
public interface IMessageQueue {
	boolean tryAdd(final MessageWithSender e) throws Exception;
	boolean add(final MessageWithSender m);
	MessageWithSender take() throws Exception;
	MessageWithSender poll();
}
