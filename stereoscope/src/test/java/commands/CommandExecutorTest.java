package commands;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * This class tests the CommandExecutor.
 * @author theide
 *
 */
public class CommandExecutorTest {
	private boolean didUndo = false;
	private boolean didExecute = false;
	
	private boolean observatorWasNotifiedUndoable = false;
	private boolean observatorWasNotifiedRedoable = false;
	
	@Test
	public final void injectCommand() throws Exception {
		final CommandExecutor ex = new CommandExecutor();
		
		ex.registerObserver(new IObserveUndoRedo() {
			
			@Override
			public void undoPossibleChanged(final boolean isUndoPossible) {
				CommandExecutorTest.this.observatorWasNotifiedUndoable = true;
			}
			
			@Override
			public void redoPossibleChanged(final boolean isRedoPossible) {
				CommandExecutorTest.this.observatorWasNotifiedRedoable = true;
			}
		});
		
		ex.executeCommand(new ICommand() {	
			
			@Override
			public void undo() throws Exception {
				CommandExecutorTest.this.didUndo = true;
			}
			
			
			@Override
			public boolean isUndoable() {
				return true;
			}
			
			
			@Override
			public Object getReturnValue() {
				return "YIEAH!";
			}
			
			
			@Override
			public String getDescription() {
				return "Just a test command";
			}
			
			
			@Override
			public void execute() throws Exception {
				CommandExecutorTest.this.didExecute = true;
			}
		});
		assertTrue("Command was executed", this.didExecute);
		assertTrue("Observer was notified about undoable action", this.observatorWasNotifiedUndoable);
		this.observatorWasNotifiedUndoable = false;
		ex.undoLastCommand();
		assertTrue("Command was undone", this.didUndo);
		assertTrue("Observer was notified about redoable action", this.observatorWasNotifiedRedoable);
		this.didExecute = false;
		ex.redoLastCommand();
		assertTrue("Command was redone", this.didExecute);
		assertTrue("Observer was notified about undoable action", this.observatorWasNotifiedUndoable);
	}
}
