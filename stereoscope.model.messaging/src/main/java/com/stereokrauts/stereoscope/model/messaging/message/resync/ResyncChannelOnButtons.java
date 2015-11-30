/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.resync;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractResyncMessage;

/**
 * This message should be triggered, when a all
 * input channel on-buttons should be resynced.
 * @author jansen
 *
 */
public class ResyncChannelOnButtons extends AbstractResyncMessage<Boolean> {

	public ResyncChannelOnButtons(boolean attachment) {
		super(attachment);
	}

}
