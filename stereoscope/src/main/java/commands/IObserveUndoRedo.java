package commands;

/**
 * This interface is to be implemented by every class that
 * wishes to be notified about changes to the undo/redo subsystem.
 * @author theide
 *
 */
public interface IObserveUndoRedo {
	void undoPossibleChanged(boolean isUndoPossible);
	void redoPossibleChanged(boolean isRedoPossible);
}
