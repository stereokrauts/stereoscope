/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message should be triggered, when a system component
 * needs to know about the number of auxiliaries of the mixer.
 * Corresponds to "ResponseMixerAuxCount".
 * @author jansen
 *
 */
public class RequestMixerAuxCount extends AbstractInternalMessage<Boolean> {

	public RequestMixerAuxCount(boolean attachment) {
		super(attachment);
	}

}
