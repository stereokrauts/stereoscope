package com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;

/** This function is called, when the side chain input of a
 * dynamics processor has been switched on/off on the observed object.
 * @param int channel The channel strip (Beginning at 0)
 * @param boolean dyna The dynamics processor (true~1, false~2)
 * @param boolean value Side Chain on/off
 */
public class MsgInputDynaLeftSideChain extends AbstractInputDynamicsMessage<Boolean> {
	public MsgInputDynaLeftSideChain(final int processor, final int channel, final boolean value) {
		super(processor, channel, value);
	}
}
