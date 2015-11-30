package com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;

/** This function is called, when the auto mode function of a
 * dynamics processor has been changed on the observed object.
 * @param int channel The channel strip (Beginning at 0)
 * @param boolean dyna The dynamics processor (true~1, false~2)
 * @param boolean value Auto mode on/off
 */
public class MsgInputDynaAutoOn extends AbstractInputDynamicsMessage<Boolean> {
	public MsgInputDynaAutoOn(final int processor, final int channel, final boolean value) {
		super(processor, channel, value);
	}
}
