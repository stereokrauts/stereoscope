package com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;

/** This function is called, when the filter type 
 * of a dynamics processor has been changed
 * on the observed object.
 * @param int channel The channel strip (Beginning at 0)
 * @param boolean dyna The dynamics processor (true~1, false~2)
 * @param float value The new filter type (HPF, LPF, BPF)
 */
public class MsgInputDynaFilterType extends AbstractInputDynamicsMessage<Float> {
	public MsgInputDynaFilterType(final int processor, final int channel, final float value) {
		super(processor, channel, value);
	}
}
