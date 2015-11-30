package com.stereokrauts.stereoscope.model.messaging.message.outputs;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;

/**
 * This function is called, when the delay time of an output is
 * changed on the observed object.
 * @param outputNumber The output number, starting at zero
 * @param value The delay time in seconds
 */
public class MsgOutputDelayChanged extends AbstractMasterMessage<Float> {

	public MsgOutputDelayChanged(final int outputNumber, final float value) {
		super(AbstractMasterMessage.SECTION.OUTPUT, outputNumber, value);
	}

}
