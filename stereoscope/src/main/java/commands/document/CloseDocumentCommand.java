package commands.document;

import model.Document;

import commands.NotUndoableCommand;

/**
 * This command creates a new document.
 * 
 * @author theide
 * 
 */
public final class CloseDocumentCommand extends NotUndoableCommand {
	private final Document document;

	public CloseDocumentCommand(final Document document) {
		this.document = document;
	}

	@Override
	public String getDescription() {
		return "Closes an open document.";
	}

	@Override
	public void execute() throws Exception {
		this.document.getModel().closeDocument(this.document);
	}

	@Override
	public Object getReturnValue() {
		return null;
	}

}
