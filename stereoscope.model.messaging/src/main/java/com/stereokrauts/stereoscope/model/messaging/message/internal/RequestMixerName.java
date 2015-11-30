/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message should be triggered, when a system component
 * needs to know about the name of the mixer (plugin name).
 * Corresponds to "ResponseMixerName".
 * @author jansen
 *
 */
public class RequestMixerName extends AbstractInternalMessage<Boolean> {

	public RequestMixerName(boolean attachment) {
		super(attachment);
	}

}
