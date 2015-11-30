package com.stereokrauts.stereoscope.mixer.yamaha.dm2000;


import model.mixer.interfaces.IMixerWithGraphicalEq;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.stereoscope.mixer.yamaha.dm2000.messaging.GeqMessageHandler;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.GenericMidsizeMixer;
import com.stereokrauts.stereoscope.mixer.yamaha.genericmidsize.midi.GenericMidsizeSysexParameter;
import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractGeqMessage;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqTypeChanged;
import com.stereokrauts.stereoscope.plugin.interfaces.AbstractApplicationContext;

public final class DM2000Mixer extends GenericMidsizeMixer implements IMixerWithGraphicalEq {
	private static final SLogger logger = StereoscopeLogManager.getLogger("yamaha-dm2000");
	DM2000MidiTransmitter sysex;
	DM2000MixerModifier modifier;

	// i/o-specs for this mixer
	private final static int inputChannelCount = 96;
	private final static int outputAuxCount = 12;
	private final static int outputBusCount = 8;
	private final static int matrixBusCount = 4;
	private final static int outputChannelCount = 2;
	private final static int outputGeqCount = 6;
	private final DM2000Plugin pluginInstance;
	private AbstractApplicationContext ctx;

	public DM2000Plugin getPluginInstance() {
		return this.pluginInstance;
	}

	@Override
	protected void requestSamplerate() {
		this.sysex.requestSetupParameter(GenericMidsizeSysexParameter.ELMT_SETUP_DIO, GenericMidsizeSysexParameter.PARAM_DIO_CLOCKMASTER, (byte) 0);
	}

	public DM2000Mixer(final DM2000Plugin pluginInstance, final ISendMidi midi) {
		super(midi);
		this.pluginInstance = pluginInstance;
		if (pluginInstance != null) {
			this.ctx = pluginInstance.getApplContext();
		}
		this.sysex = new DM2000MidiTransmitter(this, midi);
		this.modifier = new DM2000MixerModifier(this, this.sysex);

		this.messageRelay.registerMessageHandler(AbstractGeqMessage.class, new GeqMessageHandler(this));
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
			logger.info("No message handler registered for message " + msg);
		}
	}

	@Override
	public DM2000MixerModifier getModifier() {
		return this.modifier;
	}

	@Override
	public int getChannelCount() {
		return inputChannelCount;
	}

	@Override
	public int getAuxCount() {
		return outputAuxCount;
	}

	@Override
	public int getBusCount() {
		return outputBusCount;
	}

	@Override
	public int getMatrixCount() {
		return matrixBusCount;
	}

	@Override
	public int getOutputCount() {
		return outputChannelCount;
	}

	@Override
	public int getGeqCount() {
		return outputGeqCount;
	}
	
	@Override
	public String getPluginName() {
		return this.getPluginInstance().getPluginName();
	}

	@Override
	public int getMaxOutputDelaySamples() {
		return 43400;
	}

	@Override
	protected void updateChannelNames() {
		for (int i = 0; i < this.getChannelCount(); i++) {
			this.sysex.requestChannelName((byte) i);
		}
	}

	protected void fireChangeGeqBandLevel(final short eqNumber, final boolean rightChannel, final int band,
			final float floatValue) {
		for (final IMessageReceiver m : this.getObservers()) {
			m.handleNotification(new MessageWithSender(this, new MsgGeqBandLevelChanged(eqNumber, band, rightChannel, floatValue)));
		}
	}

	@Override
	public void getGeqBandLevel(final int eqNumber, /* ignored */ final boolean isRight, final int band) {
		this.sysex.requestParameter((byte) DM2000MidiTransmitter.ELMT_GRAPHICAL_EQ,
				(byte)(DM2000MidiTransmitter.PARAM_GEQ_LEFT_FIRSTBAND + band), 
				(byte) eqNumber);
	}

	@Override
	public void getAllGeqLevels(final byte eqNumber) {
		logger.entering(this.getClass().toString(), "getAllGeqLevels");

		for (int j = 0; j < 31; j++) {
			this.getGeqBandLevel(eqNumber, false, j);
		}
	}

	@Override
	public void isFlexEQ(final short eqNumber) {
		for (final IMessageReceiver m : this.getObservers()) {
			m.handleNotification(new MessageWithSender(this, new MsgGeqTypeChanged(eqNumber, false)));
		}
	}

	public void newMessageToMixer() {
		if (this.ctx != null) {
			getCommunicationAware().receive();
		}
	}

	public void newMessageFromMixer() {
		if (this.ctx != null) {
			getCommunicationAware().transmit();
		}
	}

	@Override
	public AbstractApplicationContext getContext() {
		return ctx;
	}
}
