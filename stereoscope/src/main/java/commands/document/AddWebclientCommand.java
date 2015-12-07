package commands.document;

import model.Document;
import commands.NotUndoableCommand;

/**
 * This command adds a TouchOSC surface to a document.
 * 
 * @author theide
 * 
 */
public final class AddWebclientCommand extends NotUndoableCommand {
	private final Document document;
	private final int webclientPort;

	public AddWebclientCommand(final Document document, final int webclientPort) {
		this.document = document;
		this.webclientPort = webclientPort;
	}

	@Override
	public String getDescription() {
		return "Adds a Webclient interface to the default bus on a document";
	}

	@Override
	public void execute() throws Exception {
		document.addWebclient(webclientPort);
	}

	@Override
	public Object getReturnValue() {
		return null;
	}
}
