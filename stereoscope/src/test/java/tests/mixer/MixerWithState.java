package tests.mixer;


import java.awt.image.BufferedImage;
import java.util.ArrayList;

import model.mixer.interfaces.IAmMixer;
import model.mixer.interfaces.IMixerWithGraphicalEq;
import model.mixer.interfaces.IProvideChannelNames;
import tests.simulation.SimulationMessage;

import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IObservableMessageSender;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelAuxMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractGeqMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage.SECTION;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqBandReset;
import com.stereokrauts.stereoscope.model.messaging.message.dsp.MsgGeqFullReset;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgAuxSendOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelNameChanged;
import com.stereokrauts.stereoscope.model.messaging.message.inputs.MsgChannelOnChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgAuxMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgBusMasterDelayChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgMasterLevelChanged;
import com.stereokrauts.stereoscope.model.messaging.message.outputs.MsgOutputDelayChanged;

/**
 * This class simulates a mixer, which can hold a state. Meaning: if you
 * set a value to "10" on this class and request it later, you will get that
 * "10" back. This is an "advanced" loopback mixer.
 * 
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 */
public class MixerWithState extends IAmMixer implements IMixerWithGraphicalEq, IProvideChannelNames, IMessageReceiver, IObservableMessageSender {

	private final ArrayList<Integer> messageTypesReceived;
	private synchronized void messageReceived(final int type) {
		this.messageTypesReceived.add(type);
		this.notifyAll();
	}

	private final MixerState myState;
	private IMessageReceiver myObserver = null;

	public MixerWithState(final int channels, final int auxs, final int busses, final int outputs, final int geqs) {
		this.myState = new MixerState(channels, auxs, busses, outputs, geqs);
		this.messageTypesReceived = new ArrayList<Integer>();
	}


	@Override
	public synchronized void registerObserver(final IMessageReceiver observer) {
		this.myObserver = observer;
	}


	@Override
	public synchronized void getChannelNames() {
		for (int i = 0; i < this.myState.getNumberOfChannels(); i++) {
			this.myObserver.handleNotification(new MessageWithSender(this, new MsgChannelNameChanged(i, this.myState.getChannelName(i))));
		}
	}


	@Override
	public synchronized void getGeqBandLevel(final int eqNumber, final boolean rightChannel, final int band) {
		this.myObserver.handleNotification(new MessageWithSender(this, new MsgGeqBandLevelChanged((short) eqNumber, band, rightChannel,
				this.myState.getGeqBandLevels(eqNumber, rightChannel, band))));
	}


	@Override
	public int getGeqCount() {
		return this.myState.getNumberOfGeqs();
	}


	@Override
	public synchronized void getAllGeqLevels(final byte eqNumber) {
		for (int i = 0; i < MixerState.GEQ_NUMBER_OF_BANDS; i++) {
			this.myObserver.handleNotification(new MessageWithSender(this, new MsgGeqBandLevelChanged(eqNumber, i, true,
					this.myState.getGeqBandLevels(eqNumber, true, i))));
			this.myObserver.handleNotification(new MessageWithSender(this, new MsgGeqBandLevelChanged(eqNumber, i, false,
					this.myState.getGeqBandLevels(eqNumber, true, i))));
		}
	}


	@Override
	public synchronized void getAuxMaster(final int aux) {
		this.myObserver.handleNotification(new MessageWithSender(this, new MsgAuxMasterLevelChanged(aux, this.myState.getAuxMasters(aux))));
	}


	@Override
	public synchronized void getChannelLevel(final int channel) {
		this.myObserver.handleNotification(new MessageWithSender(this, new MsgChannelLevelChanged(channel, this.myState.getChannelLevel(channel))));
	}


	@Override
	public synchronized void getChannelAux(final int channel, final int aux) {
		this.myObserver.handleNotification(new MessageWithSender(this, new MsgAuxSendChanged(channel, aux, this.myState.getChannelAuxSend(aux, channel))));
	}

	@Override
	public synchronized void getAuxChannelOnButton(final int channel, final int aux) {
		//stub
	}

