/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.resync;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractResyncMessage;

/**
 * This message should be triggered, when a all
 * input channel names should be resynced.
 * @author jansen
 *
 */
public class ResyncChannelNames extends AbstractResyncMessage<Boolean> {

	public ResyncChannelNames(boolean attachment) {
		super(attachment);
	}

}
