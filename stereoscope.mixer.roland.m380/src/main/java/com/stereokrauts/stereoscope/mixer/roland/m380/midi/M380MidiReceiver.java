package com.stereokrauts.stereoscope.mixer.roland.m380.midi;

import java.util.logging.Level;

import model.mixer.interfaces.IAmMixer;
import model.mixer.interfaces.IUnderstandMixer;

import com.stereokrauts.lib.binary.ByteStringConversion;
import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.util.FormatStringSysex;
import com.stereokrauts.stereoscope.mixer.roland.m380.M380Mixer;
import com.stereokrauts.stereoscope.model.messaging.message.control.MsgSceneChange;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelNameChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputPan;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAttack;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaDecayRelease;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaGain;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaHold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKeyIn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKnee;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRange;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRatio;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaThreshold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelAuxLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaAttackLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaDecayReleaseLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaGainLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaHoldLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaKneeLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaRangeLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaRatioLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputDynaThresholdLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPanLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqFLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqGLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqQLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqF;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;
import com.stereokrauts.stereoscope.model.messaging.message.mixerglobal.MsgDcaLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;



public final class M380MidiReceiver extends IUnderstandMixer {
	private static final SLogger logger = StereoscopeLogManager.getLogger("roland-m380");

	private static final boolean DEBUG_FINEST = false;

	private final M380Mixer yammi;

	public M380MidiReceiver(final IAmMixer partner) {
		super(partner);
		this.yammi = (M380Mixer) partner;
	}

