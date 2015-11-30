package model.protocol.osc;

import java.util.logging.Level;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;

/**
 * This class limits the amount of messages being sent to the OSC frontend
 * at a time by enforcing a minimum delay between messages.
 */
public final class DelayedOscMessageSender implements ISendOscMessages, Runnable {
	private static final int MILLIES_PER_SEC = 1000;

	private static final SLogger LOG = StereoscopeLogManager.getLogger("delayed-osc-sender");

	private static final int OSC_MESSAGE_BUFFER_SECS = 6; /* Buffer length in seconds */
	private static final long FULL_BUFFER_SLEEP_TIME_MS = 100;

	private final ISendOscMessages originalSender;

	private final long delayMillies;

	private final DelayedOverridingBlockingQueue sendBuffer;

	public DelayedOscMessageSender(final ISendOscMessages myOriginalSender, final long myDelayMillies)
	{
		this.originalSender = myOriginalSender;
		this.delayMillies = myDelayMillies;

		LOG.info("The requested minimum delay between OSC messages to " + myOriginalSender + " is: " + myDelayMillies + " milliseconds");

		this.sendBuffer = new DelayedOverridingBlockingQueue((int) (OSC_MESSAGE_BUFFER_SECS * MILLIES_PER_SEC / myDelayMillies));

		final Thread myThread = new Thread(this, "Delayed Message sender for " + myOriginalSender);
		myThread.start();
	}


	@Override
	public void sendMessage(final IOscMessage m) {
		try {
			this.sendBuffer.add(m);
		} catch (final IllegalStateException e) {
			this.retrySendMessageAfterDelay(m);
		}
	}

	private void retrySendMessageAfterDelay(final IOscMessage m) {
		try {
			LOG.info("Queue overload, retrying after " + FULL_BUFFER_SLEEP_TIME_MS + " milliseconds");
			Thread.sleep(FULL_BUFFER_SLEEP_TIME_MS);
			this.sendBuffer.add(m);
		} catch (final Exception e1) {
			LOG.log(Level.SEVERE, "There seems to be a serious system overload", e1);
		}
	}

	public void shutdown() {
		this.running = false;
	}
	private boolean running = true;


	@Override
	public void run() {
		IOscMessage msg;
		long lastMessageSent = 0;
		while (this.running) {
			try {
				msg = this.sendBuffer.take();
				this.originalSender.sendMessage(msg);
				lastMessageSent = System.currentTimeMillis();
				while (lastMessageSent >= System.currentTimeMillis() - this.delayMillies) {
					Thread.sleep(System.currentTimeMillis() + this.delayMillies - lastMessageSent);
				}
			} catch (final InterruptedException e) {
				LOG.log(Level.WARNING, "Interrupted while waiting for delay", e);
			}

		}
	}

}
