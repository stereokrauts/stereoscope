package com.stereokrauts.lib.midi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stereokrauts.lib.binary.ByteStringConversion;

/**
 * This class copies the concept of format strings as known
 * from the C "printf" function to sysex messages.
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 *
 */
public final class FormatStringSysex {
	/**
	 * all 7 bits of the byte of a MIDI message set.
	 */
	private static final byte MIDI_ALLBITSSET = 0x7F;
	/**
	 * how many bits are in a MIDI byte?
	 */
	private static final int MIDI_BITSPERBYTE = 7;
	/**
	 * How many bytes should we assume by default for a long parameter in
	 * a MIDI message? (this is currently matched to a LS9 or M7CL yamaha
	 * mixer)
	 */
	private static final int LONG_DEFAULTBYTES = 5;

	/**
	 * How many bytes should be reserved in memory for a long argument?
	 * (worst-case is okay)
	 */
	private static final int SYSEX_LONG_LENGTH = 8;
	/**
	 * How many bytes should be reserved in memory for a integer argument?
	 * (worst-case is okay)
	 */
	private static final int SYSEX_INT_LENGTH = 4;
	/**
	 * Normalized string of the prototype sysex message as
	 * provided to the constructor of this class.
	 */
	private final String sysexPrototype;
	/** This field provides information, how many arguments
	 * are encoded in the byte stream.
	 */
	private int argumentCount;
	/** This field provides information, how many bytes in the
	 * message will be occupied by arguments.
	 */
	private int argumentsByteLength = -1;
	/**
	 * A list containing the position, the type and the length of
	 * each argument of this format string.
	 */
	private List<Argument> arguments;
	/** This field provided information, how many bytes the
	 * resulting sysex message will occupy.
	 */
	private int byteMessageLength;


	/**
	 * This class represents a variable in the FormatString that
	 * will later be substituted by a value.
	 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
	 *
	 */
	private static final class Argument {
		/**
		 * The java type of this argument.
		 */
		@SuppressWarnings({ "rawtypes" })
		private Class type;
		/**
		 * Where the argument starts in the string.
		 */
		private int index;
		/**
		 * The length of the argument string.
		 */
		private int length = -1;
		/**
		 * Whether it is a signed type.
		 */
		private boolean signed = false;
		/**
		 * Hidden constructor.
		 */
		private Argument() { }
	}

	/**
	 * The regex that is used to verify the prototype string is
	 * correct.
	 */
	private static final String REGEX = "\\{-?[BSIL][0-9]?\\}";

	/**
	 * This function fills the field arguments according to the
	 * definitions in the prototype string.
	 * @param args The string to use
	 */
	private void countArguments(final String args) {
		/** This field sums the bytes that will be used by the
		 * arguments
		 */
		this.argumentsByteLength = 0;
		final Pattern p = Pattern.compile(REGEX);
		final Matcher m = p.matcher(args); // get a matcher object
		int count = 0;
		/** This field sums the characters that are used to define
		 * arguments in the format string.
		 */
		int lengthsSummed = 0;
		this.arguments = new ArrayList<Argument>();
		while (m.find()) {
			count++;

			final Argument a = new Argument();
			a.index = (m.start() - lengthsSummed) / 2 + this.argumentsByteLength;
			lengthsSummed += m.end() - m.start();

			int typePosition = m.start() + 1;
			if (args.charAt(typePosition) == '-') {
				a.signed = true;
				typePosition++;
			}

			switch (args.charAt(typePosition)) {

			case 'B': 
				a.type = Byte.class;
				this.argumentsByteLength += 1;
				break;
			case 'S': 
				a.type = Short.class;
				this.argumentsByteLength += 2;
				break;
			case 'I': 
				a.type = Integer.class;
				this.argumentsByteLength += SYSEX_INT_LENGTH;
				break;
			case 'L':
				a.type = Long.class;
				if (args.charAt(m.start() + 2) != '}') {
					final String strLength = args.substring((a.signed ? (m.start() + 3) : (m.start() + 2)), m.end() - 1);
					final int length = Integer.parseInt(strLength);
					this.argumentsByteLength += length;
					a.length = length;
				} else {
					this.argumentsByteLength += SYSEX_LONG_LENGTH;
				}
				break;
			default:
				throw new RuntimeException("Illegal type in sysex format "
						+ "string: " + args.charAt(typePosition));
			}
			this.arguments.add(a);

		}
		this.argumentCount = count;
		this.byteMessageLength = (args.length() - lengthsSummed) / 2
				+ this.argumentsByteLength;
	}

