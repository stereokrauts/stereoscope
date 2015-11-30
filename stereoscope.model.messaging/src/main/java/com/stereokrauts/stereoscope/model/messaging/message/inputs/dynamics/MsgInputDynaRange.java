package com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;

/** This function is called, when the range on a gate/ducking
 * dynamics processor has been changed on the observed object.
 * @param int channel The channel strip (Beginning at 0)
 * @param boolean dyna The dynamics processor (true~1, false~2)
 * @param float value The new range
 */
public class MsgInputDynaRange extends AbstractInputDynamicsMessage<Float> {
	public MsgInputDynaRange(final int processor, final int channel, final float value) {
		super(processor, channel, value);
	}
}
