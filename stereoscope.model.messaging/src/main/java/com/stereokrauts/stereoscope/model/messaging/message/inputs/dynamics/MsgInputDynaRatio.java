package com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;

/** This function is called, when the ratio of a comp./exp./etc.
 * dynamics processor has been changed on the observed object.
 * @param int channel The channel strip (Beginning at 0)
 * @param boolean dyna The dynamics processor (true~1, false~2)
 * @param float value The new ratio or dynamic
 */
public class MsgInputDynaRatio extends AbstractInputDynamicsMessage<Float> {
	public MsgInputDynaRatio(final int processor, final int channel, final float value) {
		super(processor, channel, value);
	}
}
