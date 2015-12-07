package model.bus;

import model.beans.BusAttendeeBean;
import model.beans.MixerBean;
import model.beans.MixerConnectionBean;
import model.beans.MixerPluginBean;
import model.mixer.interfaces.IAmMixer;
import model.properties.IPropertyProvider;
import model.properties.PropertiesException;
import model.properties.PropertyCollection;
import model.properties.beans.PropertyHandler;

import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationAware;
import com.stereokrauts.stereoscope.gui.metrics.model.ICommunicationObservable;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageReceiver;
import com.stereokrauts.stereoscope.model.messaging.api.IMessageWithSender;
import com.stereokrauts.stereoscope.plugin.interfaces.IMixerPlugin;

/**
 * This is an adapter class to connect a {@link IMixerPlugin}
 * to a bus.
 * @author theide
 *
 */
public final class BusAttendeeMixer implements BusAttendee, ICommunicationAware, ICommunicationObservable {
	/**
	 * 
	 */
	private final IMixerPlugin plugin;
	private final MixerBean bean;
	private final MixerPluginBean pluginBean;
	private final MixerConnectionBean connectionBean;
	private Bus bus;

	private final IAmMixer mixer;
	private ICommunicationAware communicationObserver;

	public BusAttendeeMixer(final IMixerPlugin plugin) {
		this.plugin = plugin;
		this.mixer = plugin.getMixer();
		this.bean = new MixerBean();
		this.connectionBean = plugin.getPersistableConfiguration().getConnectionBean();
		this.bean.setConnection(this.connectionBean);
		this.pluginBean = new MixerPluginBean();
		this.pluginBean.setPluginId(plugin.getPluginName());
		this.bean.setPlugin(this.pluginBean);
	}

	@Override
	public String getName() {
		return this.plugin.getDisplayName();
	}

	@Override
	public IMessageReceiver getDispatchEndpoint() {
		return this.plugin.getMessageEndpoint();
	}

	@Override
	public void dispatchMessage(final IMessageWithSender msg) {
		this.plugin.getMessageEndpoint().handleNotification(msg);
	}

	@Override
	public String toString() {
		return "Mixer plugin: " + this.getName();
	}

	@Override
	public BusAttendeeType getType() {
		return BusAttendeeType.MIXER;
	}

	@Override
	public IPropertyProvider getPropertyProvider() {
		return new IPropertyProvider() {
			@Override
			public PropertyCollection getPropertyCollection() throws PropertiesException {
				return PropertyHandler.extractProperties(BusAttendeeMixer.this.getBean());
			}
		};
	}

	@Override
	public BusAttendeeBean getBean() {
		return this.bean;
	}

	@Override
	public Bus getBus() {
		return this.bus;
	}

	@Override
	public void setBus(final Bus newBus) {
		this.bus = newBus;
	}

	/** @deprecated Workaround to make Stereoscope work again */
	@Deprecated
	public IAmMixer getMixer() {
		return this.mixer;
	}

	@Override
	public void startup() {
	}

	@Override
	public void shutdown() {
		this.plugin.shutdown();
	}

	@Override
	public void receive() {
		this.communicationObserver.receive();
	}

	@Override
	public void transmit() {
		this.communicationObserver.transmit();
	}

	@Override
	public void setCommunicationObserver(final ICommunicationAware communicationObserver) {
		this.communicationObserver = communicationObserver;
	}

	@Override
	public String getIdentifier() {
		return this.plugin.getPluginName();
	}
}