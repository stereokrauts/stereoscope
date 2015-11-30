package com.stereokrauts.stereoscope.mixer.roland.m380;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import model.mixer.interfaces.IAmMixer;
import model.mixer.interfaces.IMixerWithGraphicalEq;
import model.mixer.interfaces.IProvideChannelNames;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.mixer.roland.m380.messaging.ChannelAuxMessageHandler;
import com.stereokrauts.stereoscope.mixer.roland.m380.messaging.ChannelMessageHandler;
import com.stereokrauts.stereoscope.mixer.roland.m380.messaging.DcaMessageHandler;
import com.stereokrauts.stereoscope.mixer.roland.m380.messaging.GeqMessageHandler;
import com.stereokrauts.stereoscope.mixer.roland.m380.messaging.InputDynamicsMessageHandler;
import com.stereokrauts.stereoscope.mixer.roland.m380.messaging.InputMessageHandler;
import com.stereokrauts.stereoscope.mixer.roland.m380.messaging.InputPeqBandMessageHandler;
import com.stereokrauts.stereoscope.mixer.roland.m380.messaging.InternalMessageHandler;
import com.stereokrauts.stereoscope.mixer.roland.m380.messaging.MasterMessageHandler;
import com.stereokrauts.stereoscope.mixer.roland.m380.messaging.ResyncMessageHandler;
import com.stereokrauts.stereoscope.mixer.roland.m380.midi.M380MidiTransmitter;
import com.stereokrauts.stereoscope.mixer.roland.m380.midi.M380SysexParameter;
import com.stereokrauts.stereoscope.model.messaging.MessageRelay;
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
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractResyncMessage;
import com.stereokrauts.stereoscope.model.messaging.message.mixerglobal.MsgDcaLevelChanged;
import com.stereokrauts.stereoscope.plugin.interfaces.AbstractApplicationContext;

public final class M380Mixer extends IAmMixer implements IProvideChannelNames, IMixerWithGraphicalEq {

	// i/o-specs for this mixer
	private final static int inputChannelCount = 48;
	private static final SLogger logger = StereoscopeLogManager.getLogger("roland-m380");
	private final static int matrixBusCount = 8;
	private final static int outputAuxCount = 16;
	private final static int outputBusCount = outputAuxCount;
	private final static int outputChannelCount = 24;
	private final static int outputGeqCount = 4;
	private static final boolean DEBUG_FINEST = false;
	//private boolean haveRequestedChannelNames = false;

	// labelMaker is set in M380MidiReceiver
	protected M380LabelDataProvider labelMaker = new M380LabelDataProvider();
	MessageRelay messageRelay = new MessageRelay("m380");
	protected M380MidiTransmitter sysex;

	private final M380MixerModifier modifier;
	private final M380Plugin pluginInstance;


	public M380Mixer(final M380Plugin pluginInstance, final ISendMidi midi) {
		super();
		this.pluginInstance = pluginInstance;
		if (pluginInstance != null) {
			this.ctx = pluginInstance.getApplContext();
		}

		this.sysex = new M380MidiTransmitter(this, midi);
		this.modifier = new M380MixerModifier(this, this.sysex);
		this.observers = new ArrayList<IMessageReceiver>();

		this.messageRelay.registerMessageHandler(AbstractChannelMessage.class, new ChannelMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractInputMessage.class, new InputMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractChannelAuxMessage.class, new ChannelAuxMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractMasterMessage.class, new MasterMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractInputDynamicsMessage.class, new InputDynamicsMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractInputPeqBandMessage.class, new InputPeqBandMessageHandler(this));
		this.messageRelay.registerMessageHandler(AbstractGeqMessage.class, new GeqMessageHandler(this));
		this.messageRelay.registerMessageHandler(MsgDcaLevelChanged.class, new DcaMessageHandler(this));
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
		for (int i=0; i<this.getChannelCount(); i++) {
			this.getChannelOnButton(i);
		}
	}


	@Override
	public void getAllDelayTimes() {
		/*
		logger.entering(this.getClass().toString(), "getAllDelayTimes");

		for (int i = 0; i < getOutputCount(); i++) {
			sysex.requestParameter(M380SysexParameter.ELMT_OMNI_OUTPUT, M380SysexParameter.PARAM_OUTPUT_DELAY, (short) i);
			if (DEBUG_FINEST) logger.log(Level.FINER, "Requested Delay Time on Output " + i);
		}
		 */
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
		for (int i=0; i<=8; i++) {
			this.getInputGroup((byte) i);

		}
	}

