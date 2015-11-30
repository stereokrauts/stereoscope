/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message should be triggered, when a system component
 * needs to know about the number of busses of the mixer.
 * Corresponds to "ResponseMixerBusCount".
 * @author jansen
 *
 */
public class RequestMixerBusCount extends AbstractInternalMessage<Boolean> {

	public RequestMixerBusCount(boolean attachment) {
		super(attachment);
	}

}
