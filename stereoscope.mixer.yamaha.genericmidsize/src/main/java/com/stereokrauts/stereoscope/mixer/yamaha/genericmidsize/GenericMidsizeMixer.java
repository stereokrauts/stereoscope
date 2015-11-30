package com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize;


import java.util.logging.Level;

import model.mixer.interfaces.IProvideChannelNames;
import model.mixer.interfaces.SampleRate;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.mixer.mixer.yamaha.common.GeneralYamahaMixer;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.messaging.ChannelAuxMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.messaging.ChannelMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.messaging.GroupMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.messaging.InputDynamicsMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.messaging.InputMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.messaging.InputPeqBandMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.messaging.InternalMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.messaging.MasterMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.messaging.ResyncMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeMidiTransmitter;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeSysexParameter;
import com.stereokrauts.stereoscope.model.messaging.MessageRelay;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelAuxMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractGroupMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputPeqBandMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractResyncMessage;
import com.stereokrauts.stereoscope.plugin.interfaces.AbstractApplicationContext;

public abstract class GenericMidsizeMixer extends GeneralYamahaMixer implements IProvideChannelNames {
	private static final SLogger LOG = StereoscopeLogManager.getLogger("yamaha-generic-midsize");

	protected GenericMidsizeMidiTransmitter sysex;
	private final GenericMidsizeMixerModifier modifier;
	protected GenericMidsizeLabelDataProvider labelMaker = new GenericMidsizeLabelDataProvider();
	public MessageRelay messageRelay = new MessageRelay("genericmidsize");

	/* Taken from PRM Table #33, Yamaha Sysex Specification */
	private static final byte WORDCLOCK_INT_441 = 0;
	private static final byte WORDCLOCK_INT_48 = 1;
	private static final byte WORDCLOCK_INT_882 = 2;
	private static final byte WORDCLOCK_INT_96 = 3;

	private static final boolean DEBUG_FINEST = false;
	private SampleRate currentSamplerate = SampleRate.R_UNDEFINED;

	public GenericMidsizeMixer(final ISendMidi midi) {
		super();
		this.sysex = new GenericMidsizeMidiTransmitter(this, midi);
		this.modifier = new GenericMidsizeMixerModifier(this, this.sysex);		

		this.channelNames = new String[this.getChannelCount()];

		for (int i = 0; i < this.channelNames.length; i++) {
			if (i < 9) {
				this.channelNames[i] = "CH0" + (i + 1);
			} else {
				this.channelNames[i] = "CH" + (i + 1);
			}
		}

		this.messageRelay.registerMessageHandler(AbstractChannelMessage.class, new ChannelMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractInputMessage.class, new InputMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractChannelAuxMessage.class, new ChannelAuxMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractMasterMessage.class, new MasterMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractInputDynamicsMessage.class, new InputDynamicsMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractInputPeqBandMessage.class, new InputPeqBandMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractGroupMessage.class, new GroupMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractInternalMessage.class, new InternalMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractResyncMessage.class, new ResyncMessageHandler(this));
	}

	/**
	 * This methods receives all messages from the UpdateManager and distributes them
	 * to the approriate message handler.
	 * @param message The message that should be handled by this function.
	 */
	@Override
	public synchronized void handleNotification(final IMessageWithSender message) {
		final IMessage msg = message.getMessage();
		if (!this.messageRelay.handleMessage(msg)) {
			LOG.info("No message handler registered for message " + msg);
		}
	}

	public GenericMidsizeLabelDataProvider getLabelMaker() {
		return this.labelMaker;
	}

	public GenericMidsizeMixerModifier getModifier() {
		return this.modifier;
	}

