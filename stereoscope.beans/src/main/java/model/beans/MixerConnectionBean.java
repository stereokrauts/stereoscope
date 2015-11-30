package model.beans;

import javax.xml.bind.annotation.XmlType;

import aspects.observer.IAspectedObservable;
import aspects.observer.IManageObservers;
import aspects.observer.ObserverManager;
import model.properties.beans.PropertyBean;

@XmlType(name="mixerConnection")
public final class MixerConnectionBean implements IAspectedObservable {
	@PropertyBean
	private ConnectionMidiBean midiConnection;
	@PropertyBean
	private ConnectionTcpUdpIpBean networkConnection;
	private final IManageObservers manager = new ObserverManager();

	public ConnectionMidiBean getMidiConnection() {
		return this.midiConnection;
	}
	public void setMidiConnection(final ConnectionMidiBean midiConnection) {
		this.midiConnection = midiConnection;
	}
	public ConnectionTcpUdpIpBean getNetworkConnection() {
		return this.networkConnection;
	}
	public void setNetworkConnection(final ConnectionTcpUdpIpBean networkConnection) {
		this.networkConnection = networkConnection;
	}

	@Override
	public IManageObservers getObserverManager() {
		return manager ;
	}
}
