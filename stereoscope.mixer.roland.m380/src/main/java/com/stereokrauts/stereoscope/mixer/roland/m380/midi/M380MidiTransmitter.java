package com.stereokrauts.stereoscope.mixer.roland.m380.midi;


import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.lib.midi.util.FormatStringSysex;
import com.stereokrauts.stereoscope.mixer.roland.m380.M380Mixer;

public final class M380MidiTransmitter {
	ISendMidi midi;
	private final M380Mixer mixer;



	public M380MidiTransmitter(final M380Mixer mixer, final ISendMidi midi) {
		this.mixer = mixer;
		this.midi = midi;
	}

	public void changeChannelInputParameter(final int channel,
			final byte parameter, final byte data) {
		this.changeByteParameter(M380SysexParameter.ADDR_INPUT_CHANNEL,(byte) channel,
				M380SysexParameter.INPUT_CHANNEL, parameter, data);
	}

	public void changeLargeChannelInputParameter(final int channel,
			final byte parameter, final short data) {
		this.changeShortParameter(M380SysexParameter.ADDR_INPUT_CHANNEL,
				(byte) channel, M380SysexParameter.INPUT_CHANNEL, parameter, data);
	}

	public void changeChannelAuxParameter(final int channel,
			final byte parameter, final byte data) {
		this.changeByteParameter(M380SysexParameter.ADDR_INPUT_CHANNEL,
				(byte) channel, M380SysexParameter.INPUT_AUX, parameter, data);
	}

	public void changeLargeChannelAuxParameter(final int channel,
			final byte parameter, final short data) {
		this.changeShortParameter(M380SysexParameter.ADDR_INPUT_CHANNEL,
				(byte) channel, M380SysexParameter.INPUT_AUX, parameter, data);
	}

	public void changeLargeAuxiliarySendParameter(final int aux, 
			final byte parameter, final short data) {
		this.changeShortParameter(M380SysexParameter.ADDR_AUX_CHANNEL,
				(byte) aux, M380SysexParameter.AUX_SECTION_GENERAL, parameter, data);
	}

	public void changeLargeDcaParameter(final int dca, final byte parameter, final short data) {
		this.changeShortParameter(M380SysexParameter.ADDR_DCA_GROUP,
				(byte) dca, (byte) 0x00, parameter, data);
	}

	public void changeLargeMainChannelParameter(final byte parameter,
			final short data) {
		this.changeShortParameter(M380SysexParameter.ADDR_MAIN_CHANNEL,
				(byte) 0x00, M380SysexParameter.MAIN_SECTION_GENERAL,
				parameter, data);
	}

	public void changeGeqParameterByte(final int geqNumber, final byte parameter,
			final byte data) {
		final byte geqNumberOffset = (byte) (0x10 + (byte) geqNumber);
		this.changeByteParameter(M380SysexParameter.ADDR_EFFECTS, geqNumberOffset,
				M380SysexParameter.GEQ_SECTION_GENERAL, parameter, data);
	}

	public void changeGeqParameterShort(final int geqNumber, final byte parameter,
			final short data) {
		final byte geqNumberOffset = (byte) (0x10 + (byte) geqNumber);
		this.changeShortParameter(M380SysexParameter.ADDR_EFFECTS, geqNumberOffset,
				M380SysexParameter.GEQ_SECTION_GENERAL, parameter, data);
	}

	public void changeInputParameterInt(final int channel, final byte parameter,
			final int data) {
		this.changeIntParameter(M380SysexParameter.ADDR_INPUT_CHANNEL,
				(byte) channel, M380SysexParameter.INPUT_CHANNEL, parameter, data);
	}

	public void requestChannelInputParameter(final int channel,
			final byte parameter, final int sizeOfRequest) {
		this.requestParameter(M380SysexParameter.ADDR_INPUT_CHANNEL, (byte) channel,
				M380SysexParameter.INPUT_CHANNEL, parameter, sizeOfRequest);
	}

	public void requestChannelAuxParameter(final int channel,
			final byte parameter, final int sizeOfRequest) {
		this.requestParameter(M380SysexParameter.ADDR_INPUT_CHANNEL, (byte) channel,
				M380SysexParameter.INPUT_AUX, parameter, sizeOfRequest);
	}

	public void requestAuxiliarySendParameter(final int aux, 
			final byte parameter, final int sizeOfRequest) {
		this.requestParameter(M380SysexParameter.ADDR_AUX_CHANNEL, (byte) aux,
				M380SysexParameter.AUX_SECTION_GENERAL, parameter, sizeOfRequest);
	}

	public void requestMainChannelParameter(final byte parameter,
			final int sizeOfRequest) {
		this.requestParameter(M380SysexParameter.ADDR_MAIN_CHANNEL,
				M380SysexParameter.MAIN_CH_LEFT,
				M380SysexParameter.MAIN_SECTION_GENERAL,
				parameter, sizeOfRequest);

	}

	public void requestGeqParameter(final int geqNumber, final byte parameter,
			final int sizeOfRequest) {
		final byte geqNumberOffset = (byte) (0x10 + (byte) geqNumber);
		this.requestParameter(M380SysexParameter.ADDR_EFFECTS, geqNumberOffset,
				M380SysexParameter.GEQ_SECTION_GENERAL, parameter, sizeOfRequest);
	}

