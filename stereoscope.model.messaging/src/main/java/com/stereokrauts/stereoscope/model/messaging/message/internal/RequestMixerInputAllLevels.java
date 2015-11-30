/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message should be triggered, when a system component
 * needs to know all input level values, e.g. for stateful messaging.
 * @author jansen
 *
 */
public class RequestMixerInputAllLevels extends AbstractInternalMessage<Boolean> {

	/**
	 * @param attachment should always be true.
	 */
	public RequestMixerInputAllLevels(boolean attachment) {
		super(attachment);
	}

}
