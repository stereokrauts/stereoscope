package model.properties;

import java.lang.reflect.InvocationTargetException;

public class PropertiesException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PropertiesException(final String message) {
		super(message);
	}

	public PropertiesException(final InvocationTargetException e) {
		super(e);
	}

	public PropertiesException(final String string, final Exception e) {
		super(string, e);
	}

}
