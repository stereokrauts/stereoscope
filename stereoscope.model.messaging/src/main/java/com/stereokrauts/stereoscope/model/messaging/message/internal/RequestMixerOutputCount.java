/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message should be triggered, when a system component
 * needs to know about the number of outputs of the mixer.
 * Corresponds to "ResponseMixerOutputCount".
 * @author jansen
 *
 */
public class RequestMixerOutputCount extends AbstractInternalMessage<Boolean> {

	public RequestMixerOutputCount(boolean attachment) {
		super(attachment);
	}

}
