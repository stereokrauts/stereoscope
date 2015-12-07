package model.controllogic;

import model.bus.Bus;

/**
 * This class registers the control logic classes on a
 * CoreMessageDispatcher.
 * 
 * @author theide
 */
public final class ControlLogic {
	private ControlLogic(final Bus bus) {
		this.registerControlLogicInDispatcher(bus);
	}

	private void registerControlLogicInDispatcher(final Bus bus) {
		final SceneRecall recall = new SceneRecall();
		final Resync resync = new Resync();
		
		bus.addAttendee(recall, 0);
		bus.addAttendee(resync, 0);
	}
	
	public static void registerOn(final Bus bus) {
		new ControlLogic(bus);
	}
}
