package com.stereokrauts.stereoscope.model.messaging.message.inputs.peq;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;

/** This function is called, when the high pass filter of a
 * parametric eq has been turned on/off on the observed object.
 * @param int channel The channel of the peq (Beginning at 0)
 * @param boolean value HPF on/off (0=off, 1=on)
 */
public class MsgInputPeqHPFChanged extends AbstractInputMessage<Boolean> {
	public MsgInputPeqHPFChanged(final int channel, final boolean value) {
		super(channel, value);
	}
}
