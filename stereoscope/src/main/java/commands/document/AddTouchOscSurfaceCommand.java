package commands.document;

import model.Document;
import model.protocol.osc.OscConstants;
import commands.NotUndoableCommand;

/**
 * This command adds a TouchOSC surface to a document.
 * @author theide
 *
 */
public final class AddTouchOscSurfaceCommand extends NotUndoableCommand {
	private final Document document;
	private String networkAddress = "";
	private int portNumber = OscConstants.DEFAULT_OSC_CLIENT_PORT;
	private int serverPortNumber = OscConstants.DEFAULT_OSC_SERVER_PORT;

	public AddTouchOscSurfaceCommand(final Document document) {
		this.document = document;
	}

	public AddTouchOscSurfaceCommand(final Document document, final String networkAddress, final int portNumber, final int serverPortNumber) {
		this.document = document;
		this.networkAddress = networkAddress;
		this.portNumber = portNumber;
		this.serverPortNumber  = serverPortNumber;
	}

	@Override
	public String getDescription() {
		return "Adds a TouchOsc interface to the default bus on a document";
	}

	@Override
	public void execute() throws Exception {
		this.document.addTouchOscSurface(this.networkAddress, this.portNumber, this.serverPortNumber);
	}

	@Override
	public Object getReturnValue() {
		return null;
	}
}
