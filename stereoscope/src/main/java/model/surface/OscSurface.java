package model.surface;

import java.util.ArrayList;
import java.util.List;

import model.beans.ClientBean;
import model.beans.ConnectionServerTcpUdpIpBean;
import model.beans.ConnectionTcpUdpIpBean;
import model.beans.OscSurfaceBean;
import model.bus.Bus;
import model.bus.BusAttendeeSurfaceTouchOsc;
import model.mixer.interfaces.IMixerWithGraphicalEq;
import model.mixer.interfaces.IProvideChannelNames;
import model.protocol.osc.IOscMessage;
import model.protocol.osc.ISendOscMessages;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscConstants;
import model.protocol.osc.OscMessageRelay;
import model.protocol.osc.OscObjectUtil;
import model.protocol.osc.handler.OscAllMessagesHandler;
import model.protocol.osc.handler.OscDelayMsgHandler;
import model.protocol.osc.handler.OscGenericMsgHandler;
import model.protocol.osc.handler.OscGraphicalEqMsgHandler;
import model.protocol.osc.handler.OscGroupsMsgHandler;
import model.protocol.osc.handler.OscInputChannelMsgHandler;
import model.protocol.osc.handler.OscInputChannelStripDynamicsMsgHandler;
import model.protocol.osc.handler.OscInputChannelStripMsgHandler;
import model.protocol.osc.handler.OscMixerGlobalMsgHandler;
import model.protocol.osc.handler.OscOutputAuxMsgHandler;
import model.protocol.osc.handler.OscOutputMsgHandler;
import model.protocol.osc.handler.OscStateMsgHandler;
import model.protocol.osc.impl.OscMessage;
import aspects.observer.IAspectedObserver;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IObservableMessageSender;
import com.stereokrauts.stereoscope.model.messaging.dispatching.CoreMessageDispatcher;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;
import com.stereokrauts.stereoscope.model.messaging.message.control.MsgResyncStateful;
import com.stereokrauts.stereoscope.model.messaging.message.control.MsgSelectedAuxChanged;

/**
 * This class encapsulates OSC functions and has a central role
 * in controling all aspects of the OSC subsystem.
 * @author theide
 *
 */
public class OscSurface implements ISendOscMessages, IObservableMessageSender, IAspectedObserver, ICommunicationAware {
	private static final SLogger LOG = StereoscopeLogManager.getLogger(OscSurface.class);

	public static final int GEQ_BANDS = 31;

	private CoreMessageDispatcher theObserver = null;
	private final List<IMessageReceiver> observers;
	private OscSurfaceState state;

	protected OscSurfaceConnection connection;

	protected OscMessageRelay oscRelay;
	protected IOscMessageFromSurfaceHandlers handlers = new IOscMessageFromSurfaceHandlers();

	protected OscMessageSender sender;

	private final OscSurfaceBean bean;

	private BusAttendeeSurfaceTouchOsc busAttendee;

	public final OscMessageSender getOscMessageSender() {
		return this.sender;
	}

	public OscSurface() {
		this.bean = new OscSurfaceBean();
		this.getBean().setRemoteConnParams(new ConnectionTcpUdpIpBean());
		this.getBean().setLocalConnParams(new ConnectionServerTcpUdpIpBean());
		/* set default values */
		this.getBean().getLocalConnParams().setPortNumber(OscConstants.DEFAULT_OSC_SERVER_PORT);
		this.getBean().getRemoteConnParams().setUseUdp(true);
		this.getBean().setClientConfig(new ClientBean());
		this.getBean().getClientConfig().setSnapFaders(true);
		this.getBean().getClientConfig().setRestrictedClient(false);

		this.setState(new OscSurfaceState());
		this.getBean().getObserverManager().addObserver(this);

		this.oscRelay = new OscMessageRelay(this);
		this.initializeHandlers();
		this.sender = new OscMessageSender(this);

		this.observers = new ArrayList<IMessageReceiver>();
		this.connection = new OscSurfaceConnection(this, this.getBean().getRemoteConnParams(), this.getBean().getLocalConnParams());
	}

