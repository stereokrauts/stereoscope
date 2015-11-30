/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;

/**
 * This message should be triggered, when a system component
 * needs to know all the channel on (mute) values at once like for
 * stateful messaging.
 * @Param boolean attachment should always be true.
 * @author jansen
 *
 */
public class RequestMixerInputAllChannelOnButtons extends AbstractInternalMessage<Boolean> {

	/**
	 * @param attachment
	 */
	public RequestMixerInputAllChannelOnButtons(boolean attachment) {
		super(attachment);
	}

}