	/**
	 * This function gets called by the rwMidi-Library each time a sysex
	 * message is received over a Midi Port.
	 */
	@Override
	public void handleSysex(final byte[] sysexmessage) {
		/* only use messages of appropriate length. */
		if (sysexmessage.length < 14) {
			return;
		}

		this.yammi.newMessageFromMixer();

		if (DEBUG_FINEST) {
			logger.log(Level.FINEST, "CLEARMESSAGE: " + ByteStringConversion.toHex(sysexmessage));
		}



		/* From Roland Specification
		 * 
		 * Byte 0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 
		 * Msg  F0 41 0n 00 00 24 12 aa aa aa aa dd dd dd dd ss F7
		 *
		 * aa - Address pattern
		 * dd - data value (1 to 4 bytes)
		 * ss - checksum
		 */

		Object[] parameters;

		byte section = 0;
		byte channel = 0;
		byte subsec = 0;
		byte parameter = 0;
		int value = 0;

		/* build parser for one or two byte data block */
		if (sysexmessage.length == 14) {
			final FormatStringSysex parser = new FormatStringSysex("F0 41 00 00 00 24 12" +
					/* section */ "{b}" +
					/* channel */ "{b}" +
					/* subsection */ "{b}" + 
					/* parameter */ "{b}" +
					/* value */ "{-b}" +
					/* checksum */ "{b}" +
					/* EOX */ "F7");
			parameters = parser.parseMessage(sysexmessage);

			section = ((Byte)parameters[0]);
			channel = ((Byte)parameters[1]);
			subsec = ((Byte)parameters[2]);
			parameter = ((Byte)parameters[3]);
			value = ((Byte)parameters[4]);

		} else if (sysexmessage.length == 15) {
			final FormatStringSysex parser = new FormatStringSysex("F0 41 00 00 00 24 12" +
					/* section */ "{b}" +
					/* channel */ "{b}" +
					/* subsection */ "{b}" + 
					/* parameter */ "{b}" +
					/* value */ "{-s}" +
					/* checksum */ "{b}" +
					/* EOX */ "F7");
			parameters = parser.parseMessage(sysexmessage);

			section = ((Byte)parameters[0]);
			channel = ((Byte)parameters[1]);
			subsec = ((Byte)parameters[2]);
			parameter = ((Byte)parameters[3]);
			value = ((Short)parameters[4]);
		} else if (sysexmessage.length == 16) { //just for 3byte frequency values
			final FormatStringSysex parser = new FormatStringSysex("F0 41 00 00 00 24 12" +
					/* section */ "{b}" +
					/* channel */ "{b}" +
					/* subsection */ "{b}" + 
					/* parameter */ "{b}" +
					/* value msb */ "{b}" +
					/* value */ "{s}" +
					/* checksum */ "{b}" +
					/* EOX */ "F7");
			parameters = parser.parseMessage(sysexmessage);

			section = ((Byte)parameters[0]);
			channel = ((Byte)parameters[1]);
			subsec = ((Byte)parameters[2]);
			parameter = ((Byte)parameters[3]);
			final int msbValue = ((Byte)parameters[4]);
			final int lsbValue = ((Short)parameters[5]);
			value = lsbValue | (msbValue << 14);

		} else {
			return;
		}

		//final byte checksum = ((Byte)parameters[5]);


		try {
			if (section == M380SysexParameter.ADDR_INPUT_CHANNEL) {
				if (subsec == M380SysexParameter.INPUT_CHANNEL) {
					if (parameter >= 0x00 && parameter <= 0x2F) {
						this.handleInputChannelMain(channel, parameter, value);
					} else if (parameter >= 0x30 && parameter <= 0x3F) {
						this.handleInputChannelGate(channel, parameter, value);
					} else if (parameter >= 0x40 && parameter <= 0x4F) {
						this.handleInputChannelComp(channel, parameter, value);
					} else if (parameter >= 0x50 && parameter <= 0x7F) {
						this.handleInputChannelEQ(channel, parameter, value);
					}
				} else if (subsec == M380SysexParameter.INPUT_AUX) {
					this.handleInputAux(channel, parameter, value);
				} else if (subsec == M380SysexParameter.INPUT_MISC) {
					//not used by Stereoscope
					logger.info("Unsupported miscellaneous channel message");
				} else {
					logger.info("Wrong subsection parameter");
				}
			} else if (section == M380SysexParameter.ADDR_AUX_CHANNEL) {
				if (subsec == M380SysexParameter.AUX_SECTION_GENERAL) {
					if (parameter == M380SysexParameter.AUX_FADER_LEVEL) {
						float floatValue = 0;
						if (value != -8192) {
							floatValue = (float) (value + 906) / 1006;
						}
						this.yammi.fireChange(new MsgAuxMasterLevelChanged(channel, floatValue));
					}
				}
			} else if (section == M380SysexParameter.ADDR_DCA_GROUP) {
				if (subsec == 0x00) {
					if (parameter == M380SysexParameter.DCA_FADER_LEVEL) {
						float floatValue = 0;
						if (value != -8192) {
							floatValue = (float) (value + 906) / 1006;
						}
						this.yammi.fireChange(new MsgDcaLevelChanged(channel, floatValue));
					}
				}

			} else if (section == M380SysexParameter.ADDR_MAIN_CHANNEL) {
				if (channel == 0) {
					if (subsec == M380SysexParameter.MAIN_SECTION_GENERAL) {
						if (parameter == M380SysexParameter.MAIN_CH_LEVEL) {
							float floatValue = 0;
							if (value != -8192) {
								floatValue = (float) (value + 906) / 1006;
							}
							this.yammi.fireChange(new MsgMasterLevelChanged(floatValue));
						}
					}
				}
			} else if (section == M380SysexParameter.ADDR_EFFECTS) {
				if (channel >= 0x10 && channel <= 0x13) {
					if (subsec == M380SysexParameter.GEQ_SECTION_GENERAL) {
						if (parameter >= M380SysexParameter.GEQ_BAND1 &&
								parameter <= M380SysexParameter.GEQ_BAND31) {
							final short geqNumber = (short) (channel - 16);
							final int band = (parameter - M380SysexParameter.GEQ_BAND1) / 2;
							final float floatValue = (float) value / 150;
							this.yammi.fireChange(new MsgGeqBandLevelChanged(geqNumber, band, false,
									floatValue));
						}
					}
				}
			}

		} catch (final Exception e) {
			logger.log(Level.INFO, "Exception while parsing MIDI message", e);
		}

	}

