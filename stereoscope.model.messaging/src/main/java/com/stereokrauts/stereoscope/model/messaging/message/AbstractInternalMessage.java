/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message;

/**
 * This class is the super class for all 
 * internal messages, e.g. request/response.
 * It should be used for registering handlers.
 * @author jansen
 *
 */
public abstract class AbstractInternalMessage<T> extends AbstractMessage<T> {

	/**
	 * @param attachment
	 */
	public AbstractInternalMessage(final T attachment) {
		super(attachment);
	}

}
