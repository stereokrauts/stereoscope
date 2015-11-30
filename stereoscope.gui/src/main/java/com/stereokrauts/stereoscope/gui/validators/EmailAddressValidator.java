package com.stereokrauts.stereoscope.gui.validators;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public final class EmailAddressValidator implements IValidator {

	@Override
	public IStatus validate(final Object value) {
		if (value instanceof String) {
			if (!value.toString().matches(".+@.+\\..+")) {
				return ValidationStatus.error("Email address is illegal");
			}
		}
		return ValidationStatus.ok();
	}

}
