/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.resync;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractResyncMessage;

/**
 * This message should be triggered, when a all
 * components should be resynced.
 * @author jansen
 *
 */
public class ResyncOutputs extends AbstractResyncMessage<Boolean> {
	private final int currentAux;
	
	public ResyncOutputs(int currentAux, boolean attachment) {
		super(attachment);
		this.currentAux = currentAux;
	}

	public int getCurrentAux() {
		return currentAux;
	}

}
