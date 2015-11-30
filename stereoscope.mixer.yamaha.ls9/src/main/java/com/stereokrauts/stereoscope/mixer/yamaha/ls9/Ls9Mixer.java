package com.stereokrauts.stereoscope.mixer.yamaha.ls9;


import java.util.logging.Level;

import model.mixer.interfaces.IMixerWithGraphicalEq;
import model.mixer.interfaces.IProvideChannelNames;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.mixer.mixer.yamaha.common.GeneralYamahaMixer;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging.ChannelAuxMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging.ChannelMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging.GeqMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging.GroupMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging.InputDynamicsMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging.InputMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging.InputPeqBandMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging.InternalMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging.MasterMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.messaging.ResyncMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.midi.Ls9MidiTransmitter;
import com.stereokrauts.stereoscope.mixer.yamaha.ls9.midi.Ls9SysexParameter;
import com.stereokrauts.stereoscope.model.messaging.MessageRelay;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelAuxMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractGeqMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractGroupMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputPeqBandMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractResyncMessage;

public final class Ls9Mixer extends GeneralYamahaMixer implements IProvideChannelNames, IMixerWithGraphicalEq {
	private static final SLogger LOGGER = StereoscopeLogManager.getLogger("yamaha-ls9");

	// i/o-specs for this mixer
	private static final int INPUT_CHANNEL_COUNT = 64;
	private static final int MATRIX_BUS_COUNT = 8;

	/**
	 * On the LS-9 all busses are realised as FIXED
	 * auxes and are thus counted as auxes in stereoscope.
	 */
	private static final int OUTPUT_AUX_COUNT = 16;
	private static final int OUTPUT_BUS_COUNT = OUTPUT_AUX_COUNT;
	private static final int OUTPUT_CHANNEL_COUNT = 16;
	private static final int OUTPUT_GEQ_COUNT = 8;
	private int currentSamplerate = 0;
	private boolean haveRequestedChannelNames = false;

	// labelMaker is set in Ls9MidiReceiver
	private Ls9LabelDataProvider labelMaker = new Ls9LabelDataProvider();
	MessageRelay messageRelay = new MessageRelay("ls9");
	protected Ls9MidiTransmitter sysex;

	private final Ls9MixerModifier modifier;

	/* Taken from PRM Table #21, Yamaha Sysex Specification */
	private static final byte WORDCLOCK_INT_441 = 0;
	private static final byte WORDCLOCK_INT_48 = 1;
	private static final byte WORDCLOCK_INT_882 = 2;
	private static final byte WORDCLOCK_INT_96 = 3;

	private static final boolean DEBUG_FINEST = false;

	private final Ls9Plugin pluginInstance;
	private ICommunicationAware communicationAware = new ICommunicationAware() {
		@Override
		public void transmit() {}

		@Override
		public void receive() {}
	};
	;

	public Ls9Plugin getPluginInstance() {
		return this.pluginInstance;
	}

