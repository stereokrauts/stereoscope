/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message should be triggered, when a system component
 * needs to know about all status of all bus outputs of the mixer
 * @Param attachment should always be true
 * @author jansen
 *
 */
public class RequestMixerBusAllStatus extends AbstractInternalMessage<Boolean> {

	public RequestMixerBusAllStatus(boolean attachment) {
		super(attachment);
	}

}
