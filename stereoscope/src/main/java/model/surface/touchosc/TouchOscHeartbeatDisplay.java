package model.surface.touchosc;

import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscObjectUtil;
import model.protocol.osc.impl.OscMessage;
import model.surface.OscMessageSender;
import model.surface.OscSurface;

/**
 * This class periodically sends a heartbeat signal to the surface to indicate
 * the user a successful connection.
 * 
 * @author Tobias Heide &lt;tobi@s-hei.de&gt;
 * 
 */
public class TouchOscHeartbeatDisplay implements Runnable {
	private static final int HEARTBEAT_SWITCH_TIME_MS = 300;
	private final OscSurface osc;
	private boolean doRun = true;

	private String address;
	private int lastHeartBeatTarget;

	/**
	 * Create a new heartbeat message sender.
	 * 
	 * @param srf
	 *            The surface to send the messages to.
	 */
	public TouchOscHeartbeatDisplay(final OscSurface srf) {
		this.osc = srf;
	}

	/**
	 * This is the main run method of this thread.
	 */

	@Override
	public final void run() {
		this.initializeHeartbeat();
		while (this.doRun) {
			this.sendOneHeartbeat();
			try {
				Thread.sleep(HEARTBEAT_SWITCH_TIME_MS);
			} catch (final InterruptedException e) {
				this.doRun = false;
			}
		}
	}

	/**
	 * 
	 */
	private void initializeHeartbeat() {
		this.address = OscMessageSender.OSC_PREFIX + "system/heartbeat/";
		this.lastHeartBeatTarget = 1;
	}

	/**
	 * 
	 */
	private void sendOneHeartbeat() {
		final OscMessage clearOldDisplay = this.createClearOldHeartbeatMessage();
		this.swapHeartbeatTargetAddress();
		final OscMessage setNewDisplay = this.createSetNewHeartbeatMessage();
		this.swapHeartbeat(clearOldDisplay, setNewDisplay);
	}

	/**
	 * @param clearOld
	 * @param setNew
	 */
	private void swapHeartbeat(final OscMessage clearOld, final OscMessage setNew) {
		this.osc.sendMessage(setNew);
		this.osc.sendMessage(clearOld);
	}

	/**
	 * 
	 */
	private void swapHeartbeatTargetAddress() {
		this.lastHeartBeatTarget = (this.lastHeartBeatTarget + 1) % 2;
	}

	/**
	 * @return
	 */
	private OscMessage createSetNewHeartbeatMessage() {
		final OscMessage setNewDisplay = new OscMessage(OscAddressUtil.create(this.address
				+ (this.lastHeartBeatTarget + 1)));
		setNewDisplay.add(OscObjectUtil.createOscObject(1.0f));
		return setNewDisplay;
	}

	/**
	 * @return
	 */
	private OscMessage createClearOldHeartbeatMessage() {
		final OscMessage clearOldDisplay = new OscMessage(OscAddressUtil.create(this.address
				+ (this.lastHeartBeatTarget + 1)));
		clearOldDisplay.add(OscObjectUtil.createOscObject(0.0f));
		return clearOldDisplay;
	}

	/**
	 * This function should be called when the corresponding TouchOscSurface
	 * object is destroyed to stop this thread from sending silly messages...
	 */
	public final void stop() {
		this.doRun = false;
	}

}