	@Override
	public synchronized void getAllAuxChannelOn(final int aux) {
		//stub
	}


	@Override
	public synchronized void getChannelOnButton(final int channel) {
		this.myObserver.handleNotification(new MessageWithSender(this, new MsgChannelOnChanged(channel, this.myState.getChannelOnButtons(channel))));
	}


	@Override
	public synchronized void getAllAuxLevels(final int aux) {
		for (int i = 0; i < this.myState.getNumberOfChannels(); i++) {
			this.myObserver.handleNotification(new MessageWithSender(this, new MsgAuxSendChanged(i, aux, this.myState.getChannelAuxSend(aux, i))));
		}
	}


	@Override
	public synchronized void getAllChannelLevels() {
		for (int i = 0; i < this.myState.getNumberOfChannels(); i++) {
			this.myObserver.handleNotification(new MessageWithSender(this, new MsgChannelLevelChanged(i, this.myState.getChannelLevel(i))));
		}
	}


	@Override
	public synchronized void getAllChannelOnButtons() {
		for (int i = 0; i < this.myState.getNumberOfChannels(); i++) {
			this.myObserver.handleNotification(new MessageWithSender(this, new MsgChannelOnChanged(i, this.myState.getChannelOnButtons(i))));
		}
	}


	@Override
	public int getChannelCount() {
		return this.myState.getNumberOfChannels();
	}


	@Override
	public int getAuxCount() {
		return this.myState.getNumberOfAux();
	}

	public MixerState getMixerState() {
		return this.myState;
	}


	public synchronized void waitForMessage(final int MessageType) throws InterruptedException {
		while (!this.messageTypesReceived.contains(MessageType)) {
			this.wait();
		}
		this.messageTypesReceived.clear();
	}


	@Override
	public int getBusCount() {
		return this.myState.getNumberOfBus();
	}


	@Override
	public int getOutputCount() {
		return this.myState.getNumberOfOutputs();
	}


	@Override
	public synchronized void getAllDelayTimes() {
		for (int i = 0; i < this.getAuxCount(); i++) {
			this.myObserver.handleNotification(new MessageWithSender(this, new MsgAuxMasterDelayChanged(i, this.myState.getAuxDelay(i))));
		}
		for (int i = 0; i < this.getBusCount(); i++) {
			this.myObserver.handleNotification(new MessageWithSender(this, new MsgBusMasterDelayChanged(i, this.myState.getBusDelay(i))));
		}
	}


	public BufferedImage getSmallImage() {
		return null;
	}


	@Override
	public synchronized void handleNotification(final IMessageWithSender message) {
		if (!(message instanceof AbstractMessage<?>)) {
			return;
		}
		final AbstractMessage<?> msg = (AbstractMessage<?>) message.getMessage();
		if (msg instanceof AbstractChannelMessage) {
			this.handleChannelMessage(msg);
		} else if (msg instanceof AbstractChannelAuxMessage) {
			this.handleChannelAuxMessage(msg);
		} else if (msg instanceof AbstractGeqMessage) {
			this.handleGeqMessage(msg);
		} else if (msg instanceof AbstractMasterMessage) {
			this.handleMasterMessage(msg);
		}
	}

	private void handleMasterMessage(final AbstractMessage<?> msg) {
		final AbstractMasterMessage<?> ammsg = ((AbstractMasterMessage<?>) msg);
		final SECTION section = ammsg.getSection();
		final int number = ammsg.getNumber();
		if (section == SECTION.AUX) {
			if (msg instanceof MsgAuxMasterLevelChanged) {
				this.myState.setAuxMasters(number, (Float) ammsg.getAttachment());
				this.messageReceived(SimulationMessage.MESSAGE_TYPE_AUXMASTER);
			}
		} else if (section == SECTION.BUS) {
			if (msg instanceof MsgBusMasterDelayChanged) {
				this.myState.setBusMasters(number, (Float) ammsg.getAttachment());
				this.messageReceived(SimulationMessage.MESSAGE_TYPE_BUSMASTER);
			}
		} else if (section == SECTION.OUTPUT) {
			if (msg instanceof MsgOutputDelayChanged) {
				this.myState.setOutputDelay(number, (Float) ammsg.getAttachment());
				this.messageReceived(SimulationMessage.MESSAGE_TYPE_OUTDELAY);
			}
		} else if (section == SECTION.MASTER) {
			if (msg instanceof MsgMasterLevelChanged) {
				this.myState.setMasterLevel((Float) ammsg.getAttachment());
				this.messageReceived(SimulationMessage.MESSAGE_TYPE_MASTER);
			}
		}
	}

