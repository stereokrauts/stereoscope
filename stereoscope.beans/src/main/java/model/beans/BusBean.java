package model.beans;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * This class represents a generic bus that has multiple attendees.
 * @author th
 *
 */
@XmlType(name = "bus")
public final class BusBean extends BusAttendeeBean {
	@XmlElementWrapper(name = "attendeeList")
	@XmlElement(name = "attendee")
	private List<BusAttendeeBean> attendees;
	private String name;

	public List<BusAttendeeBean> getAttendeesList() {
		return this.attendees;
	}

	public void setAttendeeList(final List<BusAttendeeBean> attendees) {
		this.attendees = attendees;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