	private void handleInputChannelMain(final byte channel, final byte parameter, final int value) {
		if (parameter == M380SysexParameter.CH_NAME1) {
			this.yammi.getLabelMaker().setChannelNameChar(channel,
					M380SysexParameter.CH_NAME1, (byte) value);
			final String channelName = this.yammi.getLabelMaker().getChannelNameLabel(channel);
			this.yammi.fireChange(new MsgChannelNameChanged(channel, channelName));
		} else if (parameter == M380SysexParameter.CH_NAME2) {
			this.yammi.getLabelMaker().setChannelNameChar(channel,
					M380SysexParameter.CH_NAME2, (byte) value);
			final String channelName = this.yammi.getLabelMaker().getChannelNameLabel(channel);
			this.yammi.fireChange(new MsgChannelNameChanged(channel, channelName));
		} else if (parameter == M380SysexParameter.CH_NAME3) {
			this.yammi.getLabelMaker().setChannelNameChar(channel,
					M380SysexParameter.CH_NAME3, (byte) value);
			final String channelName = this.yammi.getLabelMaker().getChannelNameLabel(channel);
			this.yammi.fireChange(new MsgChannelNameChanged(channel, channelName));
		} else if (parameter == M380SysexParameter.CH_NAME4) {
			this.yammi.getLabelMaker().setChannelNameChar(channel,
					M380SysexParameter.CH_NAME4, (byte) value);
			final String channelName = this.yammi.getLabelMaker().getChannelNameLabel(channel);
			this.yammi.fireChange(new MsgChannelNameChanged(channel, channelName));
		} else if (parameter == M380SysexParameter.CH_LINK) {
			// not implemented (pairing)
		} else if (parameter == M380SysexParameter.CH_MUTE) {
			this.yammi.fireChange(new MsgChannelOnChanged(channel, value == 1 ? true : false));
		} else if (parameter == M380SysexParameter.CH_SOLO) {
			// not implemented
		} else if (parameter == M380SysexParameter.CH_LEVEL) {
			float floatValue = 0;
			if (value != -8192) {
				floatValue = (float) (value + 906) / 1006;
			}
			this.yammi.fireChange(new MsgChannelLevelChanged(channel, floatValue));
			final String label = this.yammi.getLabelMaker().getFaderLevelLabel(floatValue);
			this.yammi.fireChange(new MsgChannelLevelLabel(channel, 0, label));
		} else if (parameter == M380SysexParameter.CH_PAN) {
			final float floatValue = (float) (value - 64) / 63;
			this.yammi.fireChange(new MsgInputPan(channel, floatValue));
			final String label = this.yammi.getLabelMaker().getPanningLabel(floatValue);
			this.yammi.fireChange(new MsgInputPanLabel(channel, 0, label));
		} else {
			logger.info("Unknown channel parameter");
		}
	}

