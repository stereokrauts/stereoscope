/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message should be triggered, when a system component
 * needs to know about all the delay times of the mixer
 * (currently just outputs).
 * @Param attachment should always be true
 * @author jansen
 *
 */
public class RequestMixerDelayAllTimeValues extends AbstractInternalMessage<Boolean> {

	public RequestMixerDelayAllTimeValues(boolean attachment) {
		super(attachment);
	}

}
