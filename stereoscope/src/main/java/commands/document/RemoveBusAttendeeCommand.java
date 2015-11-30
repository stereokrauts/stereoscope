package commands.document;

import model.Document;
import model.bus.Bus;
import model.bus.BusAttendee;

import commands.NotUndoableCommand;

/**
 * This command removes any busattendee from a document.
 * @author theide
 *
 */
public final class RemoveBusAttendeeCommand extends NotUndoableCommand {
	private final Document document;
	private final Bus bus;
	private final BusAttendee attendee;
	@SuppressWarnings("unused") /* useful for undo later */
	private int position;

	public RemoveBusAttendeeCommand(final Document document, final Bus bus, final BusAttendee attendee) {
		this.document = document;
		this.bus = bus;
		this.attendee = attendee;
	}

	@Override
	public String getDescription() {
		return "Removes a Bus attendee from a bus on a document";
	}

	@Override
	public void execute() throws Exception {
		this.position = this.document.removeBusAttendee(this.bus, this.attendee);
	}

	@Override
	public Object getReturnValue() {
		return null;
	}
}
