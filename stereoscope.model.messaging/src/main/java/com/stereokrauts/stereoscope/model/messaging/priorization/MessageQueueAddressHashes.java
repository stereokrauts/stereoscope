package com.stereokrauts.stereoscope.model.messaging.priorization;

import java.util.HashMap;

import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.CouldNotCalculateException;


/**
 * This class manages the address hashes of AbstractMessages.
 * @author theide
 *
 */
final class MessageQueueAddressHashes {
	private final HashMap<Integer, MessageWithSender> queueHashValues;

	public MessageQueueAddressHashes() {
		this.queueHashValues = new HashMap<Integer, MessageWithSender>();
	}
	
	public MessageWithSender getCompetingMessageFromHashQueue(final MessageWithSender msg) throws CouldNotCalculateException {
		return this.queueHashValues.get(msg.getMessage().addressHashCode());
	}

	public boolean messageWithSameAddressIsInHashQueue(final MessageWithSender msg) throws CouldNotCalculateException {
		return this.queueHashValues.containsKey(msg.getMessage().addressHashCode());
	}
	
	public void removeFromHashQueue(final MessageWithSender msg) {
		try {
			this.queueHashValues.remove(msg.getMessage().addressHashCode());
		} catch (final CouldNotCalculateException e) {
			// if it throws this exception it will never have been added
			// to the queue because it threw the same exception then.
			return;
		}
	}
	
	public void addToHashQueue(final MessageWithSender msg) throws CouldNotCalculateException {
		this.queueHashValues.put(msg.getMessage().addressHashCode(), msg);
	}
}