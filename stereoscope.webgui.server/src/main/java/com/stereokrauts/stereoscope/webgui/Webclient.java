package com.stereokrauts.stereoscope.webgui;

import org.vertx.java.core.sockjs.SockJSSocket;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.model.messaging.MessageRelay;
import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelAuxMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractChannelMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractGeqMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputDynamicsMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInputPeqBandMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractInternalMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractLabelMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMasterMessage;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerAuxCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerInputCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerIsMixerWithGraphicalEQ;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerMatrixBusCount;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerName;
import com.stereokrauts.stereoscope.model.messaging.message.internal.RequestMixerOutputCount;
import com.stereokrauts.stereoscope.webgui.api.IWebclient;
import com.stereokrauts.stereoscope.webgui.messaging.FrontendMessageRelay;
import com.stereokrauts.stereoscope.webgui.messaging.JsonMessageSender;
import com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus.FromBusAuxChannelHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus.FromBusGeqHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus.FromBusInputBasicHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus.FromBusInputChannelHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus.FromBusInputDynamicsHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus.FromBusInputPeqBandHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus.FromBusLabelHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus.FromBusOutputHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus.InternalSystemResponseHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.protocol.SockJSHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.toBus.ChannelStripDynamicsMsgHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.toBus.ChannelStripMsgHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.toBus.DelayMsgHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.toBus.GeqMsgHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.toBus.InputChannelMsgHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.toBus.OutputAuxMsgHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.toBus.OutputMsgHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.toBus.StateMsgHandler;
import com.stereokrauts.stereoscope.webgui.messaging.handler.toBus.SystemMsgHandler;

public class Webclient implements IWebclient, IMessageSender {
	private final MessageRelay messageRelayToFrontend;
	private final FrontendMessageRelay messageRelayToBus;
	private final JsonMessageSender frontendModifier;
	private SockJSHandler websocket;
	private IMessageReceiver dispatchEndpoint;
	private WebclientState state;
	public static final SLogger LOG = StereoscopeLogManager
			.getLogger("frontend-msg-handling");
	public static final String OSC_PREFIX = "/stereoscope/";
	public static final float CENTER_CONTROL_OFFSET = 0.5f;
	public static final float CENTER_CONTROL_SCALE_FACTOR = 2.0f;

	private static Webclient instance;
	private final int portNumber;
	private ICommunicationAware communicationAware = new ICommunicationAware() {
		@Override
		public void transmit() {
		}

		@Override
		public void receive() {
		}
	};

	public static Webclient getInstance(final int portNumber) {
		instance = new Webclient(portNumber);
		final WebFrontendInitializer initializer = new WebFrontendInitializer(instance, portNumber);
		instance.setWebsocket(initializer.getSockJSHandler());
		LOG.info(String.format("Websocket: Listening on port %s.", portNumber));
		return instance;
	}

	private Webclient(final int portNumber) {
		this.portNumber = portNumber;
		this.setState(new WebclientState());
		this.getState().setGeqBandCount(31);
		this.messageRelayToFrontend = new MessageRelay("webclient");
		this.messageRelayToBus = new FrontendMessageRelay(this);
		this.frontendModifier = new JsonMessageSender(this);
		this.messageRelayToFrontend.registerMessageHandler(AbstractChannelMessage.class, new FromBusInputChannelHandler(this));
		this.messageRelayToFrontend.registerMessageHandler(AbstractChannelAuxMessage.class, new FromBusAuxChannelHandler(this));
		this.messageRelayToFrontend.registerMessageHandler(AbstractMasterMessage.class, new FromBusOutputHandler(this));
		this.messageRelayToFrontend.registerMessageHandler(AbstractInputMessage.class, new FromBusInputBasicHandler(this));
		this.messageRelayToFrontend.registerMessageHandler(AbstractInputPeqBandMessage.class, new FromBusInputPeqBandHandler(this));
		this.messageRelayToFrontend.registerMessageHandler(AbstractInputDynamicsMessage.class, new FromBusInputDynamicsHandler(this));
		this.messageRelayToFrontend.registerMessageHandler(AbstractGeqMessage.class, new FromBusGeqHandler(this));
		this.messageRelayToFrontend.registerMessageHandler(AbstractInternalMessage.class, new InternalSystemResponseHandler(this));
		this.messageRelayToFrontend.registerMessageHandler(AbstractLabelMessage.class, new FromBusLabelHandler(this));
		this.messageRelayToBus.registerFrontendMessageHandler(new InputChannelMsgHandler(this));
		this.messageRelayToBus.registerFrontendMessageHandler(new OutputMsgHandler(this));
		this.messageRelayToBus.registerFrontendMessageHandler(new OutputAuxMsgHandler(this));
		this.messageRelayToBus.registerFrontendMessageHandler(new ChannelStripMsgHandler(this));
		this.messageRelayToBus.registerFrontendMessageHandler(new ChannelStripDynamicsMsgHandler(this));
		this.messageRelayToBus.registerFrontendMessageHandler(new DelayMsgHandler(this));
		this.messageRelayToBus.registerFrontendMessageHandler(new GeqMsgHandler(this));
		this.messageRelayToBus.registerFrontendMessageHandler(new SystemMsgHandler(this));
		this.messageRelayToBus.registerFrontendMessageHandler(new StateMsgHandler(this));

		
	}

	public void requestMixerState() {
		this.fireChange(new RequestMixerInputCount(true));
		this.fireChange(new RequestMixerAuxCount(true));
		this.fireChange(new RequestMixerBusCount(true));
		this.fireChange(new RequestMixerMatrixBusCount(true));
		this.fireChange(new RequestMixerOutputCount(true));
		this.fireChange(new RequestMixerName(true));
		this.fireChange(new RequestMixerIsMixerWithGraphicalEQ(true));
	}

	public void fireChange(final AbstractMessage<?> theChange) {
		communicationAware.transmit();
		this.getMessageDispatcher().handleNotification(new MessageWithSender(this, theChange));
	}

	public  FrontendMessageRelay getToBusRelay() {
		return this.messageRelayToBus;
	}

	public SockJSSocket getWebsocket() {
		return websocket.getSocket();
	}

	public void setWebsocket(final SockJSHandler websocket) {
		this.websocket = websocket;
	}

	public JsonMessageSender getFrontendModifier() {
		return this.frontendModifier;
	}

	@Override
	public void handleNotification(final IMessageWithSender msg) {
		communicationAware.receive();
		final IMessage message = msg.getMessage();
		if (!this.messageRelayToFrontend.handleMessage(message)) {
			LOG.info("No message handler registered for message " + msg);
		}

	}

	public final void setState(final WebclientState myState) {
		this.state = myState;
	}

	public final WebclientState getState() {
		return this.state;
	}

	@Override
	public void registerMessageDispatcher(final IMessageReceiver dispatchEndpoint) {
		this.dispatchEndpoint = dispatchEndpoint;
	}

	public IMessageReceiver getMessageDispatcher() {
		return dispatchEndpoint;
	}

	@Override
	public int getPortNumber() {
		return portNumber;
	}

	@Override
	public void setCommunicationObserver(final ICommunicationAware communicationAware) {
		this.communicationAware = communicationAware;
	}

}
