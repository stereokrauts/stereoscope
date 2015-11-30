package model.bus;

import model.beans.BusAttendeeBean;
import model.properties.IPropertyProvider;

import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;

/**
 * A client to a {@link model.bus.Bus} must implement this interface.
 * @author theide
 *
 */
public interface BusAttendee {
	/** @deprecated */
	@Deprecated
	void dispatchMessage(IMessageWithSender msg);
	IMessageReceiver getDispatchEndpoint();
	String getName();
	String getIdentifier();
	BusAttendeeType getType();
	IPropertyProvider getPropertyProvider();
	BusAttendeeBean getBean();
	Bus getBus();
	void setBus(Bus bus);

	void startup();
	void shutdown();
}
