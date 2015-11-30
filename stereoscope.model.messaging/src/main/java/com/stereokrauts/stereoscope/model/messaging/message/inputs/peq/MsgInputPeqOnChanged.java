package com.stereokrauts.stereoscope.model.messaging.message.inputs.peq;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;

/** This function is called, when the parametric eq of
 * a channel has been turned on/off on the observed object.
 * @param int channel The channel of the peq (Beginning at 0)
 * @param boolean value PEQ on/off (0=off, 1=on)
 */
public class MsgInputPeqOnChanged extends AbstractInputMessage<Boolean> {
	public MsgInputPeqOnChanged(final int channel, final boolean value) {
		super(channel, value);
	}
}
