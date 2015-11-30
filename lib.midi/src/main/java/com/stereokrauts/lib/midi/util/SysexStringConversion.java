package com.stereokrauts.lib.midi.util;

/**
 * This class provides utility functions for byte conversions that are
 * useful for the MIDI communication.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
final class SysexStringConversion {
	/**
	 * Mask for the least significant half word of a byte.
	 */
	private static final int LSHW = 0x0f;
	/**
	 * Mask for the most significant half word of a byte.
	 */
	private static final int MSHW = 0xf0;
	/**
	 * Mask for the second 7-bit byte.
	 */
	private static final int MIDI_MASK_2ND_BYTE = 0x3F80;
	/**
	 * Bits in a byte in a MIDI connection.
	 */
	private static final int MIDI_BITSPERBYTE = 7;
	/**
	 * The maximum value of a MIDI byte.
	 */
	private static final int MIDI_BYTE_MAXVAL = 0x7F;
	/**
	 * Mask for the last seven bits.
	 */
	private static final int LAST_SEVEN_BITS = 0x0000007F;

	/**
	 * Converts a byte to hex string.
	 * @param block byte to convert
	 * @return a String of the hexadecimal representation of the block.
	 */
	public static String getByte(final byte block) {
	    final char[] hexChars = { 
	        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
	        'A', 'B', 'C', 'D', 'E', 'F' };
	    int high = 0;
	    int low = 0;
        high = ((block & MSHW) >> 4);
        low = (block & LSHW);
        return "" + hexChars[high] + hexChars[low];
	}
	
	/**
	 * Convert a short to a hex string.
	 * @param x short to convert
	 * @return a String of the hexadecimal representation of the block.
	 */
	public static String getShort(final short x) {
		return  getByte((byte) ((x & MIDI_MASK_2ND_BYTE) >> MIDI_BITSPERBYTE))
			+ getByte((byte) (x & LAST_SEVEN_BITS));
	}
	
	/**
	 * Convert a integer to a hex string.
	 * @param x int to convert
	 * @return a String of the hexadecimal representation of the block.
	 */
	public static String getInteger(final int x) {
		return getByte((byte) ((x >> 3 * MIDI_BITSPERBYTE)  & LAST_SEVEN_BITS))
			+ getByte((byte)  ((x >> 2 * MIDI_BITSPERBYTE)  & LAST_SEVEN_BITS))
			+ getByte((byte)  ((x >> MIDI_BITSPERBYTE)   & LAST_SEVEN_BITS))
			+ getByte((byte)   (x & LAST_SEVEN_BITS));
	}
	
	/**
	 * Convert a long to a hex string.
	 * @param x long to convert
	 * @return a String of the hexadecimal representation of the block.
	 */
	public static String getLong(final long x) {
		return getByte((byte) ((x >> 4 * MIDI_BITSPERBYTE)  & LAST_SEVEN_BITS))
			+ getByte((byte)  ((x >> 3 * MIDI_BITSPERBYTE)  & LAST_SEVEN_BITS))
			+ getByte((byte)  ((x >> 2 * MIDI_BITSPERBYTE)  & LAST_SEVEN_BITS))
			+ getByte((byte)  ((x >> 1 * MIDI_BITSPERBYTE)   & LAST_SEVEN_BITS))
			+ getByte((byte)   (x & LAST_SEVEN_BITS));
	}

	/**
	 * Convert a signed integer to a hex string.
	 * @param x int to convert
	 * @return a String of the hexadecimal representation of the block.
	 */
	public static String getSignedInteger(final int x) {
		if (x < 0) {
			int y;
			y = -x - 1;
			final byte byte0 = (byte)  (y        & LAST_SEVEN_BITS);
			final byte byte1 = (byte) ((y >> 1 * MIDI_BITSPERBYTE) & LAST_SEVEN_BITS);
			final byte byte2 = (byte) ((y >> 2 * MIDI_BITSPERBYTE) & LAST_SEVEN_BITS);
			final byte byte3 = (byte) ((y >> 3 * MIDI_BITSPERBYTE) & LAST_SEVEN_BITS);
			
			return  getByte((byte) (MIDI_BYTE_MAXVAL - byte3))
					+ getByte((byte) (MIDI_BYTE_MAXVAL - byte2))
					+ getByte((byte) (MIDI_BYTE_MAXVAL - byte1))
					+ getByte((byte) (MIDI_BYTE_MAXVAL - byte0));
		} else {
			return getInteger(x);
		}
	}
	
	/**
	 * Convert a signed short to a hex string.
	 * @param x short to convert
	 * @return a String of the hexadecimal representation of the block.
	 */
	public static String getSignedShort(final short x) {
		if (x < 0) {
			short y;
			y = (short) (-x - 1);
			final byte lsb = (byte)  (y & LAST_SEVEN_BITS);
			final byte msb = (byte) ((y & MIDI_MASK_2ND_BYTE) >> MIDI_BITSPERBYTE);
			return  getByte((byte) (MIDI_BYTE_MAXVAL - msb))
					+ getByte((byte) (MIDI_BYTE_MAXVAL - lsb));
		} else {
			return getShort(x);
		}
	}
	
	/**
	 * Private default constructor to make class uninstanziable.
	 */
	private SysexStringConversion() { }
}
