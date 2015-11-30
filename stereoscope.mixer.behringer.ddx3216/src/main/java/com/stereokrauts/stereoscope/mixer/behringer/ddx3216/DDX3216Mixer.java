package com.stereokrauts.stereoscope.mixer.behringer.ddx3216;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import model.mixer.interfaces.IAmMixer;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.lib.midi.api.ISendMidi;
import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelAuxMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputPeqBandMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage.SECTION;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractResyncMessage;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputPan;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelAuxLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPanLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqFLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqGLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgInputPeqQLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqF;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqHPFChanged;
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
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterLevelChanged;
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

public final class DDX3216Mixer extends IAmMixer {
	private static final int BEHRINGER_FADER_STEPS = 1496;

	private static final SLogger logger = StereoscopeLogManager.getLogger("behringer-ddx3216");

	// labelMaker is set in DDX3216MidiReceiver
	protected DDX3216LabelDataProvider labelMaker = new DDX3216LabelDataProvider();

	DDX3216MidiTransmitter sysex;

	// i/o-specs for this mixer
	private static final int INPUT_CHANNEL_COUNT = 32;
	private static final int OUTPUT_AUX_COUNT = 4;
	private static final int OUTPUT_BUS_COUNT = 16;
	private static final int MATRIX_BUS_COUNT = 24;
	private static final int OUTPUT_CHANNEL_COUNT = 8;
	private static final int OUTPUT_GEQ_COUNT = 0;

	private final DDX3216Plugin pluginInstance;


