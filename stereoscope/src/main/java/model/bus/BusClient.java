package model.bus;

import model.beans.BusAttendeeBean;
import model.properties.IPropertyProvider;
import model.properties.PropertyCollection;

import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;

/**
 * A client is a fairly generic attendee.
 * @author th
 *
 */
public final class BusClient implements BusAttendee {
	private final IMessageReceiver receiver;
	private final String name;
	private final PropertyCollection properties = new PropertyCollection();

	private final BusAttendeeBean bean;
	private Bus bus;

	public BusClient(final String name, final IMessageReceiver receiver) {
		this.name = name;
		this.receiver = receiver;
		this.bean = new BusAttendeeBean();
	}

	@Override
	public void dispatchMessage(final IMessageWithSender msg) {
		this.receiver.handleNotification(msg);
	}

	@Override
	public IMessageReceiver getDispatchEndpoint() {
		return this.receiver;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public BusAttendeeType getType() {
		return BusAttendeeType.GENERIC_ATTENDEE;
	}

	@Override
	public IPropertyProvider getPropertyProvider() {
		return new IPropertyProvider() {
			@Override
			public PropertyCollection getPropertyCollection() {
				return BusClient.this.properties;
			}
		};
	}

	@Override
	public BusAttendeeBean getBean() {
		return this.bean;
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
		return "Client";
	}

}
