package com.stereokrauts.stereoscope.model.messaging.message;

/**
 * This class represents a base class for all parametric
 * equalizer messages.
 * @author theide
 *
 * @param <T>
 */
public abstract class AbstractPeqMessage<T> extends AbstractMessage<T> {
	private final int channel;

	public AbstractPeqMessage(final int myChannel, final T attachment) {
		super(attachment);
		this.channel = myChannel;
	}

	public final int getChannel() {
		return this.channel;
	}

}
