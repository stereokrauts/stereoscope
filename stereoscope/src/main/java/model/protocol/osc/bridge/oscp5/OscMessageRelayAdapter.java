package model.protocol.osc.bridge.oscp5;

import java.util.logging.Level;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import model.surface.OscSurface;
import oscP5.OscMessage;

/**
 * This adapter translates between oscP5 and stereoscope OSC.
 * @author th
 *
 */
public final class OscMessageRelayAdapter {
	private static final SLogger LOG = StereoscopeLogManager.getLogger(OscMessageRelayAdapter.class);
	private final OscSurface oscSurface;

	public OscMessageRelayAdapter(final OscSurface oscSurface) {
		this.oscSurface = oscSurface;
	}

	public void oscEvent(final OscMessage msg) {
		try {
			if (!this.checkSourceIp(msg)) {
				LOG.warning("Ignored message because of wrong source IP: " + msg);
				return;
			}
			oscSurface.transmit();
			oscSurface.oscEvent(OscMessageUtil.adapt(msg));
		} catch (final Exception e) {
			LOG.log(Level.WARNING, "Exception occured while handling OSC message:", e);
		}
	}

	private boolean checkSourceIp(final OscMessage arg0) {
		return true; /* handle all messages for now */
	}
}
