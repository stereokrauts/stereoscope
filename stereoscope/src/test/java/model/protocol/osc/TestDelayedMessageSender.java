package model.protocol.osc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import model.protocol.osc.impl.OscMessage;

import org.junit.Ignore;


public class TestDelayedMessageSender {
	private static final int NUM_MESSAGES = 30;
	static final long DELAY_MILLIES = 5;

	static long nextEarliestReception = 0L;

	@Ignore
	public void testProperDelay()
	{
		final DummySender dummy = new DummySender();
		final DelayedOscMessageSender del = new DelayedOscMessageSender(dummy, DELAY_MILLIES);
		long earliestReception = 0;
		for (int i = 0; i < NUM_MESSAGES; i++) {
			final IOscMessage m = new OscMessage(OscAddressUtil.create("Test"));
			dummy.received = false;
			del.sendMessage(m);
			synchronized(dummy) {
				while (!dummy.received) {
					try {
						dummy.wait();
					} catch (final InterruptedException e) {
						fail("Exception catched: " + e);
					}
				}
			}
			assertEquals("Assure that sent and received message are equal", m, dummy.lastMessage);
			if (dummy.receivedTimestamp < earliestReception) {
				fail("Message " + i + " returned to early: delta=" + (earliestReception - dummy.receivedTimestamp) + ", earliest=" + earliestReception + ", received=" + dummy.receivedTimestamp);
			}
			earliestReception = nextEarliestReception;
		}
	}
}
