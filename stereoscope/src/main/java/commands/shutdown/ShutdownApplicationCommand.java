package commands.shutdown;

import model.surface.touchosc.BonjourTouchOscHelper;

import commands.NotUndoableCommand;

/**
 * This command shuts down the stereoscope application.
 * 
 * @author theide
 * 
 */
public final class ShutdownApplicationCommand extends NotUndoableCommand {

	@Override
	public String getDescription() {
		return "Does all things that are neccessary to shutdown the application";
	}

	@Override
	public void execute() {
		try {
			BonjourTouchOscHelper.getInstance().shutdown();
		} catch (final Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public Object getReturnValue() {
		return null;
	}
}
