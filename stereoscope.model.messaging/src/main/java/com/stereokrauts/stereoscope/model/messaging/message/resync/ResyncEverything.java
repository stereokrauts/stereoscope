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
public class ResyncEverything extends AbstractResyncMessage<Boolean> {
	private final int currentAux;
	private final int currentInput;
	private final int currentGeq;
	
	public ResyncEverything(int currentAux, int currentGeq, int currentInput, boolean attachment) {
		super(attachment);
		this.currentAux = currentAux;
		this.currentInput = currentInput;
		this.currentGeq = currentGeq;
	}

	public int getCurrentAux() {
		return currentAux;
	}

	public int getCurrentInput() {
		return currentInput;
	}

	public int getCurrentGeq() {
		return currentGeq;
	}

}
