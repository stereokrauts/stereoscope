package com.stereokrauts.stereoscope.model.messaging.message.outputs;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;

/**
 * This function is called, when the level of an bus is
 * changed on the observed object.
 * @param busNumber The output number, starting at zero
 * @param value The level as a value between 0..1
 */
public class MsgBusMasterLevelChanged extends AbstractMasterMessage<Float> {

	public MsgBusMasterLevelChanged(final int busNumber, final float value) {
		super(AbstractMasterMessage.SECTION.BUS, busNumber, value);
	}

}
