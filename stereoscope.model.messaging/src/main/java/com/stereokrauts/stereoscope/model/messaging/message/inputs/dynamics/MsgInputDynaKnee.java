package com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;

/** This function is called, when the knee (width)
 * factor of a compressor/expander dynamics processor
 * has been changed on the observed object.
 * @param int channel The channel strip (Beginning at 0)
 * @param boolean dyna The dynamics processor (true~1, false~2)
 * @param float value The new knee factor
 */
public class MsgInputDynaKnee extends AbstractInputDynamicsMessage<Float> {
	public MsgInputDynaKnee(final int processor, final int channel, final float value) {
		super(processor, channel, value);
	}
}
