/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalCountMessage;

/**
 * This message should be triggered, when a system component
 * needs to know all values from a specific channel strip, e.g. for
 * stateful messaging.
 * @Param int input The input channel of interest.
 * @Param boolean attachment should always be true.
 * @author jansen
 *
 */
public class RequestMixerInputAllStripValues extends AbstractInternalCountMessage<Boolean> {

	public RequestMixerInputAllStripValues(int input, boolean attachment) {
		super(input, attachment);
	}

}
