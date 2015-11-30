package commands.document;

import model.Document;
import model.IModel;

import commands.ICommand;

/**
 * This command creates a new document.
 * 
 * @author theide
 * 
 */
public final class NewDocumentCommand implements ICommand {

	private final IModel targetModel;
	private Document document;

	public NewDocumentCommand(final IModel model) {
		this.targetModel = model;
	}

	@Override
	public String getDescription() {
		return "Creates a new document";
	}

	@Override
	public void execute() throws Exception {
		this.document = this.targetModel.createNewDocument();
	}

	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public void undo() {
	}

	@Override
	public Document getReturnValue() {
		return this.document;
	}

}
