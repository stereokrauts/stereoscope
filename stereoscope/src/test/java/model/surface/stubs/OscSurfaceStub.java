package model.surface.stubs;

import java.util.ArrayList;
import java.util.List;

import model.protocol.osc.IOscMessage;
import model.surface.OscMessageSender;
import model.surface.OscSurface;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

public class OscSurfaceStub extends OscSurface {
	private AbstractMessage<?> lastReceivedIOscMessage;
	private AbstractMessage<?> lastReceivedOscLabelMessage;

	private final List<IOscMessage> sentMessages;

	public OscSurfaceStub() {
		super();
		this.sender = new OscMessageSender(this);
		this.sentMessages = new ArrayList<IOscMessage>();
	}


	@Override
	public synchronized void fireChanged(final AbstractMessage<?> theChange) {
		if (theChange.getClass().toString().endsWith("Label")) {
			this.setLastLabelMessage(theChange);
		} else {
			this.setLastMessage(theChange);
		}
	}

	public void setLastMessage(final AbstractMessage<?> lastMessage) {
		this.lastReceivedIOscMessage = lastMessage;
	}

	public AbstractMessage<?> getLastMessage() {
		return this.lastReceivedIOscMessage;
	}

	public void setLastLabelMessage(final AbstractMessage<?> lastLabelMessage) {
		this.lastReceivedOscLabelMessage = lastLabelMessage;
	}

	public AbstractMessage<?> getLastLabelMessage() {
		return this.lastReceivedOscLabelMessage;
	}


	@Override
	public synchronized void sendMessage(final IOscMessage m) {
		this.addSentMessage(m);
	}

	public void addSentMessage(final IOscMessage lastSentMessage) {
		synchronized(sentMessages) {
			this.sentMessages.add(lastSentMessage);
		}
	}

	public List<IOscMessage> getAndClearSentMessages() {
		synchronized(sentMessages) { 
			final List<IOscMessage> rv = new ArrayList<IOscMessage>();
			rv.addAll(this.sentMessages);
			this.sentMessages.clear();
			return rv;
		}
	}


	public void clearMessages() {
		synchronized(sentMessages) { 
			sentMessages.clear();
		}
	}
}
