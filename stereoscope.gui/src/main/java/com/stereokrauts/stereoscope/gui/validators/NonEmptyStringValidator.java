package com.stereokrauts.stereoscope.gui.validators;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public final class NonEmptyStringValidator implements IValidator {

	@Override
	public IStatus validate(final Object value) {
		if (value instanceof String) {
			if (((String) value).trim().length() > 0) {
				return ValidationStatus.ok();
			}
		}
		return ValidationStatus.error("The value must not be empty.");
	}

}