	private void handleInputChannelGate(final byte channel, final byte parameter, final long value) {
		if (parameter == M380SysexParameter.CH_GATE_SWITCH) {
			this.yammi.fireChange(new MsgInputDynaOn(0, channel, (value == 1)));
		} else if (parameter == M380SysexParameter.CH_GATE_KEY_IN) {
			final float floatValue = (float) (value) / (float) (127);
			this.yammi.fireChange(new MsgInputDynaKeyIn(0, channel, floatValue));
		} else if (parameter == M380SysexParameter.CH_GATE_MODE) {
			// not implemented
		} else if (parameter == M380SysexParameter.CH_GATE_THRESHOLD) {
			final int normValue = (int) value + 800;
			final float floatValue = (float) (normValue) / (float) (800);
			final String label = this.yammi.getLabelMaker().getGateThresholdLabel(floatValue);
			this.yammi.fireChange(new MsgInputDynaThreshold(0, channel, floatValue));
			this.yammi.fireChange(new MsgInputDynaThresholdLabel(0, channel, label));
		} else if (parameter == M380SysexParameter.CH_GATE_RATIO) {
			// not supported yet
			//float floatValue = (float) value / 14;
		} else if (parameter == M380SysexParameter.CH_GATE_KNEE) {
			// not supported yet
			//float floatValue = (float) value / 10;
		} else if (parameter == M380SysexParameter.CH_GATE_RANGE) {
			float floatValue = 0.0f;
			if (value != -8192) {
				floatValue = (float) (value + 906) / 906;
			}
			final String label = this.yammi.getLabelMaker().getGateRangeLabel(floatValue);
			this.yammi.fireChange(new MsgInputDynaRange(0, channel, floatValue));
			this.yammi.fireChange(new MsgInputDynaRangeLabel(0, channel, label));
		} else if (parameter == M380SysexParameter.CH_GATE_ATTACK) {
			final float floatValue = (float) (value) / 8000;

			final String label = this.yammi.getLabelMaker().getAttackLabel(floatValue);
			this.yammi.fireChange(new MsgInputDynaAttack(0, channel, floatValue));
			this.yammi.fireChange(new MsgInputDynaAttackLabel(0, channel, label));
		} else if (parameter == M380SysexParameter.CH_GATE_RELEASE) {
			final float floatValue = (float) (value) / 8000;
			final String label = this.yammi.getLabelMaker().getReleaseHoldLabel(floatValue);
			this.yammi.fireChange(new MsgInputDynaDecayRelease(0, channel, floatValue));
			this.yammi.fireChange(new MsgInputDynaDecayReleaseLabel(0, channel, label));
		} else if (parameter == M380SysexParameter.CH_GATE_HOLD) {
			final float floatValue = (float) (value) / 8000;
			final String label = this.yammi.getLabelMaker().getReleaseHoldLabel(floatValue);
			this.yammi.fireChange(new MsgInputDynaHold(0, channel, floatValue));
			this.yammi.fireChange(new MsgInputDynaHoldLabel(0, channel, label));
		} else {
			logger.info("Unknown channel gate parameter");
		}
	}

	private void handleInputChannelComp(final byte channel, final byte parameter, final long value) {
		if (parameter == M380SysexParameter.CH_COMP_SWITCH) {
			this.yammi.fireChange(new MsgInputDynaOn(1, channel, (value ==1)));
		} else if (parameter == M380SysexParameter.CH_COMP_KEY_IN) {
			// not implemented
		} else if (parameter == M380SysexParameter.CH_COMP_THRESHOLD) {
			final int normValue = (int) value + 400;
			final float floatValue = (float) (normValue) / 400;
			final String label = this.yammi.getLabelMaker().getCompressorThresholdLabel(floatValue);
			this.yammi.fireChange(new MsgInputDynaThreshold(1, channel, floatValue));
			this.yammi.fireChange(new MsgInputDynaThresholdLabel(1, channel, label));
		} else if (parameter == M380SysexParameter.CH_COMP_RATIO) {
			final float floatValue = (float) (value) / 13;
			final String label = this.yammi.getLabelMaker().getDynamicsRatioLabel(floatValue);
			this.yammi.fireChange(new MsgInputDynaRatio(1, channel, floatValue));
			this.yammi.fireChange(new MsgInputDynaRatioLabel(1, channel, label));
		} else if (parameter == M380SysexParameter.CH_COMP_KNEE) {
			final float floatValue = (float) (value) / 9;
			final String label = this.yammi.getLabelMaker().getDynamicsKneeLabel(floatValue);
			this.yammi.fireChange(new MsgInputDynaKnee(1, channel, floatValue));
			this.yammi.fireChange(new MsgInputDynaKneeLabel(1, channel, label));
		} else if (parameter == M380SysexParameter.CH_COMP_ATTACK) {
			final float floatValue = (float) (value) / 8000;
			final String label = this.yammi.getLabelMaker().getAttackLabel(floatValue);
			this.yammi.fireChange(new MsgInputDynaAttack(1, channel, floatValue));
			this.yammi.fireChange(new MsgInputDynaAttackLabel(1, channel, label));
		} else if (parameter == M380SysexParameter.CH_COMP_RELEASE) {
			final float floatValue = (float) (value) / 8000;
			final String label = this.yammi.getLabelMaker().getReleaseHoldLabel(floatValue);
			this.yammi.fireChange(new MsgInputDynaDecayRelease(1, channel, floatValue));
			this.yammi.fireChange(new MsgInputDynaDecayReleaseLabel(1, channel, label));
		} else if (parameter == M380SysexParameter.CH_COMP_GAIN) {
			final float floatValue = (float) (value + 400) / 800;
			final String label = this.yammi.getLabelMaker().getCompressorGainLabel(floatValue);
			this.yammi.fireChange(new MsgInputDynaGain(1, channel, floatValue));
			this.yammi.fireChange(new MsgInputDynaGainLabel(1, channel, label));
		} else if (parameter == M380SysexParameter.CH_COMP_AUTOGAIN) {
			// not implemented
		} else {
			logger.info("Unknown channel compressor parameter");
		}

	}


