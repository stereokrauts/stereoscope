package com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;

/** This function is called, when the high band cutoff frequency
 * of a parametric eq has been changed on the observed object.
 * @param int channel The channel of the peq (Beginning at 0)
 * @param boolean dyna The dynamics processor (true~1, false~2)
 * @param float value The new frequency (Range: 5...124, default:112)
 */
public class MsgInputDynaOn extends AbstractInputDynamicsMessage<Boolean> {
	public MsgInputDynaOn(final int processor, final int channel, final boolean value) {
		super(processor, channel, value);
	}
}
