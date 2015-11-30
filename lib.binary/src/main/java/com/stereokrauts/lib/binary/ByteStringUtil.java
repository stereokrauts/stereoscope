package com.stereokrauts.lib.binary;

/**
 * Utility class for byte strings.
 * @author th
 *
 */
public final class ByteStringUtil {
	public static String normalize(final String string) {
		return string.toUpperCase().replaceAll("\\s+", "");
	}

	private ByteStringUtil() { }
}
