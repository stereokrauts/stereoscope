package com.stereokrauts.stereoscope.model.messaging.message.inputs.peq;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;

/** This function is called, when the low pass filter of a
 * parametric eq has been turned on/off on the observed object.
 * @param int channel The channel of the peq (Beginning at 0)
 * @param boolean value LPF on/off (0=off, 1=on)
 */
public class MsgInputPeqLPFChanged extends AbstractInputMessage<Boolean> {
	public MsgInputPeqLPFChanged(final int channel, final boolean value) {
		super(channel, value);
	}
}
