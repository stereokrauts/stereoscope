package com.stereokrauts.stereoscope.model.messaging.message.inputs;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelAuxMessage;
import com.stereokrauts.stereoscope.model.messaging.priorization.PriorizationValue;


/** This function is called, when the aux send of a channel has
 * been changed on the observed object.
 * @param channel The changed channel (Beginning at 0)
 * @param aux The aux send that has been changed (Beginning at 0)
 * @param value The new level (Range: 0...1)
 */

public class MsgAuxSendChanged extends AbstractChannelAuxMessage<Float> {

	public MsgAuxSendChanged(final int channel, final int aux, final float value) {
		super(channel, aux, value);
		this.setPriority(PriorizationValue.HIGH);
	}

}
