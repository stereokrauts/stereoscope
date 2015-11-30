package commands;

import java.util.ArrayList;

/**
 * This class has the responsibility to execute
 * commands and to keep record of a undo/redo
 * history.
 * @author theide
 *
 */
public final class CommandExecutor {
	private static final int COMMAND_HIST_SIZE = 50;
	
	private final ArrayList<ICommand> commandsExecuted;
	private final ArrayList<ICommand> commandsForRedo;
	
	private final ArrayList<IObserveUndoRedo> observers;
	
	public CommandExecutor() {
		this.commandsExecuted = new ArrayList<ICommand>(COMMAND_HIST_SIZE);
		this.commandsForRedo = new ArrayList<ICommand>(COMMAND_HIST_SIZE);
		this.observers = new ArrayList<IObserveUndoRedo>();
	}
	
	public synchronized void executeCommand(final ICommand c) throws Exception {
		if (c.isUndoable()) {
			if (this.commandsExecuted.size() == COMMAND_HIST_SIZE) {
				this.commandsExecuted.remove(0);
			}
			this.commandsExecuted.add(c);
		} else {
			this.commandsExecuted.clear();
		}
		c.execute();
		this.fireUndoPossibleChanged(this.commandsExecuted.size() > 0);
		this.fireRedoPossibleChanged(this.commandsForRedo.size() > 0);
	}
	
	public synchronized void undoLastCommand() throws Exception {
		final ICommand c = this.commandsExecuted.get(this.commandsExecuted.size() - 1);
		this.commandsExecuted.remove(this.commandsExecuted.size() - 1);
		c.undo();
		this.commandsForRedo.add(c);
		this.fireUndoPossibleChanged(this.commandsExecuted.size() > 0);
		this.fireRedoPossibleChanged(this.commandsForRedo.size() > 0);
	}
	
	public synchronized void redoLastCommand() throws Exception {
		final ICommand c = this.commandsForRedo.get(this.commandsForRedo.size() - 1);
		this.commandsForRedo.remove(this.commandsForRedo.size() - 1);
		this.executeCommand(c);
	}
	
	/* Observer pattern below */
	
	public synchronized void registerObserver(final IObserveUndoRedo ob) {
		this.observers.add(ob);
	}
	
	public synchronized void unregisterObserver(final IObserveUndoRedo ob) {
		this.observers.remove(ob);
	}
	
	private void fireUndoPossibleChanged(final boolean isUndoPossible) {
		for (final IObserveUndoRedo ob : this.observers) {
			ob.undoPossibleChanged(isUndoPossible);
		}
	}
	
	private void fireRedoPossibleChanged(final boolean isRedoPossible) {
		for (final IObserveUndoRedo ob : this.observers) {
			ob.redoPossibleChanged(isRedoPossible);
		}
	}

}