	/**
	 * This method initializes all handlers used by this class.
	 */
	private void initializeHandlers() {
		this.setInputHandler(new OscInputChannelMsgHandler());
		this.setOutputAuxHandler(new OscOutputAuxMsgHandler());
		this.setInputStripHandler(new OscInputChannelStripMsgHandler());
		this.handlers.setAllHandler(new OscAllMessagesHandler(this));
		this.handlers.setDelayHandler(new OscDelayMsgHandler());
		this.handlers.setGenericHandler(new OscGenericMsgHandler());
		this.handlers.setGeqHandler(new OscGraphicalEqMsgHandler());
		this.handlers.setGroupsHandler(new OscGroupsMsgHandler());
		this.handlers.setOutputHandler(new OscOutputMsgHandler());
		this.handlers.setStateHandler(new OscStateMsgHandler());
		this.handlers.setDynamicsHandler(new OscInputChannelStripDynamicsMsgHandler());
		this.handlers.setMixerGlobalHandler(new OscMixerGlobalMsgHandler());

		this.oscRelay.registerIOscMessageHandler(this.handlers.getInputHandler());
		this.oscRelay.registerIOscMessageHandler(this.handlers.getOutputAuxHandler());
		this.oscRelay.registerIOscMessageHandler(this.handlers.getInputStripHandler());
		this.oscRelay.registerIOscMessageHandler(this.handlers.getAllHandler());
		this.oscRelay.registerIOscMessageHandler(this.handlers.getDelayHandler());
		this.oscRelay.registerIOscMessageHandler(this.handlers.getGenericHandler());
		this.oscRelay.registerIOscMessageHandler(this.handlers.getGeqHandler());
		this.oscRelay.registerIOscMessageHandler(this.handlers.getGroupsHandler());
		this.oscRelay.registerIOscMessageHandler(this.handlers.getOutputHandler());
		this.oscRelay.registerIOscMessageHandler(this.handlers.getStateHandler());
		this.oscRelay.registerIOscMessageHandler(this.handlers.getDynamicsHandler());
		this.oscRelay.registerIOscMessageHandler(this.handlers.getMixerGlobalMsgHandler());
	}

	public void connect() {
		if (this.connection == null) {
			this.connection = new OscSurfaceConnection(this, this.getBean().getRemoteConnParams(), this.getBean().getLocalConnParams());
		}
		this.connection.connect();
	}

	public void disconnect() {
		if (this.connection != null) {
			this.connection.disconnect();
		}
		this.connection = null;
	}

	@Override
	public final synchronized void registerObserver(final IMessageReceiver obs) {
		if (obs instanceof CoreMessageDispatcher) {
			this.theObserver = (CoreMessageDispatcher) obs;
		} else {
			this.observers.add(obs);
		}
	}

	public final synchronized void removeObserver(final IMessageReceiver obs) {
		if (obs instanceof CoreMessageDispatcher) {
			this.theObserver = null;
		} else {
			this.observers.remove(obs);
		}
	}

	@Override
	public synchronized void sendMessage(final IOscMessage m) {
		if (this.connection != null && this.connection.getMyRemoteLocation() != null) {
			this.connection.sendMessage(m);
		} else {
			LOG.warning("Could not send OSC message " + m + " to client, as no connection is open!");
		}
	}

	/**
	 * This method gets called when an aux button is pressed.
	 * It updates the channel levels and the message that indicates the
	 * currently selected aux.
	 */
	private void changeSelectedAux(final int aux) {
		IOscMessage newAux = new OscMessage(OscAddressUtil.create(OscMessageSender.OSC_PREFIX
				+ "system/state/selectedAux/label"));

		if (this.getState().isStickedToAux()) {
			newAux.add(OscObjectUtil.createOscObject("Sticky: AUX " + (aux + 1)));
		} else {
			newAux.add(OscObjectUtil.createOscObject("You are on AUX " + (aux + 1)));
		}
		this.sendMessage(newAux);

		for (int i = 0; i < this.getBus().getTheMixer().getAuxCount(); i++) {
			newAux = new OscMessage(OscAddressUtil.create(OscMessageSender.OSC_PREFIX
					+ "system/state/selectedAux/changeTo/" + (i + 1)));
			/* switch buttons on surface */
			if (i != this.getState().getCurrentAux()) {
				newAux.add(OscObjectUtil.createOscObject(0.0));
			} else {
				newAux.add(OscObjectUtil.createOscObject(1.0));
			}
			this.sendMessage(newAux);
		}

		this.getBus().getTheMixer().getAllAuxLevels(aux);
		this.getBus().getTheMixer().getAllAuxChannelOn(aux);
		this.fireAuxSelectionChange(aux);
	}

	private void fireAuxSelectionChange(final int aux) {
		for (final IMessageReceiver observer : this.observers) {
			observer.handleNotification(new MessageWithSender(this,
					new MsgSelectedAuxChanged(aux)));
		}
	}

