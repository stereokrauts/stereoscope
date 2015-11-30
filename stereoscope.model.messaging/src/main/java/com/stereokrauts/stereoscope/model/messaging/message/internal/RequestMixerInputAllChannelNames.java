/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message should be triggered, when a system component
 * needs to know all input channel names.
 * @author jansen
 *
 */
public class RequestMixerInputAllChannelNames extends AbstractInternalMessage<Boolean> {

	/**
	 * @param attachment should always be true.
	 */
	public RequestMixerInputAllChannelNames(boolean attachment) {
		super(attachment);
	}

}
