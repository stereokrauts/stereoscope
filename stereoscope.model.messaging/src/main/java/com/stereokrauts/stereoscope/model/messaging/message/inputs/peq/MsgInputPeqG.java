package com.stereokrauts.stereoscope.model.messaging.message.inputs.peq;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputPeqBandMessage;

/** This function is called, when the high band gain of a
 * parametric eq has been changed on the observed object.
 * @param int channel The channel of the peq (Beginning at 0)
 * @param int band The parametric band this value belongs to
 * @param float value The new gain
 */
public class MsgInputPeqG extends AbstractInputPeqBandMessage<Float> {
	public MsgInputPeqG(final int channel, final int band, final float value) {
		super(channel, band, value);
	}
}
