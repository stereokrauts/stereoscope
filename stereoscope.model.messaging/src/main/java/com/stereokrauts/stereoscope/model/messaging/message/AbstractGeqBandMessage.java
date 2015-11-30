package com.stereokrauts.stereoscope.model.messaging.message;

import com.stereokrauts.stereoscope.model.messaging.annotations.AddressComponent;

/**
 * This class represents a base class for all graphical
 * equalizer messages that refer to a band of the GEQ.
 * @author theide
 *
 * @param <T>
 */
public class AbstractGeqBandMessage<T> extends AbstractGeqMessage<T> {
	private final int band;
	private final boolean isRightChannel;
	
	public AbstractGeqBandMessage(final short geqNumber, final int myBand, final boolean myIsRightChannel, final T attachment) {
		super(geqNumber, attachment);
		this.band = myBand;
		this.isRightChannel = myIsRightChannel;
	}

	@AddressComponent
	public final int getBand() {
		return this.band;
	}
	
	@AddressComponent
	public final boolean isRightChannel() {
		return this.isRightChannel;
	}
}
