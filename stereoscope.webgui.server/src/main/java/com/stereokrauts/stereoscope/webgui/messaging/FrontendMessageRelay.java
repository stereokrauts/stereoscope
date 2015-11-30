package com.stereokrauts.stereoscope.webgui.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.webgui.Webclient;
import com.stereokrauts.stereoscope.webgui.messaging.handler.toBus.AbstractFrontendMsgHandler;

/**
 * This class distributes messages from the web-frontend to the handlers
 * that were registered to the relay earlier.
 */
public class FrontendMessageRelay {
	public static final String OSC_PREFIX = "/stereoscope";
	private static final SLogger LOG = StereoscopeLogManager.getLogger(FrontendMessageRelay.class);

	private final HashMap<String, ArrayList<AbstractFrontendMsgHandler>> regexHandlerAssociation;
	private final Webclient frontend;

	public FrontendMessageRelay(final Webclient frontend) {
		this.regexHandlerAssociation = new HashMap<String, ArrayList<AbstractFrontendMsgHandler>>();
		this.frontend = frontend;
	}

	public final void registerFrontendMessageHandler(final AbstractFrontendMsgHandler handler) {
		handler.setFrontend(this.frontend);
		for (final String s : handler.getInterestedAddresses()) {
			if (!this.regexHandlerAssociation.containsKey(s)) {
				this.regexHandlerAssociation.put(s, new ArrayList<AbstractFrontendMsgHandler>());
			}
			this.regexHandlerAssociation.get(s).add(handler);
		}
	}

	public final void unregisterFrontendMessageHandler(final AbstractFrontendMsgHandler handler) {
		for (final String key : this.regexHandlerAssociation.keySet()) {
			this.regexHandlerAssociation.get(key).remove(handler);
		}
	}

	public final void handleEvent(final FrontendMessage msg) {
		try {
			for (final String regex : this.regexHandlerAssociation.keySet()) {
				if (msg.getOscAddress().matches(regex)) {
					for (final AbstractFrontendMsgHandler handler : this.regexHandlerAssociation.get(regex)) {
						try {
							handler.handleMessage(msg);
						} catch (final Exception e) {
							LOG.warning("An exception has occured while handling a message in " + handler.getClass().getCanonicalName());
							LOG.log(Level.WARNING, "Original exception on console", e);
						}
					}
				}
			}
		} catch (final Exception e) {
			LOG.log(Level.WARNING, "Exception occured while handling frontend message:", e);
		}
	}

}