	public Ls9Mixer(final Ls9Plugin pluginInstance, final ISendMidi midi) {
		super();
		this.pluginInstance = pluginInstance;
		this.sysex = new Ls9MidiTransmitter(this, midi);
		this.modifier = new Ls9MixerModifier(this, this.sysex);

		this.messageRelay.registerMessageHandler(AbstractChannelMessage.class, new ChannelMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractInputMessage.class, new InputMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractChannelAuxMessage.class, new ChannelAuxMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractMasterMessage.class, new MasterMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractInputDynamicsMessage.class, new InputDynamicsMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractInputPeqBandMessage.class, new InputPeqBandMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractGroupMessage.class, new GroupMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractGeqMessage.class, new GeqMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractInternalMessage.class, new InternalMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractResyncMessage.class, new ResyncMessageHandler(this));
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
	public void getAllChannelLevels() {
		for (int i = 0; i < this.getChannelCount(); i++) {
			this.getChannelLevel(i);
		}
		this.getOutputMaster();
	}

	@Override
	public void getAllChannelOnButtons() {
		for (int i = 0; i < getChannelCount(); i++) {
			this.getChannelOnButton(i);
		}
	}

	@Override
	public void getAllDelayTimes() {
		LOGGER.entering(this.getClass().toString(), "getAllDelayTimes");

		for (int i = 0; i < this.getOutputCount(); i++) {
			this.sysex.requestParameter(Ls9SysexParameter.ELMT_OMNI_OUTPUT, Ls9SysexParameter.PARAM_OUTPUT_DELAY, (short) i);
			if (DEBUG_FINEST) {
				LOGGER.log(Level.FINER, "Requested Delay Time on Output " + i);
			}
		}
	}

	@Override
	public void getAllGeqLevels(final byte eqNumber) {
		for (int j = 0; j < 31; j++) {
			this.getGeqBandLevel(eqNumber, false, j);
			this.getGeqBandLevel(eqNumber, true, j);
		}
	}

	@Override
	public void getAllGroupsStatus() {
		for (int i = 0; i <= 8; i++) {
			this.getInputGroup((byte) i);

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
		for (int i = Ls9SysexParameter.PEQ_BAND1; i <= Ls9SysexParameter.PEQ_BAND4; i++) {
			this.getPeqBandParameter(i, 'G', chn);
			this.getPeqBandParameter(i, 'F', chn);
			this.getPeqBandParameter(i, 'Q', chn);
		}
		this.getPeqMode(chn);
		this.getPeqHPFOn(chn);
		this.getPeqLPFOn(chn);
		this.getPeqOn(chn);
		// Dynamics: Gate and Compressor
		final byte dyna1 = Ls9SysexParameter.ELMT_INPUT_DYNA1;
		final byte dyna2 = Ls9SysexParameter.ELMT_INPUT_DYNA2;
		this.getInputDynaOn(chn, dyna1);
		this.getInputDynaOn(chn, dyna2);
		this.getInputDynaType(chn, dyna1);
		this.getInputDynaType(chn, dyna2);
		this.getInputDynaKeyIn(chn, dyna1);
		this.getInputDynaKeyIn(chn, dyna2);
		this.getInputDynaAttack(chn, dyna1);
		this.getInputDynaAttack(chn, dyna2);
		this.getInputDynaRange(chn, dyna1); //gate: dyna1 only
		this.getInputDynaHold(chn, dyna1); // gate: dyna1 only
		this.getInputDynaReleaseDecay(chn, dyna1); // gate decay
		this.getInputDynaReleaseDecay(chn, dyna2); // comp release
		this.getInputDynaRatio(chn, dyna1); // comp only
		this.getInputDynaRatio(chn, dyna2);
		this.getInputDynaGain(chn, dyna1); // comp only
		this.getInputDynaGain(chn, dyna2);
		this.getInputDynaKnee(chn, dyna1); // comp only
		this.getInputDynaKnee(chn, dyna2);
		this.getInputDynaThreshold(chn, dyna1);
		this.getInputDynaThreshold(chn, dyna2);
	}

	@Override
	public int getAuxCount() {
		return OUTPUT_AUX_COUNT;
	}

	@Override
	public void getAuxMaster(final int aux) {
		this.sysex.requestParameter(Ls9SysexParameter.ELMT_AUX_MASTER_LEVEL, (short) 0, (short) aux);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "AUXMASTERGET aux=" + aux);
		}
	}

	@Override
	public void getBusMaster(final int bus) {
		// ignore this message, mixer has no busses
	}

	@Override
	public int getBusCount() {
		return OUTPUT_BUS_COUNT;
	}

