package com.stereokrauts.stereoscope.model.messaging.api;


/**
 * This interface defines methods for observable
 * surfaces.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public interface IObservableMessageSender extends IMessageSender {
	/** This function registers a new observer, that will be
	 * informed when any message is sent by the implementing
	 * object.
	 * @param observer A observer that will be informed in future.
	 */
	void registerObserver(IMessageReceiver observer);
}