	/**
	 * This method gets called when a GEQ button is pressed.
	 * It updates the display of the GEQ page to represent the new selected GEQ.
	 */
	private synchronized void changeSelectedGeq(final byte currentGEQ) {
		if (!(this.getBus().getTheMixer() instanceof IMixerWithGraphicalEq)) {
			return;
		}
		final IMixerWithGraphicalEq mixer = (IMixerWithGraphicalEq) this.getBus().getTheMixer();

		IOscMessage newGeq = new OscMessage(OscAddressUtil.create(OscMessageSender.OSC_PREFIX
				+ "system/state/selectedGeq/label"));
		newGeq.add(OscObjectUtil.createOscObject("You selected GEQ " + (this.getState().getCurrentGEQ() + 1)));
		this.sendMessage(newGeq);

		for (int i = 0; i < mixer.getGeqCount(); i++) {
			newGeq = new OscMessage(OscAddressUtil.create(OscMessageSender.OSC_PREFIX
					+ "system/state/selectedGeq/changeTo/" + (i + 1)));
			/* switch buttons on surface */
			if (i != currentGEQ) {
				newGeq.add(OscObjectUtil.createOscObject(0.0));
			} else {
				newGeq.add(OscObjectUtil.createOscObject(1.0));
			}
			this.sendMessage(newGeq);
		}

		mixer.isFlexEQ(currentGEQ);
		mixer.getAllGeqLevels(currentGEQ);
	}

	/**
	 * This method gets called when an input button is
	 * pressed. It updates the display of the input page (channel strip) to
	 * represent the new selected input.
	 */
	private synchronized void changeSelectedInput(final int input) {

		IOscMessage newInput = new OscMessage(OscAddressUtil.create(OscMessageSender.OSC_PREFIX
				+ "system/state/selectedInput/label"));
		newInput.add(OscObjectUtil.createOscObject("You are on input " + (input + 1)));
		this.sendMessage(newInput);

		for (int i = 0; i < this.getBus().getTheMixer().getChannelCount(); i++) {
			newInput = new OscMessage(OscAddressUtil.create(OscMessageSender.OSC_PREFIX
					+ "system/state/selectedInput/changeTo/" + (i + 1)));
			/* switch buttons on surface */
			if (i != input) {
				newInput.add(OscObjectUtil.createOscObject(0.0));
			} else {
				newInput.add(OscObjectUtil.createOscObject(1.0));
			}
			this.sendMessage(newInput);
		}

		this.getBus().getTheMixer().getAllInputValues(input);
	}

	public final synchronized void updateClientViewAux() {
		this.changeSelectedAux(this.getState().getCurrentAux());
		this.theObserver.getChannelNames();
	}

	public final synchronized void updateClientViewGEQ() {
		this.changeSelectedGeq(this.getState().getCurrentGEQ());
	}

	public final synchronized void updateClientViewInput() {
		LOG.fine("updateClientViewInput: " + this.getBus().getTheMixer());
		this.changeSelectedInput(this.getState().getCurrentInput());
	}


	public final synchronized void updateClientViewChannelNames() {
		LOG.fine("updateClientViewChannelNames: " + this.getBus().getTheMixer());
		if (this.getBus().getTheMixer() instanceof IProvideChannelNames) {
			((IProvideChannelNames) this.getBus().getTheMixer()).getChannelNames();
		}
	}

	public final synchronized void updateClientViewChannels() {
		LOG.fine("updateClientViewChannels: " + this.getBus().getTheMixer());
		this.getBus().getTheMixer().getAllChannelLevels();
		this.getBus().getTheMixer().getAllChannelOnButtons();
		if (this.getBus().getTheMixer() instanceof IProvideChannelNames) {
			((IProvideChannelNames) this.getBus().getTheMixer()).getChannelNames();
		}
	}

	public final synchronized void updateClientViewDelay() {
		LOG.fine("updateClientViewDelay: " + this.getBus().getTheMixer());
		this.getBus().getTheMixer().getAllDelayTimes();
		this.getBus().getTheMixer().getAllGroupsStatus();
		this.getBus().getTheMixer().getAllBusStatus();
	}

	public final synchronized boolean isStickyAux() {
		return this.getState().isStickedToAux();
	}


	public final synchronized ConnectionTcpUdpIpBean getConnection() {
		return this.getBean().getRemoteConnParams();
	}


	public final synchronized byte getCurrentAux() {
		return 0;
	}

	public synchronized void fireChanged(final AbstractMessage<?> theChange) {
		this.theObserver
		.handleNotification(new MessageWithSender(this.sender, theChange));
	}

