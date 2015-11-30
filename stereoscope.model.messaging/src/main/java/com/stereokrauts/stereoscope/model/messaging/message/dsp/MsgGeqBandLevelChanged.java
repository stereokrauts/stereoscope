package com.stereokrauts.stereoscope.model.messaging.message.dsp;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractGeqBandMessage;

/** This function is called, when a graphical eq band has been changed
 * on the observed object.
 * @param geqNumber The internal number of the GEQ, starting at 0.
 * @param isRightChannel false if the left channel has changed, true
 * 		for the right channel.
 * @param band The number of the band, starting at 0.
 * @param level The level of the GEQ band, ranging from -1 to 1.
 */

public class MsgGeqBandLevelChanged extends AbstractGeqBandMessage<Float> {
	
	public MsgGeqBandLevelChanged(final short geqNumber, final int band, final boolean isRightChannel, final float level) {
		super(geqNumber, band, isRightChannel, level);
	}

}
