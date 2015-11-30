package com.stereokrauts.stereoscope.model.messaging.priorization;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.CouldNotCalculateException;


/**
 * This class implements a message queue with priorization for
 * messages that have a sender.
 * @author theide
 *
 */
public final class OverridingPriorityMessageQueue implements IMessageQueue {
	private static final int MESSAGE_QUEUE_SIZE = 1000;
	
	private static final int MAX_SIZE_BEFORE_LOW_DISCARD = 10000;
	private static final int LOW_DISCARD_BACKPRESSURE_SLEEPTIME = 1000;
	
	private static final int MAX_SIZE_BEFORE_NORMAL_DISCARD = 50000;
	private static final int NORMAL_DISCARD_BACKPRESSURE_SLEEPTIME = 200;
	
	private final ReentrantLock hashesLock = new ReentrantLock();
	private final MessageQueueAddressHashes hashes = new MessageQueueAddressHashes();

	private final PriorityBlockingQueue<MessageWithSender> q;
	
	OverridingPriorityMessageQueue() {
		this.q = new PriorityBlockingQueue<MessageWithSender>(MESSAGE_QUEUE_SIZE, new PriorityMessageComparator());
	}
		
	@Override
	public boolean tryAdd(final MessageWithSender e) throws OverridingPriorityQueueOverloadedException {
		if (this.shouldAddMessageToQueue(e)) {
			if (this.q.size() > MAX_SIZE_BEFORE_NORMAL_DISCARD) {
				if (e.getMessage().getPriority().compareTo(PriorizationValue.NORMAL) >= 0) {
					try {
						// Backpressure on the sender
						Thread.sleep(NORMAL_DISCARD_BACKPRESSURE_SLEEPTIME);
					} catch (final InterruptedException e1) {
						throw new OverridingPriorityQueueOverloadedException(e);
					}
				}
			} else if (this.q.size() > MAX_SIZE_BEFORE_LOW_DISCARD) {
				if (e.getMessage().getPriority().compareTo(PriorizationValue.LOW) >= 0) {
					try {
						// Backpressure on the sender
						Thread.sleep(LOW_DISCARD_BACKPRESSURE_SLEEPTIME);
					} catch (final InterruptedException e1) {
						throw new OverridingPriorityQueueOverloadedException(e);
					}
				}
			}
			return this.q.add(e);
		}
		return false;
	}
	
	private boolean shouldAddMessageToQueue(final MessageWithSender message) {
		try {
			this.hashesLock.lock();
			if (this.hashes.messageWithSameAddressIsInHashQueue(message)) {
				final MessageWithSender competitor = this.hashes.getCompetingMessageFromHashQueue(message);
				if (competitor.getMessage().createdTimestamp() <= message.getMessage().createdTimestamp()) {
					// Message in the queue is older, remove it.
					this.q.remove(competitor);
					this.hashes.removeFromHashQueue(competitor);
					this.hashes.addToHashQueue(message);
					return true;
				} else {
					return false;
				}
			}
			this.hashes.addToHashQueue(message);
			return true;
		} catch (final CouldNotCalculateException e) {
			// Something went wrong... deliver the message!
			return true;
		} finally {
			this.hashesLock.unlock();
		}
	}


	
	@Override
	public boolean add(final MessageWithSender m) {
		if (this.shouldAddMessageToQueue(m)) {
			return this.q.add(m);
		} else {
			return false;
		}
	}
	
	@Override
	public MessageWithSender take() throws InterruptedException {
		final MessageWithSender returnValue = this.q.take();
		this.hashesLock.lock();
		this.hashes.removeFromHashQueue(returnValue);
		this.hashesLock.unlock();
		return returnValue;
	}

	@Override
	public MessageWithSender poll() {
		final MessageWithSender returnValue = this.q.poll();
		this.hashesLock.lock();
		this.hashes.removeFromHashQueue(returnValue);
		this.hashesLock.unlock();
		return returnValue;
	}

}
