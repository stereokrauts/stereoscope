package com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;

/** This function is called, when the gain parameter
 * of a dynamics processor has been changed on the observed object.
 * @param int channel The channel strip (Beginning at 0)
 * @param boolean dyna The dynamics processor (true~1, false~2)
 * @param float value The new gain
 */
public class MsgInputDynaGain extends AbstractInputDynamicsMessage<Float> {
	public MsgInputDynaGain(final int processor, final int channel, final float value) {
		super(processor, channel, value);
	}
}
