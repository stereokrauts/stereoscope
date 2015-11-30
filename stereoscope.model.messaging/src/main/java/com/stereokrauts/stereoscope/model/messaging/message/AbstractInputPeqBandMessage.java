package com.stereokrauts.stereoscope.model.messaging.message;

import com.stereokrauts.stereoscope.model.messaging.annotations.AddressComponent;

/**
 * This class represents a base class for all parametric equalizer messages that
 * refer to a certain band of the eq.
 * 
 * @author theide
 * 
 * @param <T>
 */
public abstract class AbstractInputPeqBandMessage<T> extends AbstractMessage<T> {
	private final int channel;
	private final int band;

	public AbstractInputPeqBandMessage(final int myChannel, final int myBand, final T attachment) {
		super(attachment);
		this.channel = myChannel;
		this.band = myBand;
	}

	@AddressComponent
	public final int getChannel() {
		return this.channel;
	}

	@AddressComponent
	public final int getBand() {
		return this.band;
	}

}
