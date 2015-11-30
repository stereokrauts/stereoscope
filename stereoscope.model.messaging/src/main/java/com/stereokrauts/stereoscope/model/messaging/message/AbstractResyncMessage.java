package com.stereokrauts.stereoscope.model.messaging.message;

/**
 * This class represents a base class for all resync messages.
 * 
 * @author theide
 * @author jansen
 * 
 * @param <T>
 */
public abstract class AbstractResyncMessage<T> extends AbstractMessage<T> {

	public AbstractResyncMessage(final T attachment) {
		super(attachment);
	}

}