	/**
	 * This function checks the syntax of the prototype format
	 * string.
	 * @param formatString The string to check
	 * @return Result of the check: true if syntax is correct,
	 * 		false if not.
	 */
	private static boolean checkFormatSyntax(final String formatString) {
		int leftParanthesis = 0;
		int rightParanthesis = 0;
		for (final char c : formatString.toCharArray()) {
			if (c == '{') {
				leftParanthesis++;
			} else if (c == '}') {
				rightParanthesis++;
			}
		}
		if (leftParanthesis != rightParanthesis) {
			return false;
		}
		return true;
	}

	/**
	 * Constructor of a new object using the given prototype format string.
	 * @param prototype The string specifying the sysex message to create,
	 * 		with placeholders for the arguments that are to be filled in
	 * 		using the fillInMessage method.
	 */
	public FormatStringSysex(final String prototype) {
		String normalizedPrototype = prototype.replaceAll(" ", "");
		normalizedPrototype = normalizedPrototype.toUpperCase();
		if (!checkFormatSyntax(normalizedPrototype)) {
			throw new IllegalArgumentException("Syntax error for Format "
					+ "String \"" + normalizedPrototype + "\"");
		}

		this.countArguments(normalizedPrototype);
		this.sysexPrototype = normalizedPrototype;
	}

	/**
	 * This message returns a byte array containing the sysex message that
	 * is created using the prototype format string of the constructor and
	 * filling in the arguments of this function.
	 * @param values An array of objects that match the type of the
	 * 		arguments in the format string and that are in correct order,
	 * 		starting on the left side of the format string and that has
	 * 		the correct number of items, one for each argument in the
	 * 		format string.
	 * @return the filled in sysex message.
	 */
	public byte[] fillInMessage(final Object... values) {
		// Allocate a bit too much of memory, because it is difficult to
		// calculate the exact amount of required memory, and we just drop
		// the end of the array at the end of the function.
		if (values != null && values.length != this.argumentCount) {
			throw new IllegalArgumentException("Given arguments have length "
					+ values.length + " but the format string \""
					+ this.sysexPrototype + "\" only allows for "
					+ this.argumentCount + " arguments.");
		}

		String filledIn = "";

		int currentArgument = 0;

		for (int i = 0; i < this.sysexPrototype.length(); i++) {
			final char c = this.sysexPrototype.charAt(i);
			if (c == '{') {
				boolean signed = false;
				char type;
				if (this.sysexPrototype.charAt(i + 1) == '-') {
					/* signed variable, special treatment */
					signed = true;

					/* when a "-" has ben provided in the format string, then
					 * everything else is one character behind, so increase
					 * i by one. */
					i++;
				}
				type = this.sysexPrototype.charAt(i + 1);

				final char thirdChar = this.sysexPrototype.charAt(i + 2);
				int length = -1;

				if (thirdChar != '}') {
					// length parameter provided
					length = Integer.parseInt(this.sysexPrototype.substring(i + 2,
							this.sysexPrototype.indexOf('}', i + 1)));


					if (type != 'S' && type != 'L') {
						// only applies to strings, so this is a failure
						throw new IllegalArgumentException(
								"Format string error: length argument can only "
										+ "be given with String or Long arguments.");
					}

				}

				switch (type) {
				case 'B':
					if (signed) {
						throw new IllegalArgumentException("Byte does not "
								+ "support signed operation.");
					}
					filledIn += SysexStringConversion.getByte(
							(Byte) values[currentArgument]);
					break;
				case 'S':
					if (signed) {
						filledIn += SysexStringConversion.getSignedShort(
								(Short) values[currentArgument]);
					} else {
						filledIn += SysexStringConversion.getShort(
								(Short) values[currentArgument]);
					}
					break;
				case 'I':
					if (signed) {
						filledIn += SysexStringConversion.getSignedInteger(
								(Integer) values[currentArgument]);
					} else {
						filledIn += SysexStringConversion.getInteger(
								(Integer) values[currentArgument]);
					}
					break;
				case 'L':
					if (length != LONG_DEFAULTBYTES) {
						throw new IllegalArgumentException("Format string "
								+ "error: "
								+ "Long arguments MUST be given length "
								+ LONG_DEFAULTBYTES
								+ " at the moment..");
					}
					if (signed) {
						throw new IllegalArgumentException("Byte does not "
								+ "support signed operation.");
					}
					filledIn += SysexStringConversion.getLong(
							(Long) values[currentArgument]);
					break;
				default:
					throw new IllegalArgumentException("Unknown Format: "
							+ type);

				}

				currentArgument++;
				i = this.sysexPrototype.indexOf('}', i + 1);
			} else {
				filledIn += String.valueOf(c);
			}
		}

		return ByteStringConversion.toBytes(filledIn);
	}

