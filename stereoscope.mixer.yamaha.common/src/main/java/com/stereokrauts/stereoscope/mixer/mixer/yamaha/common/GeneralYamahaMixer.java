package com.stereokrauts.stereoscope.mixer.mixer.yamaha.common;


import java.util.ArrayList;
import java.util.List;

import model.mixer.interfaces.IAmMixer;

import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

/**
 * This class centralizes functions that are equal on all
 * Yamaha mixers.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public abstract class GeneralYamahaMixer extends IAmMixer {
	/**
	 * This list contains all objects that are currently
	 * registered as observers of this object.
	 */
	private final ArrayList<IMessageReceiver> observers;
	
	/**
	 * @return a list of the current observers of this object.
	 */
	protected final List<IMessageReceiver> getObservers() {
		return this.observers;
	}

	/**
	 * Creates a new GeneralYamahaMixer object.
	 */
	protected GeneralYamahaMixer() {
		this.observers = new ArrayList<IMessageReceiver>();
	}

	/**
	 * Adds a new observer to this object.
	 * @param observer The new observing object.
	 */
	@Override
	public final void registerObserver(
			final IMessageReceiver observer) {
		this.observers.add(observer);
	}
	
	/**
	 * Notifies all observers that an parameter which is controlled through
	 * messages has changed.
	 * @param msg A message further specifying the parameter change.
	 */
	public final void fireChange(final AbstractMessage<?> msg) {
		for (final IMessageReceiver m : this.observers) {
			m.handleNotification(new MessageWithSender(this, msg));
		}
	}
		
}
