package com.stereokrauts.stereoscope.model.messaging.message;

import com.stereokrauts.stereoscope.model.messaging.annotations.AddressComponent;
import com.stereokrauts.stereoscope.model.messaging.priorization.PriorizationValue;


/**
 * This class represents a base class for all graphical
 * equalizer messages.
 * @author theide
 *
 * @param <T>
 */
public class AbstractGeqMessage<T> extends AbstractMessage<T> {
	private final short geqNumber;
	
	public AbstractGeqMessage(final short myGeqNumber, final T attachment) {
		super(attachment);
		this.geqNumber = myGeqNumber;
		this.setPriority(PriorizationValue.HIGH);
	}

	@AddressComponent
	public final short getGeqNumber() {
		return this.geqNumber;
	}

}