	private void handleInputChannelEQ(final byte channel, final byte parameter, final long value) {
		if (parameter == M380SysexParameter.CH_EQ_SWITCH) {
			this.yammi.fireChange(new MsgInputPeqOnChanged(channel, (value == 1)));
		} else if (parameter == M380SysexParameter.CH_EQ_ATT) {
			// not implemented
		} else if (parameter == M380SysexParameter.CH_EQ_LO_GAIN) {
			final float floatValue = (float) (value) / 150;
			final String label = this.yammi.getLabelMaker().getPeqGainLabel(floatValue);
			this.yammi.fireChange(new MsgInputPeqG(channel, M380SysexParameter.PEQ_BAND1, floatValue));
			this.yammi.fireChange(new MsgInputPeqGLabel(channel, M380SysexParameter.PEQ_BAND1, label));
		} else if (parameter == M380SysexParameter.CH_EQ_LO_FREQ) {
			final float floatValue = (((float) value - 20) / 980);
			final String label = this.yammi.getLabelMaker().getPeqFrequencyTable((int) value);
			this.yammi.fireChange(new MsgInputPeqF(channel, M380SysexParameter.PEQ_BAND1, floatValue));
			this.yammi.fireChange(new MsgInputPeqFLabel(channel, M380SysexParameter.PEQ_BAND1, label));
		} else if (parameter == M380SysexParameter.CH_EQ_LOMID_GAIN) {
			final float floatValue = (float) (value) / 150;
			final String label = this.yammi.getLabelMaker().getPeqGainLabel(floatValue);
			this.yammi.fireChange(new MsgInputPeqG(channel, M380SysexParameter.PEQ_BAND2, floatValue));
			this.yammi.fireChange(new MsgInputPeqGLabel(channel, M380SysexParameter.PEQ_BAND2, label));
		} else if (parameter == M380SysexParameter.CH_EQ_LOMID_FREQ) {
			final float floatValue = (((float) value - 20) / 19980);
			final String label = this.yammi.getLabelMaker().getPeqFrequencyTable((int) value);
			this.yammi.fireChange(new MsgInputPeqF(channel, M380SysexParameter.PEQ_BAND2, floatValue));
			this.yammi.fireChange(new MsgInputPeqFLabel(channel, M380SysexParameter.PEQ_BAND2, label));
		} else if (parameter == M380SysexParameter.CH_EQ_LOMID_Q) {
			final float floatValue = (float) (value - 36) / 1564;
			final String label = this.yammi.getLabelMaker().getPeqQualityLabel(floatValue);
			this.yammi.fireChange(new MsgInputPeqQ(channel, M380SysexParameter.PEQ_BAND2, floatValue));
			this.yammi.fireChange(new MsgInputPeqQLabel(channel, M380SysexParameter.PEQ_BAND2, label));
		} else if (parameter == M380SysexParameter.CH_EQ_HIMID_GAIN) {
			final float floatValue = (float) (value) / 150;
			final String label = this.yammi.getLabelMaker().getPeqGainLabel(floatValue);
			this.yammi.fireChange(new MsgInputPeqG(channel, M380SysexParameter.PEQ_BAND3, floatValue));
			this.yammi.fireChange(new MsgInputPeqGLabel(channel, M380SysexParameter.PEQ_BAND3, label));
		} else if (parameter == M380SysexParameter.CH_EQ_HIMID_FREQ) {
			final float floatValue = ((float) (value - 20) / 19980);
			final String label = this.yammi.getLabelMaker().getPeqFrequencyTable((int) value);
			this.yammi.fireChange(new MsgInputPeqF(channel, M380SysexParameter.PEQ_BAND3, floatValue));
			this.yammi.fireChange(new MsgInputPeqFLabel(channel, M380SysexParameter.PEQ_BAND3, label));
		} else if (parameter == M380SysexParameter.CH_EQ_HIMID_Q) {
			final float floatValue = (float) (value - 36) / 1564;
			final String label = this.yammi.getLabelMaker().getPeqQualityLabel(floatValue);
			this.yammi.fireChange(new MsgInputPeqQ(channel, M380SysexParameter.PEQ_BAND3, floatValue));
			this.yammi.fireChange(new MsgInputPeqQLabel(channel, M380SysexParameter.PEQ_BAND3, label));
		} else if (parameter == M380SysexParameter.CH_EQ_HI_GAIN) {
			final float floatValue = (float) (value) / 150;
			final String label = this.yammi.getLabelMaker().getPeqGainLabel(floatValue);
			this.yammi.fireChange(new MsgInputPeqG(channel, M380SysexParameter.PEQ_BAND4, floatValue));
			this.yammi.fireChange(new MsgInputPeqGLabel(channel, M380SysexParameter.PEQ_BAND4, label));
		} else if (parameter == M380SysexParameter.CH_EQ_HI_FREQ) {
			final float floatValue = ((float) (value - 1000) / 19000);
			final String label = this.yammi.getLabelMaker().getPeqFrequencyTable((int) value);
			this.yammi.fireChange(new MsgInputPeqF(channel, M380SysexParameter.PEQ_BAND4, floatValue));
			this.yammi.fireChange(new MsgInputPeqFLabel(channel, M380SysexParameter.PEQ_BAND4, label));
		} else {
			logger.info("Unknown channel EQ parameter");
		}
	}

