package com.stereokrauts.stereoscope.model.messaging.message.inputs.peq;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;

/** This function is called, when the eq mode (type I/II) of a
 * parametric eq has been changed on the observed object.
 * @param int channel The channel of the peq (Beginning at 0)
 * @param boolean value switches peq type (0=TypeI, 1=TypeII)
 */
public class MsgInputPeqModeChanged extends AbstractInputMessage<Boolean> {
	public MsgInputPeqModeChanged(final int channel, final boolean value) {
		super(channel, value);
	}
}
