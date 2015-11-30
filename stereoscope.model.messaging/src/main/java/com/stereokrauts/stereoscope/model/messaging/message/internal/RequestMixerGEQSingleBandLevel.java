/**
 * 
 */
package com.stereokrauts.stereoscope.model.messaging.message.internal;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalCountMessage;

/**
 * This message should be triggered, when a system component
 * needs to know about a single band level from a specific
 * graphical EQ, e.g. for a band reset.
 * @param int geq The graphical EQ that the band belong to.
 * @Param int band The frequency band of interest.
 * @author jansen
 *
 */
public class RequestMixerGEQSingleBandLevel extends AbstractInternalCountMessage<Boolean> {
	private int band;
	
	/**
	 * @param boolean attachment The "isRight" parameter which is important for stereo GEQs.
	 */
	public RequestMixerGEQSingleBandLevel(int geq, int band, boolean attachment) {
		super(geq, attachment);
		this.band = band;
	}

	public int getBand() {
		return band;
	}

}