	@Override
	public void getChannelAux(final int channel, final int aux) {
		this.sysex.requestParameter(Ls9SysexParameter.ELMT_INPUT_AUX_SEND, (short) ((aux + 1) * 3 + 2),
				(short) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "AUXGET channel=" + channel + ", aux=" + aux);
		}
	}

	@Override
	public void getAuxChannelOnButton(final int channel, final int aux) {
		this.sysex.requestParameter(Ls9SysexParameter.ELMT_INPUT_AUX_SEND,
				(short) ((aux + 1) * 3), (short) channel);
	}

	@Override
	public int getChannelCount() {
		return INPUT_CHANNEL_COUNT;
	}

	@Override
	public void getChannelLevel(final int channel) {
		this.sysex.requestParameter(Ls9SysexParameter.ELMT_INPUT_LEVEL, (short) 0, (short) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "LEVELGET channel=" + channel);
		}
	}

	@Override
	public void getChannelNames() {
		if (!this.haveRequestedChannelNames) {
			this.updateChannelNames();
			this.haveRequestedChannelNames = true;
		}
	}

	@Override
	public void getChannelOnButton(final int channel) {
		this.sysex.requestParameter(Ls9SysexParameter.ELMT_INPUT_ON, 
				(short) 0, (short) channel);
	}

	@Override
	public void getGeqBandLevel(final int eqNumber, final boolean rightChannel, final int band) {
		if (!rightChannel) {
			// left band selected
			this.sysex.requestParameter(Ls9SysexParameter.ELMT_GRAPHICAL_EQ,
					(short) (Ls9SysexParameter.PARAM_GEQ_LEFT_FIRSTBAND + band), 
					(short) eqNumber);
		} else {
			this.sysex.requestParameter(Ls9SysexParameter.ELMT_GRAPHICAL_EQ,
					(short) (Ls9SysexParameter.PARAM_GEQ_RIGHT_FIRSTBAND + band), 
					(short) eqNumber);
		}		
	}

	@Override
	public int getGeqCount() {
		return OUTPUT_GEQ_COUNT;
	}

	public void getInputDynaAttack(final int channel, final byte element) {
		this.sysex.requestParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_ATTACK, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Dynamics " +
					(element - (byte) 0x36) + " Attack on Channel " + channel);
		}
	}

	public void getInputDynaGain(final int channel, final byte element) {
		// just compressors
		this.sysex.requestParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_GAIN, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Dynamics " +
					(element - (byte) 0x36) + " Compressor Gain on Channel " + channel);
		}
	}

	public void getInputDynaHold(final int channel, final byte element) {
		// just gates
		this.sysex.requestParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_HOLD, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Dynamics " +
					(element - (byte) 0x36) + " Gate Hold on Channel " + channel);
		}
	}

	public void getInputDynaKeyIn(final int channel, final byte element) {
		// just gates
		this.sysex.requestParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_KEY_IN, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Dynamics " +
					(element - (byte) 0x36) + " Gate KeyIn Status on Channel " + channel);
		}
	}

	public void getInputDynaKnee(final int channel, final byte element) {
		// just compressors
		this.sysex.requestParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_KNEE, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Dynamics " +
					(element - (byte) 0x36) + " Compressor Knee on Channel " + channel);
		}
	}

	public void getInputDynaOn(final int channel, final byte element) {
		this.sysex.requestParameter(element, 
				Ls9SysexParameter.PARAM_INPUT_DYNA_ON, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Dynamics " + 
					(element - (byte) 0x36) + " On/Off Status on Channel " + channel);
		}
	}

	public void getInputDynaRange(final int channel, final byte element) {
		// just gates
		this.sysex.requestParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_RANGE, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Dynamics " + 
					(element - (byte) 0x36) + " Gate Range on Channel " + channel);
		}
	}

	public void getInputDynaRatio(final int channel, final byte element) {
		// just compressors
		this.sysex.requestParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_RATIO, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Dynamics " +
					(element - (byte) 0x36) + " Compressor Ratio on Channel " + channel);
		}
	}

	public void getInputDynaReleaseDecay(final int channel, final byte element) {
		// release is for compressors, decay for gates
		this.sysex.requestParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_DECAY_RELEASE, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Dynamics " +
					(element - (byte) 0x36) + 
					"Decay/Release on Channel " + channel);
		}
	}

	public void getInputDynaThreshold(final int channel, final byte element) {
		this.sysex.requestParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_THRESHOLD, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Dynamics " +
					(element - (byte) 0x36) + " Threshold on Channel " + channel);
		}
	}

	public void getInputDynaType(final int channel, final byte element) {
		this.sysex.requestParameter(element,
				Ls9SysexParameter.PARAM_INPUT_DYNA_TYPE, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input Dynamics " +
					(element - (byte) 0x36) + " Type on Channel " + channel);
		}
	}

	public void getInputGroup(final byte group) {
		this.sysex.requestParameter(
				Ls9SysexParameter.ELMT_INPUT_MUTE_GROUP,
				group, (byte) 0);
	}

	public void getInputPanLevel(final int channel) {
		this.sysex.requestParameter(Ls9SysexParameter.ELMT_INPUT_PAN, Ls9SysexParameter.PARAM_INPUT_PAN, (byte) channel);
		// OSCremote.println("LEVELGET channel=" + channel);
	}

	public Ls9LabelDataProvider getLabelMaker() {
		return this.labelMaker;
	}

	@Override
	public void getOutputMaster() {
		this.sysex.requestParameter(Ls9SysexParameter.ELMT_MASTER_LEVEL, (short) 0, (short) 0);		
	}

	@Override
	public int getMatrixCount() {
		return MATRIX_BUS_COUNT;
	}

	public int getMaxOutputDelayMilliSec() {
		return 600;
	}

	@Override
	public int getOutputCount() {
		return OUTPUT_CHANNEL_COUNT;
	}

	public void getPeqBandParameter(final int band, final char param, final int channel) {
		byte parameter = 0;
		String logmsg = "";
		try {
			if (param == 'G') {
				if (band == Ls9SysexParameter.PEQ_BAND1) {
					parameter = Ls9SysexParameter.PARAM_PEQ_LOWG;
					logmsg = "Requested Input PEQ Band 1 Gain";
				} else if (band == 1) {
					parameter = Ls9SysexParameter.PARAM_PEQ_LOWMIDG;
					logmsg = "Requested Input PEQ Band 2 Gain";
				} else if (band == 2) {
					parameter = Ls9SysexParameter.PARAM_PEQ_HIMIDG;
					logmsg = "Requested Input PEQ Band 3 Gain";
				} else if (band == 3) {
					parameter = Ls9SysexParameter.PARAM_PEQ_HIG;
					logmsg = "Requested Input PEQ Band 4 Gain";
				}
			} else if (param == 'F') {
				if (band == 0) {
					parameter = Ls9SysexParameter.PARAM_PEQ_LOWF;
					logmsg = "Requested Input PEQ Band 1 Frequency";
				} else if (band == 1) {
					parameter = Ls9SysexParameter.PARAM_PEQ_LOWMIDF;
					logmsg = "Requested Input PEQ Band 2 Frequency";
				} else if (band == 2) {
					parameter = Ls9SysexParameter.PARAM_PEQ_HIMIDF;
					logmsg = "Requested Input PEQ Band 3 Frequency";
				} else if (band == 3) {
					parameter = Ls9SysexParameter.PARAM_PEQ_HIF;
					logmsg = "Requested Input PEQ Band 4 Frequency";
				}
			} else if (param == 'Q') {
				if (band == 0) {
					parameter = Ls9SysexParameter.PARAM_PEQ_LOWQ;
					logmsg = "Requested Input PEQ Band 1 Quality";
				} else if (band == 1) {
					parameter = Ls9SysexParameter.PARAM_PEQ_LOWMIDQ;
					logmsg = "Requested Input PEQ Band 2 Quality";
				} else if (band == 2) {
					parameter = Ls9SysexParameter.PARAM_PEQ_HIMIDQ;
					logmsg = "Requested Input PEQ Band 3 Quality";
				} else if (band == 3) {
					parameter = Ls9SysexParameter.PARAM_PEQ_HIQ;
					logmsg = "Requested Input PEQ Band 4 Quality";
				}
			} else {
				throw new Exception();
			}
		} catch (final Exception e) {
			LOGGER.info("Unsupported PEQ Parameter.");
		}

		this.sysex.requestParameter(Ls9SysexParameter.ELMT_INPUT_PEQ,
				parameter, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, logmsg + " on Channel " + channel);
		}
	}

	public void getPeqHPFOn(final int channel) {
		this.sysex.requestParameter(Ls9SysexParameter.ELMT_INPUT_PEQ,
				Ls9SysexParameter.PARAM_PEQ_HPFON, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input PEQ HPF Status on Channel " + channel);
		}
	}

	public void getPeqLPFOn(final int channel) {
		this.sysex.requestParameter(Ls9SysexParameter.ELMT_INPUT_PEQ,
				Ls9SysexParameter.PARAM_PEQ_LPF0N, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input PEQ LPF Status on Channel " + channel);
		}
	}

	public void getPeqMode(final int channel) {
		this.sysex.requestParameter(Ls9SysexParameter.ELMT_INPUT_PEQ,
				Ls9SysexParameter.PARAM_PEQ_MODE, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input PEQ Mode on Channel " + channel);
		}
	}

	public void getPeqOn(final int channel) {
		this.sysex.requestParameter(Ls9SysexParameter.ELMT_INPUT_PEQ,
				Ls9SysexParameter.PARAM_PEQ_EQON, (byte) channel);
		if (DEBUG_FINEST) {
			LOGGER.log(Level.FINER, "Requested Input PEQ-On Status on Channel " + channel);
		}
	}

	public int getSamplerate() {
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
		if (!this.messageRelay.handleMessage(msg)) {
			LOGGER.info("No message handler registered for message " + msg);
		}
	}

	@Override
	public void isFlexEQ(final short eqNumber) {
		this.sysex.requestParameter(Ls9SysexParameter.ELMT_GRAPHICAL_EQ,
				Ls9SysexParameter.PARAM_GEQ_ISFLEXEQ,
				eqNumber);
	}

	protected void requestSamplerate() {
		this.sysex.requestSetupParameter(Ls9SysexParameter.ELMT_SETUP_DIO,
				Ls9SysexParameter.PARAM_DIO_CLOCKMASTER, (short) 0);
	}

	public void setLabelMaker(final Ls9LabelDataProvider labelMaker) {
		this.labelMaker = labelMaker;
	}

	public void setSamplerate(final long value) {
		System.out.println(value);
		switch ((int) value) {
		case WORDCLOCK_INT_441:
			this.currentSamplerate = 44100;
			break;
		case WORDCLOCK_INT_48:
			this.currentSamplerate = 48000;
			break;
		case WORDCLOCK_INT_882:
			this.currentSamplerate = 88200;
			break;
		case WORDCLOCK_INT_96:
			this.currentSamplerate = 96000;
			break;
		default:
			LOGGER.warning("Mixer is synchronized to external source, could not determine sample rate - assuming 44100, some settings might be odd...");
			this.currentSamplerate = 44100;
			break;
		}
	}

	void updateChannelNames() {
		for (int i = 0; i < this.getChannelCount(); i++) {
			this.sysex.requestChannelName((short) i);
			if (DEBUG_FINEST) {
				LOGGER.log(Level.FINER, "Requested All Channel Names");
			}
		}
	}

	public Ls9MixerModifier getModifier() {
		return this.modifier;
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

	@Override
	public String getPluginName() {
		return this.getPluginInstance().getPluginName();
	}

}
