/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.resync;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractResyncMessage;

/**
 * This message should be triggered, when a all
 * geq band levels should be resynced. Attachment
 * is the geq (0-n) to be synced.
 * @author jansen
 *
 */
public class ResyncGeqBandLevels extends AbstractResyncMessage<Integer> {

	public ResyncGeqBandLevels(int attachment) {
		super(attachment);
	}

}
