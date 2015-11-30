package com.stereokrauts.stereoscope.model.messaging.message.outputs;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;

/**
 * This function is called, when the delay time of an bus is
 * changed on the observed object.
 * @param busNumber The output number, starting at zero
 * @param value The delay time in seconds
 */
public class MsgBusMasterDelayChanged extends AbstractMasterMessage<Float> {

	public MsgBusMasterDelayChanged(final int busNumber, final float value) {
		super(AbstractMasterMessage.SECTION.BUS, busNumber, value);
	}

}
