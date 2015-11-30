package com.stereokrauts.stereoscope.webgui.messaging;

import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.sockjs.SockJSSocket;

import com.stereokrauts.stereoscope.webgui.JsonConverter;
import com.stereokrauts.stereoscope.webgui.Webclient;

public class JsonMessageSender {
	private final Webclient frontend;
	private final JsonConverter jsonConverter;

	public JsonMessageSender(final Webclient frontend) {
		this.frontend = frontend;
		this.jsonConverter = new JsonConverter();
	}

	public void sendToFrontend(final String oscAddress, final float value) {
		final FrontendMessage message = new FrontendMessage();
		message.setFloatValue(value);
		message.setOscAddress(oscAddress);
		final String json = getSerializedJsonObject(message);
		sendBufferToFrontend(getBuffer(json));
	}

	public void sendToFrontend(final String oscAddress, final boolean value) {
		final FrontendMessage message = new FrontendMessage();
		message.setBooleanValue(value);
		message.setOscAddress(oscAddress);
		final String json = getSerializedJsonObject(message);
		sendBufferToFrontend(getBuffer(json));
	}

	public void sendToFrontend(final String oscAddress, final String value) {
		final FrontendMessage message = new FrontendMessage();
		message.setStringValue(value);
		message.setOscAddress(oscAddress);
		final String json = getSerializedJsonObject(message);
		sendBufferToFrontend(getBuffer(json));
	}

	private String getSerializedJsonObject(final FrontendMessage message) {
		final String jsonData = jsonConverter.convertToJson(message);
		return jsonData;
	}

	private Buffer getBuffer(final String serializedJsonObject) {
		final Buffer buffer = new Buffer(serializedJsonObject);
		return buffer;
	}

	private void sendBufferToFrontend(final Buffer buffer) {
		final SockJSSocket sock = frontend.getWebsocket();
		if (sock != null && buffer != null) {
			sock.writeBuffer(buffer);
		} else {
			Webclient.LOG.info("Can't send buffer to frontend. Socket not open or buffer empty.");
		}
	}

}
