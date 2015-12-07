package model.bus;

import model.beans.ClientBean;
import model.beans.WebclientBean;
import model.properties.IPropertyProvider;
import model.properties.PropertiesException;
import model.properties.PropertyCollection;
import model.properties.beans.PropertyHandler;

import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationObservable;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.webgui.api.IWebclient;

public class BusAttendeeWebclient implements BusAttendee, IPropertyProvider, ICommunicationObservable {
	private final IWebclient webclient;
	private Bus bus;
	private final WebclientBean webclientBean;

	public BusAttendeeWebclient(final IWebclient webclient) {
		this.webclient = webclient;
		webclientBean = new WebclientBean();
		webclientBean.setClientConfig(new ClientBean());
		webclientBean.getClientConfig().setClientName("Webclient " + webclient.getPortNumber());
	}

	public BusAttendeeWebclient(final IWebclient webclient, final WebclientBean webclientBean) {
		this.webclient = webclient;
		this.webclientBean = webclientBean; 
	}

	@Override
	public String getName() {
		return webclientBean.getClientConfig().getClientName();
	}

	@Override
	public IMessageReceiver getDispatchEndpoint() {
		return this.webclient;
	}

	@Override
	public void dispatchMessage(final IMessageWithSender msg) {
		this.webclient.handleNotification(msg);
	}

	@Override
	public BusAttendeeType getType() {
		return BusAttendeeType.WEBCLIENT;
	}

	@Override
	public IPropertyProvider getPropertyProvider() {
		return this;
	}

	@Override
	public WebclientBean getBean() {
		return webclientBean;
	}


	@Override
	public Bus getBus() {
		return this.bus;
	}

	@Override
	public void setBus(final Bus bus) {
		this.bus = bus;
	}

	@Override
	public void startup() {

	}

	@Override
	public void shutdown() {
	}

	@Override
	public PropertyCollection getPropertyCollection()
			throws PropertiesException {
		return PropertyHandler.extractProperties(this.getBean());
	}

	@Override
	public String getIdentifier() {
		return "Webclient";
	}

	@Override
	public void setCommunicationObserver(final ICommunicationAware communicationAware) {
		webclient.setCommunicationObserver(communicationAware);
	}
}
