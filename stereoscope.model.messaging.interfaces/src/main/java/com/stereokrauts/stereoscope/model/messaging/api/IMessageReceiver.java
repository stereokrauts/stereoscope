package com.stereokrauts.stereoscope.model.messaging.api;


/**
 * Defines functions for an observer for a surface.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 * @author Roland Jansen&lt;jansen@stereokrauts.com&gt;
 */
public interface IMessageReceiver {
	/**
	 * This function is called, when a parameter which is controled by
	 * messages has changed.
	 * @param sender The sender of this message
	 * @param msg The message which contains specific information about
	 * what has changed.
	 */
	void handleNotification(IMessageWithSender msg);
}
