package com.stereokrauts.stereoscope.model.messaging.message.inputs.peq;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputPeqBandMessage;

/** This function is called, when the high band cutoff frequency
 * of a parametric eq has been changed on the observed object.
 * @param int channel The channel of the peq (Beginning at 0)
 * @param int band The parametric band this value belongs to
 * @param float value The new frequency
 */
public class MsgInputPeqF extends AbstractInputPeqBandMessage<Float> {
	public MsgInputPeqF(final int channel, final int band, final float value) {
		super(channel, band, value);
	}
}