	private void handleInputAux(final byte channel, final byte parameter, final long value) {
		if (parameter % 8 == 0) {
			final int aux = parameter / 8; 
			this.yammi.fireChange(new MsgAuxSendOnChanged(channel, aux, (value == 1)));
		} else if (parameter == M380SysexParameter.CH_AUX_SEND_POSITION) {
			// not implemented
		} else if ((parameter - M380SysexParameter.CH_AUX_SEND_LEVEL) % 8 == 0) {
			float floatValue = 0;
			if (value != -8192) {
				floatValue = (float) (value + 906) / 1006;
			}
			final int aux = (parameter - M380SysexParameter.CH_AUX_SEND_LEVEL) / 8;
			final String label = this.yammi.getLabelMaker().getFaderLevelLabel(floatValue);
			this.yammi.fireChange(new MsgAuxSendChanged(channel, aux, floatValue));
			this.yammi.fireChange(new MsgChannelAuxLevelLabel(channel, aux, label));
		} else if (parameter == M380SysexParameter.CH_AUX_SEND_PAN) {
			// not implemented
		} else if (parameter == M380SysexParameter.CH_AUX_SEND_PAN_LINK) {
			// not implemented
		} else {
			logger.info("Unknown channel aux parameter");
		}
	}

	@Override
	public void handleProgramChange(final int newProgram) {
		this.yammi.fireChange(new MsgSceneChange(newProgram, "unknown"));
	}
}
