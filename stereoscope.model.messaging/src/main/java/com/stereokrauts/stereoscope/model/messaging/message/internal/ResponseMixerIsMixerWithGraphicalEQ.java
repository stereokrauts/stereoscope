/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message is a response from the mixer 
 * to the message "RequestMixerIsMixerWithGraphicalEQ".
 * @Param attachment rather true or false
 * @author jansen
 *
 */
public class ResponseMixerIsMixerWithGraphicalEQ extends AbstractInternalMessage<Boolean> {

	public ResponseMixerIsMixerWithGraphicalEQ(boolean attachment) {
		super(attachment);
	}

}
