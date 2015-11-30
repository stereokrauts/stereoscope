/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message should be triggered, when a system component
 * needs to know about all status of groups of the mixer
 * @Param attachment should always be true
 * @author jansen
 *
 */
public class RequestMixerGroupsAllStatus extends AbstractInternalMessage<Boolean> {

	public RequestMixerGroupsAllStatus(boolean attachment) {
		super(attachment);
	}

}
