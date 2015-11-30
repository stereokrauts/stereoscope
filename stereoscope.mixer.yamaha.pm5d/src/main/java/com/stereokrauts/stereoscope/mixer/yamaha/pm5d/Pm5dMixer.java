package com.stereokrauts.stereoscope.mixer.yamaha.pm5d;

import java.util.logging.Level;

import model.mixer.interfaces.IMixerWithGraphicalEq;
import model.mixer.interfaces.IProvideChannelNames;
import model.mixer.interfaces.SampleRate;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.mixer.mixer.yamaha.common.GeneralYamahaMixer;
import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelAuxMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractGeqMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputPeqBandMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractResyncMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage.SECTION;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandReset;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqFullReset;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqTypeChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputPan;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAttack;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaAutoOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaDecayRelease;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterFreq;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterQ;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaFilterType;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaGain;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaHold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKeyIn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaKnee;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaLeftSideChain;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaOn;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRange;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRatio;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaThreshold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelAuxLevelLabel;
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
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqFilterType;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqHPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqLPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqModeChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerAuxCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerGEQCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerInputCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerMatrixBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerName;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerOutputCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerAuxCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerGEQCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerInputCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerMatrixBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerName;
import com.stereokrauts.stereoscope.model.messaging.message.internal.ResponseMixerOutputCount;
import com.stereokrauts.stereoscope.model.messaging.message.mixerglobal.MsgDcaLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncAuxSendLevels;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncAuxSendOnButtons;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncChannelLevels;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncChannelNames;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncChannelOnButtons;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncChannelStrip;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncDelayTimes;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncEverything;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncGeqBandLevels;
import com.stereokrauts.stereoscope.model.messaging.message.resync.ResyncOutputs;
import com.stereokrauts.stereoscope.plugin.interfaces.AbstractApplicationContext;

public final class Pm5dMixer extends GeneralYamahaMixer implements IProvideChannelNames, IMixerWithGraphicalEq {
	private static final SLogger LOGGER = StereoscopeLogManager.getLogger("yamaha-pm5d");
	protected Pm5dMidiTransmitter sysex;
	// labelMaker is set in Ls9MidiReceiver
	protected Pm5dLabelDataProvider labelMaker = new Pm5dLabelDataProvider();

	// i/o-specs for this mixer
	private static final int INPUT_CHANNEL_COUNT = 72;
	private static final int OUTPUT_AUX_COUNT = 16;
	/**
	 * abused for matrix delays in the PM5D - not the real
	 * bus count - pay attention....
	 */
	private static final int OUTPUT_BUS_COUNT = 8;
	private static final int MATRIX_BUS_COUNT = 8;
	private static final int OUTPUT_CHANNEL_COUNT = 32;
	private static final int OUTPUT_GEQ_COUNT = 12;

	/* Taken from PRM Table #21, Yamaha Sysex Specification */
	private static final byte WORDCLOCK_INT_441 = 0;
	private static final byte WORDCLOCK_INT_48 = 1;
	private static final byte WORDCLOCK_INT_882 = 2;
	private static final byte WORDCLOCK_INT_96 = 3;
	private static final boolean DEBUG_FINEST = false;

	private SampleRate currentSamplerate = SampleRate.R_UNDEFINED;
	private final Pm5dPlugin pluginInstance;
	private AbstractApplicationContext ctx;
	private ICommunicationAware communicationAware = new ICommunicationAware() {
		@Override
		public void transmit() {}

		@Override
		public void receive() {}
	};


	public Pm5dPlugin getPluginInstance() {
		return this.pluginInstance;
	}

	public Pm5dMixer(final Pm5dPlugin pluginInstance, final ISendMidi midi) {
		super();
		this.pluginInstance = pluginInstance;
		if (pluginInstance != null) {
			this.ctx = pluginInstance.getApplContext();
		}
		this.sysex = new Pm5dMidiTransmitter(this, midi);
	}

	protected void requestSamplerate() {
		this.sysex.requestSetupParameter(Pm5dSysexParameter.ELMT_SETUP_DIO, Pm5dSysexParameter.PARAM_DIO_CLOCKMASTER, (byte) 0);
	}

	void setSamplerate(final int value) {
		switch (value) {
		case WORDCLOCK_INT_441:
			this.currentSamplerate = SampleRate.R_44100;
			break;
		case WORDCLOCK_INT_48:
			this.currentSamplerate = SampleRate.R_48000;
			break;
		case WORDCLOCK_INT_882:
			this.currentSamplerate = SampleRate.R_88200;
			break;
		case WORDCLOCK_INT_96:
			this.currentSamplerate = SampleRate.R_96000;
			break;
		default:
			LOGGER.warning("Mixer is synchronized to external source, could not determine sample rate - assuming 44100, delay settings might be odd...");
			this.currentSamplerate = SampleRate.R_44100;
			break;
		}
	}

	public SampleRate getSamplerate() {
		return this.currentSamplerate;
	}

	/**
	 * This methods receives all messages from the UpdateManager and distributes them
	 * to the functions in this class.
	 * @param message The message that should be handled by this function.
	 */
	@Override
	public synchronized void handleNotification(final IMessageWithSender message) {
		final IMessage msg = message.getMessage();
		if (msg instanceof AbstractChannelMessage) {
			this.handleChannelMessage(msg);
		} else if (msg instanceof AbstractChannelAuxMessage) {
			this.handleChannelAuxMessage(msg);
		} else if (msg instanceof AbstractGeqMessage) {
			this.handleGeqMessage(msg);
		} else if (msg instanceof AbstractMasterMessage) {
			this.handleMasterMessage(msg);
		} else if (msg instanceof AbstractInputMessage) {
			this.handleInputMessage(msg);
		} else if (msg instanceof AbstractInputPeqBandMessage) {
			this.handlePeqBandMessage(msg);
		} else if (msg instanceof AbstractInputDynamicsMessage) {
			this.handleInputDynamicsMessage(msg);
		} else if (msg instanceof MsgDcaLevelChanged) {
			changedDcaLevel(((MsgDcaLevelChanged) msg).getDcaNumber(), (Float) msg.getAttachment());
		} else if (msg instanceof AbstractInternalMessage) {
			this.handleInternalMessage(msg);
		} else if (msg instanceof AbstractResyncMessage) {
			this.handleResyncMessage(msg);
		}
	}

