package com.stereokrauts.stereoscope.model.messaging.priorization;

import java.util.Comparator;

import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;


/**
 * This class compares the priority of two messages.
 * @author theide
 *
 */
class PriorityMessageComparator implements Comparator<MessageWithSender> {

	@Override
	public int compare(final MessageWithSender aMessage, final MessageWithSender otherMessage) {
		return aMessage.getMessage().getPriority().compareTo(otherMessage.getMessage().getPriority());
	}

}
