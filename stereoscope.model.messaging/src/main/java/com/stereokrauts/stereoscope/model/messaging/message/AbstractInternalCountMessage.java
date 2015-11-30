/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message;

import com.stereokrauts.stereoscope.model.messaging.priorization.PriorizationValue;

/**
 * This class can be used to construct system messages
 * that contain integer values. E.g. "ResponseMixerInputCount".
 * 
 * @param <T> attachment
 * @param int count 
 * @author jansen
 *
 */
public abstract class AbstractInternalCountMessage<T> extends
		AbstractInternalMessage<T> {
	private final int count;
	
	public AbstractInternalCountMessage(final int count, final T attachment) {
		super(attachment);
		this.count = count;
		this.setPriority(PriorizationValue.HIGH);
	}
	
	public final int getCount() {
		return this.count;
	}

}
