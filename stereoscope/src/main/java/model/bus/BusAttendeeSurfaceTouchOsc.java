package model.bus;

import model.beans.BusAttendeeBean;
import model.properties.IPropertyProvider;
import model.properties.PropertiesException;
import model.surface.OscMessageSender;
import model.surface.touchosc.TouchOscSurface;

import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationObservable;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;

/**
 * This bus attendee is a TouchOSC surface.
 * @author th
 *
 */
public final class BusAttendeeSurfaceTouchOsc implements BusAttendee, ICommunicationAware, ICommunicationObservable {
	private final TouchOscSurface srf;
	private final OscMessageSender relay;

	private final IPropertyProvider propertyProvider;
	private Bus bus;
	private ICommunicationAware communicationObserver = new ICommunicationAware() {
		@Override
		public void transmit() {
		}

		@Override
		public void receive() {
		}
	};

	public BusAttendeeSurfaceTouchOsc(final TouchOscSurface srf) throws PropertiesException {
		this.srf = srf;
		srf.setBusAttendee(this);
		this.relay = new OscMessageSender(srf);
		this.propertyProvider = srf.getPropertyProvider();
	}

	@Override
	public String getName() {
		return this.srf.getBean().getClientConfig().getClientName();
	}

	@Override
	public IMessageReceiver getDispatchEndpoint() {
		return this.srf.getOscMessageSender();
	}

	@Override
	public void dispatchMessage(final IMessageWithSender msg) {
		this.relay.handleNotification(msg);
	}

	@Override
	public BusAttendeeType getType() {
		return BusAttendeeType.SURFACE_TOUCHOSC;
	}

	@Override
	public IPropertyProvider getPropertyProvider() {
		return this.propertyProvider;
	}

	@Override
	public BusAttendeeBean getBean() {
		return this.srf.getBean();
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
		this.srf.shutdown();
	}

	@Override
	public void receive() {
		this.communicationObserver.receive();
	}

	@Override
	public void transmit() {
		this.communicationObserver.transmit();
	}

	@Override
	public void setCommunicationObserver(final ICommunicationAware communicationObserver) {
		this.communicationObserver = communicationObserver;
	}

	@Override
	public String getIdentifier() {
		return "TouchOSC";
	}
}