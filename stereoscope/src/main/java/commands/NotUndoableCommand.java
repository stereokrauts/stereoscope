package commands;

/**
 * This class can be used by clients to get a command that will
 * not be undoable.
 * @author th
 *
 */
public abstract class NotUndoableCommand implements ICommand {
	@Override
	public abstract String getDescription();

	@Override
	public abstract void execute() throws Exception;

	@Override
	public abstract Object getReturnValue();

	@Override
	public final boolean isUndoable() {
		return false;
	}

	@Override
	public final void undo() throws Exception {
		throw new UnsupportedOperationException("Commands of class " + getClass() + " cannot be undone");
	}

}
