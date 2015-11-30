package com.stereokrauts.stereoscope.model.messaging.message;

import com.stereokrauts.stereoscope.model.messaging.annotations.AddressComponent;

/**
 * This class represents a base class for all channel specific messages.
 * 
 * @author theide
 * 
 * @param <T>
 */
public abstract class AbstractChannelMessage<T> extends AbstractMessage<T> {
	private final int channel;

	public AbstractChannelMessage(final int myChannel, final T attachment) {
		super(attachment);
		this.channel = myChannel;
	}

	@AddressComponent
	public final int getChannel() {
		return this.channel;
	}

}
