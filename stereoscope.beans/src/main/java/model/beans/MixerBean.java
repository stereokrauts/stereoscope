package model.beans;

import javax.xml.bind.annotation.XmlType;

import aspects.observer.IAspectedObservable;
import model.properties.beans.PropertyBean;

/**
 * Representation of the configuration of a mixer plugin in stereoscope.
 * @author th
 *
 */
@XmlType(name = "mixer")
public final class MixerBean extends BusAttendeeBean implements IAspectedObservable {
	private MixerPluginBean plugin;
	@PropertyBean
	private MixerConnectionBean connection;

	public MixerConnectionBean getConnection() {
		return this.connection;
	}

	public void setConnection(final MixerConnectionBean connection) {
		this.connection = connection;
	}

	public MixerPluginBean getPlugin() {
		return this.plugin;
	}

	public void setPlugin(final MixerPluginBean plugin) {
		this.plugin = plugin;
	}
}
