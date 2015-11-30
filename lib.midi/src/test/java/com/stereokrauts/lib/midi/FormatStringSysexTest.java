package com.stereokrauts.lib.midi;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import com.stereokrauts.lib.midi.util.FormatStringSysex;



public class FormatStringSysexTest {

	@Test
	public final void testFillInMessage() {
		{
			final FormatStringSysex fss = new FormatStringSysex("F0ABCDF7");
			final byte[] result = { (byte) 0xF0, (byte) 0xAB, (byte) 0xCD, (byte) 0xF7 };
			assertArrayEquals(result, fss.fillInMessage((Object[]) null));
		}

		{
			final FormatStringSysex fss = new FormatStringSysex("F0AB{b}CD{s}F7");
			final byte[] result = { (byte) 0xF0, (byte) 0xAB,
					(byte) 0x33, /* first parameter */
					(byte) 0xCD, 
					(byte) 0x08, (byte) 0x55, /* second parameter */
					(byte) 0xF7 };
			final Object[] parameters = { new Byte((byte) 0x33),
					new Short((short) 0x4455)};
			
			assertArrayEquals(result, fss.fillInMessage(parameters));
		}
		
		{
			final FormatStringSysex fss = new FormatStringSysex("F0AB{i}CD{l5}F7");
			final byte[] result = { (byte) 0xF0, (byte) 0xAB,
					
					(byte) 0x79, (byte) 0x08, (byte) 0x66, (byte) 0x44, /* first parameter */
					
					(byte) 0xCD, 
					
					(byte) 0x35, /* second parameter */
					(byte) 0x2B, (byte) 0x19, (byte) 0x6F, (byte) 0x08,
					
					(byte) 0xF7 };
			final Object[] parameters = { new Integer(0x8F223344),
					new Long(0x8F11223355667788L)};
			
			assertArrayEquals(result, fss.fillInMessage(parameters));
		}
	}
	
	@Test
	public final void testParseMessage() {
		{
			final FormatStringSysex fss = new FormatStringSysex("F0ABCDF7");
			final byte[] stimuli = { (byte) 0xF0, (byte) 0xAB, (byte) 0xCD, (byte) 0xF7 };
			final Object[] realRes = fss.parseMessage(stimuli);
			assertArrayEquals(new Object[0], realRes);
		}

		{
			final FormatStringSysex fss = new FormatStringSysex("F0AB{b}CD{s}F7");
			final byte[] stimuli = { (byte) 0xF0, (byte) 0xAB,
					(byte) 0x33, /* first parameter */
					(byte) 0xCD, 
					(byte) 0x08, (byte) 0x55, /* second parameter */
					(byte) 0xF7 };
			final Object[] parameters = { new Byte((byte) 0x33),
					new Short((short) 0x455)};
			final Object[] realRes = fss.parseMessage(stimuli);
			
			assertArrayEquals(parameters, realRes);
		}
		
		{
			final FormatStringSysex fss = new FormatStringSysex("F0AB{i}CD{l5}F7");
			final byte[] stimuli = { (byte) 0xF0, (byte) 0xAB,
					(byte) 0x8F, (byte) 0x22, (byte) 0x33, (byte) 0x44, /* first parameter */
					(byte) 0xCD, 
					
					(byte) 0x35, /* second parameter */
					(byte) 0x2B, (byte) 0x19, (byte) 0x6F, (byte) 0x08,
					
					(byte) 0xF7 };
			final Object[] parameters = { new Integer(0xF1E899C4),
					new Long(0x55667788)};
			final Object[] realRes = fss.parseMessage(stimuli);
						
			assertArrayEquals(parameters, realRes);
		}
		
		{
			final FormatStringSysex fss = new FormatStringSysex("F0 43 10 3E 0F 01" +
					/* element */ "{b}" +
					/* parameter */ "{b}" +
					/* channel */ "{b}" + 
					/* value */ "{-l5}" + 
			/* STOP */ "F7");
			
			// F0 43 10 3E 0F 01 2B 0D 01 0F 7F 7F 7B 4A F7 PM5D Channel1 Gate Threshold
			final byte[] stimuli = { (byte) 0xF0, (byte) 0x43,
					(byte) 0x10, (byte) 0x3E, (byte) 0x0F, (byte) 0x01,
					(byte) 0x2B, (byte) 0x0D, (byte) 0x01,
					
					(byte) 0x0F, (byte) 0x7F, (byte) 0x7F, (byte) 0x7B, (byte) 0x4A, /* Signed 5-byte Long */
					
					(byte) 0xF7 };
			final Object[] parameters = { new Byte((byte) 0x2B),
					new Byte((byte) 0x0D),
					new Byte((byte) 1),
					new Long(-566) };
			final Object[] realRes = fss.parseMessage(stimuli);
						
			assertArrayEquals(parameters, realRes);
		}
	}

}
