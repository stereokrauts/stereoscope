package com.stereokrauts.stereoscope.wizards.userfeedback;

import java.util.ArrayList;
import java.util.Collection;

import com.stereokrauts.stereoscope.gui.AbstractGuiModel;

public final class UserFeedbackModel extends AbstractGuiModel {
	private String name;
	private String email;
	private String message;

	private final Collection<String> validationErrors = new ArrayList<String>();

	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.firePropertyChange("name", this.name, name);
		this.name = name;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(final String email) {
		this.firePropertyChange("email", this.email, email);
		this.email = email;
	}
	public String getMessage() {
		return this.message;
	}
	public void setMessage(final String message) {
		this.message = message;
	}

	public boolean isValid() {
		final boolean valid = true;
		return valid;
	}

	public Collection<String> getValidationErrors() {
		return this.validationErrors;
	}
}
