package model.bus;

import java.util.List;

import model.beans.BusBean;
import model.mixer.interfaces.IAmMixer;

/**
 * This class describes the interface of a bus. As this is one of the
 * central classes/interfaces, it should be replaceable and is thus
 * defined as interface, although it currently only has one implementer.
 * @author theide
 *
 */
public abstract class Bus implements BusAttendee {	
	public abstract void addAttendee(BusAttendee attendee, int position);
	public abstract int removeAttendee(BusAttendee attendee);
	public abstract List<BusAttendee> getAttendees();
	@Override
	public abstract BusBean getBean();
	/** @deprecated This method should be removed as soon as possible */
	@Deprecated
	public abstract IAmMixer getTheMixer();
}
