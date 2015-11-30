package com.stereokrauts.stereoscope.helper.tests;

import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

public class MessagingSysexAssociation {
	private final String sysexmessage;
	private final AbstractMessage<?> message;
	private String description = "no description";
	
	public MessagingSysexAssociation(final String sysex, final AbstractMessage<?> message) {
		this.sysexmessage = sysex.replaceAll(" ","");
		this.message = message;
	}
	
	public MessagingSysexAssociation(final String description, final String sysex, final AbstractMessage<?> message) {
		this(sysex, message);
		this.description = description;
	}
	
	public String getSysex() {
		return this.sysexmessage;
	}
	
	public AbstractMessage<?> getMessage() {
		return this.message;
	}
	
	public String getDescription() {
		return this.description;
	}

}
