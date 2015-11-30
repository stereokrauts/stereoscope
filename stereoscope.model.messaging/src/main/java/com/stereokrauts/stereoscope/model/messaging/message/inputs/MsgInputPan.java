package com.stereokrauts.stereoscope.model.messaging.message.inputs;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;
import com.stereokrauts.stereoscope.model.messaging.priorization.PriorizationValue;


/** This function is called, when panning
 * has been changed on the observed object.
 * @param int channel The channel strip (Beginning at 0)
 * @param float value The panning level (+-1)
 */
public class MsgInputPan extends AbstractInputMessage<Float> {
	public MsgInputPan(final int channel, final float value) {
		super(channel, value);
		this.setPriority(PriorizationValue.LOW);
	}
}
