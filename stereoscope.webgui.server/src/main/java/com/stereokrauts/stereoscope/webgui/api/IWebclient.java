package com.stereokrauts.stereoscope.webgui.api;

import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;

public interface IWebclient extends IMessageReceiver {

	void registerMessageDispatcher(IMessageReceiver dispatchEndpoint);

	int getPortNumber();

	void setCommunicationObserver(ICommunicationAware communicationAware);

}
