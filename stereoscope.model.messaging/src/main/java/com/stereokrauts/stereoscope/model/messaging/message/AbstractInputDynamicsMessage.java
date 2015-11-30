package com.stereokrauts.stereoscope.model.messaging.message;

import com.stereokrauts.stereoscope.model.messaging.annotations.AddressComponent;
/**
 * This class represents a base class for all input strip
 * dynamics messages.
 * @author theide
 *
 * @param <T>
 */
public abstract class AbstractInputDynamicsMessage<T> extends
		AbstractMessage<T> {
	private final int channel;
	private final int processor;

	public AbstractInputDynamicsMessage(final int processorNr, final int myChannel,
			final T attachment) {
		super(attachment);
		this.channel = myChannel;
		this.processor = processorNr;

	}

	@AddressComponent
	public final int getChannel() {
		return this.channel;
	}

	@AddressComponent
	public final int getProcessor() {
		return this.processor;
	}

}
