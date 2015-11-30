package com.stereokrauts.stereoscope.model.messaging.message;

import com.stereokrauts.stereoscope.model.messaging.annotations.AddressComponent;
import com.stereokrauts.stereoscope.model.messaging.priorization.PriorizationValue;


/**
 * This class represents a base class for all 
 * mixer master (bus) messages.
 * @author theide
 *
 * @param <T>
 */
public abstract class AbstractMasterMessage<T> extends AbstractMessage<T> {
	/**
	 * This denotes the subsection on the mixers master section.
	 * @author theide
	 *
	 */
	public enum SECTION {
		MASTER, AUX, BUS, OUTPUT;
	}

	private final SECTION section;
	private final int number;

	public AbstractMasterMessage(final SECTION mySection, final int myNumber, final T attachment) {
		super(attachment);
		this.section = mySection;
		this.number = myNumber;
		this.setPriority(PriorizationValue.HIGH);
	}

	@AddressComponent
	public final SECTION getSection() {
		return this.section;
	}

	@AddressComponent
	public final int getNumber() {
		return this.number;
	}
}
