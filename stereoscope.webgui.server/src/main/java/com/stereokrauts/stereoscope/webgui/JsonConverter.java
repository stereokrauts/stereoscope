/**
 * 
 */
package com.stereokrauts.stereoscope.webgui;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.stereokrauts.stereoscope.webgui.messaging.FrontendMessage;

/**
 * This class converts Javascript JSON objects to
 * plain old Java objects and vice versa.
 * It uses the Jackson streaming API which seems
 * to be a fast and resource efficient way.
 *
 * @author jansen
 *
 */
public class JsonConverter {
	private JsonFactory jf;
	
	public JsonConverter() {
		jf = new JsonFactory();
	}
	
	public final FrontendMessage convertFromJson(final String jsonData) 
			throws IOException {
		FrontendMessage message = new FrontendMessage();
		JsonParser jsonParser = jf.createJsonParser(jsonData);
		
		jsonParser.nextToken(); //skip opening curly brace
		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
			String fieldname = jsonParser.getCurrentName();
			jsonParser.nextToken();
			if ("type".equals(fieldname)) {
				message.setMsgType(jsonParser.getText());
			} else if ("oscStr".equals(fieldname)) {
				message.setOscAddress(jsonParser.getText());
			} else if ("val".equals(fieldname)) { 
				if (message.getMsgType().matches("float")) {
					message.setFloatValue(jsonParser.getFloatValue());
				} else if (message.getMsgType().matches("string")) {
					message.setStringValue(jsonParser.getText());
				} else if (message.getMsgType().matches("boolean")) {
					message.setBooleanValue(jsonParser.getBooleanValue());
				}
			} else if ("file".equals(fieldname)) {
				//jsonParser.nextToken();
				while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
					String innerField = jsonParser.getCurrentName();
					jsonParser.nextToken();
					if ("fname".equals(innerField)) {
						message.setFileName(jsonParser.getText());
					} else if ("fsize".equals(innerField)) {
						message.setFileSize(jsonParser.getIntValue());
					} else if ("data".equals(innerField)) {
						message.setBinaryValue(jsonParser.getBinaryValue());
					}
				}
			} else {
				throw new IllegalStateException(
						"Unrecognized field '" + fieldname + "'!");
			}
			
		}
		jsonParser.close();
		return message;
	}
	
	public final String convertToJson(final FrontendMessage msg) {
		String jsonMessage;
		jsonMessage = "{"
				+ "\"type\":\"" + msg.getMsgType() + "\","
				+ "\"oscStr\":\"" + msg.getOscAddress() + "\",";
		if (msg.getMsgType() == "float") {
			jsonMessage += "\"val\":" + msg.getFloatValue();
		} else if (msg.getMsgType() == "string") {
			if (msg.getStringValue().substring(0, 1).matches("\\{")) {
				jsonMessage += "\"val\":" + msg.getStringValue();
			} else {
				jsonMessage += "\"val\":\"" + msg.getStringValue() + "\"";
			}
		} else if (msg.getMsgType() == "boolean") {
			jsonMessage += "\"val\":" + msg.getBooleanValue();
		}
		jsonMessage += "}";
		// js eval doesn't understand line breaks
		jsonMessage = jsonMessage.replaceAll("\\r\\n|\\r|\\n", " ");
		return jsonMessage;
	}
}
