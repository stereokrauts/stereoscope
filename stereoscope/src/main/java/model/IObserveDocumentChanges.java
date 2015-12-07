package model;

import model.bus.Bus;
import model.bus.BusAttendee;

/**
 * Implementers of this interface may register on a {@link Document}
 * for changes to the {@link Bus}.
 * @author theide
 *
 */
public interface IObserveDocumentChanges {
	void busElementAdded(Bus bus, BusAttendee attendee);
	void busElementRemoved(Bus bus, BusAttendee attendee);
	void documentShutdown(Document document);
}
