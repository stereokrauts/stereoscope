/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.resync;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractResyncMessage;

/**
 * This message should be triggered, when a all
 * input channel levels should be resynced.
 * @author jansen
 *
 */
public class ResyncChannelLevels extends AbstractResyncMessage<Boolean> {

	public ResyncChannelLevels(boolean attachment) {
		super(attachment);
	}

}
