package commands.bonjour;

import model.surface.touchosc.BonjourTouchOscHelper;

import commands.ICommand;

/**
 * This commands starts advertising of stereoscope services on the local
 * network.
 * 
 * @author theide
 * 
 */
public final class AdvertiseStereoscopeCommand implements ICommand {

	@Override
	public String getDescription() {
		return "Advertises Stereoscope services via Bonjour on the network";
	}

	@Override
	public void execute() throws Exception {
		BonjourTouchOscHelper.getInstance();
	}

	@Override
	public Object getReturnValue() {
		return null;
	}

	@Override
	public boolean isUndoable() {
		return false;
	}

	@Override
	public void undo() throws Exception {
	}

}
