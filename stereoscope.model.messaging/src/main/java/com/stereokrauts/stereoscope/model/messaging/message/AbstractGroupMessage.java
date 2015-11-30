package com.stereokrauts.stereoscope.model.messaging.message;

import com.stereokrauts.stereoscope.model.messaging.annotations.AddressComponent;

/**
 * This class represents a base class for all group messages.
 * @author theide
 *
 * @param <T>
 */
public abstract class AbstractGroupMessage<T> extends AbstractMessage<T> {
	private final int type;
	private final int group;

	public AbstractGroupMessage(final int myType, final int myGroup, final T attachment) {
		super(attachment);
		this.type = myType;
		this.group = myGroup;
	}

	@AddressComponent
	public final int getGroupType() {
		return this.type;
	}
	
	@AddressComponent
	public final int getGroup() {
		return this.group;
	}
	

}