	public DDX3216Mixer(final DDX3216Plugin pluginInstance, final ISendMidi midi) {
		super();
		this.pluginInstance = pluginInstance;
		this.ctx = pluginInstance.getApplContext();
		this.sysex = new DDX3216MidiTransmitter(this, midi);
		this.observers = new ArrayList<IMessageReceiver>();
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
		} else if (msg instanceof AbstractMasterMessage) {
			this.handleMasterMessage(msg);
		} else if (msg instanceof AbstractInputMessage) {
			this.handleInputMessage(msg);
		} else if (msg instanceof AbstractInputPeqBandMessage) {
			this.handleInputPeqBandMessage(msg);
			//} else if (msg instanceof AbstractInputDynamicsMessage) {
			//handleInputDynamicsMessage(msg);
			//} else if (msg instanceof AbstractGroupMessage) {
			//handleGroupMessage(msg);
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
			final String label = this.labelMaker.getLabelPan(msgInputPanning.getAttachment());
			this.changedInputPan(chn, msgInputPanning.getAttachment());
			this.fireChange(new MsgInputPanLabel(chn, 0, label));
		} else if (msg instanceof MsgInputPeqOnChanged) {
			final MsgInputPeqOnChanged msgPeqOnChanged = (MsgInputPeqOnChanged) msg;
			this.changedPeqOn(chn, msgPeqOnChanged.getAttachment());
		} else if (msg instanceof MsgInputPeqHPFChanged) {
			final MsgInputPeqHPFChanged msgPeqHPFChanged = (MsgInputPeqHPFChanged) msg;
			this.changedPeqHPFOn(chn, msgPeqHPFChanged.getAttachment());
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
			final Float value = msgAuxSendChanged.getAttachment();
			final String label = this.labelMaker.getLabelLevel(value);
			this.changedChannelAuxLevel(channel, aux, value);
			this.fireChange(new MsgChannelAuxLevelLabel(channel, aux, label));

		}
	}

	/**
	 * This methods receives all channel parametric eq related
	 * messages from the function handleNotification.
	 * @param msg The message that should be handled by this function.
	 */
	private void handleInputPeqBandMessage(final IMessage msg) {
		final int chn = ((AbstractInputPeqBandMessage<?>) msg).getChannel();

		if (msg instanceof MsgInputPeqQ) {
			final MsgInputPeqQ msgInputPeqQ = (MsgInputPeqQ) msg;
			final int band = msgInputPeqQ.getBand();
			final String label = this.labelMaker.getLabelPeqQ(msgInputPeqQ.getAttachment());
			this.changedPeqBand(chn, band, 'Q', msgInputPeqQ.getAttachment());
			this.fireChange(new MsgInputPeqQLabel(chn, band, label));
		} else if (msg instanceof MsgInputPeqF) {
			final MsgInputPeqF msgInputPeqF = (MsgInputPeqF) msg;
			final int band = msgInputPeqF.getBand();
			final String label = this.labelMaker.getLabelPeqF(msgInputPeqF.getAttachment());
			this.changedPeqBand(chn, band, 'F', msgInputPeqF.getAttachment());
			this.fireChange(new MsgInputPeqFLabel(chn, band, label));
		} else if (msg instanceof MsgInputPeqG) {
			final MsgInputPeqG msgInputPeqG = (MsgInputPeqG) msg;
			final int band = msgInputPeqG.getBand();
			final String label = this.labelMaker.getLabelPeqG(msgInputPeqG.getAttachment());
			this.changedPeqBand(chn, band, 'G', msgInputPeqG.getAttachment());
			this.fireChange(new MsgInputPeqGLabel(chn, band, label));
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
			}
		} else if (section == SECTION.BUS) {
			if (msg instanceof MsgBusMasterLevelChanged) {
				changedBusMaster(number, (Float) msg.getAttachment());
			}
			return;
		} else if (section == SECTION.OUTPUT) {
			/********** NOT YET IMPLEMENTED **********
			if (msg instanceof MsgOutputLevelChanged) {
				changedOutputMaster(number, (Float) msg.getAttachment());
			} */
			return;
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
			final String label = this.labelMaker.getLabelLevel(msgChannelLevelChanged.getAttachment());
			this.fireChange(new MsgChannelLevelLabel(chn, 0, label));
		} else if (msg instanceof MsgChannelOnChanged) {
			final MsgChannelOnChanged msgChannelOnChanged = (MsgChannelOnChanged) msg;
			this.changedChannelOnButton(chn, msgChannelOnChanged.getAttachment());
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
			//this.getChannelNames();
		} else if (msg instanceof ResyncChannelOnButtons) {
			this.getAllChannelOnButtons();
		} else if (msg instanceof ResyncAuxSendLevels) {
			int aux = (int) msg.getAttachment();
			this.getAllAuxLevels(aux);
		} else if (msg instanceof ResyncAuxSendOnButtons) {
			int aux = (int) msg.getAttachment();
			this.getAllAuxChannelOn(aux);
		} else if (msg instanceof ResyncChannelStrip) {
			int chn = (int) msg.getAttachment();
			this.getAllInputValues(chn);
		} else if (msg instanceof ResyncGeqBandLevels) {
			//byte geq = (byte) msg.getAttachment();
			//mixer.getAllGeqLevels(geq);
		} else if (msg instanceof ResyncDelayTimes) {
			this.getAllDelayTimes();
		} else if (msg instanceof ResyncOutputs) {
			int aux = ((ResyncOutputs) msg).getCurrentAux();
			this.getOutputs(aux);
		} else if (msg instanceof ResyncEverything) {
			this.getAllMixerValues((ResyncEverything) msg);
		}
	}
	
	private void getAllMixerValues(final ResyncEverything msg) {
		int currentAux = msg.getCurrentAux();
		int currentInput = msg.getCurrentInput();
		//int currentGeq = msg.getCurrentGeq();
		this.getAllChannelLevels();
		//this.getChannelNames();
		this.getAllChannelOnButtons();
		this.getAllDelayTimes();
		this.getAllAuxLevels(currentAux);
		this.getAllAuxChannelOn(currentAux);
		this.getAllInputValues(currentInput);
		//mixer.getAllGeqLevels((byte) currentGeq);
	}

	/**
	 * This method changes the master level of the mixer.
	 * @param level The new level of the master fader.
	 */
	public void changedMasterLevel(final float level) {
		final short value = (short) (level * 1496);
		this.sysex.changeParameter(DDX3216SysexParameter.PARAM_LEVEL, 
				DDX3216SysexParameter.SECTION_MASTER, value);
		// OSCremote.println("LEVELSET MASTER value=" + value);
	}

	public void changedAuxMaster(final int aux, final float level) {
		final short value = (short) (level * 1496);
		if (aux == 0) {
			this.sysex.changeParameter(DDX3216SysexParameter.PARAM_LEVEL, 
					DDX3216SysexParameter.SECTION_AUX1_MASTER, value);
		} else if (aux == 1) {
			this.sysex.changeParameter(DDX3216SysexParameter.PARAM_LEVEL, 
					DDX3216SysexParameter.SECTION_AUX2_MASTER, value);
		} else if (aux == 2) {
			this.sysex.changeParameter(DDX3216SysexParameter.PARAM_LEVEL, 
					DDX3216SysexParameter.SECTION_AUX3_MASTER, value);
		} else if (aux == 3) {
			this.sysex.changeParameter(DDX3216SysexParameter.PARAM_LEVEL, 
					DDX3216SysexParameter.SECTION_AUX4_MASTER, value);
		}
		// OSCremote.println("LEVELSET AUXMASTER aux=" + aux + ", value=" +
		// value);
	}


	private void changedBusMaster(final int number, final Float level) {
		final short value = (short) (level * BEHRINGER_FADER_STEPS);
		this.sysex.changeParameter(DDX3216SysexParameter.PARAM_BUS_LEVEL, (byte) number, value);
	}


	public void changedChannelLevel(final int channel, final float level) {
		final short value = (short) (level * 1496);
		this.sysex.changeParameter(DDX3216SysexParameter.PARAM_LEVEL, (byte) channel, value);
		// OSCremote.println("LEVELSET channel=" + channel + ", value=" +
		// value);
	}


	public void changedChannelAuxLevel(final int channel, final int aux, final float level) {
		final short value = (short) (level * 1496);
		if (aux == 0) {
			this.sysex.changeParameter(DDX3216SysexParameter.PARAM_AUX1_LEVEL, (byte) channel, value);
		} else if (aux == 1) {
			this.sysex.changeParameter(DDX3216SysexParameter.PARAM_AUX2_LEVEL, (byte) channel, value);
		} else if (aux == 2) {
			this.sysex.changeParameter(DDX3216SysexParameter.PARAM_AUX3_LEVEL, (byte) channel, value);
		} else if (aux == 3) {
			this.sysex.changeParameter(DDX3216SysexParameter.PARAM_AUX4_LEVEL, (byte) channel, value);
		}

		// OSCremote.println("AUXSET channel=" + channel + ", aux=" + aux +
		// ", value=" + value);
	}

	@Override
	public void getChannelAux(final int channel, final int aux) {
		if (aux == 0) {
			this.sysex.requestParameter(DDX3216SysexParameter.PARAM_AUX1_LEVEL, (byte) channel);
		} else if (aux == 1) {
			this.sysex.requestParameter(DDX3216SysexParameter.PARAM_AUX2_LEVEL, (byte) channel);
		} else if (aux == 2) {
			this.sysex.requestParameter(DDX3216SysexParameter.PARAM_AUX3_LEVEL, (byte) channel);
		} else if (aux == 3) {
			this.sysex.requestParameter(DDX3216SysexParameter.PARAM_AUX4_LEVEL, (byte) channel);
		}
		// OSCremote.println("AUXGET channel=" + channel + ", aux=" + aux);
	}

	@Override
	public void getAuxMaster(final int aux) {
		if (aux == 0) {
			this.sysex.requestParameter(DDX3216SysexParameter.PARAM_LEVEL,
					DDX3216SysexParameter.SECTION_AUX1_MASTER);
		} else if (aux == 1) {
			this.sysex.requestParameter(DDX3216SysexParameter.PARAM_LEVEL,
					DDX3216SysexParameter.SECTION_AUX2_MASTER);
		} else if (aux == 2) {
			this.sysex.requestParameter(DDX3216SysexParameter.PARAM_LEVEL,
					DDX3216SysexParameter.SECTION_AUX3_MASTER);
		} else if (aux == 3) {
			this.sysex.requestParameter(DDX3216SysexParameter.PARAM_LEVEL,
					DDX3216SysexParameter.SECTION_AUX4_MASTER);
		}
		// OSCremote.println("AUXMASTERGET aux=" + aux);
	}

	@Override
	public void getAllAuxLevels(final int aux) {
		for (int i = 0; i < this.getChannelCount(); i++) {
			this.getChannelAux(i, aux);
		}
		this.getAuxMaster(aux);
	}

	@Override
	public void getChannelLevel(final int channel) {
		this.sysex.requestParameter(DDX3216SysexParameter.PARAM_LEVEL, (byte) channel);
		// OSCremote.println("LEVELGET channel=" + channel);
	}

	@Override
	public void getAllChannelLevels() {
		for (int i = 0; i < this.getChannelCount(); i++) {
			this.getChannelLevel(i);
		}
		this.getOutputMaster();
	}

	@Override
	public void getOutputMaster() {
		this.sysex.requestParameter(DDX3216SysexParameter.PARAM_LEVEL,
				DDX3216SysexParameter.SECTION_MASTER);
	}


	public void changedChannelOnButton(final int channel, final boolean status) {
		this.sysex.changeParameter(DDX3216SysexParameter.PARAM_MUTE, 
				(byte) channel, status ? (byte) 1 : (byte) 0);

	}

	@Override
	public void getChannelOnButton(final int channel) {
		this.sysex.requestParameter(DDX3216SysexParameter.PARAM_MUTE, (byte) channel);

	}

	@Override
	public void getAllChannelOnButtons() {
		//logger.entering(this.getClass().toString(), "getAllChannelOnButtons");

		for (int i = 0; i < this.getChannelCount(); i++) {
			this.getChannelOnButton(i);
		}

	}

	public void changedInputPan(final int chn, final float value) {
		final float denormedValue = value / 2 + 0.5f; 
		final short panning = (short) (denormedValue * 60);
		this.sysex.changeParameter(DDX3216SysexParameter.PARAM_PAN, (byte) chn, panning);
	}

	public void getInputPanLevel(final int channel) {
		this.sysex.requestParameter(DDX3216SysexParameter.PARAM_PAN, (byte) channel);
		// OSCremote.println("LEVELGET channel=" + channel);
	}


	public void changedPeqBand(final int chn, final int band, final char param, final Float attachment) {
		short value = 0;
		byte parameter = 0;
		try {
			// set eq band gain
			if (param == 'G') {
				final float denormedValue = attachment / 2 + 0.5f;
				value = (short) (denormedValue * 72);
				if (band == DDX3216SysexParameter.PEQ_BAND1) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND1_G;
				} else if (band == DDX3216SysexParameter.PEQ_BAND2) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND2_G;
				} else if (band == DDX3216SysexParameter.PEQ_BAND3) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND3_G;
				} else if (band == DDX3216SysexParameter.PEQ_BAND4) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND4_G;
				}
				// set eq band frequency
			} else if (param == 'F') {
				value = (short) (attachment * 159);
				if (band == DDX3216SysexParameter.PEQ_BAND1) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND1_F;
				} else if (band == DDX3216SysexParameter.PEQ_BAND2) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND2_F;
				} else if (band == DDX3216SysexParameter.PEQ_BAND3) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND3_F;
				} else if (band == DDX3216SysexParameter.PEQ_BAND4) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND4_F;
				}
				// set eq band quality
			} else if (param == 'Q') {
				value = (short) (attachment * 40);
				if (band == DDX3216SysexParameter.PEQ_BAND1) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND1_Q;
				} else if (band == DDX3216SysexParameter.PEQ_BAND2) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND2_Q;
				} else if (band == DDX3216SysexParameter.PEQ_BAND3) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND3_Q;
				} else if (band == DDX3216SysexParameter.PEQ_BAND4) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND4_Q;
				}
			} else {
				throw new Exception();
			}
		} catch (final Exception e) {
			logger.info("Unsupported PEQ Parameter.");
		}

		this.sysex.changeParameter(parameter, (byte) chn, value);
	}

	public void changedPeqHPFOn(final int chn, final Boolean attachment) {
		this.sysex.changeParameter(DDX3216SysexParameter.PARAM_PEQ_HPF_ON,
				(byte) chn, attachment ? (byte) 1 : (byte) 0);

	}

	public void changedPeqOn(final int chn, final Boolean attachment) {
		this.sysex.changeParameter(DDX3216SysexParameter.PARAM_PEQ_EQON,
				(byte) chn, attachment ? (byte) 1 : (byte) 0);		
	}

	public void getPeqBandParameter(final int band, final char param, final int channel) {
		byte parameter = 0;
		String logmsg = "";
		try {
			if (param == 'G') {
				if (band == 0) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND1_G;
					logmsg = "Requested Input PEQ Band 1 Gain";
				} else if (band == 1) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND2_G;
					logmsg = "Requested Input PEQ Band 2 Gain";
				} else if (band == 2) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND3_G;
					logmsg = "Requested Input PEQ Band 3 Gain";
				} else if (band == 3) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND4_G;
					logmsg = "Requested Input PEQ Band 4 Gain";
				}
			} else if (param == 'F') {
				if (band == 0) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND1_F;
					logmsg = "Requested Input PEQ Band 1 Frequency";
				} else if (band == 1) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND2_F;
					logmsg = "Requested Input PEQ Band 2 Frequency";
				} else if (band == 2) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND3_F;
					logmsg = "Requested Input PEQ Band 3 Frequency";
				} else if (band == 3) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND4_F;
					logmsg = "Requested Input PEQ Band 4 Frequency";
				}
			} else if (param == 'Q') {
				if (band == 0) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND1_Q;
					logmsg = "Requested Input PEQ Band 1 Quality";
				} else if (band == 1) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND2_Q;
					logmsg = "Requested Input PEQ Band 2 Quality";
				} else if (band == 2) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND3_Q;
					logmsg = "Requested Input PEQ Band 3 Quality";
				} else if (band == 3) {
					parameter = DDX3216SysexParameter.PARAM_PEQ_BAND4_Q;
					logmsg = "Requested Input PEQ Band 4 Quality";
				}
			} else {
				throw new Exception();
			}
		} catch (final Exception e) {
			logger.info("Unsupported PEQ Parameter.");
		}

		this.sysex.requestParameter(parameter, (byte) channel);
		logger.log(Level.FINER, logmsg + " on Channel " + channel);
	}


	public void getPeqHPFOn(final int channel) {
		this.sysex.requestParameter(DDX3216SysexParameter.PARAM_PEQ_HPF_ON, (byte) channel);
		logger.log(Level.FINER, "Requested Input PEQ HPF Status on Channel " + channel);
	}

	public void getPeqOn(final int channel) {
		this.sysex.requestParameter(DDX3216SysexParameter.PARAM_PEQ_EQON, (byte) channel);
		logger.log(Level.FINER, "Requested Input PEQ-On Status on Channel " + channel);
	}

	@Override
	public void getAllInputValues(final int chn) {
		// Channel Parameters
		this.getChannelLevel(chn);
		this.getChannelOnButton(chn);
		this.getInputPanLevel(chn);
		// Parametric Equalizers
		for (int i = DDX3216SysexParameter.PEQ_BAND1; i <= DDX3216SysexParameter.PEQ_BAND4; i++) {
			this.getPeqBandParameter(i, 'G', chn);
			this.getPeqBandParameter(i, 'F', chn);
			this.getPeqBandParameter(i, 'Q', chn);
		}
		this.getPeqHPFOn(chn);
		this.getPeqOn(chn);

	}

	@Override
	public void getBusMaster(final int bus) {
		this.sysex.requestParameter(DDX3216SysexParameter.PARAM_BUS_LEVEL, (byte) bus);
	}



	@Override
	public void getAllDelayTimes() {
		// stub: not needed for this mixer

	}
	
	private void getOutputs(int aux) {
		this.getOutputMaster();
		this.getAuxMaster(aux);
	}

	@Override
	public void getAllBusStatus() {
		for (int i = 0; i < getBusCount(); i++) {
			getBusMaster(i);
		}
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
	public void getAllGroupsStatus() {
		// stub: not needed for this mixer

	}

	/**
	 * This list contains all objects that are currently
	 * registered as observers of this object.
	 */
	private final ArrayList<IMessageReceiver> observers;

	private final AbstractApplicationContext ctx;

	private ICommunicationAware communicationAware;

	/**
	 * @return a list of the current observers of this object.
	 */
	protected List<IMessageReceiver> getObservers() {
		return this.observers;
	}

	/**
	 * Adds a new observer to this object.
	 * @param observer The new observing object.
	 */
	@Override
	public void registerObserver(
			final IMessageReceiver observer) {
		this.observers.add(observer);
	}

	/**
	 * Notifies all observers that an parameter which is controled through
	 * messages has changed.
	 * @param msg A message further specifying the parameter change.
	 */
	public void fireChange(final AbstractMessage<?> msg) {
		for (final IMessageReceiver m : this.observers) {
			m.handleNotification(new MessageWithSender(this, msg));
		}
	}


	public DDX3216Plugin getPluginInstance() {
		return this.pluginInstance;
	}


	public AbstractApplicationContext getApplicationCtx() {
		return this.ctx;
	}


	@Override
	public void getAuxChannelOnButton(final int channel, final int aux) {
		// fail silently:
		// this mixer doesn't have aux-send mute

	}


	@Override
	public void getAllAuxChannelOn(final int aux) {
		// fail silently:
		// this mixer doesn't have aux-send mute

	}


	@Override
	public String getPluginName() {
		return this.getPluginInstance().getPluginName();
	}

	public void registerCommunicationAware(final ICommunicationAware communicationAware) {
		this.communicationAware = communicationAware;
	}

	public ICommunicationAware getCommunicationAware() {
		return this.communicationAware;
	}
}
