package com.stereokrauts.stereoscope.model.messaging.message.inputs.peq;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputPeqBandMessage;
import com.stereokrauts.stereoscope.model.messaging.priorization.PriorizationValue;


/** This function is called, when the characteristic of a
 * parametric eq filter has been changed on the observed object.
 * 
 * Characteristic can be one of the following:
 * 0 - parametric
 * 1 - low shelving
 * 2 - high shelving
 * 3 - LPF
 * 4 - HPF
 * 
 * @param int channel The channel of the peq (Beginning at 0)
 * @param int band The parametric band this value belongs to
 * @param int value The characteristics switch
 */
public class MsgInputPeqFilterType extends AbstractInputPeqBandMessage<Integer> {
	public MsgInputPeqFilterType(final int channel, final int band, final int value) {
		super(channel, band, value);
		this.setPriority(PriorizationValue.LOW);
	}
}
