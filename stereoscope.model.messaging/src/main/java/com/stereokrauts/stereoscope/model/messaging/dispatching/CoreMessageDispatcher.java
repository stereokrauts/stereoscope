package com.stereokrauts.stereoscope.model.messaging.dispatching;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import model.mixer.interfaces.IProvideChannelNames;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IObservableMessageSender;
import com.stereokrauts.stereoscope.model.messaging.priorization.IMessageQueue;
import com.stereokrauts.stereoscope.model.messaging.priorization.MessageQueueFactory;

/**
 * This class receives messages from the mixer and the surfaces and distributes
 * them to all other registered observers (excluding the sender of the original
 * message).
 * 
 * Boring stuff, really.
 * 
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 * 
 */
public class CoreMessageDispatcher implements IMessageReceiver,
		IObservableMessageSender, IProvideChannelNames, Runnable {

	private static final SLogger LOG = StereoscopeLogManager
			.getLogger("message-dispatcher");

	private final IMessageQueue messageQueue = MessageQueueFactory
			.getMessageQueue("overriding-priority");

	/**
	 * All the registered clients that want to be notified of changes.
	 */
	private final List<IMessageReceiver> observers;

	private boolean running;

	/**
	 * Create a new UpdateManager with no clients.
	 */
	protected CoreMessageDispatcher() {
		this.observers = new ArrayList<IMessageReceiver>();
	}

	/**
	 * Register a new client (surface, mixer) to the update manager.
	 * 
	 * @param client
	 *            The client to add
	 */
	@Override
	public final void registerObserver(final IMessageReceiver client) {
		synchronized (this.observers) {
			if (!this.observers.contains(client)) {
				this.observers.add(client);
			}
		}
	}

	/**
	 * Remove a client (surface, mixer) form the update manager.
	 * 
	 * @param client
	 *            The client to remove
	 */
	public final void unregisterObserver(final IMessageReceiver client) {
		synchronized (this.observers) {
			if (this.observers.contains(client)) {
				this.observers.remove(client);
			}
		}
	}

	@Override
	public final void getChannelNames() {
		synchronized (this.observers) {
			for (final IMessageReceiver ob : this.observers) {
				if (ob instanceof IProvideChannelNames) {
					((IProvideChannelNames) ob).getChannelNames();
				}
			}
		}
	}

	@Override
	public void handleNotification(final IMessageWithSender msg) {
		try {
			if (msg instanceof MessageWithSender) {
				this.messageQueue.tryAdd((MessageWithSender) msg);
			}
		} catch (final Exception e) {
			LOG.log(Level.SEVERE, "Enqueueing of a message failed", e);
		}
	}

	@Override
	public final void run() {
		this.running = true;
		while (this.running) {
			try {
				this.processOneQueueItem();
			} catch (final Exception e) {
				LOG.log(Level.SEVERE, "Message dispatching failed", e);
			}
		}
	}

	private MessageWithSender processOneQueueItem() throws Exception {
		final MessageWithSender msg = this.messageQueue.take();
		synchronized (this.observers) {
			for (final IMessageReceiver ob : this.observers) {
				if (ob != msg.getSender()) {
					LOG.finest("Dispatching " + msg.getSender() + ":"
							+ msg.getMessage() + " --> " + ob);
					ob.handleNotification(msg);
				}
			}
		}
		return msg;
	}

}
