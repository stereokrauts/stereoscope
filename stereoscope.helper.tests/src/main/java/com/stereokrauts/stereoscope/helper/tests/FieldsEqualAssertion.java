package com.stereokrauts.stereoscope.helper.tests;

import java.lang.reflect.Method;

import org.junit.Assert;

public class FieldsEqualAssertion extends Assert {
	protected FieldsEqualAssertion() { }
	
	private static final float DELTA_TOLERANCE = 0.001f; /* Fader resolution is most likely 1024 */
	
	public static void assertBothNullOrBothNotNull(final Object first, final Object second) {
		if (first == null || second == null) {
			assertSame("Actual and expected must either be both null or both not null", first, second);
		}
	}
	
	public static void assertFieldsEqual(final Object expected, final Object actual) {
		assertFieldsEqual(null, expected, actual);
	}
	
	public static void assertFieldsEqual(String message, final Object expected, final Object actual) {
		if (message != null && !message.equals("")) {
			message += ": ";
		}
		assertBothNullOrBothNotNull(expected, actual);
		
		// Null- and Typecheck
		if (expected != null && actual != null) {
			assertEquals(message + "classes of objects must be same.", expected.getClass(), actual.getClass());
			// Alle Felder durchgehen
			for (final Method f : expected.getClass().getMethods()) {
				if (f.getName().startsWith("get")) {
					Object thisFieldValue;
					try {
						thisFieldValue = f.invoke(expected);
						final Object thatFieldvalue = f.invoke(actual);
						if (thisFieldValue instanceof Float) {
							assertEquals(message + "comparing class " + expected.getClass().getCanonicalName() + " value of field " + f.getName(), (Float) thisFieldValue, (Float) thatFieldvalue, DELTA_TOLERANCE);
						} else if (thisFieldValue instanceof Double) {
							assertEquals(message + "comparing class " + expected.getClass().getCanonicalName() + " value of field " + f.getName(), (Double) thisFieldValue, (Double) thatFieldvalue, DELTA_TOLERANCE);
						} else {
							assertEquals(message + "comparing class " + expected.getClass().getCanonicalName() + " value of field " + f.getName(), thisFieldValue, thatFieldvalue);
						}
					} catch (final Exception e) {
						fail(message + "inaccessible getter Method: " + e);
					}
				}
			}
		}
	}
}
