package model.protocol.osc;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This implementation of a overriding blocking queue is for the delayed
 * message sender.
 * @author theide
 *
 */
public final class DelayedOverridingBlockingQueue {
	private final ReentrantLock addressHashesLock = new ReentrantLock();
	private final HashMap<Integer, IOscMessage> addressHashesList = new HashMap<Integer, IOscMessage>();

	private final BlockingQueue<IOscMessage> q;

	public DelayedOverridingBlockingQueue(final int initialSize) {
		this.q = new ArrayBlockingQueue<IOscMessage>(initialSize);
	}

	public void add(final IOscMessage m) {
		final Integer hash = OscAddressUtil.stringify(m.address()).hashCode();
		this.addressHashesLock.lock();
		if (this.addressHashesList.containsKey(hash)) {
			this.q.remove(this.addressHashesList.get(hash));
			this.addressHashesList.remove(hash);
		}
		this.addressHashesList.put(hash, m);
		this.addressHashesLock.unlock();
		this.q.add(m);
	}

	public IOscMessage take() throws InterruptedException {
		IOscMessage returnValue;
		returnValue = this.q.take();
		this.addressHashesLock.lock();
		this.addressHashesList.remove(OscAddressUtil.stringify(returnValue.address()).hashCode());
		this.addressHashesLock.unlock();

		return returnValue;
	}
}
