package com.stereokrauts.stereoscope.model.messaging.api;


public interface IMessageWithSender {
	IMessage getMessage();
	IMessageSender getSender();
}