	private void handleGeqMessage(final AbstractMessage<?> msg) {
		final short geqNumber = ((AbstractGeqMessage<?>) msg).getGeqNumber();
		if (msg instanceof MsgGeqFullReset) {
			for (int i = 0; i < MixerState.GEQ_NUMBER_OF_BANDS; i++) {
				this.myState.setGeqBandLevels(geqNumber, true, i, 0.0f);
				this.myState.setGeqBandLevels(geqNumber, false, i, 0.0f);
			}
			this.messageReceived(SimulationMessage.MESSAGE_TYPE_GEQFULLRESET);
		} else if (msg instanceof MsgGeqBandLevelChanged) {
			final MsgGeqBandLevelChanged msgGeqBandLevelChanged = (MsgGeqBandLevelChanged) msg;
			final boolean rightChannel = msgGeqBandLevelChanged.isRightChannel();
			final int band = msgGeqBandLevelChanged.getBand();
			final float floatValue = msgGeqBandLevelChanged.getAttachment();
			this.myState.setGeqBandLevels(geqNumber, rightChannel, band, floatValue);
		} else if (msg instanceof MsgGeqBandReset) {
			final MsgGeqBandReset msgGeqBandReset = (MsgGeqBandReset) msg;
			final boolean rightChannel = msgGeqBandReset.isRightChannel();
			final int band = msgGeqBandReset.getBand();
			this.myState.setGeqBandLevels(geqNumber, rightChannel, band, 0.0f);
		}
	}

	private void handleChannelAuxMessage(final AbstractMessage<?> msg) {
		if (msg instanceof MsgAuxSendChanged) {
			final MsgAuxSendChanged msgAuxSendChanged = (MsgAuxSendChanged) msg;
			this.myState.setChannelAuxSend(msgAuxSendChanged.getAux(), msgAuxSendChanged.getChannel(), msgAuxSendChanged.getAttachment());
			this.messageReceived(SimulationMessage.MESSAGE_TYPE_CHAUXLEVEL);
		}
	}

	private void handleChannelMessage(final AbstractMessage<?> msg) {
		final int chn = ((AbstractChannelMessage<?>) msg).getChannel();
		if (msg instanceof MsgChannelLevelChanged) {
			final MsgChannelLevelChanged msgChannelLevelChanged = (MsgChannelLevelChanged) msg;
			this.myState.setChannelLevel(chn, msgChannelLevelChanged.getAttachment());
			this.messageReceived(SimulationMessage.MESSAGE_TYPE_CHLEVEL);
		} else if (msg instanceof MsgChannelOnChanged) {
			final MsgChannelOnChanged msgChannelOnChanged = (MsgChannelOnChanged) msg;
			this.myState.setChannelOnButtons(chn, msgChannelOnChanged.getAttachment());
			this.messageReceived(SimulationMessage.MESSAGE_TYPE_CHONBUTTON);
		}
	}


	@Override
	public void isFlexEQ(final short eqNumber) {
		/* ignore */
	}


	@Override
	public void getAllInputValues(final int inputChn) {
		// TODO rjansen Auto-generated method stub

	}


	@Override
	public int getMatrixCount() {
		// TODO rjansen Auto-generated method stub
		return 0;
	}


	@Override
	public void getAllGroupsStatus() {

	}


	@Override
	public void getBusMaster(final int bus) {

	}


	@Override
	public void getAllBusStatus() {

	}
	
	@Override
	public void getOutputMaster() {
		
	}


	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return null;
	}
}
