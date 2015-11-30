package com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;

/** This function is called, when the threshold of a 
 * dynamics processor has been changed on the observed object.
 * @param int processorNr The number of the dynamics processor
 * @param int channel The channel strip (Beginning at 0)
 * @param float value The new threshold level
 */
public class MsgInputDynaThreshold extends AbstractInputDynamicsMessage<Float> {
	
	public MsgInputDynaThreshold(final int processorNr, final int channel, final float value) {
		super(processorNr, channel, value);
	}
}