	/**
	 * This is the inverse function of fillInMessage and it returns
	 * an array of Objects that contain the arguments that are contained
	 * in a sysex messages at the positions and of the lengths that
	 * were previously defined in the prototype format string.
	 * @param message The message whose arguments should be extracted.
	 * @return The arguments that were contained in the message
	 */
	public Object[] parseMessage(final byte[] message) {
		if (message.length != this.byteMessageLength) {
			throw new IllegalArgumentException("The message does not "
					+ "match to the provided Sysex Prototype.");
		}

		final Object[] rv = new Object[this.argumentCount];

		for (int i = 0; i < this.argumentCount; i++) {
			final Argument a = this.arguments.get(i);
			if (a.type.equals(Byte.class)) {
				rv[i] = Byte.valueOf(message[a.index]);
			} else if (a.type.equals(Short.class)) {
				if (a.signed && /* Highest bit set? */
						message[a.index] >> (MIDI_BITSPERBYTE - 1) == 1) {
					// CHECKSTYLE:OFF
					final short number = (short) (((MIDI_ALLBITSSET - message[a.index])
							<< MIDI_BITSPERBYTE) | (MIDI_ALLBITSSET - message[a.index + 1]));
					// CHECKSTYLE: ON
					rv[i] = Short.valueOf((short) (-(number + 1)));
				} else {
					rv[i] = Short.valueOf(
							(short) ((message[a.index] << MIDI_BITSPERBYTE)
									| message[a.index + 1]));
				}
			} else if (a.type.equals(Integer.class)) {
				if (a.signed
						&& /* Highest bit set? */
						message[a.index] >> (MIDI_BITSPERBYTE - 1) == 1) {

					// CHECKSTYLE:OFF
					/* negative number and we do mind signs... sigh... */
					final int number = (((MIDI_ALLBITSSET - message[a.index]) 
							<< MIDI_BITSPERBYTE * 3)
							| ((MIDI_ALLBITSSET - message[a.index + 1]) 
									<< MIDI_BITSPERBYTE * 2)
									| ((MIDI_ALLBITSSET - message[a.index + 2]) 
											<< MIDI_BITSPERBYTE * 1)
											|  (MIDI_ALLBITSSET - message[a.index + 3]));
					// CHECKSTYLE:ON

					rv[i] = Integer.valueOf(-(number + 1)); /* two's complement */
				} else {
					// CHECKSTYLE:OFF
					rv[i] = Integer.valueOf((
							(message[a.index] << MIDI_BITSPERBYTE * 3)
							| (message[a.index + 1] << MIDI_BITSPERBYTE * 2)
							| (message[a.index + 2] << MIDI_BITSPERBYTE * 1)
							| message[a.index + 3]));
					// CHECKSTYLE:ON
				}
			} else if (a.type.equals(Long.class)) {
				if (a.signed && /* Highest bit set? */
						message[a.index] >> (MIDI_BITSPERBYTE - 1) == 1) {
					long number = 0;
					for (int j = 0; j < a.length; j++) {
						number += (MIDI_ALLBITSSET - message[a.index + j]) << MIDI_BITSPERBYTE * (a.length - j - 1);
					}
					rv[i] = Long.valueOf(-(number + 1));
				} else {
					long value = 0;
					if (a.length != -1) {
						for (int j = 0; j < a.length; j++) {
							value |= (message[a.index + j]
									<< ((a.length - (j + 1)) * MIDI_BITSPERBYTE));
						}
					} else {
						throw new IllegalArgumentException("Long argument must "
								+ "have defined length (Argument nr. "
								+ (i + 1) + ").");
					}
					rv[i] = Long.valueOf(value);
				}
			} else {
				throw new IllegalArgumentException("Unknown class type in "
						+ "internal structure: " + a.type);
			}
		}

		return rv;

	}

}
