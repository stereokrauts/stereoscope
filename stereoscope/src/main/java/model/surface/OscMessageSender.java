package model.surface;

import java.util.ArrayList;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscConstants;
import model.protocol.osc.OscObjectUtil;
import model.protocol.osc.impl.OscMessage;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelAuxMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractGeqMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractGroupMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputPeqBandMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractLabelMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage.SECTION;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandReset;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqFullReset;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqTypeChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelNameChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgInputGroups;
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
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaPair;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRange;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaRatio;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.dynamics.MsgInputDynaThreshold;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelAuxLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.labels.MsgChannelAuxOnLabel;
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
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqFilterType;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqG;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqHPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqLPFChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqModeChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.peq.MsgInputPeqQ;
import com.stereokrauts.stereoscope.model.messaging.message.mixerglobal.MsgDcaLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgOutputDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgOutputGroups;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.labels.MsgAuxMasterLevelLabel;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.labels.MsgBusMasterLevelLabel;

/**
 * This class also implements IMessageSender, as the TouchOscSurface sends
 * messages in the name of this object to get rid of feedback loop.
 * 
 * @author theide
 * 
 */
public class OscMessageSender implements IMessageReceiver, IMessageSender {
	public static final String OSC_PREFIX = "/stereoscope/";

	private static final String NS_STATEFUL    = "stateful/";
	private static final String NS_GEQ_BAND    = NS_STATEFUL + "dsp/geq/band/";
	private static final String NS_INPUT       = "input/";
	private static final String NS_IN_PEQ_BAND = NS_STATEFUL + "input/peq/band/";
	private static final String NS_IN_DYNAMICS = NS_STATEFUL + "input/dynamics/";

	private static final SLogger LOG = StereoscopeLogManager
			.getLogger("osc-sender");

	private static final int GEQ_BANDS = 31;
	private final transient OscSurface surface;

	public OscMessageSender(final OscSurface oscSurface) {
		this.surface = oscSurface;
	}

