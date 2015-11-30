/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalCountMessage;

/**
 * This message should be triggered, when a system component
 * needs to know if a specific GEQ is a FlexEQ.
 * @param int geq The GEQ of interest.
 * @author jansen
 *
 */
public class RequestMixerGEQIsFlexEQ extends AbstractInternalCountMessage<Boolean> {

	/**
	 * @param attachment should always be true.
	 */
	public RequestMixerGEQIsFlexEQ(int geq, boolean attachment) {
		super(geq, attachment);
	}

}
