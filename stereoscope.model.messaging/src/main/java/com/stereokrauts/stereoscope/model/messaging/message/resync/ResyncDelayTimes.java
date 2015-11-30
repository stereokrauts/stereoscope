/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.resync;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractResyncMessage;

/**
 * This message should be triggered, when a all
 * delay times should be resynced.
 * @author jansen
 *
 */
public class ResyncDelayTimes extends AbstractResyncMessage<Boolean> {

	public ResyncDelayTimes(boolean attachment) {
		super(attachment);
	}

}