	/**
	 * This methods receives all messages from the UpdateManager and distributes
	 * them to the functions in this class.
	 * 
	 * @param sender
	 *            The object that this message originates from
	 * @param msg
	 *            The message that should be handled by this function.
	 */
	private final transient Object msgReceiveLock = new Object();
	@Override
	public final void handleNotification(final IMessageWithSender message) {
		synchronized (this.msgReceiveLock) {
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
			} else if (msg instanceof AbstractInputDynamicsMessage) {
				this.handleInputDynamicsMessage(msg);
			} else if (msg instanceof AbstractInputPeqBandMessage) {
				this.handleInputPeqBandMessage(msg);
			} else if (msg instanceof AbstractGroupMessage) {
				this.handleGroupsMessage(msg);
			} else if (msg instanceof AbstractLabelMessage) {
				this.handleLabelMessage(msg);
			} else if (msg instanceof MsgDcaLevelChanged) {
				final MsgDcaLevelChanged dcaLevelMessage = (MsgDcaLevelChanged) msg;
				this.changedDcaLevel(dcaLevelMessage.getDcaNumber(), dcaLevelMessage.getAttachment());
			}
		}
	}

	/**
	 * This methods receives all groups related messages from the function
	 * handleNotification.
	 * 
	 * @param msg
	 *            The message that should be handled by this function.
	 */
	private void handleGroupsMessage(final IMessage msg) {
		final int type = ((AbstractGroupMessage<?>) msg).getGroupType(); // NOPMD by th on 09.10.12 20:20
		final int group = ((AbstractGroupMessage<?>) msg).getGroup();    // NOPMD by th on 09.10.12 20:20
		if (msg instanceof MsgInputGroups) {
			final MsgInputGroups msgInputGroups = (MsgInputGroups) msg;
			this.setInputGroupStatus(type, group, msgInputGroups.getAttachment());
		} else if (msg instanceof MsgOutputGroups) {
			final MsgOutputGroups msgOutputGroups = (MsgOutputGroups) msg;
			this.setOutputGroupStatus(type, group, msgOutputGroups.getAttachment());
		}

	}

	/**
	 * This methods receives all channel aux related messages from the function
	 * handleNotification.
	 * 
	 * @param msg
	 *            The message that should be handled by this function.
	 */
	private void handleChannelAuxMessage(final IMessage msg) {
		if (msg instanceof MsgAuxSendChanged) {
			final MsgAuxSendChanged msgAuxSendChanged = (MsgAuxSendChanged) msg;
			this.setChannelAuxLevel(msgAuxSendChanged.getChannel(),
					msgAuxSendChanged.getAux(),
					msgAuxSendChanged.getAttachment());
		} else if (msg instanceof MsgAuxSendOnChanged) {
			final MsgAuxSendOnChanged msgAuxSendOnChanged = (MsgAuxSendOnChanged) msg;
			this.setChannelAuxSendOn(msgAuxSendOnChanged.getChannel(),
					msgAuxSendOnChanged.getAux(),
					msgAuxSendOnChanged.getAttachment());
		}
	}

	/**
	 * This methods receives all master section related messages from the
	 * function handleNotification.
	 * 
	 * @param msg
	 *            The message that should be handled by this function.
	 */
	private void handleMasterMessage(final IMessage msg) {
		final AbstractMasterMessage<?> ammsg = ((AbstractMasterMessage<?>) msg);
		final SECTION section = ammsg.getSection();
		final int number = ammsg.getNumber(); // NOPMD by th on 09.10.12 20:21
		if (section == SECTION.AUX) {
			if (msg instanceof MsgAuxMasterLevelChanged) {
				this.changedAuxMaster(number, (Float) msg.getAttachment());
			} else if (msg instanceof MsgAuxMasterDelayChanged) {
				this.changedAuxDelayTime(number, (Float) msg.getAttachment());
			}
		} else if (section == SECTION.BUS) {
			if (msg instanceof MsgBusMasterLevelChanged) {
				changedBusMaster(number, (Float) msg.getAttachment());
			} else if (msg instanceof MsgBusMasterDelayChanged) {
				this.changedBusDelayTime(number, (Float) msg.getAttachment());
			}
		} else if (section == SECTION.OUTPUT) {
			/**********
			 * NOT YET IMPLEMENTED ********** if (msg instanceof
			 * MsgOutputLevelChanged) { changedOutputMaster(number, (Float)
			 * msg.getAttachment()); } else
			 */
			if (msg instanceof MsgOutputDelayChanged) {
				this.changedOutputDelayTime(number, (Float) msg.getAttachment());
			}
		} else if (section == SECTION.MASTER && msg instanceof MsgMasterLevelChanged) {
			this.setMasterLevel((Float) msg.getAttachment());
		}

	}

	/**
	 * This methods receives all graphical EQ related messages from the function
	 * handleNotification.
	 * 
	 * @param msg
	 *            The message that should be handled by this function.
	 */
	private void handleGeqMessage(final IMessage msg) {
		final int geqNumber = ((AbstractGeqMessage<?>) msg).getGeqNumber(); // NOPMD by th on 09.10.12 20:21
		if (msg instanceof MsgGeqFullReset) {
			this.doGeqFullReset(geqNumber);
		} else if (msg instanceof MsgGeqTypeChanged) {
			this.setCurrentGeqType((Boolean) msg.getAttachment());
		} else if (msg instanceof MsgGeqBandLevelChanged) {
			final MsgGeqBandLevelChanged geqMsg = (MsgGeqBandLevelChanged) msg;
			final boolean rightChannel = geqMsg.isRightChannel();
			final int band = geqMsg.getBand();
			final float floatValue = geqMsg.getAttachment();
			this.setGeqBandLevel(geqNumber, rightChannel, band, floatValue);
		} else if (msg instanceof MsgGeqBandReset) {
			final MsgGeqBandReset msgGeqBandReset = (MsgGeqBandReset) msg;
			final boolean rightChannel = msgGeqBandReset.isRightChannel();
			final int band = msgGeqBandReset.getBand();
			this.doGeqBandReset(geqNumber, rightChannel, band);
		}
	}

	/**
	 * This methods receives all channel related messages from the function
	 * handleNotification.
	 * 
	 * @param msg
	 *            The message that should be handled by this function.
	 */
	private void handleChannelMessage(final IMessage msg) {
		final int chn = ((AbstractChannelMessage<?>) msg).getChannel(); // NOPMD by th on 09.10.12 20:21
		if (msg instanceof MsgChannelLevelChanged) {
			final MsgChannelLevelChanged chLevel = (MsgChannelLevelChanged) msg;
			this.setChannelLevel(chn, chLevel.getAttachment());
		} else if (msg instanceof MsgChannelNameChanged) {
			final MsgChannelNameChanged chName = (MsgChannelNameChanged) msg;
			this.setChannelName(chn, chName.getAttachment());
		} else if (msg instanceof MsgChannelOnChanged) {
			final MsgChannelOnChanged chOn = (MsgChannelOnChanged) msg;
			this.setChannelOnButton(chn, chOn.getAttachment());
		}
	}

	private void handleInputMessage(final IMessage msg) {
		final int chn = ((AbstractInputMessage<?>) msg).getChannel(); // NOPMD by th on 09.10.12 20:21

		if (msg instanceof MsgInputPan) {
			final MsgInputPan msgInputPanChanged = (MsgInputPan) msg;
			this.setInputPan(chn, msgInputPanChanged.getAttachment());
		} else if (msg instanceof MsgInputPeqOnChanged) {
			final MsgInputPeqOnChanged msgPeqOnChanged = (MsgInputPeqOnChanged) msg;
			this.setPeqOn(chn, msgPeqOnChanged.getAttachment());
		} else if (msg instanceof MsgInputPeqModeChanged) {
			final MsgInputPeqModeChanged msgPeqModeChanged = (MsgInputPeqModeChanged) msg;
			this.setPeqMode(chn, msgPeqModeChanged.getAttachment());
		} else if (msg instanceof MsgInputPeqLPFChanged) {
			final MsgInputPeqLPFChanged msgPeqLPFChanged = (MsgInputPeqLPFChanged) msg;
			this.setPeqLPFOn(chn, msgPeqLPFChanged.getAttachment());
		} else if (msg instanceof MsgInputPeqHPFChanged) {
			final MsgInputPeqHPFChanged msgPeqHPFChanged = (MsgInputPeqHPFChanged) msg;
			this.setPeqHPFOn(chn, msgPeqHPFChanged.getAttachment());
		}
	}

	private void handleInputPeqBandMessage(final IMessage msg) {
		final int chn = ((AbstractInputPeqBandMessage<?>) msg).getChannel(); // NOPMD by th on 09.10.12 20:21
		final int band = ((AbstractInputPeqBandMessage<?>) msg).getBand(); // NOPMD by th on 09.10.12 20:21

		if (msg instanceof MsgInputPeqQ) {
			final MsgInputPeqQ msgInputPeqQ = (MsgInputPeqQ) msg;
			this.setPeqBandQ(chn, band, msgInputPeqQ.getAttachment());
		} else if (msg instanceof MsgInputPeqF) {
			final MsgInputPeqF msgInputPeqF = (MsgInputPeqF) msg;
			this.setPeqBandF(chn, band, msgInputPeqF.getAttachment());
		} else if (msg instanceof MsgInputPeqG) {
			final MsgInputPeqG msgInputPeqG = (MsgInputPeqG) msg;
			this.setPeqBandG(chn, band, msgInputPeqG.getAttachment());
		} else if (msg instanceof MsgInputPeqFilterType) {
			final MsgInputPeqFilterType msgInputPeqFilterCharacteristics = (MsgInputPeqFilterType) msg;
			this.setInputPeqFilterCharacteristics(chn, band, msgInputPeqFilterCharacteristics.getAttachment());
		}
	}

	private void handleInputDynamicsMessage(final IMessage msg) {
		final int chn = ((AbstractInputDynamicsMessage<?>) msg).getChannel(); // NOPMD by th on 09.10.12 20:21
		final int dyna = ((AbstractInputDynamicsMessage<?>) msg).getProcessor(); // NOPMD by th on 09.10.12 20:21
		if (msg instanceof MsgInputDynaAttack) {
			final MsgInputDynaAttack msgInputDynaAttack = (MsgInputDynaAttack) msg;
			this.setInputDynaAttack(chn, dyna, msgInputDynaAttack.getAttachment());
		} else if (msg instanceof MsgInputDynaAutoOn) {
			final MsgInputDynaAutoOn msgInputDynaAutoOn = (MsgInputDynaAutoOn) msg;
			this.setInputDynaAutoOn(chn, dyna, msgInputDynaAutoOn.getAttachment());
		} else if (msg instanceof MsgInputDynaDecayRelease) {
			final MsgInputDynaDecayRelease msgInputDynaDecayRelease = (MsgInputDynaDecayRelease) msg;
			this.setInputDynaDecayRelease(chn, dyna,
					msgInputDynaDecayRelease.getAttachment());
		} else if (msg instanceof MsgInputDynaFilterFreq) {
			final MsgInputDynaFilterFreq msgInputDynaFilterFreq = (MsgInputDynaFilterFreq) msg;
			this.setInputDynaFilterFreq(chn, dyna,
					msgInputDynaFilterFreq.getAttachment());
		} else if (msg instanceof MsgInputDynaFilterOn) {
			final MsgInputDynaFilterOn msgInputDynaFilterOn = (MsgInputDynaFilterOn) msg;
			this.setInputDynaFilterOn(chn, dyna,
					msgInputDynaFilterOn.getAttachment());
		} else if (msg instanceof MsgInputDynaFilterQ) {
			final MsgInputDynaFilterQ msgInputDynaFilterQ = (MsgInputDynaFilterQ) msg;
			this.setInputDynaFilterQ(chn, dyna, msgInputDynaFilterQ.getAttachment());
		} else if (msg instanceof MsgInputDynaFilterType) {
			final MsgInputDynaFilterType msgInputDynaFilterType = (MsgInputDynaFilterType) msg;
			this.setInputDynaFilterType(chn, dyna,
					msgInputDynaFilterType.getAttachment());
		} else if (msg instanceof MsgInputDynaGain) {
			final MsgInputDynaGain msgInputDynaGain = (MsgInputDynaGain) msg;
			this.setInputDynaGain(chn, dyna, msgInputDynaGain.getAttachment());
		} else if (msg instanceof MsgInputDynaHold) {
			final MsgInputDynaHold msgInputDynaHold = (MsgInputDynaHold) msg;
			this.setInputDynaHold(chn, dyna, msgInputDynaHold.getAttachment());
		} else if (msg instanceof MsgInputDynaKeyIn) {
			final MsgInputDynaKeyIn msgInputDynaKeyIn = (MsgInputDynaKeyIn) msg;
			this.setInputDynaKeyIn(chn, dyna, msgInputDynaKeyIn.getAttachment());
		} else if (msg instanceof MsgInputDynaKnee) {
			final MsgInputDynaKnee msgInputDynaKnee = (MsgInputDynaKnee) msg;
			this.setInputDynaKnee(chn, dyna, msgInputDynaKnee.getAttachment());
		} else if (msg instanceof MsgInputDynaLeftSideChain) {
			final MsgInputDynaLeftSideChain msgInputDynaLeftSideChain = (MsgInputDynaLeftSideChain) msg;
			this.setInputDynaLeftSideChain(chn, dyna,
					msgInputDynaLeftSideChain.getAttachment());
		} else if (msg instanceof MsgInputDynaOn) {
			final MsgInputDynaOn msgInputDynaOn = (MsgInputDynaOn) msg;
			this.setInputDynaOn(chn, dyna, msgInputDynaOn.getAttachment());
		} else if (msg instanceof MsgInputDynaPair) {
			// on GenericMidsize and Pm5d this is called DynaLink
			final MsgInputDynaPair msgInputDynaPair = (MsgInputDynaPair) msg;
			this.setInputDynaPair(chn, dyna, msgInputDynaPair.getAttachment());
		} else if (msg instanceof MsgInputDynaRange) {
			final MsgInputDynaRange msgInputDynaRange = (MsgInputDynaRange) msg;
			this.setInputDynaRange(chn, dyna, msgInputDynaRange.getAttachment());
		} else if (msg instanceof MsgInputDynaRatio) {
			final MsgInputDynaRatio msgInputDynaRatio = (MsgInputDynaRatio) msg;
			this.setInputDynaRatio(chn, dyna, msgInputDynaRatio.getAttachment());
		} else if (msg instanceof MsgInputDynaThreshold) {
			final MsgInputDynaThreshold msgInputDynaThreshold = (MsgInputDynaThreshold) msg;
			this.setInputDynaThreshold(chn, dyna,
					msgInputDynaThreshold.getAttachment());
		}
	}

	private void handleLabelMessage(final IMessage msg) {
		final int channel = ((AbstractLabelMessage<?>) msg).getChannel(); // NOPMD by th on 09.10.12 20:21
		final int id = ((AbstractLabelMessage<?>) msg).getLabelIdentifier(); // NOPMD by th on 09.10.12 20:21

		/** INPUT PARAMETRIC EQ **/
		if (msg instanceof MsgInputPeqQLabel) {
			final MsgInputPeqQLabel msgInputPeqQLabel = (MsgInputPeqQLabel) msg;
			this.setInputPeqQLabel(channel, id, msgInputPeqQLabel.getAttachment());
		} else if (msg instanceof MsgInputPeqFLabel) {
			final MsgInputPeqFLabel msgInputPeqFLabel = (MsgInputPeqFLabel) msg;
			this.setInputPeqFLabel(channel, id, msgInputPeqFLabel.getAttachment());
		} else if (msg instanceof MsgInputPeqGLabel) {
			final MsgInputPeqGLabel msgInputPeqGLabel = (MsgInputPeqGLabel) msg;
			this.setInputPeqGLabel(channel, id, msgInputPeqGLabel.getAttachment());

			/** INPUT DYNAMICS **/
		} else if (msg instanceof MsgInputDynaAttackLabel) {
			final MsgInputDynaAttackLabel msgInputDynaAttackLabel = (MsgInputDynaAttackLabel) msg;
			this.setInputDynaAttackLabel(channel, id,
					msgInputDynaAttackLabel.getAttachment());
		} else if (msg instanceof MsgInputDynaDecayReleaseLabel) {
			final MsgInputDynaDecayReleaseLabel msgInputDynaDecayReleaseLabel = (MsgInputDynaDecayReleaseLabel) msg;
			this.setInputDynaDecayReleaseLabel(channel, id,
					msgInputDynaDecayReleaseLabel.getAttachment());
		} else if (msg instanceof MsgInputDynaGainLabel) {
			final MsgInputDynaGainLabel msgInputDynaGainLabel = (MsgInputDynaGainLabel) msg;
			this.setInputDynaGainLabel(channel, id,
					msgInputDynaGainLabel.getAttachment());
		} else if (msg instanceof MsgInputDynaHoldLabel) {
			final MsgInputDynaHoldLabel msgInputDynaHoldLabel = (MsgInputDynaHoldLabel) msg;
			this.setInputDynaHoldLabel(channel, id,
					msgInputDynaHoldLabel.getAttachment());
		} else if (msg instanceof MsgInputDynaKneeLabel) {
			final MsgInputDynaKneeLabel msgInputDynaKneeLabel = (MsgInputDynaKneeLabel) msg;
			this.setInputDynaKneeLabel(channel, id,
					msgInputDynaKneeLabel.getAttachment());
		} else if (msg instanceof MsgInputDynaRangeLabel) {
			final MsgInputDynaRangeLabel msgInputDynaRangeLabel = (MsgInputDynaRangeLabel) msg;
			this.setInputDynaRangeLabel(channel, id,
					msgInputDynaRangeLabel.getAttachment());
		} else if (msg instanceof MsgInputDynaRatioLabel) {
			final MsgInputDynaRatioLabel msgInputDynaRatioLabel = (MsgInputDynaRatioLabel) msg;
			this.setInputDynaRatioLabel(channel, id,
					msgInputDynaRatioLabel.getAttachment());
		} else if (msg instanceof MsgInputDynaThresholdLabel) {
			final MsgInputDynaThresholdLabel msgInputDynaThresholdLabel = (MsgInputDynaThresholdLabel) msg;
			this.setInputDynaThresholdLabel(channel, id,
					msgInputDynaThresholdLabel.getAttachment());

			/** INPUT MISC **/
		} else if (msg instanceof MsgChannelLevelLabel) {
			final MsgChannelLevelLabel msgChannelLevelLabel = (MsgChannelLevelLabel) msg;
			this.setChannelLevelLabel(channel, msgChannelLevelLabel.getAttachment());
		} else if (msg instanceof MsgInputPanLabel) {
			final MsgInputPanLabel msgInputPanLabel = (MsgInputPanLabel) msg;
			this.setInputPanLabel(channel, msgInputPanLabel.getAttachment());


			/** INPUT AUX **/
		} else if (msg instanceof MsgChannelAuxLevelLabel) {
			final MsgChannelAuxLevelLabel msgChannelAuxSendLevelLabel = (MsgChannelAuxLevelLabel) msg;
			this.setChannelAuxSendLevelLabel(channel, id, msgChannelAuxSendLevelLabel.getAttachment());
		} else if (msg instanceof MsgChannelAuxOnLabel) {
			final MsgChannelAuxOnLabel msgChannelAuxOnLabel = (MsgChannelAuxOnLabel) msg;
			this.setChannelAuxSendOnLabel(channel, id, msgChannelAuxOnLabel.getAttachment());


			/** BUS/AUX MASTERS **/
		} else if (msg instanceof MsgAuxMasterLevelLabel) {
			final MsgAuxMasterLevelLabel msgChannelAuxSendLevelLabel = (MsgAuxMasterLevelLabel) msg;
			this.setAuxMasterLevelLabel(id, msgChannelAuxSendLevelLabel.getAttachment());
		} else if (msg instanceof MsgBusMasterLevelLabel) {
			final MsgBusMasterLevelLabel msgChannelAuxOnLabel = (MsgBusMasterLevelLabel) msg;
			this.setBusMasterLevelLabel(id, msgChannelAuxOnLabel.getAttachment());
		}
	}

	public final void setControlElementVisibility(final int channel,
			final String path, final int value) {

		final IOscMessage newVisibilityStatus = new OscMessage(OscAddressUtil.create(OSC_PREFIX + path
				+ "/visible"));
		newVisibilityStatus.add(OscObjectUtil.createOscObject(value));
		this.send(newVisibilityStatus);
	}

	private void setInputDynaThreshold(final int chn, final int processor,
			final float attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaThreshold = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1)
					+ "/threshold"));
			newInputDynaThreshold.add(OscObjectUtil.createOscObject(attachment));
			this.send(newInputDynaThreshold);
		}

	}

	private void setInputDynaThresholdLabel(final int chn, final int processor,
			final String label) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newThresholdLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1)
					+ "/thresholdLabel"));
			newThresholdLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newThresholdLabel);
		}
	}

	private void setInputDynaRatio(final int chn, final int processor,
			final float attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaRatio = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1) + "/ratio"));
			newInputDynaRatio.add(OscObjectUtil.createOscObject(attachment));
			this.send(newInputDynaRatio);
		}

	}

	private void setInputDynaRatioLabel(final int chn, final int processor,
			final String label) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newRatioLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1)
					+ "/ratioLabel"));
			newRatioLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newRatioLabel);
		}

	}

	private void setInputDynaRange(final int chn, final int processor,
			final float attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaRange = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1) + "/range"));
			newInputDynaRange.add(OscObjectUtil.createOscObject(attachment));
			this.send(newInputDynaRange);
		}

	}

	private void setInputDynaRangeLabel(final int chn, final int processor,
			final String label) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newRangeLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1)
					+ "/rangeLabel"));
			newRangeLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newRangeLabel);
		}

	}

	private void setInputDynaKnee(final int chn, final int processor,
			final float attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaKnee = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1) + "/knee"));
			newInputDynaKnee.add(OscObjectUtil.createOscObject(attachment));
			this.send(newInputDynaKnee);
		}

	}

	private void setInputDynaKneeLabel(final int chn, final int processor,
			final String label) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newKneeLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1)
					+ "/kneeLabel"));
			newKneeLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newKneeLabel);
		}

	}

	private void setInputDynaKeyIn(final int chn, final int processor,
			final float attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaKeyIn = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1) + "/keyIn"));
			newInputDynaKeyIn.add(OscObjectUtil.createOscObject(attachment));
			// surface.getInputStripHandler().setCurrentElementValues(chn,
			// attachment);
			this.send(newInputDynaKeyIn);
		}

	}

	private void setInputDynaHold(final int chn, final int processor,
			final float attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaHold = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1) + "/hold"));
			newInputDynaHold.add(OscObjectUtil.createOscObject(attachment));
			this.send(newInputDynaHold);
		}

	}

	private void setInputDynaHoldLabel(final int chn, final int processor,
			final String label) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newHoldLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1)
					+ "/holdLabel"));
			newHoldLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newHoldLabel);
		}

	}

	private void setInputDynaGain(final int chn, final int processor,
			final float attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaGain = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1) + "/gain"));
			newInputDynaGain.add(OscObjectUtil.createOscObject(attachment));
			this.send(newInputDynaGain);
		}

	}

	private void setInputDynaGainLabel(final int chn, final int processor,
			final String label) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newGainLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1)
					+ "/gainLabel"));
			newGainLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newGainLabel);
		}

	}

	private void setInputDynaFilterType(final int chn, final int processor,
			final float attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaFilterType = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1)
					+ "/filterType"));
			newInputDynaFilterType.add(OscObjectUtil.createOscObject(attachment));
			this.send(newInputDynaFilterType);
		}

	}

	private void setInputDynaFilterQ(final int chn, final int processor,
			final float attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaFilterQ = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1)
					+ "/filterQ"));
			newInputDynaFilterQ.add(OscObjectUtil.createOscObject(attachment));
			this.send(newInputDynaFilterQ);
		}

	}

	private void setInputDynaFilterFreq(final int chn, final int processor,
			final float attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaFilterFreq = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1)
					+ "/filterFreq"));
			newInputDynaFilterFreq.add(OscObjectUtil.createOscObject(attachment));
			this.send(newInputDynaFilterFreq);
		}

	}

	private void setInputDynaDecayRelease(final int chn, final int processor,
			final float attachment) {

		if (chn == this.surface.getState().getCurrentInput()) {
			if (processor == 0) {
				// set decay
				final IOscMessage newInputDynaDecay = new OscMessage(OscAddressUtil.create(OSC_PREFIX
						+ NS_IN_DYNAMICS + (processor + 1) + "/decay"));
				newInputDynaDecay.add(OscObjectUtil.createOscObject(attachment));
				this.send(newInputDynaDecay);
			} else if (processor == 1) {
				// set release
				final IOscMessage newInputDynaRelease = new OscMessage(OscAddressUtil.create(OSC_PREFIX
						+ NS_IN_DYNAMICS + (processor + 1)
						+ "/release"));
				newInputDynaRelease.add(OscObjectUtil.createOscObject(attachment));
				this.send(newInputDynaRelease);
			}
		}

	}

	private void setInputDynaDecayReleaseLabel(final int chn,
			final int processor, final String label) {
		if (chn == this.surface.getState().getCurrentInput()) {
			if (processor == 0) {
				final IOscMessage newInputDynaDecayLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
						+ NS_IN_DYNAMICS + (processor + 1)
						+ "/decayLabel"));
				newInputDynaDecayLabel.add(OscObjectUtil.createOscObject(label));
				this.send(newInputDynaDecayLabel);
			} else if (processor == 1) {
				// release label
				final IOscMessage newReleaseLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
						+ NS_IN_DYNAMICS + (processor + 1)
						+ "/releaseLabel"));
				newReleaseLabel.add(OscObjectUtil.createOscObject(label));
				this.send(newReleaseLabel);
			}
		}
	}

	private void setInputDynaAttack(final int chn, final int processor,
			final float attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaAttack = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1) + "/attack"));
			newInputDynaAttack.add(OscObjectUtil.createOscObject(attachment));
			this.send(newInputDynaAttack);
		}

	}

	private void setInputDynaAttackLabel(final int chn, final int processor,
			final String label) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newAttackLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1)
					+ "/attackLabel"));
			newAttackLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newAttackLabel);
		}
	}

	private void setInputDynaPair(final int chn, final int processor,
			final boolean attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaPairStatus = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1) + "/pair"));
			newInputDynaPairStatus.add(OscObjectUtil.createOscObject(attachment ? 1.0f : 0.0f));
			this.send(newInputDynaPairStatus);
		}

	}

	private void setInputDynaOn(final int chn, final int processor,
			final boolean attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaOnStatus = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_DYNAMICS + (processor + 1) + "/dynaOn"));
			newInputDynaOnStatus.add(OscObjectUtil.createOscObject(attachment ? 1.0f : 0.0f));
			this.send(newInputDynaOnStatus);
		}

	}

	private void setInputDynaLeftSideChain(final int chn, final int processor,
			final boolean attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaLeftSideChainStatus = new OscMessage(OscAddressUtil.create(
					OSC_PREFIX + NS_IN_DYNAMICS + (processor + 1)
					+ "/leftSideChain"));
			newInputDynaLeftSideChainStatus.add(OscObjectUtil.createOscObject(attachment ? 1.0f : 0.0f));
			this.send(newInputDynaLeftSideChainStatus);
		}

	}

	private void setInputDynaFilterOn(final int chn, final int processor,
			final boolean attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaFilterOnStatus = new OscMessage(OscAddressUtil.create(
					OSC_PREFIX + NS_IN_DYNAMICS + (processor + 1)
					+ "/filterOn"));
			newInputDynaFilterOnStatus.add(OscObjectUtil.createOscObject(attachment ? 1.0f : 0.0f));
			this.send(newInputDynaFilterOnStatus);
		}

	}

	private void setInputDynaAutoOn(final int chn, final int processor,
			final boolean attachment) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputDynaAutoOnStatus = new OscMessage(OscAddressUtil.create(
					OSC_PREFIX + NS_IN_DYNAMICS + (processor + 1)
					+ "/autoOn"));
			newInputDynaAutoOnStatus.add(OscObjectUtil.createOscObject(attachment ? 1.0f : 0.0f));
			this.send(newInputDynaAutoOnStatus);
		}

	}

	private void setPeqBandG(final int channel, final int band,
			final float attachment) {
		if (channel == this.surface.getState().getCurrentInput()) {
			final float gain = (attachment / OscConstants.CENTER_CONTROL_SCALE_FACTOR) 
					+ OscConstants.CENTER_CONTROL_OFFSET;
			final IOscMessage newPeqBandSet = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_PEQ_BAND + (band + 1) + "/g"));
			newPeqBandSet.add(OscObjectUtil.createOscObject(gain));
			this.send(newPeqBandSet);

		}
	}

	private void setInputPeqGLabel(final int channel, final int band,
			final String label) {
		if (channel == this.surface.getState().getCurrentInput()) {
			final IOscMessage newPeqGLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_PEQ_BAND + (band + 1) + "/gLabel"));
			newPeqGLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newPeqGLabel);
		}
	}

	private void setPeqBandF(final int channel, final int band,
			final float attachment) {
		if (channel == this.surface.getState().getCurrentInput()) {
			final IOscMessage newPeqBandSet = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_PEQ_BAND + (band + 1) + "/f"));
			newPeqBandSet.add(OscObjectUtil.createOscObject(attachment));
			this.send(newPeqBandSet);

		}

	}

	private void setInputPeqFLabel(final int channel, final int band,
			final String label) {
		if (channel == this.surface.getState().getCurrentInput()) {
			final IOscMessage newPeqFLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_PEQ_BAND + (band + 1) + "/fLabel"));
			newPeqFLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newPeqFLabel);
		}
	}

	private void setPeqBandQ(final int channel, final int band,
			final float attachment) {
		if (channel == this.surface.getState().getCurrentInput()) {
			final IOscMessage newPeqBandSet = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_PEQ_BAND + (band + 1) + "/q"));
			newPeqBandSet.add(OscObjectUtil.createOscObject(attachment));
			this.send(newPeqBandSet);

		}

	}

	private void setInputPeqQLabel(final int channel, final int band,
			final String label) {
		if (channel == this.surface.getState().getCurrentInput()) {
			final IOscMessage newPeqQLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_IN_PEQ_BAND + (band + 1) + "/qLabel"));
			newPeqQLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newPeqQLabel);
		}
	}

	private void setInputPeqFilterCharacteristics(final int channel, final int band,
			final int attachment) {
		final ArrayList<String> filterType = new ArrayList<String>();

		filterType.add("parametric");
		filterType.add("lowShelf");
		filterType.add("hiShelf");
		filterType.add("lpf");
		filterType.add("hpf");

		final String activeType = filterType.get(attachment); // NOPMD by th on 09.10.12 20:21
		filterType.remove(attachment);

		if (channel == this.surface.getState().getCurrentInput()) {
			final IOscMessage newActiveFilterType = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/input/peq/band/" + (band + 1)
					+ "/type/" + activeType));
			newActiveFilterType.add(OscObjectUtil.createOscObject(1));
			this.send(newActiveFilterType);

			for (int i = 0; i < filterType.size(); i++) {
				final String inactiveType = filterType.get(i);
				final IOscMessage newInactiveFilterType = new OscMessage(OscAddressUtil.create(OSC_PREFIX
						+ "stateful/input/peq/band/" + (band + 1)
						+ "/type/" + inactiveType));
				newInactiveFilterType.add(OscObjectUtil.createOscObject(0));
				this.send(newInactiveFilterType);
			}

		}
	}

	private void setPeqHPFOn(final int channel, final boolean status) {
		if (channel == this.surface.getState().getCurrentInput()) {
			final IOscMessage newPeqHPFOnStatus = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/input/peq/hpfOn"));
			newPeqHPFOnStatus.add(OscObjectUtil.createOscObject(status ? 1.0f : 0.0f));
			this.send(newPeqHPFOnStatus);
		}
	}

	private void setPeqLPFOn(final int channel, final boolean status) {
		if (channel == this.surface.getState().getCurrentInput()) {
			final IOscMessage newPeqLPFOnStatus = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/input/peq/lpfOn"));
			newPeqLPFOnStatus.add(OscObjectUtil.createOscObject(status ? 1.0f : 0.0f));
			this.send(newPeqLPFOnStatus);
		}
	}

	private void setPeqMode(final int channel, final boolean status) {
		if (channel == this.surface.getState().getCurrentInput()) {
			final IOscMessage newPeqModeStatus = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/input/peq/mode"));
			newPeqModeStatus.add(OscObjectUtil.createOscObject(status ? 1.0f : 0.0f));
			this.send(newPeqModeStatus);
			/* set the mode label */
			final IOscMessage newPeqModeLabelStatus = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/input/peq/modeLabel"));
			newPeqModeLabelStatus.add(OscObjectUtil.createOscObject(status ? "Type 2" : "Type 1"));
			this.send(newPeqModeLabelStatus);
		}
	}

	private void setPeqOn(final int channel, final boolean status) {
		if (channel == this.surface.getState().getCurrentInput()) {
			final IOscMessage newPeqOnStatus = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/input/peq/peqOn"));
			newPeqOnStatus.add(OscObjectUtil.createOscObject(status ? 1.0f : 0.0f));
			this.send(newPeqOnStatus);
		}
	}

	protected void setChannelLevel(final int chn, final float level) {
		final IOscMessage newChannelLevel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ NS_INPUT + (chn + 1) + "/level"));
		newChannelLevel.add(OscObjectUtil.createOscObject(level));
		// surface.getInputStripHandler().setCurrentElementValues(28, level);
		this.send(newChannelLevel);

		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newChannelStripLevel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/input/misc/level"));
			newChannelStripLevel.add(OscObjectUtil.createOscObject(level));
			this.surface.getInputStripHandler().setCurrentElementValues(chn, level);
			this.send(newChannelStripLevel);
		}
	}

	private void setChannelLevelLabel(final int chn, final String label) {
		final IOscMessage newLevelLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX + NS_INPUT
				+ (chn + 1) + "/levelLabel"));
		newLevelLabel.add(OscObjectUtil.createOscObject(label));
		this.send(newLevelLabel);
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newStripLevelLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/input/misc/levelLabel"));
			newStripLevelLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newStripLevelLabel);
		}
	}

	private void setChannelOnButton(final int channel, final boolean status) {
		final IOscMessage newChannelOnStatus = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ NS_INPUT + (channel + 1) + "/channelOn"));
		newChannelOnStatus.add(OscObjectUtil.createOscObject(status ? 1.0f : 0.0f));
		this.send(newChannelOnStatus);

		if (channel == this.surface.getState().getCurrentInput()) {
			final IOscMessage newChannelStripOn = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/input/misc/channelOn"));
			newChannelStripOn.add(OscObjectUtil.createOscObject(status ? 1.0f : 0.0f));
			this.send(newChannelStripOn);
		}
	}

	private void setInputPan(final int chn, final float level) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputPan = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/input/misc/pan"));
			final float panLevel = (level / OscConstants.CENTER_CONTROL_SCALE_FACTOR) + OscConstants.CENTER_CONTROL_OFFSET;
			newInputPan.add(OscObjectUtil.createOscObject(panLevel));
			this.send(newInputPan);
		}

	}

	private void setInputPanLabel(final int chn, final String label) {
		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newInputPanLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/input/misc/panLabel"));
			newInputPanLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newInputPanLabel);
		}
	}

	private void setChannelName(final int chn, final String name) {
		final IOscMessage newChannelName = new OscMessage(OscAddressUtil.create(OSC_PREFIX + NS_INPUT
				+ (chn + 1) + "/label"));
		newChannelName.add(OscObjectUtil.createOscObject(name));
		this.send(newChannelName);

		if (chn == this.surface.getState().getCurrentInput()) {
			final IOscMessage newChannelStripName = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/input/misc/channelLabel"));
			newChannelStripName.add(OscObjectUtil.createOscObject(name));
			this.send(newChannelStripName);
		}

		LOG.fine("new name for channel " + (chn + 1) + ": " + name);
	}

	protected void setChannelAuxLevel(final int chn, final int aux,
			final float level) {
		int oscChannelNr = chn + 1;
		int oscAuxNr = aux + 1;
		
		final IOscMessage newAuxSendLevel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ NS_INPUT + oscChannelNr + "/toAux/" + oscAuxNr + "/level"));
		newAuxSendLevel.add(OscObjectUtil.createOscObject(level));
		this.send(newAuxSendLevel);
		if (aux == this.surface.getState().getCurrentAux()) {
			final IOscMessage newChannelLevel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/aux/level/fromChannel/" + oscChannelNr));
			newChannelLevel.add(OscObjectUtil.createOscObject(level));
			this.send(newChannelLevel);
		}
	}

	private void setChannelAuxSendLevelLabel(final int chn, final int aux, final String label) {
		final IOscMessage newAuxLevelLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX + NS_INPUT
				+ (chn + 1) + "/toAux/" + (aux + 1) + "/levelLabel"));
		newAuxLevelLabel.add(OscObjectUtil.createOscObject(label));
		this.send(newAuxLevelLabel);
		if (chn == this.surface.getState().getCurrentInput() || aux == this.surface.getState().getCurrentAux()) {
			final IOscMessage newStatefulAuxLevelLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/aux/levelLabel/fromChannel/" + (chn + 1)));
			newStatefulAuxLevelLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newStatefulAuxLevelLabel);
		}
	}

	private void setChannelAuxSendOn(final int chn, final int aux,
			final boolean status) {
		final IOscMessage newAuxSendStatus = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ NS_INPUT + (chn + 1) + "/toAux/" + (aux + 1) + "/channelOn"));
		newAuxSendStatus.add(OscObjectUtil.createOscObject(status));
		this.send(newAuxSendStatus);
		if (aux == this.surface.getState().getCurrentAux()) {
			final IOscMessage newAuxSendOnStateful = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/aux/channelOn/fromChannel/" + (chn + 1)));
			newAuxSendOnStateful.add(OscObjectUtil.createOscObject(status));
			this.send(newAuxSendOnStateful);
		}
	}

	private void setChannelAuxSendOnLabel(final int chn, final int aux,
			final String label) {
		final IOscMessage newAuxSendOnLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ NS_INPUT + (chn + 1) + "/toAux/" + (aux + 1) + "channelOnLabel"));
		newAuxSendOnLabel.add(OscObjectUtil.createOscObject(label));
		this.send(newAuxSendOnLabel);
		if (aux == this.surface.getState().getCurrentAux()) {
			final IOscMessage newStatefulAuxSendOnLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_STATEFUL + "input/aux/channelOnLabel"));
			newStatefulAuxSendOnLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newStatefulAuxSendOnLabel);
		}
	}

	private void setMasterLevel(final float level) {
		final IOscMessage newChannelLevel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ "output/master/level"));
		newChannelLevel.add(OscObjectUtil.createOscObject(level));
		this.send(newChannelLevel);
	}

	private void changedAuxMaster(final int aux, final float level) {
		final IOscMessage newMasterLevel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ "output/aux/" + (aux + 1) + "/level"));
		newMasterLevel.add(OscObjectUtil.createOscObject(level));
		this.send(newMasterLevel);
		if (aux == this.surface.getState().getCurrentAux()) {
			final IOscMessage newChannelLevel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/aux/level"));
			newChannelLevel.add(OscObjectUtil.createOscObject(level));
			this.send(newChannelLevel);
		}
	}

	private void setAuxMasterLevelLabel(final int auxNumber, final String label) {
		final IOscMessage newLevelLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ "output/aux/" + (auxNumber + 1) + "/levelLabel"));
		newLevelLabel.add(OscObjectUtil.createOscObject(label));
		this.send(newLevelLabel);
		if (auxNumber == this.surface.getState().getCurrentAux()) {
			final IOscMessage newStripLevelLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ "stateful/aux/levelLabel"));
			newStripLevelLabel.add(OscObjectUtil.createOscObject(label));
			this.send(newStripLevelLabel);
		}
	}

	public final void changedAuxDelayTime(final int aux, final float delayTime) {
		final IOscMessage newDelayTime = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ "output/aux/" + (aux + 1) + "/delay"));
		LOG.info("Aux Delay time: " + delayTime);
		newDelayTime.add(OscObjectUtil.createOscObject(delayTime));
		this.send(newDelayTime);
	}

	private void changedBusMaster(final int number, final float level) {
		final IOscMessage newMasterLevel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ "output/bus/" + (number + 1) + "/level"));
		newMasterLevel.add(OscObjectUtil.createOscObject(level));
		this.send(newMasterLevel);
	}

	private void setBusMasterLevelLabel(final int id, final String label) {
		final IOscMessage newLevelLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ "output/bus/" + (id + 1) + "/levelLabel"));
		newLevelLabel.add(OscObjectUtil.createOscObject(label));
		this.send(newLevelLabel);
	}

	public final void changedBusDelayTime(final int busNumber, final float delayTime) {
		final IOscMessage newDelayTime = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ "output/bus/" + (busNumber + 1) + "/delay"));
		newDelayTime.add(OscObjectUtil.createOscObject(delayTime));
		this.send(newDelayTime);
	}

	public final void changedOutputDelayTime(final int outputNumber, final float delayTime) {
		final IOscMessage newDelayTime = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ "output/omni/" + (outputNumber + 1) + "/delay"));
		newDelayTime.add(OscObjectUtil.createOscObject(delayTime));
		this.send(newDelayTime);
	}


	private void changedDcaLevel(final int dcaNumber, final Float level) {
		final IOscMessage newLevel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ "global/dca/" + (dcaNumber + 1) + "/level"));
		newLevel.add(OscObjectUtil.createOscObject(level));
		this.send(newLevel);
	}

	private void setGeqBandLevel(final int eqNumber, final boolean rightChannel,
			final int band, final float floatValue) {
		if (eqNumber == this.surface.getState().getCurrentGEQ()) {
			final IOscMessage newChannelLevel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_GEQ_BAND + (band + 1)
					+ (rightChannel ? "/right/" : "/left/") + "level"));
			newChannelLevel.add(OscObjectUtil.createOscObject((floatValue / OscConstants.CENTER_CONTROL_SCALE_FACTOR) + OscConstants.CENTER_CONTROL_OFFSET));
			this.send(newChannelLevel);
		}
	}

	private void doGeqFullReset(final int eqNumber) {
		if (eqNumber == this.surface.getState().getCurrentGEQ()) {
			final float zeroValue = OscConstants.CENTER_CONTROL_OFFSET; // NOPMD by th on 09.10.12 20:23
			for (int i = 0; i < 2 * GEQ_BANDS; i++) {
				if (i < GEQ_BANDS) {
					final IOscMessage newBandLevel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
							+ NS_GEQ_BAND + (i + 1)
							+ "/left/level"));
					newBandLevel.add(OscObjectUtil.createOscObject(zeroValue));
					this.send(newBandLevel);
				} else {
					final IOscMessage newBandLevel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
							+ NS_GEQ_BAND + (i - GEQ_BANDS + 1)
							+ "/right/level"));
					newBandLevel.add(OscObjectUtil.createOscObject(zeroValue));
					this.send(newBandLevel);
				}
			}
		}
	}

	private void doGeqBandReset(final int eqNumber, final boolean rightChannel, final int band) {
		final float zeroValue = OscConstants.CENTER_CONTROL_OFFSET; // NOPMD by th on 09.10.12 20:22
		if (eqNumber == this.surface.getState().getCurrentGEQ()) {
			final IOscMessage newGeqBandReset = new OscMessage(OscAddressUtil.create(OSC_PREFIX
					+ NS_GEQ_BAND + (band + 1)
					+ (rightChannel ? "/right/" : "/left/") + "level"));
			newGeqBandReset.add(OscObjectUtil.createOscObject(zeroValue));
			this.send(newGeqBandReset);
		}
	}

	public final void setCurrentGeqType(final boolean isFlexEq15) {
		final IOscMessage displayFlexEq = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ "stateful/dsp/geq/isFlexEq15/visible"));
		displayFlexEq.add(OscObjectUtil.createOscObject(isFlexEq15 ? 0.0 : 1.0));
		this.send(displayFlexEq);
	}

	private void setInputGroupStatus(final int type, final int group, final boolean status) {
		String typeString = ""; // NOPMD by th on 09.10.12 20:22
		if (type == 1) {
			typeString = "mute/";
		}
		final IOscMessage newGroupMessage = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ "/input/groups/" + typeString + group));
		newGroupMessage.add(OscObjectUtil.createOscObject(status ? 1.0f : 0.0f));
		this.send(newGroupMessage);
		// group label
		final IOscMessage newGroupLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ "/input/groups/" + typeString + group + "/label"));
		newGroupLabel.add(OscObjectUtil.createOscObject(status ? "On" : "Off"));
		this.send(newGroupLabel);
	}

	private void setOutputGroupStatus(final int type, final int group, final boolean status) {
		String typeString = ""; // NOPMD by th on 09.10.12 20:23
		if (type == 1) {
			typeString = "mute/";
		}
		final IOscMessage newGroupMessage = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ "/output/groups/" + typeString + group));
		newGroupMessage.add(OscObjectUtil.createOscObject(status ? 1.0f : 0.0f));
		this.send(newGroupMessage);
		// group label
		final IOscMessage newGroupLabel = new OscMessage(OscAddressUtil.create(OSC_PREFIX
				+ "/output/groups/" + typeString + group + "/label"));
		newGroupLabel.add(OscObjectUtil.createOscObject(status ? "On" : "Off"));
		this.send(newGroupLabel);
	}

	protected void send(final IOscMessage msg) {
		this.surface.sendMessage(msg);
	}

	protected void shutdown() {

	}

}
