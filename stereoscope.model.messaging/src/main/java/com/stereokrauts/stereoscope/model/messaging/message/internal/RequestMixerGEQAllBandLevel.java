/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalCountMessage;

/**
 * This message should be triggered, when a system component
 * needs to know all band level values from a specific GEQ.
 * @Param int geq The GEQ of interest.
 * @Param boolean attachment should always be true.
 * @author jansen
 *
 */
public class RequestMixerGEQAllBandLevel extends AbstractInternalCountMessage<Boolean> {

	public RequestMixerGEQAllBandLevel(int geq, boolean attachment) {
		super(geq, attachment);
	}

}
