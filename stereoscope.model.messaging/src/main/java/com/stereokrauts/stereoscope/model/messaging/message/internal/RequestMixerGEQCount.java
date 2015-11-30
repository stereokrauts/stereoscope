/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message should be triggered, when a system component
 * needs to know about the number of GEQs of the mixer.
 * Corresponds to "ResponseMixerGEQCount".
 * @author jansen
 *
 */
public class RequestMixerGEQCount extends AbstractInternalMessage<Boolean> {

	public RequestMixerGEQCount(boolean attachment) {
		super(attachment);
	}

}
