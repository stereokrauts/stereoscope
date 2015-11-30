package com.stereokrauts.stereoscope.model.messaging.priorization;

import java.util.concurrent.PriorityBlockingQueue;

import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;


/**
 * This class implements a message queue with priorization for
 * messages that have a sender.
 * @author theide
 *
 */
public final class PriorityMessageQueue implements IMessageQueue {


	private static final int MESSAGE_QUEUE_SIZE = 1000;
	
	private static final int MAX_SIZE_BEFORE_LOW_DISCARD = 10000;
	private static final int LOW_DISCARD_BACKPRESSURE_SLEEPTIME = 1000;
	
	private static final int MAX_SIZE_BEFORE_NORMAL_DISCARD = 50000;
	private static final int NORMAL_DISCARD_BACKPRESSURE_SLEEPTIME = 200;
	
	private final PriorityBlockingQueue<MessageWithSender> q;
	
	PriorityMessageQueue() {
		this.q = new PriorityBlockingQueue<MessageWithSender>(MESSAGE_QUEUE_SIZE, new PriorityMessageComparator());
	}
	
	@Override
	public boolean tryAdd(final MessageWithSender e) throws PriorityQueueOverloadedException {
		if (this.q.size() > MAX_SIZE_BEFORE_NORMAL_DISCARD) {
			if (e.getMessage().getPriority().compareTo(PriorizationValue.NORMAL) >= 0) {
				try {
					// Backpressure on the sender
					Thread.sleep(NORMAL_DISCARD_BACKPRESSURE_SLEEPTIME);
				} catch (final InterruptedException e1) {
					throw new PriorityQueueOverloadedException(e);
				}
			}
		} else if (this.q.size() > MAX_SIZE_BEFORE_LOW_DISCARD) {
			if (e.getMessage().getPriority().compareTo(PriorizationValue.LOW) >= 0) {
				try {
					// Backpressure on the sender
					Thread.sleep(LOW_DISCARD_BACKPRESSURE_SLEEPTIME);
				} catch (final InterruptedException e1) {
					throw new PriorityQueueOverloadedException(e);
				}
			}
		}
		return this.q.add(e);
	}
	
	@Override
	public boolean add(final MessageWithSender m) {
		return this.q.add(m);
	}
	
	@Override
	public MessageWithSender take() throws InterruptedException {
		return this.q.take();
	}
	
	@Override
	public MessageWithSender poll() {
		return this.q.poll();
	}
}
