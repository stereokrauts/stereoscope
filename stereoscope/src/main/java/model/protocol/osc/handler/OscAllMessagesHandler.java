package model.protocol.osc.handler;

import model.protocol.osc.IOscMessage;
import model.protocol.osc.OscAddressUtil;

import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;

/**
 * This handler reacts to all OSC messages and logs them in the
 * finest LOG level as well as displaying the activity to the
 * user.
 */
public final class OscAllMessagesHandler extends AbstractOscMessageHandler {

	private final ICommunicationAware communicationAware;

	public OscAllMessagesHandler(final ICommunicationAware communicationAware) {
		this.communicationAware = communicationAware;
	}

	@Override
	public String[] getInterestedAddresses() {
		return new String[] { ".*" };
	}


	@Override
	public MessageHandleStatus handleOscMessage(final IOscMessage msg) {
		communicationAware.transmit();
		/* print the address pattern and the typetag of the
		 * received IOscMessage */
		LOG.finest("received an osc message from " + msg.address() 
				+ ": addrpattern: " + OscAddressUtil.stringify(msg.address())
				+ ", toString: " + msg);
		return MessageHandleStatus.MESSAGE_NOT_HANDLED; /* we do nothing interesting so we let the caller
						 believe we ignored this message */
	}

}
