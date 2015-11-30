package com.stereokrauts.stereoscope.model.messaging.message.dsp;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractGeqBandMessage;

/** This function is called, when a single band reset event for a
 *  specific geq is triggered on the observed object.
 * @param eqNumber Number of the actual graphical EQ
 * @param isRightChannel distinct right-/left channel bands
 * @param band The band to be set to zero
 */

public class MsgGeqBandReset extends AbstractGeqBandMessage<Object> {

	public MsgGeqBandReset(final short geqNumber, final int band, final boolean isRightChannel, final Object attachment) {
		super(geqNumber, band, isRightChannel, attachment);
	}

}
