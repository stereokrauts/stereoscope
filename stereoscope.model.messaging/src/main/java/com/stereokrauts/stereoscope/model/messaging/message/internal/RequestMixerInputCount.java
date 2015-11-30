/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message should be triggered, when a system component
 * needs to know about the number of inputs of the mixer.
 * Corresponds to "ResponseMixerInputCount".
 * @author jansen
 *
 */
public class RequestMixerInputCount extends AbstractInternalMessage<Boolean> {

	public RequestMixerInputCount(boolean attachment) {
		super(attachment);
	}

}