	/*
	 * Data Set
	 * --------
	 * F0         - System Exclusive Message
	 * 41         - Manufacturer ID (Roland)
	 * 00         - Device ID (1)
	 * 00 00 24   - Model ID (M-380/M-400)
	 * 12         - Data Set (DT1)
	 * aa  (byte) - Address MSB
	 * bb  (byte) - Address
	 * cc  (byte) - Address
	 * dd  (byte) - Address LSB
	 * ee  (byte) - Data (MSB, Integer)
	 * ff  (byte) - Data (I3 (not used))
	 * gg  (byte) - Data (Short)
	 * hh  (byte) - Data (LSB, Byte)
	 * Sum (byte) - Check Sum
	 * F7         - EOX (End Of system eXclusive message)
	 */
	/*
	public void changeIntegerParameter(final byte section, final byte channel,
			final byte subsec, final byte parameter, final int data) {
		final byte sum = calculateCheckSum(section, channel, subsec, parameter, data);
		MixerCommunicationVisualisator.getInstance().newMessageToMixer();
		FormatStringSysex fss = new FormatStringSysex("F0 41 00 00 00 24 12 {b} {b} {b} {b} {i} {b} F7");
		Object[] par = { new Byte(section),
				new Byte(channel),
				new Byte(subsec),
				new Byte(parameter),
				new Integer(data),
				new Byte(sum)};
		midi.sendSysexData(fss.fillInMessage(par));
	}
	 */
	public void changeIntParameter(final byte section, final byte channel,
			final byte subsec, final byte parameter, final int data) {
		// 3 byte values for frequencies
		final byte dataMsb = (byte) (data >> 14);
		final short dataLsb = (short) (data); // & 0x7F7F7F);
		final byte sum = this.calculateCheckSum(section, channel, subsec, parameter, dataMsb, dataLsb);
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 41 00 00 00 24 12 {b} {b} {b} {b} {b} {s} {b} F7");
		final Object[] par = { new Byte(section),
				new Byte(channel),
				new Byte(subsec),
				new Byte(parameter),
				new Byte(dataMsb),
				new Short(dataLsb),
				new Byte(sum)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	public void changeShortParameter(final byte section, final byte channel,
			final byte subsec, final byte parameter, final short data) {
		final byte sum = this.calculateCheckSum(section, channel, subsec, parameter, (byte) 0x00, data);
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 41 00 00 00 24 12 {b} {b} {b} {b} {-s} {b} F7");
		final Object[] par = { new Byte(section),
				new Byte(channel),
				new Byte(subsec),
				new Byte(parameter),
				new Short(data),
				new Byte(sum)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	public void changeByteParameter(final byte section, final byte channel,
			final byte subsec, final byte parameter, final byte data) {
		final byte sum = this.calculateCheckSum(section, channel, subsec, parameter, (byte) 0x00, data);
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 41 00 00 00 24 12 {b} {b} {b} {b} {b} {b} F7");
		final Object[] par = { new Byte(section),
				new Byte(channel),
				new Byte(subsec),
				new Byte(parameter),
				new Byte(data),
				new Byte(sum)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}


	/*
	 * Data Request
	 * ------------
	 * F0         - System Exclusive Message
	 * 41         - Manufacturer ID (Roland)
	 * 00         - Device ID (1)
	 * 00 00 24   - Model ID (M-380/M-400)
	 * 11         - Data Request (RQ1)
	 * aa  (byte) - Address MSB
	 * bb  (byte) - Address
	 * cc  (byte) - Address
	 * dd  (byte) - Address LSB
	 * ee  (int)  - Size
	 * Sum (byte) - Check Sum
	 * F7         - EOX
	 */
	public void requestParameter(final byte section, final byte channel,
			final byte subsec, final byte parameter, final int sizeOfRequest) {
		final byte sum = this.calculateCheckSum(section, channel, subsec, parameter, (byte) 0x00, (short) sizeOfRequest);
		this.mixer.newMessageToMixer();
		final FormatStringSysex fss = new FormatStringSysex("F0 41 00 00 00 24 11 {b} {b} {b} {b} {i} {b} F7");
		final Object[] par = { new Byte(section),
				new Byte(channel),
				new Byte(subsec),
				new Byte(parameter),
				new Integer(sizeOfRequest),
				new Byte(sum)};
		this.midi.sendSysexData(fss.fillInMessage(par));
	}

	public byte calculateCheckSum(final byte section, final byte channel,
			final byte subsec, final byte parameter, final byte msb, final short data) {
		byte checksum;
		final Byte[] dataAsBytes = this.devideShortIntoBytes(data);
		final int sum = section + channel + subsec + parameter + msb + dataAsBytes[0] + dataAsBytes[1];
		final byte odd = (byte) (sum % 128);

		if (odd == 0) {
			checksum = 0;
		} else {
			checksum = (byte) (128 - odd);
		}
		return checksum;

	}

	public Byte[] devideShortIntoBytes(final short data) {
		final Byte[] dataAsBytes = new Byte[2];
		final short lsb7BitModifier = 0x007F;
		final short msb7BitModifier = 0x3F80;
		final byte sevenBitMaxVal = 0x7F;
		if (data < 0) {
			final short normalized = (short) (-data - 1);
			final byte lsb = (byte)  (normalized & lsb7BitModifier);
			final byte msb = (byte) ((normalized & msb7BitModifier) >> 7);
			dataAsBytes[0] = (byte) (sevenBitMaxVal - lsb);
			dataAsBytes[1] = (byte) (sevenBitMaxVal - msb);
		} else {
			dataAsBytes[0] = (byte) (data & lsb7BitModifier);
			dataAsBytes[1] = (byte) ((data & msb7BitModifier) >> 7);
		}
		return dataAsBytes;
	}

}
