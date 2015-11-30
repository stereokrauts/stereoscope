package com.stereokrauts.stereoscope.webgui.messaging.handler.fromBus;

import com.stereokrauts.stereoscope.model.messaging.api.IMessage;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageHandler;
import com.stereokrauts.stereoscope.model.messaging.message.layouts.MsgLayoutListResponse;
import com.stereokrauts.stereoscope.model.messaging.message.layouts.MsgLayoutResponse;
import com.stereokrauts.stereoscope.webgui.Webclient;

public class FromBusSystemHandler implements IMessageHandler {
	private Webclient frontend;
	
	public FromBusSystemHandler(final Webclient frontend) {
		this.frontend = frontend;
	}

	@Override
	public boolean handleMessage(IMessage msg) {
		if (msg instanceof MsgLayoutListResponse) {
			final MsgLayoutListResponse msgLayoutListResponse = (MsgLayoutListResponse) msg;
			this.setLayoutListResponse(msgLayoutListResponse);
			return true;
		} else if (msg instanceof MsgLayoutResponse) {
			final MsgLayoutResponse msgLayoutResponse = (MsgLayoutResponse) msg;
			this.setLayoutResponse(msgLayoutResponse);
			return true;
		}
		return false;
	}
	
	private void setLayoutListResponse(
			MsgLayoutListResponse msgLayoutListResponse) {
		final String oscAddress = Webclient.OSC_PREFIX
				+ "system/response/frontend/layoutList";
		final String[] layoutList = msgLayoutListResponse.getAttachment();
		final String layouts = joinStringArray(layoutList, "§");
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, layouts);
		
	}

	private void setLayoutResponse(MsgLayoutResponse msgLayoutResponse) {
		final String oscAddress = Webclient.OSC_PREFIX
				+ "system/response/frontend/layout";
		this.frontend.getFrontendModifier().sendToFrontend(oscAddress, 
				msgLayoutResponse.getAttachment());
		
	}

	private String joinStringArray(String[] list, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		for (String str : list) {
			buffer.append(str);
			buffer.append(delimiter);
		}
		return buffer.toString();
	}
}
