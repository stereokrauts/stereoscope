package com.stereokrauts.stereoscope.mixer.yamaha.dm2000;

import model.mixer.interfaces.IAmMixer;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.util.FormatStringSysex;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeMidiReceiver;

public class DM2000MidiReceiver extends GenericMidsizeMidiReceiver {
	private static final SLogger logger = StereoscopeLogManager.getLogger("yamaha-dm2000");
	DM2000Mixer partner;

	public DM2000MidiReceiver(final IAmMixer partner) {
		super(partner);
		this.partner = (DM2000Mixer) partner;
	}

	@Override
	public void handleSysex(final byte[] sysexmessage) {
		if (sysexmessage.length != 14) {
			logger.info("Unknown Sysex Message of length " + sysexmessage.length + ": " + ByteStringConversion.toHex(sysexmessage));
			return;
		}

		if (sysexmessage[6] == DM2000MidiTransmitter.ELMT_GRAPHICAL_EQ) {
			final FormatStringSysex parser = new FormatStringSysex("F0 43 10 3E 7F 01" +
					/* element */ "{b}" +
					/* parameter */ "{b}" +
					/* channel */ "{b}" + 
					/* value */ "{-i}" + 
					/* STOP */ "F7");

			final Object[] parameters = parser.parseMessage(sysexmessage);

			final byte elementNo = ((Byte)parameters[0]);
			final byte parameter = ((Byte)parameters[1]);
			final byte channel = ((Byte)parameters[2]);
			final int value = ((Integer)parameters[3]);

			// this check is already done above, but just to be sure... ;-)
			if (elementNo == DM2000MidiTransmitter.ELMT_GRAPHICAL_EQ) {
				final float floatValue = (float)(value) / DM2000MidiTransmitter.GEQ_BAND_RANGE;
				// check for bands are useless as there is just leftband
				this.partner.fireChangeGeqBandLevel(channel, false, parameter - DM2000MidiTransmitter.PARAM_GEQ_LEFT_FIRSTBAND,
						floatValue);
			}
			this.partner.newMessageFromMixer();
			return;
		}

		super.handleSysex(sysexmessage);
	}
}