	@Override
	public void getAllInputValues(final int channel) {
		// Channel Parameters
		this.getChannelLevel(channel);
		this.getChannelNames();
		this.getChannelOnButton(channel);
		this.getInputPanLevel(channel);
		// Parametric Equalizers
		for (int band = M380SysexParameter.PEQ_BAND1; band <= M380SysexParameter.PEQ_BAND4; band++) {
			this.getPeqBandParameter(band, 'G', channel);
			this.getPeqBandParameter(band, 'F', channel);
			if (band==1 || band==2) {
				this.getPeqBandParameter(band, 'Q', channel);
			}
		}

		this.getPeqOn(channel);

		// Dynamics: the numeric parameter represents the size of the request
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_GATE_SWITCH, 1, "Gate on/off");
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_COMP_SWITCH, 1, "Compressor on/off");
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_GATE_KEY_IN, 1, "Gate Key-In");
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_COMP_KEY_IN, 1, "Compressor Key-In");
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_GATE_ATTACK, 2, "Gate Attack");
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_COMP_ATTACK, 2, "Compressor Attack");
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_GATE_RANGE, 2, "Gate Range"); //gate only
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_GATE_HOLD, 2, "Gate Hold"); // gate only
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_GATE_RELEASE, 2, "Gate Release");
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_COMP_RELEASE, 2, "Compressor Release");
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_COMP_RATIO, 1, "Compressor Ratio"); // comp only
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_COMP_GAIN, 2, "Compressor Gain"); // comp only
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_COMP_KNEE, 1, "Compressor Knee"); // comp only
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_GATE_THRESHOLD, 2, "Gate Threshold");
		this.getInputDynamicsParameter(channel, M380SysexParameter.CH_COMP_THRESHOLD, 2, "Compressor Threshold");
	}

	@Override
	public int getAuxCount() {
		return outputAuxCount;
	}

	@Override
	public void getAuxMaster(final int aux) {
		final int sizeOfRequest=2;
		this.sysex.requestAuxiliarySendParameter(aux,
				M380SysexParameter.AUX_FADER_LEVEL, sizeOfRequest);

		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "AUXMASTERGET aux=" + aux);
		}
	}



	@Override
	public void getBusMaster(final int bus) {
		logger.log(Level.WARNING, "BUSMASTERGET bus=" + bus  + " - not implemented on M380 yet!");
	}

	@Override
	public int getBusCount() {
		return outputBusCount;
	}

	@Override
	public void getChannelAux(final int channel, final int aux) {
		final int sizeOfRequest=2;
		this.sysex.requestChannelAuxParameter((byte) channel, (byte) ((aux * 8) + 2), sizeOfRequest);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "AUXGET channel=" + channel + ", aux=" + aux);
		}
	}

	@Override
	public void getAuxChannelOnButton(final int channel, final int aux) {
		final int sizeOfRequest=1;
		this.sysex.requestChannelAuxParameter((byte) channel,
				(byte) (aux * 8), sizeOfRequest);

	}

	@Override
	public int getChannelCount() {
		return inputChannelCount;
	}

	@Override
	public void getChannelLevel(final int channel) {
		final int sizeOfRequest=2;
		this.sysex.requestChannelInputParameter((byte) channel, M380SysexParameter.CH_LEVEL, sizeOfRequest);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "LEVELGET channel=" + channel);
		}
	}

	@Override
	public void getChannelNames() {
		this.updateChannelNames();
	}

	@Override
	public void getChannelOnButton(final int channel) {
		final int sizeOfRequest=1;
		this.sysex.requestChannelInputParameter((byte) channel,
				M380SysexParameter.CH_MUTE, sizeOfRequest);
	}

	@Override
	public void getGeqBandLevel(final int eqNumber, final boolean rightChannel, final int band) {
		final int sizeOfRequest = 2;
		this.sysex.requestGeqParameter(eqNumber, (byte) (M380SysexParameter.GEQ_BAND1 + (band * 2)), sizeOfRequest);

	}

	@Override
	public int getGeqCount() {
		return outputGeqCount;
	}

	public void getInputDynamicsParameter(final int channel, final byte parameter,
			final int sizeOfRequest, final String nameOfParameter) {
		this.sysex.requestChannelInputParameter(channel, parameter, sizeOfRequest);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "Requested Input Dynamics " +
					(parameter - (byte) 0x30) + " " + nameOfParameter +
					" on Channel " + channel);
		}
	}

	public void getInputGroup(final byte group) {
		/*
		sysex.requestParameter(
					M380SysexParameter.ELMT_INPUT_MUTE_GROUP,
					group, (byte) 0);
		 */
	}

	public void getInputPanLevel(final int channel) {
		final byte sizeOfRequest=1;
		this.sysex.requestChannelInputParameter(channel, M380SysexParameter.CH_PAN, sizeOfRequest);
	}

	public M380LabelDataProvider getLabelMaker() {
		return this.labelMaker;
	}

	@Override
	public void getOutputMaster() {
		final int sizeOfRequest = 2;
		this.sysex.requestMainChannelParameter(M380SysexParameter.MAIN_CH_LEVEL, sizeOfRequest);		
	}

	@Override
	public int getMatrixCount() {
		return matrixBusCount;
	}

	public int getMaxOutputDelayMilliSec() {
		return 600;
	}

	@Override
	public int getOutputCount() {
		return outputChannelCount;
	}

	public void getPeqBandParameter(final int band, final char param, final int channel) {
		int sizeOfRequest = 0;
		byte parameter = 0;
		String logmsg = "";
		try {
			if (param=='G') {
				sizeOfRequest=2;
				if (band==M380SysexParameter.PEQ_BAND1) {
					parameter = M380SysexParameter.CH_EQ_LO_GAIN;
					logmsg = "Requested Input PEQ Band 1 Gain";
				} else if (band==1) {
					parameter = M380SysexParameter.CH_EQ_LOMID_GAIN;
					logmsg = "Requested Input PEQ Band 2 Gain";
				} else if (band==2) {
					parameter = M380SysexParameter.CH_EQ_HIMID_GAIN;
					logmsg = "Requested Input PEQ Band 3 Gain";
				} else if (band==3) {
					parameter = M380SysexParameter.CH_EQ_HI_GAIN;
					logmsg = "Requested Input PEQ Band 4 Gain";
				}
			} else if (param=='F') {
				sizeOfRequest=3;
				if (band==0) {
					parameter = M380SysexParameter.CH_EQ_LO_FREQ;
					logmsg = "Requested Input PEQ Band 1 Frequency";
				} else if (band==1) {
					parameter = M380SysexParameter.CH_EQ_LOMID_FREQ;
					logmsg = "Requested Input PEQ Band 2 Frequency";
				} else if (band==2) {
					parameter = M380SysexParameter.CH_EQ_HIMID_FREQ;
					logmsg = "Requested Input PEQ Band 3 Frequency";
				} else if (band==3) {
					parameter = M380SysexParameter.CH_EQ_HI_FREQ;
					logmsg = "Requested Input PEQ Band 4 Frequency";
				}
			} else if (param=='Q') {
				sizeOfRequest=2;
				if (band==1) {
					parameter = M380SysexParameter.CH_EQ_LOMID_Q;
					logmsg = "Requested Input PEQ Band 2 Quality";
				} else if (band==2) {
					parameter = M380SysexParameter.CH_EQ_HIMID_Q;
					logmsg = "Requested Input PEQ Band 3 Quality";
				}
			} else {
				throw new Exception();
			}
		} catch (final Exception e) {
			logger.info("Unsupported PEQ Parameter.");
		}

		this.sysex.requestChannelInputParameter((byte) channel, parameter, sizeOfRequest);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, logmsg + " on Channel " + channel);
		}
	}

	public void getPeqOn(final int channel) {
		final int sizeOfRequest=1;
		this.sysex.requestChannelInputParameter((byte) channel, M380SysexParameter.CH_EQ_SWITCH, sizeOfRequest);
		if (DEBUG_FINEST) {
			logger.log(Level.FINER, "Requested Input PEQ-On Status on Channel " + channel);
		}
	}

	public void getChannelName(final int channel) {
		final int sizeOfRequest=1;
		for (byte charPosition=0; charPosition<4; charPosition++) {
			this.sysex.requestChannelInputParameter((byte) channel, charPosition, sizeOfRequest);
		}

	}

	/**
	 * This methods receives all messages from the UpdateManager and distributes them
	 * to the functions in this class.
	 * @param message The message that should be handled by this function.
	 */
	@Override
	public final synchronized void handleNotification(final IMessageWithSender message) {
		final IMessage msg = message.getMessage();
		if (!this.messageRelay.handleMessage(msg)) {
			logger.info("No message handler registered for message " + msg);
		}
	}

	@Override
	public void isFlexEQ(final short eqNumber) {
	}

	public void setLabelMaker(final M380LabelDataProvider labelMaker) {
		this.labelMaker = labelMaker;
	}


	void updateChannelNames() {
		for (int i = 0; i < this.getChannelCount(); i++) {
			this.getChannelName(i);
			if (DEBUG_FINEST) {
				logger.log(Level.FINER, "Requested All Channel Names");
			}
		}
	}

	public M380MixerModifier getModifier() {
		return this.modifier;
	}

	/**
	 * This list contains all objects that are currently
	 * registered as observers of this object.
	 */
	private final ArrayList<IMessageReceiver> observers;
	private AbstractApplicationContext ctx;
	private ICommunicationAware communicationAware = new ICommunicationAware() {
		@Override
		public void transmit() {
		}

		@Override
		public void receive() {
		}
	};

	/**
	 * @return a list of the current observers of this object.
	 */
	protected final List<IMessageReceiver> getObservers() {
		return this.observers;
	}

	/**
	 * Adds a new observer to this object.
	 * @param observer The new observing object.
	 */
	@Override
	public final void registerObserver(
			final IMessageReceiver observer) {
		this.observers.add(observer);
	}

	/**
	 * Notifies all observers that an parameter which is controled through
	 * messages has changed.
	 * @param msg A message further specifying the parameter change.
	 */
	public final void fireChange(final AbstractMessage<?> msg) {
		for (final IMessageReceiver m : this.observers) {
			m.handleNotification(new MessageWithSender(this, msg));
		}
	}

	public M380Plugin getPluginInstance() {
		return this.pluginInstance;
	}

	public void newMessageToMixer() {
		this.communicationAware.receive();
	}

	public void newMessageFromMixer() {
		this.communicationAware.transmit();
	}


	@Override
	public String getPluginName() {
		return this.getPluginInstance().getPluginName();
	}

	public void registerCommunicationAware(final ICommunicationAware communicationAware) {
		this.communicationAware = communicationAware;
	}


}