	@Override
	public void getChannelOnButton(final int channel) {
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_ON, 
				(byte) 0, (byte) channel);
	}

	@Override
	public void getAuxChannelOnButton(final int channel, final int aux) {
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_AUX,
				(byte) (aux * 3), (byte) channel);
	}

	@Override
	public void getChannelAux(final int channel, final int aux) {
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_AUX, (byte) ((aux + 1) * 3 - 1), (byte) channel);
		// OSCremote.println("AUXGET channel=" + channel + ", aux=" + aux);
	}

	@Override
	public void getAuxMaster(final int aux) {
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_AUX_FADER, (byte) (0), (byte) aux);
		// OSCremote.println("AUXMASTERGET aux=" + aux);
	}

	@Override
	public void getAllAuxLevels(final int aux) {
		LOG.entering(this.getClass().toString(), "getAllAuxLevels");

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
	public void getAllChannelOnButtons() {
		LOG.entering(this.getClass().toString(), "getAllChannelOnButtons");

		for (int i = 0; i < this.getChannelCount(); i++) {
			this.getChannelOnButton(i);
		}
	}

	/* The old mixers need some caching, as they only return the
	 * channel names one character at a time. This makes the whole
	 * thing a bit ugly, the following functions are for "internal use"
	 * only, which is why they are package-private.
	 */
	String channelNames[];

	private ICommunicationAware communicationAware = new ICommunicationAware() {
		@Override
		public void transmit() {}

		@Override
		public void receive() {}
	};

	public void updateChannelName(final byte channel, final byte pos,
			final char name) {
		this.channelNames[channel] = this.channelNames[channel].substring(0, pos) + name
				+ this.channelNames[channel].substring(pos + 1, 4);
	}

	public String getInternalChannelName(final int channel) {
		return this.channelNames[channel];
	}

	protected void updateChannelNames() {
		LOG.entering(this.getClass().toString(), "updateChannelNames");

		for (int i = 0; i < this.getChannelCount(); i++) {
			this.sysex.requestChannelName((byte) i);
		}
	}

	/** This is the public function to request channel names
	 * (non-Javadoc)
	 * @see model.mixer.interfaces.IProvideChannelNames#getChannelNames()
	 */
	@Override
	public void getChannelNames() {
		this.updateChannelNames();
	}

	@Override
	public void getChannelLevel(final int channel) {
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_FADER, (byte) 0, (byte) channel);
	}

	@Override
	public void getOutputMaster() {
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_STEREO_FADER, (byte) 0, (byte) 0);		
	}

	@Override
	public void getAllChannelLevels() {
		LOG.entering(this.getClass().toString(), "getAllChannelLevels");
		for (int i = 0; i < this.getChannelCount(); i++) {
			this.getChannelLevel(i);
		}
		this.getOutputMaster();
	}

	@Override
	public void getAllDelayTimes() {
		LOG.entering(this.getClass().toString(), "getAllDelayTimes");

		for (int i = 0; i < this.getAuxCount(); i++) {
			this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_AUX_DELAY, (byte) 1, (byte) i);
		}
		for (int i = 0; i < this.getBusCount(); i++) {
			this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_BUS_DELAY, (byte) 1, (byte) i);
		}
	}

	public void getInputGroup(final byte group) {
		this.sysex.requestParameter(
				GenericMidsizeSysexParameter.ELMT_INPUT_GROUP,
				group, (byte) 0);
	}

	public void getOutputGroup(final byte group) {
		this.sysex.requestParameter(
				GenericMidsizeSysexParameter.ELMT_INPUT_GROUP,
				group, (byte) 0);
	}

	@Override
	public void getAllGroupsStatus() {
		for (int i=0; i<=27; i++) {
			this.getInputGroup((byte) i);
			if (i <= 18) {
				this.getOutputGroup((byte) i);
			}
		}
	}

	public void getInputPanLevel(final int channel) {
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PAN, (byte) 0, (byte) channel);
	}

	public void getPeqBandParameter(final int band, final char param, final int channel) {
		byte parameter = 0;
		String logmsg = "";
		try {
			if (param=='G') {
				if (band==GenericMidsizeSysexParameter.PEQ_LOW_BAND) {
					parameter = GenericMidsizeSysexParameter.PARAM_PEQ_LOWG;
					logmsg = "Requested Input PEQ Band 1 Gain";
				} else if (band==GenericMidsizeSysexParameter.PEQ_LOWMID_BAND) {
					parameter = GenericMidsizeSysexParameter.PARAM_PEQ_LOWMIDG;
					logmsg = "Requested Input PEQ Band 2 Gain";
				} else if (band==GenericMidsizeSysexParameter.PEQ_HIMID_BAND) {
					parameter = GenericMidsizeSysexParameter.PARAM_PEQ_HIMIDG;
					logmsg = "Requested Input PEQ Band 3 Gain";
				} else if (band==GenericMidsizeSysexParameter.PEQ_HI_BAND) {
					parameter = GenericMidsizeSysexParameter.PARAM_PEQ_HIG;
					logmsg = "Requested Input PEQ Band 4 Gain";
				}
			} else if (param=='F') {
				if (band==GenericMidsizeSysexParameter.PEQ_LOW_BAND) {
					parameter = GenericMidsizeSysexParameter.PARAM_PEQ_LOWF;
					logmsg = "Requested Input PEQ Band 1 Frequency";
				} else if (band==GenericMidsizeSysexParameter.PEQ_LOWMID_BAND) {
					parameter = GenericMidsizeSysexParameter.PARAM_PEQ_LOWMIDF;
					logmsg = "Requested Input PEQ Band 2 Frequency";
				} else if (band==GenericMidsizeSysexParameter.PEQ_HIMID_BAND) {
					parameter = GenericMidsizeSysexParameter.PARAM_PEQ_HIMIDF;
					logmsg = "Requested Input PEQ Band 3 Frequency";
				} else if (band==GenericMidsizeSysexParameter.PEQ_HI_BAND) {
					parameter = GenericMidsizeSysexParameter.PARAM_PEQ_HIF;
					logmsg = "Requested Input PEQ Band 4 Frequency";
				}
			} else if (param=='Q') {
				if (band==GenericMidsizeSysexParameter.PEQ_LOW_BAND) {
					parameter = GenericMidsizeSysexParameter.PARAM_PEQ_LOWQ;
					logmsg = "Requested Input PEQ Band 1 Quality";
				} else if (band==GenericMidsizeSysexParameter.PEQ_LOWMID_BAND) {
					parameter = GenericMidsizeSysexParameter.PARAM_PEQ_LOWMIDQ;
					logmsg = "Requested Input PEQ Band 2 Quality";
				} else if (band==GenericMidsizeSysexParameter.PEQ_HIMID_BAND) {
					parameter = GenericMidsizeSysexParameter.PARAM_PEQ_HIMIDQ;
					logmsg = "Requested Input PEQ Band 3 Quality";
				} else if (band==GenericMidsizeSysexParameter.PEQ_HI_BAND) {
					parameter = GenericMidsizeSysexParameter.PARAM_PEQ_HIQ;
					logmsg = "Requested Input PEQ Band 4 Quality";
				}
			} else {
				throw new Exception();
			}
		} catch (final Exception e) {
			LOG.info("Unsupported PEQ Parameter.");
		}

		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PEQ,
				parameter, (byte) channel);
		if (DEBUG_FINEST) {
			LOG.log(Level.FINEST, logmsg + " on Channel " + channel);
		}
	}

	public void getPeqMode(final int channel) {
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PEQ,
				GenericMidsizeSysexParameter.PARAM_PEQ_MODE, (byte) channel);
		if (DEBUG_FINEST) {
			LOG.log(Level.FINEST, "Requested Input PEQ Mode on Channel " + channel);
		}
	}

	public void getPeqHPFOn(final int channel) {
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PEQ,
				GenericMidsizeSysexParameter.PARAM_PEQ_HPFON, (byte) channel);
		if (DEBUG_FINEST) {
			LOG.log(Level.FINEST, "Requested Input PEQ HPF Status on Channel " + channel);
		}
	}

	public void getPeqLPFOn(final int channel) {
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PEQ,
				GenericMidsizeSysexParameter.PARAM_PEQ_LPF0N, (byte) channel);
		if (DEBUG_FINEST) {
			LOG.log(Level.FINEST, "Requested Input PEQ LPF Status on Channel " + channel);
		}
	}

	public void getPeqOn(final int channel) {
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_PEQ,
				GenericMidsizeSysexParameter.PARAM_PEQ_EQON, (byte) channel);
		if (DEBUG_FINEST) {
			LOG.log(Level.FINEST, "Requested Input PEQ-On Status on Channel " + channel);
		}
	}

	public void getInputDynaOn(final int channel, final boolean isGateComp) {
		if (isGateComp == true) {
			this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_GATE, 
					GenericMidsizeSysexParameter.PARAM_INPUT_GATE_ON, (byte) channel);
			if (DEBUG_FINEST) {
				LOG.log(Level.FINEST, "Requested Input Gate On/Off Status on Channel " + channel);
			}
		} else {
			this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_COMP,
					GenericMidsizeSysexParameter.PARAM_INPUT_COMP_ON, (byte) channel);
			if (DEBUG_FINEST) {
				LOG.log(Level.FINEST, "Requested Input Compressor On/Off Status on Channel " + channel);
			}
		}
	}

	public void getInputDynaType(final int channel, final boolean isGateComp) {
		if (isGateComp == true) {
			this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_GATE,
					GenericMidsizeSysexParameter.PARAM_INPUT_GATE_TYPE, (byte) channel);
			if (DEBUG_FINEST) {
				LOG.log(Level.FINEST, "Requested Input Gate Type on Channel " + channel);
			}
		} else {
			this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_COMP,
					GenericMidsizeSysexParameter.PARAM_INPUT_COMP_TYPE, (byte) channel);
			if (DEBUG_FINEST) {
				LOG.log(Level.FINEST, "Requested Input Compressor Type on Channel " + channel);
			}
		}
	}

	public void getInputDynaKeyIn(final int channel) {
		// just gates
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_GATE,
				GenericMidsizeSysexParameter.PARAM_INPUT_GATE_KEY_IN, (byte) channel);
		if (DEBUG_FINEST) {
			LOG.log(Level.FINEST, "Requested Input Gate KeyIn Status on Channel " + channel);
		}
	}

	public void getInputDynaAttack(final int channel, final boolean isGateComp) {
		if (isGateComp == true) {
			this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_GATE,
					GenericMidsizeSysexParameter.PARAM_INPUT_GATE_ATTACK, (byte) channel);
			if (DEBUG_FINEST) {
				LOG.log(Level.FINEST, "Requested Input Gate Attack on Channel " + channel);
			}
		} else {
			this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_COMP,
					GenericMidsizeSysexParameter.PARAM_INPUT_COMP_ATTACK, (byte) channel);
			if (DEBUG_FINEST) {
				LOG.log(Level.FINEST, "Requested Input Compressor Attack on Channel " + channel);
			}
		}
	}

	public void getInputDynaRange(final int channel) {
		// just gates
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_GATE,
				GenericMidsizeSysexParameter.PARAM_INPUT_GATE_RANGE, (byte) channel);
		if (DEBUG_FINEST) {
			LOG.log(Level.FINEST, "Requested Input Gate Range on Channel " + channel);
		}
	}

	public void getInputDynaHold(final int channel) {
		// just gates
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_GATE,
				GenericMidsizeSysexParameter.PARAM_INPUT_GATE_HOLD, (byte) channel);
		if (DEBUG_FINEST) {
			LOG.log(Level.FINEST, "Requested Input Gate Hold on Channel " + channel);
		}
	}

	public void getInputDynaReleaseDecay(final int channel, final boolean isGateComp) {
		// release is for compressors, decay for gates
		if (isGateComp == true) {
			this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_GATE,
					GenericMidsizeSysexParameter.PARAM_INPUT_GATE_DECAY, (byte) channel);
			if (DEBUG_FINEST) {
				LOG.log(Level.FINEST, "Requested Input Gate Decay on Channel " + channel);
			}
		} else {
			this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_COMP,
					GenericMidsizeSysexParameter.PARAM_INPUT_COMP_RELEASE, (byte) channel);
			if (DEBUG_FINEST) {
				LOG.log(Level.FINEST, "Requested Input Compressor Release on Channel " + channel);
			}
		}
	}

	public void getInputDynaRatio(final int channel) {
		// just compressors
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_COMP,
				GenericMidsizeSysexParameter.PARAM_INPUT_COMP_RATIO, (byte) channel);
		if (DEBUG_FINEST) {
			LOG.log(Level.FINEST, "Requested Input Compressor Ratio on Channel " + channel);
		}
	}

	public void getInputDynaGain(final int channel) {
		// just compressors
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_COMP,
				GenericMidsizeSysexParameter.PARAM_INPUT_COMP_GAIN, (byte) channel);
		if (DEBUG_FINEST) {
			LOG.log(Level.FINEST, "Requested Input Compressor Gain on Channel " + channel);
		}
	}

	public void getInputDynaKnee(final int channel) {
		// just compressors
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_COMP,
				GenericMidsizeSysexParameter.PARAM_INPUT_COMP_KNEE, (byte) channel);
		if (DEBUG_FINEST) {
			LOG.log(Level.FINEST, "Requested Input Compressor Knee on Channel " + channel);
		}
	}

	public void getInputDynaThreshold(final int channel, final boolean isGateComp) {
		if (isGateComp == true) {
			this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_GATE,
					GenericMidsizeSysexParameter.PARAM_INPUT_GATE_THRESHOLD, (byte) channel);
			if (DEBUG_FINEST) {
				LOG.log(Level.FINEST, "Requested Input Gate Threshold on Channel " + channel);
			}
		} else {
			this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_INPUT_COMP,
					GenericMidsizeSysexParameter.PARAM_INPUT_COMP_THRESHOLD, (byte) channel);
			if (DEBUG_FINEST) {
				LOG.log(Level.FINEST, "Requested Input Compressor Threshold on Channel " + channel);
			}
		}
	}

	@Override
	public void getAllInputValues(final int chn) {
		LOG.entering(this.getClass().toString(), "getAllInputValues");
		// Channel Parameters
		this.getChannelLevel(chn);
		this.getChannelNames();
		this.getChannelOnButton(chn);
		this.getInputPanLevel(chn);
		// Parametric Equalizers
		for (int i = GenericMidsizeSysexParameter.PEQ_LOW_BAND; i <= GenericMidsizeSysexParameter.PEQ_HI_BAND; i++) {
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
		throw new UnsupportedOperationException("This method MUST be "
				+" overwritten in all subclasses!");
	}

	@Override
	public int getAuxCount() {
		throw new UnsupportedOperationException("This method MUST be "
				+" overwritten in all subclasses!");
	}

	@Override
	public int getBusCount() {
		throw new UnsupportedOperationException("This method MUST be "
				+" overwritten in all subclasses!");
	}

	@Override
	public int getMatrixCount() {
		throw new UnsupportedOperationException("This method MUST be "
				+" overwritten in all subclasses!");
	}

	@Override
	public int getOutputCount() {
		throw new UnsupportedOperationException("This method MUST be "
				+" overwritten in all subclasses!");
	}

	@Override
	public int getGeqCount() {
		throw new UnsupportedOperationException("This method MUST be "
				+" overwritten in all subclasses!");
	}

	@Override
	public String getPluginName() {
		throw new UnsupportedOperationException("This method MUST be "
				+" overwritten in all subclasses!");
	}

	public void getAllGeqLevels(final byte geq) {
		throw new UnsupportedOperationException("This method MUST be "
				+" overwritten in all subclasses!");
	}

	public SampleRate getSamplerate() {
		return this.currentSamplerate;
	}

	public int getMaxOutputDelaySamples() {
		return 29100;
	}

	protected void requestSamplerate() {
		throw new UnsupportedOperationException("This method must be overridden by the mixer plugin.");
	}

	public void setSamplerate(final int value) {
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
			LOG.warning("Mixer is synchronized to external source, could not determine sample rate - assuming 44100, delay settings might be odd...");
			this.currentSamplerate = SampleRate.R_44100;
			break;
		}	
	}

	public void setLabelMaker(final GenericMidsizeLabelDataProvider labelMaker) {
		this.labelMaker = labelMaker;
	}

	@Override
	public void getBusMaster(final int bus) {
		this.sysex.requestParameter(GenericMidsizeSysexParameter.ELMT_BUS_FADER, (byte) 0, (byte) bus);
	}

	public abstract AbstractApplicationContext getContext();

	public void registerCommunicationAware(final ICommunicationAware communicationAware) {
		this.communicationAware = communicationAware;
	}

	public ICommunicationAware getCommunicationAware() {
		return this.communicationAware;
	}
}