	/**
	 * This methods receives all basic channel strip related
	 * messages from the function handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	private void handleInputMessage(final IMessage msg) {
		final int chn = ((AbstractInputMessage<?>) msg).getChannel();

		if (msg instanceof MsgInputPan) {
			final MsgInputPan msgInputPanning = (MsgInputPan) msg;
			final String label = this.labelMaker.getYamahaLabelPanning(msgInputPanning.getAttachment());
			this.changedInputPan(chn, msgInputPanning.getAttachment());
			this.fireChange(new MsgInputPanLabel(chn, 0, label));
		} else if (msg instanceof MsgInputPeqOnChanged) {
			final MsgInputPeqOnChanged msgPeqOnChanged = (MsgInputPeqOnChanged) msg;
			this.changedPeqOn(chn, msgPeqOnChanged.getAttachment());
		} else if (msg instanceof MsgInputPeqModeChanged) {
			final MsgInputPeqModeChanged msgPeqModeChanged = (MsgInputPeqModeChanged) msg;
			this.changedPeqMode(chn, msgPeqModeChanged.getAttachment());
		} else if (msg instanceof MsgInputPeqLPFChanged) {
			final MsgInputPeqLPFChanged msgPeqLPFChanged = (MsgInputPeqLPFChanged) msg;
			this.changedPeqLPFOn(chn, msgPeqLPFChanged.getAttachment());
		} else if (msg instanceof MsgInputPeqHPFChanged) {
			final MsgInputPeqHPFChanged msgPeqHPFChanged = (MsgInputPeqHPFChanged) msg;
			this.changedPeqHPFOn(chn, msgPeqHPFChanged.getAttachment());
		}
	}

	/**
	 * This methods receives all dynamics related messages
	 * of the current input from the function handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	private void handleInputDynamicsMessage(final IMessage msg) {
		final int chn = ((AbstractInputDynamicsMessage<?>) msg).getChannel();
		final int dyna = ((AbstractInputDynamicsMessage<?>) msg).getProcessor();
		// set element byte (dyna1 or dyna2)
		final byte element = (dyna == 0)
				? Pm5dSysexParameter.ELMT_INPUT_DYN1
						: Pm5dSysexParameter.ELMT_INPUT_DYN2;

		if (msg instanceof MsgInputDynaOn) {
			final MsgInputDynaOn msgInputDynaOn = (MsgInputDynaOn) msg;
			this.changedInputDynaOn(chn, element, msgInputDynaOn.getAttachment());
		} else if (msg instanceof MsgInputDynaAutoOn) {
			// not supported by these mixers
			return;
		} else if (msg instanceof MsgInputDynaLeftSideChain) {
			// not supported by these mixers
			return;
		} else if (msg instanceof MsgInputDynaKeyIn) {
			// just gate
			if (element == Pm5dSysexParameter.ELMT_INPUT_DYN1) {
				final MsgInputDynaKeyIn msgInputDynaKeyIn = (MsgInputDynaKeyIn) msg;
				this.changedInputDynaKeyIn(chn, msgInputDynaKeyIn.getAttachment());
			}
		} else if (msg instanceof MsgInputDynaFilterOn) {
			// not supported by these mixers
			return;
		} else if (msg instanceof MsgInputDynaFilterType) {
			// not supported by these mixers
			return;
		} else if (msg instanceof MsgInputDynaFilterFreq) {
			// not supported by these mixers
			return;
		} else if (msg instanceof MsgInputDynaFilterQ) {
			// not supported by these mixers
			return;
		} else if (msg instanceof MsgInputDynaAttack) {
			final MsgInputDynaAttack msgInputDynaAttack = (MsgInputDynaAttack) msg;
			this.changedInputDynaAttack(chn, element, msgInputDynaAttack.getAttachment());
			final String label = this.labelMaker.getYamahaLabelDynaAttack(msgInputDynaAttack.getAttachment());
			this.fireChange(new MsgInputDynaAttackLabel(dyna, chn, label));
		} else if (msg instanceof MsgInputDynaRange) {
			// just gate
			final MsgInputDynaRange msgInputDynaRange = (MsgInputDynaRange) msg;
			this.changedInputDynaRange(chn, msgInputDynaRange.getAttachment());
			final String label = this.labelMaker.getYamahaLabelDynaRange(msgInputDynaRange.getAttachment());
			this.fireChange(new MsgInputDynaRangeLabel(dyna, chn, label));
		} else if (msg instanceof MsgInputDynaHold) {
			// just gate
			final MsgInputDynaHold msgInputDynaHold = (MsgInputDynaHold) msg;
			this.changedInputDynaHold(chn, msgInputDynaHold.getAttachment());
			final String label = this.labelMaker.getYamahaLabelDynaHold(msgInputDynaHold.getAttachment());
			this.fireChange(new MsgInputDynaHoldLabel(dyna, chn, label));
		} else if (msg instanceof MsgInputDynaDecayRelease) {
			final MsgInputDynaDecayRelease msgInputDynaDecayRelease = (MsgInputDynaDecayRelease) msg;
			this.changedInputDynaReleaseDecay(chn, element, msgInputDynaDecayRelease.getAttachment());
			final String label = this.labelMaker.getYamahaLabelDynaDecayRelease(msgInputDynaDecayRelease.getAttachment());
			this.fireChange(new MsgInputDynaDecayReleaseLabel(dyna, chn, label));
		} else if (msg instanceof MsgInputDynaRatio) {
			// just compressor
			final MsgInputDynaRatio msgInputDynaRatio = (MsgInputDynaRatio) msg;
			this.changedInputDynaRatio(chn, msgInputDynaRatio.getAttachment());
			final String label = this.labelMaker.getYamahaLabelDynaRatio(msgInputDynaRatio.getAttachment());
			this.fireChange(new MsgInputDynaRatioLabel(dyna, chn, label));
		} else if (msg instanceof MsgInputDynaGain) {
			// just compressor
			final MsgInputDynaGain msgInputDynaGain = (MsgInputDynaGain) msg;
			this.changedInputDynaGain(chn, msgInputDynaGain.getAttachment());
			final String label = this.labelMaker.getYamahaLabelDynaGain(msgInputDynaGain.getAttachment());
			this.fireChange(new MsgInputDynaGainLabel(dyna, chn, label));
		} else if (msg instanceof MsgInputDynaKnee) {
			// just compressor
			final MsgInputDynaKnee msgInputDynaKnee = (MsgInputDynaKnee) msg;
			this.changedInputDynaKnee(chn, msgInputDynaKnee.getAttachment());
			final String label = this.labelMaker.getYamahaLabelDynaKnee(msgInputDynaKnee.getAttachment());
			this.fireChange(new MsgInputDynaKneeLabel(dyna, chn, label));
		} else if (msg instanceof MsgInputDynaThreshold) {
			final MsgInputDynaThreshold msgInputDynaThreshold = (MsgInputDynaThreshold) msg;
			this.changedInputDynaThreshold(chn, element, msgInputDynaThreshold.getAttachment());
			final String label = this.labelMaker.getYamahaLabelDynaThreshold(msgInputDynaThreshold.getAttachment());
			this.fireChange(new MsgInputDynaThresholdLabel(dyna, chn, label));
		}
	}

	/**
	 * This methods receives all channel aux related messages from the function
	 * handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	private void handleChannelAuxMessage(final IMessage msg) {
		if (msg instanceof MsgAuxSendChanged) {
			final MsgAuxSendChanged msgAuxSendChanged = (MsgAuxSendChanged) msg;
			final int channel = msgAuxSendChanged.getChannel();
			final int aux = msgAuxSendChanged.getAux();
			final float value = msgAuxSendChanged.getAttachment();
			this.changedChannelAuxLevel(channel, aux, value);
			final String label = labelMaker.getYamahaLabelLevel10Db(value);
			fireChange(new MsgChannelAuxLevelLabel(channel, aux, label));
		} else if (msg instanceof MsgAuxSendOnChanged) {
			final MsgAuxSendOnChanged msgAuxSendOnChanged = (MsgAuxSendOnChanged) msg;
			this.changedChannelAuxOn(msgAuxSendOnChanged.getChannel(),
					msgAuxSendOnChanged.getAux(),
					msgAuxSendOnChanged.getAttachment());
		}
	}

	/**
	 * This methods receives all channel parametric eq related
	 * messages from the function handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	private void handlePeqBandMessage(final IMessage msg) {
		final int chn = ((AbstractInputPeqBandMessage<?>) msg).getChannel();

		if (msg instanceof MsgInputPeqQ) {
			final MsgInputPeqQ msgInputPeqQ = (MsgInputPeqQ) msg;
			final int band = msgInputPeqQ.getBand();
			this.changedPeqBandQ(chn, band, msgInputPeqQ.getAttachment());
		} else if (msg instanceof MsgInputPeqF) {
			final MsgInputPeqF msgInputPeqF = (MsgInputPeqF) msg;
			final int band = msgInputPeqF.getBand();
			this.changedPeqBandF(chn, band, msgInputPeqF.getAttachment());
		} else if (msg instanceof MsgInputPeqG) {
			final MsgInputPeqG msgInputPeqG = (MsgInputPeqG) msg;
			final int band = msgInputPeqG.getBand();
			this.changedPeqBandG(chn, band, msgInputPeqG.getAttachment());
		} else if (msg instanceof MsgInputPeqFilterType) {
			final MsgInputPeqFilterType msgInputPeqFilterType = (MsgInputPeqFilterType) msg;
			final int band = msgInputPeqFilterType.getBand();
			this.changedPeqBandFilterType(chn, band, msgInputPeqFilterType.getAttachment());
		}
	}

	/**
	 * This methods receives all master section related messages from the function
	 * handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	private void handleMasterMessage(final IMessage msg) {
		final AbstractMasterMessage<?> ammsg = ((AbstractMasterMessage<?>) msg);
		final SECTION section = ammsg.getSection();
		final int number = ammsg.getNumber();
		if (section == SECTION.AUX) {
			if (msg instanceof MsgAuxMasterLevelChanged) {
				this.changedAuxMaster(number, (Float) msg.getAttachment());
			} else if (msg instanceof MsgAuxMasterDelayChanged) {
				this.changedAuxDelayTime(number, (Float) msg.getAttachment());
			}
		} else if (section == SECTION.BUS) {
			if (msg instanceof MsgBusMasterDelayChanged) {
				this.changedBusDelayTime(number, (Float) msg.getAttachment());
			}
		} else if (section == SECTION.MASTER) {
			if (msg instanceof MsgMasterLevelChanged) {
				this.changedMasterLevel((Float) msg.getAttachment());
			}
		}

	}

	/**
	 * This methods receives all channel related messages from the function
	 * handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	private void handleChannelMessage(final IMessage msg) {
		final int chn = ((AbstractChannelMessage<?>) msg).getChannel();
		if (msg instanceof MsgChannelLevelChanged) {
			final MsgChannelLevelChanged msgChannelLevelChanged = (MsgChannelLevelChanged) msg;
			this.changedChannelLevel(chn, msgChannelLevelChanged.getAttachment());
		} else if (msg instanceof MsgChannelOnChanged) {
			final MsgChannelOnChanged msgChannelOnChanged = (MsgChannelOnChanged) msg;
			this.changedChannelOnButton(chn, msgChannelOnChanged.getAttachment());
		}
	}

	/**
	 * This methods receives all graphical EQ related messages from the function
	 * handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	private void handleGeqMessage(final IMessage msg) {
		final short geqNumber = ((AbstractGeqMessage<?>) msg).getGeqNumber();
		if (msg instanceof MsgGeqFullReset) {
			this.changedGeqFullReset(geqNumber);
		} else if (msg instanceof MsgGeqBandLevelChanged) {
			final MsgGeqBandLevelChanged msgGeqBandLevelChanged = (MsgGeqBandLevelChanged) msg;
			final boolean rightChannel = msgGeqBandLevelChanged.isRightChannel();
			final int band = msgGeqBandLevelChanged.getBand();
			final float floatValue = msgGeqBandLevelChanged.getAttachment();
			this.changedGeqBandLevel(geqNumber, rightChannel, band, floatValue);
		} else if (msg instanceof MsgGeqBandReset) {
			final MsgGeqBandReset msgGeqBandReset = (MsgGeqBandReset) msg;
			final boolean rightChannel = msgGeqBandReset.isRightChannel();
			final int band = msgGeqBandReset.getBand();
			this.changedGeqBandReset(geqNumber, rightChannel, band);
		}
	}

	/**
	 * This methods receives all system internal
	 * messages from the function handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	private void handleInternalMessage(final IMessage msg) {
		if (msg instanceof RequestMixerInputCount) {
			this.fireChange(new ResponseMixerInputCount(getChannelCount(), true));
		} else if (msg instanceof RequestMixerAuxCount) {
			this.fireChange(new ResponseMixerAuxCount(getAuxCount(), true));
		} else if (msg instanceof RequestMixerGEQCount) {
			this.fireChange(new ResponseMixerGEQCount(getGeqCount(), true));
		} else if (msg instanceof RequestMixerBusCount) {
			this.fireChange(new ResponseMixerBusCount(getBusCount(), true));
		} else if (msg instanceof RequestMixerMatrixBusCount) {
			this.fireChange(new ResponseMixerMatrixBusCount(getMatrixCount(), true));
		} else if (msg instanceof RequestMixerOutputCount) {
			this.fireChange(new ResponseMixerOutputCount(getOutputCount(), true));
		} else if (msg instanceof RequestMixerName) {
			this.fireChange(new ResponseMixerName(this.getPluginName(), true));
		}
	}

	/**
	 * This methods receives all resync
	 * messages from the function handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	private void handleResyncMessage(final IMessage msg) {
		if (msg instanceof ResyncChannelLevels) {
			this.getAllChannelLevels();
		} else if (msg instanceof ResyncChannelNames) {
			this.getChannelNames();
		} else if (msg instanceof ResyncChannelOnButtons) {
			this.getAllChannelOnButtons();
		} else if (msg instanceof ResyncAuxSendLevels) {
			final int aux = (int) msg.getAttachment();
			this.getAllAuxLevels(aux);
		} else if (msg instanceof ResyncAuxSendOnButtons) {
			final int aux = (int) msg.getAttachment();
			this.getAllAuxChannelOn(aux);
		} else if (msg instanceof ResyncChannelStrip) {
			final int chn = (int) msg.getAttachment();
			this.getAllInputValues(chn);
		} else if (msg instanceof ResyncGeqBandLevels) {
			final byte geq = (byte) msg.getAttachment();
			this.getAllGeqLevels(geq);
		} else if (msg instanceof ResyncDelayTimes) {
			this.getAllDelayTimes();
		} else if (msg instanceof ResyncOutputs) {
			final int aux = ((ResyncOutputs) msg).getCurrentAux();
			this.getOutputs(aux);
		} else if (msg instanceof ResyncEverything) {
			this.getAllMixerValues((ResyncEverything) msg);
		}
	}

	private void getAllMixerValues(final ResyncEverything msg) {
		final int currentAux = msg.getCurrentAux();
		final int currentInput = msg.getCurrentInput();
		final int currentGeq = msg.getCurrentGeq();
		this.getAllChannelLevels();
		this.getChannelNames();
		this.getAllChannelOnButtons();
		this.getAllDelayTimes();
		this.getAllAuxLevels(currentAux);
		this.getAllAuxChannelOn(currentAux);
		this.getAllInputValues(currentInput);
		this.getAllGeqLevels((byte) currentGeq);
	}

	private void changedMasterLevel(final float level) {
		final short value = (short) (level * 1023);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_MASTER_LEVEL, (byte) 1, (byte) 0, value);
		LOGGER.fine("LEVELSET MASTER value=" + value);
	}

	private void changedAuxMaster(final int aux, final float level) {
		final short value = (short) (level * 1023);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_AUX_MASTER_LEVEL, (byte) 1, (byte) aux, value);
		LOGGER.fine("LEVELSET AUXMASTER aux=" + aux + ", value=" + value);
	}

	private void changedDcaLevel(final int dca, final float level) {
		final short value = (short) (level * 1023);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_LEVEL_DCA, (byte) 1, (byte) dca, value);
		LOGGER.fine("LEVELSET DCA dca=" + dca + ", value=" + value);
	}

	private void changedChannelLevel(final int channel, final float level) {
		final short value = (short) (level * 1023);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_LEVEL, (byte) 1, (byte) channel, value);
		LOGGER.fine("LEVELSET channel=" + channel + ", value=" + value);
	}

	private void changedChannelAuxLevel(final int channel, final int aux,
			final float level) {

		final short value = (short) (level * 1023);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_AUX_SEND, (byte) ((aux + 1) * 3 + 3),
				(byte) channel, value);
		LOGGER.fine("AUXSET channel=" + channel + ", aux=" + aux
				+ ", value=" + value);
	}

	private void changedChannelAuxOn(final int channel, final int aux,
			final boolean status) {
		sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_AUX_SEND,
				(byte) (aux*3 + 3), (byte) channel, status ? (byte) 1 : (byte) 0); 
	}

	private void changedChannelOnButton(final int channel, final boolean status) {
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_ON, 
				(byte) 1, (byte) channel, status ? (byte) 1 : (byte) 0);
	}

	@Override
	public void getChannelOnButton(final int channel) {
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_ON, 
				(byte) 1, (byte) channel);
	}

	@Override
	public void getChannelAux(final int channel, final int aux) {
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_AUX_SEND, (byte) ((aux + 1) * 3 + 3),
				(byte) channel);
		LOGGER.fine("AUXGET channel=" + channel + ", aux=" + aux);
	}

	@Override
	public void getAuxChannelOnButton(final int channel, final int aux) {
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_AUX_SEND,
				(byte) ((aux + 1) * 3 + 1), (byte) channel);
	}

	@Override
	public void getAuxMaster(final int aux) {
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_AUX_MASTER_LEVEL, (byte) 1, (byte) aux);
		LOGGER.fine("AUXMASTERGET aux=" + aux);
	}


	@Override
	public void getBusMaster(final int bus) {
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_LEVEL_DCA, (byte) 1, (byte) bus);
		LOGGER.fine("DCAMASTERGET aux=" + bus);
	}

	@Override
	public void getAllAuxLevels(final int aux) {
		for (int i = 0; i < this.getChannelCount(); i++) {
			this.getChannelAux(i, aux);
		}
		this.getAuxMaster(aux);
	}

	@Override
	public void getAllAuxChannelOn(final int aux) {
		for (int i = 0; i < this.getChannelCount(); i++) {
			this.getAuxChannelOnButton(i, aux);
		}

	}

	@Override
	public void getAllBusStatus() {
		for (int i = 0; i < getBusCount(); i++) {
			getBusMaster(i);
		}
	}

	@Override
	public void getChannelLevel(final int channel) {
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_LEVEL, (byte) 1, (byte) channel);
		LOGGER.fine("LEVELGET channel=" + channel);
	}

	@Override
	public void getAllChannelLevels() {
		for (int i = 0; i < this.getChannelCount(); i++) {
			this.getChannelLevel(i);
		}
		this.getOutputMaster();
	}

	@Override
	public void getAllChannelOnButtons() {
		for (int i = 0; i < this.getChannelCount(); i++) {
			this.getChannelOnButton(i);
		}
	}

	@Override
	public void getOutputMaster() {
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_MASTER_LEVEL, (byte) 1, (byte) 0);	
	}

	@Override
	public void getChannelNames() {
		this.updateChannelNames();
	}

	void updateChannelNames() {
		for (int i = 0; i < this.getChannelCount(); i++) {
			this.sysex.requestChannelName((byte) i);
		}
	}

	// rightChannel is useless here
	private void changedGeqBandLevel(final short eqNumber, final boolean rightChannel, final int band, final float level) {
		// 	see getEqBandLevel
		final long value = (long) (level*Pm5dSysexParameter.GEQ_BAND_RANGE);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_GRAPHICAL_EQ,
				(byte) (Pm5dSysexParameter.PARAM_GEQ_FIRSTBAND + band), 
				(byte) eqNumber, value);
		LOGGER.fine("GEQ Number=" + eqNumber + ", Band=" + band
				+ ", value=" + value);
	}

	void fireChangeGeqBandLevel(final short eqNumber, final boolean rightChannel, final int band,
			final float floatValue) {
		for (final IMessageReceiver m : this.getObservers()) {
			m.handleNotification(new MessageWithSender(this, new MsgGeqBandLevelChanged(eqNumber, band, rightChannel, floatValue)));
		}
	}

	private void fireChangeGeqType(final short eqNumber, final boolean isFlexEq15) {
		for (final IMessageReceiver m : this.getObservers()) {
			m.handleNotification(new MessageWithSender(this, new MsgGeqTypeChanged(eqNumber, isFlexEq15)));
		}
	}

	@Override
	public void getGeqBandLevel(final int eqNumber, final boolean rightChannel, final int band) {
		// there is just one band to select
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_GRAPHICAL_EQ,
				(byte) (Pm5dSysexParameter.PARAM_GEQ_FIRSTBAND + band), 
				(byte) eqNumber);

	}

	@Override
	public void getAllGeqLevels(final byte eqNumber) {
		for (int j = 0; j < 31; j++) {
			this.getGeqBandLevel(eqNumber, false, j);
			this.getGeqBandLevel(eqNumber, true, j);
		}
	}

	private void changedGeqFullReset(final short eqNumber) {
		final float zeroValue = 0;
		boolean channel;
		for (int i = 0; i < 62; i++) {
			if (i < 31) {
				channel = false;
				this.changedGeqBandLevel(eqNumber, channel, i, zeroValue);
			} else {
				channel = false;
				this.changedGeqBandLevel(eqNumber, channel, i, zeroValue);
			}
		}

	}

	private void changedGeqBandReset(final short eqNumber, final boolean rightChannel, final int band) {
		this.changedGeqBandLevel(eqNumber, rightChannel, band, 0.0f);
	}

	private void changedAuxDelayTime(final int aux, final float delayTime) {
		final int delayMiliseconds = Math.min(this.getMaxDelayMiliseconds(),
				(int) (delayTime * 1000));
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_AUX_DELAY, 
				Pm5dSysexParameter.PARAM_AUX_DELAY_TIME, (byte) aux, delayMiliseconds*100);
	}

	private int getMaxDelayMiliseconds() {
		return 1000;
	}

	private void changedBusDelayTime(final int busNumber, final float delayTime) {
		final int delayMiliseconds = Math.min(this.getMaxDelayMiliseconds(),
				(int) (delayTime * 1000));
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_MATRIX_DELAY, 
				Pm5dSysexParameter.PARAM_MATRIX_DELAY_TIME, (byte) busNumber, delayMiliseconds*100);
	}

	@Override
	public void getAllDelayTimes() {
		LOGGER.entering(this.getClass().toString(), "getAllDelayTimes");

		for (int i = 0; i < this.getAuxCount(); i++) {
			this.sysex.requestParameter(Pm5dSysexParameter.ELMT_AUX_DELAY, Pm5dSysexParameter.PARAM_AUX_DELAY_TIME, (byte) i);
		}
		for (int i = 0; i < this.getBusCount(); i++) {
			this.sysex.requestParameter(Pm5dSysexParameter.ELMT_MATRIX_DELAY, Pm5dSysexParameter.PARAM_MATRIX_DELAY_TIME, (byte) i);
		}
	}

	private void getOutputs(final int aux) {
		this.getOutputMaster();
		this.getAuxMaster(aux);
	}

	@Override
	public void isFlexEQ(final short eqNumber) {
		this.fireChangeGeqType(eqNumber, false);
	}

	@Override
	public void getAllGroupsStatus() {};

	public void changedInputPan(final int chn, final float value) {
		final int panning = (int) (value * 63);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_PAN,
				Pm5dSysexParameter.PARAM_INPUT_PAN, (byte) chn, panning);
	}

	public void getInputPanLevel(final int channel) {
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_PAN, Pm5dSysexParameter.PARAM_INPUT_PAN, (byte) channel);
		// OSCremote.println("LEVELGET channel=" + channel);
	}

	public void changedPeqBandG(final int chn, final int band, final Float attachment) {

		byte parameter = 0;
		final int value = (int) (attachment * 180);
		final String label = this.labelMaker.getYamahaLabelPeqG(attachment);

		if (band == Pm5dSysexParameter.PEQ_BAND1) {
			parameter = Pm5dSysexParameter.PARAM_PEQ_LOWG;
		} else if (band == Pm5dSysexParameter.PEQ_BAND2) {
			parameter = Pm5dSysexParameter.PARAM_PEQ_LOWMIDG;
		} else if (band == Pm5dSysexParameter.PEQ_BAND3) {
			parameter = Pm5dSysexParameter.PARAM_PEQ_HIMIDG;
		} else if (band == Pm5dSysexParameter.PEQ_BAND4) {
			parameter = Pm5dSysexParameter.PARAM_PEQ_HIG;
		}

		LOGGER.info("Peq Parameter changed: " + parameter);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_PEQ,
				parameter, (byte) chn, value);
		this.fireChange(new MsgInputPeqGLabel(chn, band, label));
	}

	public void changedPeqBandF(final int chn, final int band, final Float attachment) {

		byte parameter = 0;
		final int value = (int) (attachment * 119) + 5;
		final String label = this.labelMaker.getYamahaLabelPeqF(value);

		if (band == Pm5dSysexParameter.PEQ_BAND1) {
			parameter = Pm5dSysexParameter.PARAM_PEQ_LOWF;
		} else if (band == Pm5dSysexParameter.PEQ_BAND2) {
			parameter = Pm5dSysexParameter.PARAM_PEQ_LOWMIDF;
		} else if (band == Pm5dSysexParameter.PEQ_BAND3) {
			parameter = Pm5dSysexParameter.PARAM_PEQ_HIMIDF;
		} else if (band == Pm5dSysexParameter.PEQ_BAND4) {
			parameter = Pm5dSysexParameter.PARAM_PEQ_HIF;
		}

		LOGGER.info("Peq Parameter changed: " + parameter);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_PEQ,
				parameter, (byte) chn, value);
		this.fireChange(new MsgInputPeqFLabel(chn, band, label));
	}

	public void changedPeqBandQ(final int chn, final int band, final Float attachment) {

		byte parameter = 0;
		final int value = (int) (attachment * 44) - 4;
		final String label = this.labelMaker.getYamahaLabelPeqQ(value);

		if (band == Pm5dSysexParameter.PEQ_BAND1) {
			parameter = Pm5dSysexParameter.PARAM_PEQ_LOWQ;
		} else if (band == Pm5dSysexParameter.PEQ_BAND2) {
			parameter = Pm5dSysexParameter.PARAM_PEQ_LOWMIDQ;
		} else if (band == Pm5dSysexParameter.PEQ_BAND3) {
			parameter = Pm5dSysexParameter.PARAM_PEQ_HIMIDQ;
		} else if (band == Pm5dSysexParameter.PEQ_BAND4) {
			parameter = Pm5dSysexParameter.PARAM_PEQ_HIQ;
		}

		LOGGER.info("Peq Parameter changed: " + parameter);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_PEQ,
				parameter, (byte) chn, value);
		this.fireChange(new MsgInputPeqQLabel(chn, band, label));
	}

	public void changedPeqBandFilterType(final int chn, final int band, final int attachment) {

		byte parameter = 0;
		final int mixerValue = attachment + 41;
		final String label = this.labelMaker.getYamahaLabelPeqQ(attachment);

		if (band == Pm5dSysexParameter.PEQ_BAND1) {
			parameter = Pm5dSysexParameter.PARAM_PEQ_LOWQ;
		} else if (band == Pm5dSysexParameter.PEQ_BAND4) {
			parameter = Pm5dSysexParameter.PARAM_PEQ_HIQ;
		}


		LOGGER.info("Peq Parameter changed: " + parameter);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_PEQ,
				parameter, (byte) chn, mixerValue);
		this.fireChange(new MsgInputPeqQLabel(chn, band, label));
	}



	public void changedPeqMode(final int chn, final Boolean attachment) {
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_PEQ, 
				Pm5dSysexParameter.PARAM_PEQ_MODE, (byte) chn, attachment ? (byte) 1 : (byte) 0);

	}

	public void changedPeqHPFOn(final int chn, final Boolean attachment) {
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_PEQ, 
				Pm5dSysexParameter.PARAM_PEQ_HPFON, (byte) chn, attachment ? (byte) 1 : (byte) 0);

	}

	public void changedPeqLPFOn(final int chn, final Boolean attachment) {
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_PEQ, 
				Pm5dSysexParameter.PARAM_PEQ_LPF0N, (byte) chn, attachment ? (byte) 1 : (byte) 0);

	}

	public void changedPeqOn(final int chn, final Boolean attachment) {
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_PEQ, 
				Pm5dSysexParameter.PARAM_PEQ_EQON, (byte) chn, attachment ? (byte) 1 : (byte) 0);		
	}

	public void getPeqBandParameter(final int band, final char param, final int channel) {
		byte parameter = 0;
		String logmsg = "";
		try {
			if (param == 'G') {
				if (band == 0) {
					parameter = Pm5dSysexParameter.PARAM_PEQ_LOWG;
					logmsg = "Requested Input PEQ Band 1 Gain";
				} else if (band == 1) {
					parameter = Pm5dSysexParameter.PARAM_PEQ_LOWMIDG;
					logmsg = "Requested Input PEQ Band 2 Gain";
				} else if (band == 2) {
					parameter = Pm5dSysexParameter.PARAM_PEQ_HIMIDG;
					logmsg = "Requested Input PEQ Band 3 Gain";
				} else if (band == 3) {
					parameter = Pm5dSysexParameter.PARAM_PEQ_HIG;
					logmsg = "Requested Input PEQ Band 4 Gain";
				}
			} else if (param == 'F') {
				if (band == 0) {
					parameter = Pm5dSysexParameter.PARAM_PEQ_LOWF;
					logmsg = "Requested Input PEQ Band 1 Frequency";
				} else if (band == 1) {
					parameter = Pm5dSysexParameter.PARAM_PEQ_LOWMIDF;
					logmsg = "Requested Input PEQ Band 2 Frequency";
				} else if (band == 2) {
					parameter = Pm5dSysexParameter.PARAM_PEQ_HIMIDF;
					logmsg = "Requested Input PEQ Band 3 Frequency";
				} else if (band == 3) {
					parameter = Pm5dSysexParameter.PARAM_PEQ_HIF;
					logmsg = "Requested Input PEQ Band 4 Frequency";
				}
			} else if (param == 'Q') {
				if (band == 0) {
					parameter = Pm5dSysexParameter.PARAM_PEQ_LOWQ;
					logmsg = "Requested Input PEQ Band 1 Quality";
				} else if (band == 1) {
					parameter = Pm5dSysexParameter.PARAM_PEQ_LOWMIDQ;
					logmsg = "Requested Input PEQ Band 2 Quality";
				} else if (band == 2) {
					parameter = Pm5dSysexParameter.PARAM_PEQ_HIMIDQ;
					logmsg = "Requested Input PEQ Band 3 Quality";
				} else if (band == 3) {
					parameter = Pm5dSysexParameter.PARAM_PEQ_HIQ;
					logmsg = "Requested Input PEQ Band 4 Quality";
				}
			} else {
				throw new Exception();
			}
		} catch (final Exception e) {
			LOGGER.info("Unsupported PEQ Parameter.");
		}

		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_PEQ,
				parameter, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, logmsg + " on Channel " + channel);
		}
	}

	public void getPeqMode(final int channel) {
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_PEQ,
				Pm5dSysexParameter.PARAM_PEQ_MODE, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input PEQ Mode on Channel " + channel);
		}
	}

	public void getPeqHPFOn(final int channel) {
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_PEQ,
				Pm5dSysexParameter.PARAM_PEQ_HPFON, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input PEQ HPF Status on Channel " + channel);
		}
	}

	public void getPeqLPFOn(final int channel) {
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_PEQ,
				Pm5dSysexParameter.PARAM_PEQ_LPF0N, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input PEQ LPF Status on Channel " + channel);
		}
	}

	public void getPeqOn(final int channel) {
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_PEQ,
				Pm5dSysexParameter.PARAM_PEQ_EQON, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input PEQ-On Status on Channel " + channel);
		}
	}

	public void changedInputDynaOn(final int chn, final byte element,
			final Boolean value) {
		if (element == Pm5dSysexParameter.ELMT_INPUT_DYN1) {
			this.sysex.changeParameter(element,
					Pm5dSysexParameter.PARAM_INPUT_GATE_ON,
					(byte) chn, value ? (byte) 1 : (byte) 0);
		} else {
			this.sysex.changeParameter(element,
					Pm5dSysexParameter.PARAM_INPUT_COMP_ON,
					(byte) chn, value ? (byte) 1 : (byte) 0);
		}
	}

	// not verified for 1.5
	public void changedInputDynaType(final int chn, final byte element,
			final float value) {
		if (element == Pm5dSysexParameter.ELMT_INPUT_DYN1) {
			final int type = (int) (value * 5);
			this.sysex.changeParameter(element,
					Pm5dSysexParameter.PARAM_INPUT_GATE_TYPE,
					(byte) chn, type);
		} else {
			final int type = (int) (value * 3);
			this.sysex.changeParameter(element,
					Pm5dSysexParameter.PARAM_INPUT_COMP_TYPE,
					(byte) chn, type);
		}

	}

	// not verified for 1.5
	public void changedInputDynaKeyIn(final int chn, final float value) {
		// only Gate/Ducking, no check needed
		final int key = (int) (value * 2);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_DYN1,
				Pm5dSysexParameter.PARAM_INPUT_GATE_KEY_IN,
				(byte) chn, key);

	}

	public void changedInputDynaAttack(final int chn, final byte element, final float value) {
		final int attack = (int) (value * 120);
		if (element == Pm5dSysexParameter.ELMT_INPUT_DYN1) {
			this.sysex.changeParameter(element,
					Pm5dSysexParameter.PARAM_INPUT_GATE_ATTACK,
					(byte) chn, attack);
		} else {
			this.sysex.changeParameter(element,
					Pm5dSysexParameter.PARAM_INPUT_COMP_ATTACK,
					(byte) chn, attack);
		}

	}

	public void changedInputDynaRange(final int chn, final float value) {
		// only Gate/Ducking, no check needed
		// value<0: model.protocol.osc.handler.OscInputChannelStrip[...]
		final int range = (int) (value * 70) - (70);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_DYN1,
				Pm5dSysexParameter.PARAM_INPUT_GATE_RANGE,
				(byte) chn, range);

	}

	public void changedInputDynaHold(final int chn, final float value) {
		// Hold is only available for Gate/Ducking, no check needed
		final int hold = (int) (value * 215);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_DYN1,
				Pm5dSysexParameter.PARAM_INPUT_GATE_HOLD,
				(byte) chn, hold);

	}

	public void changedInputDynaReleaseDecay(final int chn, final byte element, final float value) {
		// Decay is for gates, release for compressors/expanders
		final int dr = (int) (value * 159);
		if (element == Pm5dSysexParameter.ELMT_INPUT_DYN1) {
			this.sysex.changeParameter(element,
					Pm5dSysexParameter.PARAM_INPUT_GATE_DECAY,
					(byte) chn, dr);
		} else {
			this.sysex.changeParameter(element,
					Pm5dSysexParameter.PARAM_INPUT_COMP_RELEASE,
					(byte) chn, dr);
		}

	}

	public void changedInputDynaRatio(final int chn, final float value) {
		// Ratio is only available for Compressor/Expander, no check needed
		final int ratio = (int) (value * 15);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_DYN2,
				Pm5dSysexParameter.PARAM_INPUT_COMP_RATIO,
				(byte) chn, ratio);

	}

	public void changedInputDynaGain(final int chn, final float value) {
		// Gain is only available for Compressor/Expander, no check needed
		final int gain = (int) (value * 180);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_DYN2,
				Pm5dSysexParameter.PARAM_INPUT_COMP_GAIN,
				(byte) chn, gain);

	}

	public void changedInputDynaKnee(final int chn, final float value) {
		// only Compressor/Expander, no check needed
		// knee-range in the sysex spec is 0-89 (but it's actually 0-5)
		final int knee = (int) (value * 5);
		this.sysex.changeParameter(Pm5dSysexParameter.ELMT_INPUT_DYN2,
				Pm5dSysexParameter.PARAM_INPUT_COMP_KNEE,
				(byte) chn, knee);

	}

	public void changedInputDynaThreshold(final int chn, final byte element, final float value) {
		// value<0: model.protocol.osc.handler.OscInputChannelStrip[...]
		if (element == Pm5dSysexParameter.ELMT_INPUT_DYN1) {
			final int threshold = (int) (value * (Pm5dSysexParameter.DYNAMICS_GATE_THRESHOLD_VALUECOUNT - 1)) - (Pm5dSysexParameter.DYNAMICS_GATE_THRESHOLD_VALUECOUNT - 1);

			this.sysex.changeParameter(element,
					Pm5dSysexParameter.PARAM_INPUT_GATE_THRESHOLD,
					(byte) chn, threshold);
		} else {
			final int threshold = (int) (value * (Pm5dSysexParameter.DYNAMICS_COMP_THRESHOLD_VALUECOUNT - 1)) - (Pm5dSysexParameter.DYNAMICS_COMP_THRESHOLD_VALUECOUNT - 1);

			this.sysex.changeParameter(element,
					Pm5dSysexParameter.PARAM_INPUT_COMP_THRESHOLD,
					(byte) chn, threshold);
		}

	}

	public void getInputDynaOn(final int channel, final boolean isGateComp) {
		if (isGateComp) {
			this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN1, 
					Pm5dSysexParameter.PARAM_INPUT_GATE_ON, (byte) channel);
			if (DEBUG_FINEST) {
				LOGGER.log(Level.FINER, "Requested Input Gate On/Off Status on Channel " + channel);
			}
		} else {
			this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN2,
					Pm5dSysexParameter.PARAM_INPUT_COMP_ON, (byte) channel);
			if (DEBUG_FINEST) {
				LOGGER.log(Level.FINER, "Requested Input Compressor On/Off Status on Channel " + channel);
			}
		}
	}

	public void getInputDynaType(final int channel, final boolean isGateComp) {
		if (isGateComp) {
			this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN1,
					Pm5dSysexParameter.PARAM_INPUT_GATE_TYPE, (byte) channel);
			if (DEBUG_FINEST) {
				LOGGER.log(Level.FINER, "Requested Input Gate Type on Channel " + channel);
			}
		} else {
			this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN2,
					Pm5dSysexParameter.PARAM_INPUT_COMP_TYPE, (byte) channel);
			if (DEBUG_FINEST) {
				LOGGER.log(Level.FINER, "Requested Input Compressor Type on Channel " + channel);
			}
		}
	}

	public void getInputDynaKeyIn(final int channel) {
		// just gates
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN1,
				Pm5dSysexParameter.PARAM_INPUT_GATE_KEY_IN, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Gate KeyIn Status on Channel " + channel);
		}
	}

	public void getInputDynaAttack(final int channel, final boolean isGateComp) {
		if (isGateComp) {
			this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN1,
					Pm5dSysexParameter.PARAM_INPUT_GATE_ATTACK, (byte) channel);
			if (DEBUG_FINEST) {
				LOGGER.log(Level.FINER, "Requested Input Gate Attack on Channel " + channel);
			}
		} else {
			this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN2,
					Pm5dSysexParameter.PARAM_INPUT_COMP_ATTACK, (byte) channel);
			if (DEBUG_FINEST) {
				LOGGER.log(Level.FINER, "Requested Input Compressor Attack on Channel " + channel);
			}
		}
	}

	public void getInputDynaRange(final int channel) {
		// just gates
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN1,
				Pm5dSysexParameter.PARAM_INPUT_GATE_RANGE, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Gate Range on Channel " + channel);
		}
	}

	public void getInputDynaHold(final int channel) {
		// just gates
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN1,
				Pm5dSysexParameter.PARAM_INPUT_GATE_HOLD, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Gate Hold on Channel " + channel);
		}
	}

	public void getInputDynaReleaseDecay(final int channel, final boolean isGateComp) {
		// release is for compressors, decay for gates
		if (isGateComp) {
			this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN1,
					Pm5dSysexParameter.PARAM_INPUT_GATE_DECAY, (byte) channel);
			if (DEBUG_FINEST) {
				LOGGER.log(Level.FINER, "Requested Input Gate Decay on Channel " + channel);
			}
		} else {
			this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN2,
					Pm5dSysexParameter.PARAM_INPUT_COMP_RELEASE, (byte) channel);
			if (DEBUG_FINEST) {
				LOGGER.log(Level.FINER, "Requested Input Compressor Release on Channel " + channel);
			}
		}
	}

	public void getInputDynaRatio(final int channel) {
		// just compressors
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN2,
				Pm5dSysexParameter.PARAM_INPUT_COMP_RATIO, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Compressor Ratio on Channel " + channel);
		}
	}

	public void getInputDynaGain(final int channel) {
		// just compressors
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN2,
				Pm5dSysexParameter.PARAM_INPUT_COMP_GAIN, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Compressor Gain on Channel " + channel);
		}
	}

	public void getInputDynaKnee(final int channel) {
		// just compressors
		this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN2,
				Pm5dSysexParameter.PARAM_INPUT_COMP_KNEE, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Compressor Knee on Channel " + channel);
		}
	}

	public void getInputDynaThreshold(final int channel, final boolean isGateComp) {
		if (isGateComp) {
			this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN1,
					Pm5dSysexParameter.PARAM_INPUT_GATE_THRESHOLD, (byte) channel);
			if (DEBUG_FINEST) {
				LOGGER.log(Level.FINER, "Requested Input Gate Threshold on Channel " + channel);
			}
		} else {
			this.sysex.requestParameter(Pm5dSysexParameter.ELMT_INPUT_DYN2,
					Pm5dSysexParameter.PARAM_INPUT_COMP_THRESHOLD, (byte) channel);
			if (DEBUG_FINEST) {
				LOGGER.log(Level.FINER, "Requested Input Compressor Threshold on Channel " + channel);
			}
		}
	}

	@Override
	public void getAllInputValues(final int chn) {
		// Channel Parameters
		this.getChannelLevel(chn);
		this.getChannelNames();
		this.getChannelOnButton(chn);
		this.getInputPanLevel(chn);
		// Parametric Equalizers
		for (int i = Pm5dSysexParameter.PEQ_BAND1; i <= Pm5dSysexParameter.PEQ_BAND4; i++) {
			this.getPeqBandParameter(i, 'G', chn);
			this.getPeqBandParameter(i, 'F', chn);
			this.getPeqBandParameter(i, 'Q', chn);
		}
		this.getPeqMode(chn);
		this.getPeqHPFOn(chn);
		this.getPeqLPFOn(chn);
		this.getPeqOn(chn);
		// Dynamics: Gate and Compressor
		this.getInputDynaOn(chn, false);
		this.getInputDynaOn(chn, true);
		this.getInputDynaType(chn, false);
		this.getInputDynaType(chn, true);
		this.getInputDynaKeyIn(chn); // gate only
		this.getInputDynaAttack(chn, false);
		this.getInputDynaAttack(chn, true);
		this.getInputDynaRange(chn); // gate only
		this.getInputDynaHold(chn); // gate only
		this.getInputDynaReleaseDecay(chn, false); // gate decay
		this.getInputDynaReleaseDecay(chn, true); // comp release
		this.getInputDynaRatio(chn); // comp only
		this.getInputDynaGain(chn); // comp only
		this.getInputDynaKnee(chn); // comp only
		this.getInputDynaThreshold(chn, false);
		this.getInputDynaThreshold(chn, true);
	}

	@Override
	public int getChannelCount() {
		return INPUT_CHANNEL_COUNT;
	}

	@Override
	public int getAuxCount() {
		return OUTPUT_AUX_COUNT;
	}

	@Override
	public int getBusCount() {
		return OUTPUT_BUS_COUNT;
	}

	@Override
	public int getMatrixCount() {
		return MATRIX_BUS_COUNT;
	}

	@Override
	public int getOutputCount() {
		return OUTPUT_CHANNEL_COUNT;
	}

	@Override
	public int getGeqCount() {
		return OUTPUT_GEQ_COUNT;
	}

	@Override
	public String getPluginName() {
		return this.getPluginInstance().getPluginName();
	}


	public void newMessageToMixer() {
		getCommunicationAware().receive();
	}

	public void newMessageFromMixer() {
		getCommunicationAware().transmit();
	}


	public void registerCommunicationAware(final ICommunicationAware communicationAware) {
		this.communicationAware = communicationAware;
	}

	public ICommunicationAware getCommunicationAware() {
		return this.communicationAware;
	}
}
