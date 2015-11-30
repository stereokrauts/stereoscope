package com.stereokrauts.stereoscope.mixer.roland.m380;

import java.util.logging.Level;


import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.mixer.roland.m380.midi.M380MidiTransmitter;
import com.stereokrauts.stereoscope.mixer.roland.m380.midi.M380SysexParameter;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqFLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqGLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqQLabel;

public class M380MixerModifier {
	private static final SLogger logger = StereoscopeLogManager.getLogger("roland-m380-modifier");

	private static final boolean DEBUG_FINEST = false;

	private final M380MidiTransmitter sysex;
	private final M380Mixer mixer;

	public M380MixerModifier(final M380Mixer mixer, final M380MidiTransmitter mysysex)
	{
		this.mixer = mixer;
		this.sysex = mysysex;
	}

	public void changedAuxMaster(final int aux, final float level) {
		short shortValue = 0;
		if (level == 0.0f) {
			shortValue = -8192;
		} else {
			final short normValue = (short) (level * 1006);
			shortValue = (short) (normValue - 906);
		}
		this.sysex.changeLargeAuxiliarySendParameter(aux, M380SysexParameter.AUX_FADER_LEVEL, shortValue);

		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET AUXMASTER aux=" + aux + ", value=" + shortValue);
		}
	}

	public void changedDcaMaster(final int dca, final float level) {
		short shortValue = 0;
		if (level == 0.0f) {
			shortValue = -8192;
		} else {
			final short normValue = (short) (level * 1006);
			shortValue = (short) (normValue - 906);
		}
		this.sysex.changeLargeDcaParameter(dca, M380SysexParameter.DCA_FADER_LEVEL, shortValue);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET DCAMASTER dca=" + dca + ", value=" + shortValue);
		}
	}

	public void changedChannelAuxLevel(final int channel, final int aux,
			final float level) {
		short shortValue = 0;
		if (level == 0.0f) {
			shortValue = -8192;
		} else {
			final short normValue = (short) (level * 1006);
			shortValue = (short) (normValue - 906);
		}
		this.sysex.changeLargeChannelAuxParameter(channel, (byte) (aux*8 + 2), shortValue);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "AUXLEVELSET channel=" + channel + ", aux=" + aux
					+ ", value=" + shortValue);
		}
	}

	public void changedChannelAuxOn(final int channel, final int aux,
			final boolean status) {
		final byte value = status ? (byte) 1 : (byte) 0;
		this.sysex.changeChannelAuxParameter(channel, (byte) (aux*8), value);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "AUXSENDONSET channel=" + channel + ", aux=" + aux
					+ ", value=" + value);
		}
	}

	public void changedChannelLevel(final int channel, final float level) {
		short shortValue = 0;
		if (level == 0.0f) {
			shortValue = -8192;
		} else {
			final short normValue = (short) (level * 1006);
			shortValue = (short) (normValue - 906);
		}
		this.sysex.changeLargeChannelInputParameter(channel, M380SysexParameter.CH_LEVEL, shortValue);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET channel=" + channel + ", value=" + shortValue);
		}
	}	

	public void changedChannelOnButton(final int channel, final boolean status) {
		this.sysex.changeChannelInputParameter(channel, M380SysexParameter.CH_MUTE, status ? (byte) 1 : (byte) 0);
	}

	public void changedGeqBandLevel(final short eqNumber, final boolean rightChannel, final int band, final float level) {
		final short value = (short) (level * 150);
		this.sysex.changeGeqParameterShort(eqNumber, (byte) (M380SysexParameter.GEQ_BAND1 + (band * 2)), value);

	}

	public void changedGeqBandReset(final short eqNumber, final boolean rightChannel, final int band) {
		this.changedGeqBandLevel(eqNumber, rightChannel, band, 0.0f);
	}

	public void changedGeqFullReset(final short eqNumber) {
		final float zeroValue = 0;
		boolean channel;
		for (int i=0; i<62; i++) {
			if (i<31) {
				channel = false;
				this.changedGeqBandLevel(eqNumber, channel, i, zeroValue);
			} else {
				channel = false;
				this.changedGeqBandLevel(eqNumber, channel, i, zeroValue);
			}
		}
	}

	public void changedInputDynamicsParameterByte(final int channel, final byte parameter, final byte value) {
		this.sysex.changeChannelInputParameter(channel, parameter, value);
	}

	public void changedInputDynamicsParameterShort(final int channel, final byte parameter, final short value) {
		this.sysex.changeLargeChannelInputParameter(channel, parameter, value);
	}

	public void changedInputPan(final int channel, final float value) {
		final byte panning = (byte) ((short) (value * 63) + 64);
		this.sysex.changeChannelInputParameter(channel, M380SysexParameter.CH_PAN, panning);

	}

	public void changedMasterLevel(final float level) {
		short shortValue = 0;
		if (level == 0.0f) {
			shortValue = -8192;
		} else {
			final short normValue = (short) (level * 1006);
			shortValue = (short) (normValue - 906);
		}
		this.sysex.changeLargeMainChannelParameter(M380SysexParameter.MAIN_CH_LEVEL, shortValue);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELSET MASTER value=" + shortValue);
		}
	}

	public void changedPeqBandQ(final int channel, final int band, final Float attachment) {

		byte parameter = 0;
		final short value = (short) ((attachment * 1564) + 36);
		final String label = this.mixer.getLabelMaker().getPeqQualityLabel(attachment);

		if (band==1) {
			parameter = M380SysexParameter.CH_EQ_LOMID_Q;
		} else if (band==2) {
			parameter = M380SysexParameter.CH_EQ_HIMID_Q;
		}

		if (band==1 || band==2) {
			this.sysex.changeLargeChannelInputParameter(channel, parameter, value);
			this.mixer.fireChange(new MsgInputPeqQLabel(channel, band, label));
		}

	}

	public void changedPeqBandF(final int channel, final int band, final Float attachment) {

		byte parameter = 0;
		int value = 0;

		if (band==M380SysexParameter.PEQ_BAND1) {
			parameter = M380SysexParameter.CH_EQ_LO_FREQ;
			value = (int) ((attachment * 980) + 20);
		} else if (band==M380SysexParameter.PEQ_BAND2) {
			parameter = M380SysexParameter.CH_EQ_LOMID_FREQ;
			value = (int) ((attachment * 19980) + 20);
		} else if (band==M380SysexParameter.PEQ_BAND3) {
			parameter = M380SysexParameter.CH_EQ_HIMID_FREQ;
			value = (int) ((attachment * 19980) + 20);
		} else if (band==M380SysexParameter.PEQ_BAND4) {
			parameter = M380SysexParameter.CH_EQ_HI_FREQ;
			value = (int) ((attachment * 19000) + 1000);
		}

		this.sysex.changeInputParameterInt(channel, parameter, value);
		final String label = this.mixer.getLabelMaker().getPeqFrequencyTable(value);
		this.mixer.fireChange(new MsgInputPeqFLabel(channel, band, label));

	}

	public void changedPeqBandG(final int channel, final int band, final Float attachment) {

		byte parameter = 0;
		final short value = (short) (attachment * 150);
		final String label = this.mixer.getLabelMaker().getPeqGainLabel(attachment);

		if (band==M380SysexParameter.PEQ_BAND1) {
			parameter = M380SysexParameter.CH_EQ_LO_GAIN;
		} else if (band==M380SysexParameter.PEQ_BAND2) {
			parameter = M380SysexParameter.CH_EQ_LOMID_GAIN;
		} else if (band==M380SysexParameter.PEQ_BAND3) {
			parameter = M380SysexParameter.CH_EQ_HIMID_GAIN;
		} else if (band==M380SysexParameter.PEQ_BAND4) {
			parameter = M380SysexParameter.CH_EQ_HI_GAIN;
		}

		this.sysex.changeLargeChannelInputParameter(channel, parameter, value);
		this.mixer.fireChange(new MsgInputPeqGLabel(channel, band, label));

	}

	public void changedPeqOn(final int channel, final Boolean attachment) {
		this.sysex.changeChannelInputParameter(channel, M380SysexParameter.CH_EQ_SWITCH, attachment ? (byte) 1 : (byte) 0);

	}

}
