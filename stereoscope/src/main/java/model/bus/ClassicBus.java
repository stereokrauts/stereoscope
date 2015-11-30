package model.bus;

import java.util.ArrayList;
import java.util.List;

import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageSender;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.dispatching.CoreMessageDispatcher;
import com.stereokrauts.stereoscope.model.messaging.dispatching.CoreMessageDispatcherFactory;

import model.beans.BusAttendeeBean;
import model.beans.BusBean;
import model.mixer.interfaces.IAmMixer;
import model.properties.IPropertyProvider;
import model.properties.PropertyCollection;

/**
 * This class implements a simple bus with attendees and message forwarding
 * between them using a {@link com.stereokrauts.stereoscope.model.messaging.dispatching.CoreMessageDispatcher}.
 * @author theide
 *
 */
public final class ClassicBus extends Bus implements IMessageSender {
	private CoreMessageDispatcher dispatcher;
	private final List<BusAttendee> attendees = new ArrayList<BusAttendee>();
	private final List<BusAttendeeBean> attendeesBean = new ArrayList<BusAttendeeBean>();
	private final PropertyCollection properties = new PropertyCollection();
	private final BusBean bean;
	private IAmMixer mixer;

	public ClassicBus() {
		this.dispatcher = CoreMessageDispatcherFactory.getInstance("standard-dispatcher");
		this.bean = new BusBean();
		this.bean.setAttendeeList(this.attendeesBean);
	}

	@Override
	public void dispatchMessage(final IMessageWithSender msg) {
		this.dispatcher.handleNotification(msg);
	}

	/**
	 * @param position -1 = insert at end, 0..* = insert at this index
	 */
	@Override
	public void addAttendee(final BusAttendee attendee, final int position) {
		this.dispatcher.registerObserver(attendee.getDispatchEndpoint());
		int calculatedPosition = position;
		if (position == -1) {
			calculatedPosition = this.attendees.size();
		}

		/* FIXME: This is a workaround to replace the old TheMixer class */
		this.workAroundOldTheMixerClass(attendee);

		this.attendees.add(calculatedPosition, attendee);
		this.attendeesBean.add(attendee.getBean());
	}

	/**
	 * @deprecated This MUST be replaced as soon as possible, DO NOT USE!!
	 * @param attendee
	 */
	@Deprecated
	private void workAroundOldTheMixerClass(final BusAttendee attendee) {
		attendee.setBus(this);
		if (attendee instanceof BusAttendeeMixer) {
			final BusAttendeeMixer busAttendeeMixer = (BusAttendeeMixer) attendee;
			if (this.mixer != null) {
				throw new IllegalStateException("Only one mixer allowed on bus currently!!");
			}
			this.mixer = busAttendeeMixer.getMixer();
		}
	}

	@Override
	public int removeAttendee(final BusAttendee attendee) {
		final int position = this.attendees.indexOf(attendee);
		this.attendeesBean.remove(attendee.getBean());
		this.attendees.remove(attendee);
		this.dispatcher.unregisterObserver(attendee.getDispatchEndpoint());
		return position;
	}

	@Override
	public IMessageReceiver getDispatchEndpoint() {
		return this.dispatcher;
	}

	@Override
	public List<BusAttendee> getAttendees() {
		return new ArrayList<BusAttendee>(this.attendees);
	}

	@Override
	public String getName() {
		return "Standard Dispatcher Bus";
	}

	@Override
	public BusAttendeeType getType() {
		return BusAttendeeType.BUS;
	}


	@Override
	public IPropertyProvider getPropertyProvider() {
		return new IPropertyProvider() {
			@Override
			public PropertyCollection getPropertyCollection() {
				return ClassicBus.this.properties;
			}
		};
	}

	@Override
	public BusBean getBean() {
		return this.bean;
	}

	@Override
	public Bus getBus() {
		return this;
	}

	@Override
	public void setBus(final Bus bus) {
		throw new IllegalStateException("setBus MUST NOT be called on a bus currently.");
	}

	@Override
	public IAmMixer getTheMixer() {
		return this.mixer;
	}

	@Override
	public void startup() {
		for (final BusAttendee attendee : this.attendees) {
			attendee.startup();
		}
	}

	@Override
	public void shutdown() {
		for (final BusAttendee attendee : this.attendees) {
			attendee.shutdown();
		}
	}

	@Override
	public String getIdentifier() {
		return "ClassicBus";
	}
}
