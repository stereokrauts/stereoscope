package com.stereokrauts.stereoscope.model.messaging.message;

import com.stereokrauts.stereoscope.model.messaging.annotations.AddressComponent;
import com.stereokrauts.stereoscope.model.messaging.priorization.PriorizationValue;

/**
 * This class represents a base class for all label messages.
 * @author theide
 *
 * @param <T>
 */
public abstract class AbstractLabelMessage<T> extends AbstractMessage<T> {
	private final int channel;
	private final int identifier;

	public AbstractLabelMessage(final int myIdentifier, final int myChannel, final T attachment) {
		super(attachment);
		this.channel = myChannel;
		this.identifier = myIdentifier;
		this.setPriority(PriorizationValue.LOW);
	}

	@AddressComponent
	public final int getChannel() {
		return this.channel;
	}
	
	@AddressComponent
	public final int getLabelIdentifier() {
		return this.identifier;
	}
	

}
