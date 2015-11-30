package model.protocol.osc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import model.protocol.osc.handler.AbstractOscMessageHandler;
import model.surface.OscSurface;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;

/**
 * This class distributes OSC messages from the surface to the handlers
 * that were registered at the Relay earlier.
 */
public class OscMessageRelay implements IOscEventListener {
	public static final String OSC_PREFIX = "/stereoscope";
	private static final SLogger LOG = StereoscopeLogManager.getLogger(OscMessageRelay.class);

	private final HashMap<String, ArrayList<AbstractOscMessageHandler>> regexHandlerAssociation;
	private final OscSurface surface;

	public OscMessageRelay(final OscSurface oscSurface) {
		this.regexHandlerAssociation = new HashMap<String, ArrayList<AbstractOscMessageHandler>>();
		this.surface = oscSurface;
	}

	public final void registerIOscMessageHandler(final AbstractOscMessageHandler handler) {
		handler.setSurface(this.surface);
		for (final String s : handler.getInterestedAddresses()) {
			if (!this.regexHandlerAssociation.containsKey(s)) {
				this.regexHandlerAssociation.put(s, new ArrayList<AbstractOscMessageHandler>());
			}
			this.regexHandlerAssociation.get(s).add(handler);
		}
	}

	public final void unregisterIOscMessageHandler(final AbstractOscMessageHandler handler) {
		for (final String key : this.regexHandlerAssociation.keySet()) {
			this.regexHandlerAssociation.get(key).remove(handler);
		}
	}


	@Override
	public final void oscEvent(final IOscMessage msg) {
		try {
			for (final String regex : this.regexHandlerAssociation.keySet()) {
				if (OscAddressUtil.stringify(msg.address()).matches(regex)) {
					for (final AbstractOscMessageHandler hnd : this.regexHandlerAssociation.get(regex)) {
						try {
							hnd.handleOscMessage(msg);
						} catch (final Exception e) {
							LOG.warning("An exception has occured while handling a message in " + hnd.getClass().getCanonicalName());
							LOG.log(Level.WARNING, "Original exception on console", e);
						}
					}
				}
			}
		} catch (final Exception e) {
			LOG.log(Level.WARNING, "Exception occured while handling OSC message:", e);
		}
	}

}
