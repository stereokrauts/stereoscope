/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.resync;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractResyncMessage;

/**
 * This message should be triggered, when a 
 * complete channel strip (input) should be resynced.
 * Attachment is the channel number.
 * @author jansen
 *
 */
public class ResyncChannelStrip extends AbstractResyncMessage<Integer> {

	public ResyncChannelStrip(int attachment) {
		super(attachment);
	}

}
