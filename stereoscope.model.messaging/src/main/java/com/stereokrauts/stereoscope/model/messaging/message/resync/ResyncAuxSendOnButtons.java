/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.resync;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractResyncMessage;

/**
 * This message should be triggered, when a all
 * aux send buttons should be resynced. Attachment
 * is the auxiliary (0-n) to be synced.
 * @author jansen
 *
 */
public class ResyncAuxSendOnButtons extends AbstractResyncMessage<Integer> {

	public ResyncAuxSendOnButtons(int attachment) {
		super(attachment);
	}

}