	public final synchronized void requestAllParameters() {
		LOG.entering(this.getClass().toString(), "requestAllParameters");
		this.theObserver.handleNotification(new MessageWithSender(this.sender,
				new MsgResyncStateful(this.getState().getCurrentInput(), this.getState()
						.getCurrentAux(), this.getState().getCurrentGEQ())));
	}

	public final void setState(final OscSurfaceState myState) {
		this.state = myState;
	}

	public final OscSurfaceState getState() {
		return this.state;
	}

	public final void setInputHandler(final OscInputChannelMsgHandler inputHandler) {
		this.handlers.setInputHandler(inputHandler);
	}

	public OscInputChannelMsgHandler getInputHandler() {
		return this.handlers.getInputHandler();
	}

	public final void setOutputAuxHandler(final OscOutputAuxMsgHandler outputAuxHandler) {
		this.handlers.setOutputAuxHandler(outputAuxHandler);
	}

	public OscOutputAuxMsgHandler getOutputAuxHandler() {
		return this.handlers.getOutputAuxHandler();
	}

	public final void setInputStripHandler(
			final OscInputChannelStripMsgHandler inputStripMsgHandler) {
		this.handlers.setInputStripHandler(inputStripMsgHandler);
	}

	public final OscInputChannelStripMsgHandler getInputStripHandler() {
		return this.handlers.getInputStripHandler();
	}

	public final void shutdown() {
		this.sender.shutdown();
		this.disconnect();
		this.sender = null;
		this.connection = null;
	}

	public final OscSurfaceBean getBean() {
		return this.bean;
	}

	protected final void setBean(final OscSurfaceBean bean) {
		this.bean.getObserverManager().removeObserver(this);
		this.bean.getClientConfig().setAccessibleAux(bean.getClientConfig().getAccessibleAux());
		this.bean.getClientConfig().setRestrictedClient(bean.getClientConfig().getRestrictedClient());
		this.bean.getClientConfig().setSnapFaders(bean.getClientConfig().getSnapFaders());
		this.bean.getClientConfig().setClientName(bean.getClientConfig().getClientName());
		if (bean.getLocalConnParams() != null) {
			this.connection.getServerBean().setPortNumber(bean.getLocalConnParams().getPortNumber());
		}
		if (bean.getRemoteConnParams() != null) {
			this.connection.getBean().setNetworkAddress(bean.getRemoteConnParams().getNetworkAddress());
			this.connection.getBean().setPortNumber(bean.getRemoteConnParams().getPortNumber());
			this.connection.getBean().setUseUdp(bean.getRemoteConnParams().getUseUdp());
		}
		this.bean.getClientConfig().getObserverManager().addObserver(this);
		this.bean.getObserverManager().addObserver(this);
	}

	@Override
	public void valueChangedEvent(final Object sender, final String fieldName, final Object oldValue, final Object newValue) {
		LOG.info("value changed event: " + sender + ", " + fieldName + ", " + oldValue + ", " + newValue);
		if (fieldName.equals("restrictedClient") || fieldName.equals("accessibleAux")) {
			if (Boolean.TRUE.equals(this.getBean().getClientConfig().getRestrictedClient())) {
				if (this.getBean().getClientConfig().getAccessibleAux() != null) {
					this.setStickyAux(this.getBean().getClientConfig().getAccessibleAux());
				}
			} else {
				if (this.isStickyAux()) {
					this.releaseStickyAux();
				}
			}
		}
		if (fieldName.equals("snapFaders")) {
			this.setSnapFaders(this.getBean().getClientConfig().getSnapFaders());
		}
	}


	private synchronized void setStickyAux(final int currentAux) {
		this.getState().setStickyAux(currentAux);
		this.changeSelectedAux(currentAux);
	}

	private synchronized void releaseStickyAux() {
		this.getState().releaseStickyAux();
		this.changeSelectedAux(this.getState().getCurrentAux());
	}

	protected synchronized void setSnapFaders(final boolean selected) {

	}

	public final void setBusAttendee(final BusAttendeeSurfaceTouchOsc busAttendee) {
		this.busAttendee = busAttendee;
	}

	public final Bus getBus() {
		return this.busAttendee.getBus();
	}

	@Override
	public void receive() {
		busAttendee.receive();
	}

	@Override
	public void transmit() {
		busAttendee.transmit();
	}

	public void oscEvent(final IOscMessage adapt) {
		oscRelay.oscEvent(adapt);
	}


}