/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message should be triggered, when a system component
 * needs to know if the mixer has graphical EQs.
 * Corresponds to "ResponseMixerIsMixerWithGraphicalEQ".
 * @Param attachment should always be true.
 * @author jansen
 *
 */
public class RequestMixerIsMixerWithGraphicalEQ extends AbstractInternalMessage<Boolean> {

	public RequestMixerIsMixerWithGraphicalEQ(boolean attachment) {
		super(attachment);
	}

}
