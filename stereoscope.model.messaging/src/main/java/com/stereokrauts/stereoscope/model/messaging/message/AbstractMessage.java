package com.stereokrauts.stereoscope.model.messaging.message;

import java.lang.reflect.Method;

import com.stereokrauts.stereoscope.model.messaging.annotations.AddressComponent;
import com.stereokrauts.stereoscope.model.messaging.annotations.ValueComponent;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.priorization.PriorizationValue;


/**
 * This class is the base class for all messages that are being
 * sent inside of stereoscope.
 * @author theide
 *
 * @param <T> A attachment to the message must be of a certain
 * type which is denoted with this parameter. May be Object with
 * a value of null if
 * no parameter is needed.
 */
public abstract class AbstractMessage<T> implements IMessage {
	private String description = null;
	private PriorizationValue priority = PriorizationValue.NORMAL;
	private final long createdTimestamp;
	
	private T attachment = null;
	
	public AbstractMessage(final T attachmentValue) {
		this.attachment = attachmentValue;
		this.createdTimestamp = System.nanoTime();
	}
	
	@Override
	@ValueComponent
	public final T getAttachment() {
		return this.attachment;
	}

	public final void setDescription(final String descr) {
		this.description = descr;
	}
	
	public final String getDescription() {
		return this.description;
	}
	
	public final void setPriority(final PriorizationValue prio) {
		this.priority = prio;
	}
	
	public final PriorizationValue getPriority() {
		return this.priority;
	}
	
	@Override
	public final String toString() {
		String rv = this.getClass().toString() + " with attachment " + (this.getAttachment() != null ? this.getAttachment().toString() : "(null)") + " (";
		
		// Alle Felder durchgehen
		for (final Method method : this.getClass().getMethods()) {
			if (this.isGetterMethod(method)) {
				try {
					rv += method.getName() + ": " + method.invoke(this, new Object[0]) + ", ";
				} catch (final Exception e) {
					rv += method.getName() + " !ERROR!, ";
				}
			}
		}
		
		rv += ")";
		
		return rv;
	}
	
	@Override
	public final boolean equals(final Object other) {
		// Null- and Typecheck
		if (other == null || !this.getClass().equals(other.getClass())) {
			return false;
		}
		
		final AbstractMessage<?> casted = (AbstractMessage<?>) other;
		return this.internalEquals(casted);
	}
	
	@Override
	public final int hashCode() {
		int hashCode = 0;
		for (final Method method : this.getClass().getMethods()) {
			if (this.isGetterMethod(method)) {
				try {
					hashCode ^= method.invoke(this, new Object[0]).hashCode();
				} catch (final Exception e) {
					hashCode ^= 1;
				}
			}
		}
		return hashCode;
	}
	
	public final int addressHashCode() throws CouldNotCalculateException {
		int hashCode = this.getClass().hashCode();
		for (final Method method : this.getClass().getMethods()) {
			if (this.hasAddressAnnotation(method)) {
				try {
					hashCode ^= method.invoke(this, new Object[0]).hashCode();
				} catch (final Exception e) {
					throw new CouldNotCalculateException(e);
				}
			}
		}
		return hashCode;
	}
	
	public final boolean addressEquals(final Object other) {
		// Null- and Typecheck
		if (other == null || !this.getClass().equals(other.getClass())) {
			return false;
		}
		
		final AbstractMessage<?> casted = (AbstractMessage<?>) other;
		if (!this.attachmentTypeEquals(casted)) {
			return false;
		}
		
		for (final Method method : this.getClass().getMethods()) {
			if (this.hasAddressAnnotation(method)) {
				if (!this.checkAddressEqualness(casted, method)) {
					return false;
				}
			}
		}
		
		return true;
	}

	private boolean checkAddressEqualness(final AbstractMessage<?> casted, final Method method) {
		try {
			if (!method.invoke(this, new Object[0]).equals(method.invoke(casted, new Object[0]))) {
				return false;
			}
		} catch (final Exception e) {
			return false;
		}
		return true;
	}

	private boolean hasAddressAnnotation(final Method method) {
		return method.isAnnotationPresent(AddressComponent.class);
	}

	/**
	 * @param casted
	 */
	private boolean internalEquals(final AbstractMessage<?> casted) {
		// Generic Typecheck
		if (!this.attachmentTypeEquals(casted)) {
			return false;
		}
		
		// Alle Felder durchgehen
		for (final Method method : this.getClass().getMethods()) {
			if (this.isGetterMethod(method) && !this.fieldEqual(casted, method)) {
				return false;
			}
		}
		
		return true;

	}

	/**
	 * @param casted
	 * @return
	 */
	private boolean attachmentTypeEquals(final AbstractMessage<?> casted) {
		return this.getAttachment().getClass().equals(casted.getAttachment().getClass());
	}

	/**
	 * @param method
	 * @return
	 */
	private boolean isGetterMethod(final Method method) {
		return method.getName().startsWith("get");
	}

	/**
	 * @param casted
	 * @param f
	 */
	private boolean fieldEqual(final AbstractMessage<?> casted, final Method f) {
		try {
			final Object thisFieldValue = f.invoke(this);
			final Object thatFieldvalue = f.invoke(casted);
			if (thisFieldValue != null && !thisFieldValue.equals(thatFieldvalue)) {
				return false;
			} else if (thisFieldValue == null && !(thatFieldvalue == null)) {
				return false;
			}
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public final long createdTimestamp() {
		return this.createdTimestamp;
	}
}
