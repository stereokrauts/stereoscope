/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message is a response from the mixer 
 * to the message "RequestMixerGEQIsFlexEQ".
 * @Param attachment rather true or false
 * @author jansen
 *
 */
public class ResponseMixerGEQIsFlexEQ extends AbstractInternalMessage<Boolean> {

	public ResponseMixerGEQIsFlexEQ(boolean attachment) {
		super(attachment);
	}

}
