package com.stereokrauts.lib.binary;

/**
 * This is a utility class to convert between hex strings and bytes.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public final class ByteStringConversion {
	/**
	 * The least significant half word.
	 */
	private static final int LSHW = 0x0F;
	/**
	 * The most significant half word.
	 */
	private static final int MSHW = 0xF0;
	/**
	 * The hex numbers, ordered :-).
	 */
	static final String HEXNUMBERS = "0123456789ABCDEF";
	private static final int HEX_BASE = 16;

	/**
	 * Convert a byte array into a pretty string.
	 * @param hexArray the array to convert
	 * @return the pretty string.
	 */
	public static String toHex(final byte[] hexArray) {
		final StringBuilder hex = new StringBuilder(2 * hexArray.length);
		for (final byte b : hexArray) {
			hex.append(HEXNUMBERS.charAt((b & MSHW) >> 4))
			.append(HEXNUMBERS.charAt((b & LSHW)));
		}
		return hex.toString();
	}

	/**
	 * Convert a string to a byte array.
	 * @param bytes the string to convert
	 * @return the byte array
	 */
	public static byte[] toBytes(final String bytes) {
		if (bytes.length() % 2 != 0) {
			throw new IllegalArgumentException("The length of the argument must by even.");
		}

		final byte[] returnValue = new byte[bytes.length() / 2];
		for (int i = 0; i < bytes.length() / 2; i++) {
			returnValue[i] = hexCharsToByte(bytes.charAt(2 * i),
					bytes.charAt(2 * i + 1));
		}

		return returnValue;
	}

	/**
	 * Convert two separate numbers to a byte.
	 * @param hexChar1 Integer in range 0...15 (most significant)
	 * @param hexChar2 Integer in range 0...15 (least significant)
	 * @return a Byte consisting of hexChar1 * 16 + hexChar2.
	 */
	public static byte hexCharsToByte(final char hexChar1, final char hexChar2) {
		return (byte) Integer.parseInt("" + hexChar1 + hexChar2, HEX_BASE);
	}

	/**
	 * Hidden default constructor.
	 */
	private ByteStringConversion() { }
}
