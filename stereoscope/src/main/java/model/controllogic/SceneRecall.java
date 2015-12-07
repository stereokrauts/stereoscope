package model.controllogic;


import model.beans.BusAttendeeBean;
import model.bus.Bus;
import model.bus.BusAttendee;
import model.bus.BusAttendeeType;
import model.properties.IPropertyProvider;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.model.messaging.MessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.control.MsgResyncEverything;
import com.stereokrauts.stereoscope.model.messaging.message.control.MsgSceneChange;

/**
 * This class implements the scene recall behaviour of
 * stereoscope.
 * 
 * @author theide
 */
public final class SceneRecall implements IMessageReceiver, IMessageSender, BusAttendee {
	private static final SLogger LOG = StereoscopeLogManager.getLogger("control-logic-scene-recall");

	private Bus bus;

	@Override
	public void handleNotification(final IMessageWithSender msg) {
		if (msg.getMessage() instanceof MsgSceneChange) {
			final MsgSceneChange message = (MsgSceneChange) msg.getMessage();
			LOG.info("Scene change to " +  message.getAttachment() + " requested by " + ((MessageWithSender) msg).getSender());
			this.requestCompleteResync();
		}
	}

	private void requestCompleteResync() {
		this.bus.getDispatchEndpoint().handleNotification(new MessageWithSender(this, new MsgResyncEverything(null)));
	}

	@Override
	public void dispatchMessage(final IMessageWithSender msg) {
		/* ignore all messages that are sent to us */
	}

	@Override
	public IMessageReceiver getDispatchEndpoint() {
		return new IMessageReceiver() {
			@Override
			public void handleNotification(final IMessageWithSender msg) {
				/* ignore everything */
			}
		};
	}

	@Override
	public String getName() {
		return "Scene recall control logic";
	}

	@Override
	public BusAttendeeType getType() {
		return BusAttendeeType.CONTROL_LOGIC;
	}

	@Override
	public IPropertyProvider getPropertyProvider() {
		return null;
	}

	@Override
	public BusAttendeeBean getBean() {
		return null;
	}

	@Override
	public Bus getBus() {
		return this.bus;
	}

	@Override
	public void setBus(final Bus bus) {
		this.bus = bus;
	}

	@Override
	public void startup() {
	}

	@Override
	public void shutdown() {
	}

	@Override
	public String getIdentifier() {
		return "SceneRecall";
	}

}
