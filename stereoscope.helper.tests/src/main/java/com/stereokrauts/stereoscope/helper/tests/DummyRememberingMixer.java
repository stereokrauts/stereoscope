package com.stereokrauts.stereoscope.helper.tests;

import model.mixer.interfaces.IAmMixer;

import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.model.messaging.message.AbstractMessage;

public class DummyRememberingMixer implements IMessageReceiver {

	AbstractMessage<?> lastMessage = null;
	
	public DummyRememberingMixer(final IAmMixer toObserve) {
		toObserve.registerObserver(this);
	}
	
	public void handleNotification(final IMessageWithSender message) {
		final IMessage msg = message.getMessage();
		if (msg instanceof AbstractMessage<?> && !msg.getClass().getName().endsWith("Label")) {
			this.lastMessage = (AbstractMessage<?>) msg;
		}
	}
	
	public AbstractMessage<?> getLastMessage() {
		return this.lastMessage;
	}
}
