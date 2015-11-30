package com.stereokrauts.stereoscope.model.messaging.api;


/**
 * This interface must be implemented by all classes that
 * are able to receive messages.
 * @author theide
 *
 */
public interface IMessageHandler {
	boolean handleMessage(IMessage msg);
}
