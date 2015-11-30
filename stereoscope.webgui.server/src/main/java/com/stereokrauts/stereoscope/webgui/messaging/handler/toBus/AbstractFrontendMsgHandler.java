package com.stereokrauts.stereoscope.webgui.messaging.handler.toBus;

import com.stereokrauts.lib.logging.SLogger;
import com.stereokrauts.lib.logging.StereoscopeLogManager;
import com.stereokrauts.stereoscope.webgui.Webclient;
import com.stereokrauts.stereoscope.webgui.messaging.FrontendMessage;

/**
 * This class is used as a base class for frontend message
 * handlers
 * @author theide
 * @author jansen
 *
 */
public abstract class AbstractFrontendMsgHandler {
	protected Webclient frontend;
	
	public AbstractFrontendMsgHandler(Webclient frontend) {
		this.frontend = frontend;
	}

	public abstract String[] getInterestedAddresses();

	public abstract boolean handleMessage(FrontendMessage msg);

	protected static final SLogger LOG = StereoscopeLogManager
			.getLogger("frontend-msg-handling");

	
	protected static final String OSC_PREFIX = "/stereoscope";

	public final Webclient getFrontend() {
		return this.frontend;
	}

	public final void setFrontend(final Webclient webclient) {
		this.frontend = webclient;
	}
}
