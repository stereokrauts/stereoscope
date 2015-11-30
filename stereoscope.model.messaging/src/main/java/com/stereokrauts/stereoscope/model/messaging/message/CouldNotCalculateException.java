package com.stereokrauts.stereoscope.model.messaging.message;

/**
 * This exception is thrown when an address hash value of an AbstractMessage
 * can not be calculated.
 * @author theide
 *
 */
public final class CouldNotCalculateException extends Exception {

	public CouldNotCalculateException(final Exception e) {
		super(e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
