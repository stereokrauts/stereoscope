package com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;

/** This function is called, when trigger source of a gate 
 * dynamics processor has been changed on the observed object.
 * @param int channel The channel strip (Beginning at 0)
 * @param boolean dyna The dynamics processor (true~1, false~2)
 * @param float value The new trigger source
 */
public class MsgInputDynaKeyIn extends AbstractInputDynamicsMessage<Float> {
	public MsgInputDynaKeyIn(final int processor, final int channel, final float value) {
		super(processor, channel, value);
	}
}
