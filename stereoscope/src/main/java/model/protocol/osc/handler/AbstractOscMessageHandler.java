package model.protocol.osc.handler;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;
import model.protocol.osc.OscObjectUtil;
import model.surface.OscSurface;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;

/**
 * This class is used as a base class for handlers that
 * can process OSC messages.
 * @author theide
 *
 */
public abstract class AbstractOscMessageHandler {
	public abstract String[] getInterestedAddresses();

	public abstract MessageHandleStatus handleOscMessage(IOscMessage msg);

	protected static final SLogger LOG = StereoscopeLogManager
			.getLogger("osc-handling");

	private OscSurface surface;

	public final OscSurface getSurface() {
		return this.surface;
	}

	public final void setSurface(final OscSurface surface2) {
		this.surface = surface2;
	}

	public final boolean isZMessage(final IOscMessage msg) {
		return OscAddressUtil.stringify(msg.address()).endsWith("/z");
	}

	public final boolean isZMessagePushed(final IOscMessage msg) {
		boolean zMessagePushed = false;
		if (this.isZMessage(msg)) {
			if (OscObjectUtil.toFloat(msg.get(0)) == 1.0) {
				zMessagePushed = true;
			}
		}
		return zMessagePushed;
	}

	public final boolean isZMessageReleased(final IOscMessage msg) {
		boolean zMessageReleased = false;
		if (this.isZMessage(msg)) {
			if (OscObjectUtil.toFloat(msg.get(0)) == 0.0) {
				zMessageReleased = true;
			}
		}
		return zMessageReleased;
	}
}
