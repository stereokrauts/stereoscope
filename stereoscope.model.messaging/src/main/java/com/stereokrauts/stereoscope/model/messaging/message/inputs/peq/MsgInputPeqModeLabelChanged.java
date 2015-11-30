package com.stereokrauts.stereoscope.model.messaging.message.inputs.peq;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;
import com.stereokrauts.stereoscope.model.messaging.priorization.PriorizationValue;


/** This function is called, when the eq mode (type I/II) of a
 * parametric eq has been changed on the observed object.
 * @param int channel The channel of the peq (Beginning at 0)
 * @param String value "Type I" or "Type II"
 */
public class MsgInputPeqModeLabelChanged extends AbstractInputMessage<String> {
	public MsgInputPeqModeLabelChanged(final int channel, final String value) {
		super(channel, value);
		this.setPriority(PriorizationValue.LOW);
	}
}
