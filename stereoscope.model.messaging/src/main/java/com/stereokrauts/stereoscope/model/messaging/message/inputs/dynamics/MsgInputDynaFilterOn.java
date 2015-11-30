package com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;

/** This function is called, when the filter of a 
 * dynamics processor has been switched on/off on the observed object.
 * @param int channel The channel strip (Beginning at 0)
 * @param boolean dyna The dynamics processor (true~1, false~2)
 * @param boolean value Dynamics filter on/off
 */
public class MsgInputDynaFilterOn extends AbstractInputDynamicsMessage<Boolean> {
	public MsgInputDynaFilterOn(final int processor, final int channel, final boolean value) {
		super(processor, channel, value);
	}
}
