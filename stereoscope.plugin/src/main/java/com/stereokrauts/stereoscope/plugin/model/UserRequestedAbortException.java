package com.stereokrauts.stereoscope.plugin.model;

public class UserRequestedAbortException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserRequestedAbortException(final String errorMessage) {
		super(errorMessage);
	}

	public UserRequestedAbortException() {
		super();
	}
}
