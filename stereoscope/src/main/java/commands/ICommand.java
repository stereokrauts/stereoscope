package commands;

/**
 * This interface is to be implemented by every command.
 * It's interface is needed by the CommandExecutor.
 * @author theide
 *
 */
public interface ICommand {
	String getDescription();
	void execute() throws Exception;
	Object getReturnValue();
	
	boolean isUndoable();
	void undo() throws Exception;
}
