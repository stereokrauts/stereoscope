package com.stereokrauts.stereoscope.model.messaging.message.outputs;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;

/**
 * This function is called, when the delay time of an aux is
 * changed on the observed object.
 * @param aux The output number, starting at zero
 * @param value The delay time in seconds
 */
public class MsgAuxMasterDelayChanged extends AbstractMasterMessage<Float> {

	public MsgAuxMasterDelayChanged(final int aux, final float value) {
		super(AbstractMasterMessage.SECTION.AUX, aux, value);
	}

}
