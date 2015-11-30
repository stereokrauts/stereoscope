package com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;

/** This function is called, when the filter q of a
 * dynamics processor has been changed on the observed object.
 * @param int channel The channel strip (Beginning at 0)
 * @param boolean dyna The dynamics processor (true~1, false~2)
 * @param float value The new Q factor
 */
public class MsgInputDynaFilterQ extends AbstractInputDynamicsMessage<Float> {
	public MsgInputDynaFilterQ(final int processor, final int channel, final float value) {
		super(processor, channel, value);
	}
}
