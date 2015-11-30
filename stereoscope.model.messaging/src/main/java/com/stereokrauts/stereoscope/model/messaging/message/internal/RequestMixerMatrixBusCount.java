/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message should be triggered, when a system component
 * needs to know about the number of matrix busses of the mixer.
 * Corresponds to "ResponseMixerMatrixBusCount".
 * @author jansen
 *
 */
public class RequestMixerMatrixBusCount extends AbstractInternalMessage<Boolean> {

	public RequestMixerMatrixBusCount(boolean attachment) {
		super(attachment);
	}

}
