package com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;

/** This function is called, when the decay (gate) or release (comp.)
 * time of a dynamics processor has been changed on the observed object.
 * @param int channel The channel strip (Beginning at 0)
 * @param boolean dyna The dynamics processor (true~1, false~2)
 * @param float value The new decay or release time
 */
public class MsgInputDynaDecayRelease extends AbstractInputDynamicsMessage<Float> {
	public MsgInputDynaDecayRelease(final int processor, final int channel, final float value) {
		super(processor, channel, value);
	}
}
