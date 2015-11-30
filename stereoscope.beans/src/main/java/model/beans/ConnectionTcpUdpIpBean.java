package model.beans;

import javax.xml.bind.annotation.XmlType;

import model.beans.groups.ClientNetworkSettings;
import model.properties.beans.PropertyBind;
import aspects.observer.IAspectedObservable;
import aspects.observer.IManageObservers;
import aspects.observer.ObserverManager;

@XmlType(name="tcpUdpIpConnection")
public final class ConnectionTcpUdpIpBean implements IAutoDirty, IAspectedObservable {
	@PropertyBind(displayName="Remote Network address", group=ClientNetworkSettings.class)
	private String networkAddress;
	@PropertyBind(displayName="Remote Port", group=ClientNetworkSettings.class)
	private Integer portNumber;
	@PropertyBind(displayName="Use UDP", group=ClientNetworkSettings.class)
	private boolean useUdp;

	public String getNetworkAddress() {
		return this.networkAddress;
	}
	public void setNetworkAddress(final String ip) {
		if (this.networkAddress != null && !this.networkAddress.equals(ip)) {
			this.setDirty();
		}
		this.networkAddress = ip;
	}
	public Integer getPortNumber() {
		return this.portNumber;
	}
	public void setPortNumber(final Integer portNumber) {
		if (this.portNumber != portNumber) {
			this.setDirty();
		}
		this.portNumber = portNumber;
	}
	public boolean getUseUdp() {
		return this.useUdp;
	}
	public void setUseUdp(final boolean useUdp) {
		if (this.useUdp != useUdp) {
			this.setDirty();
		}
		this.useUdp = useUdp;
	}

	private boolean isDirty = false;
	@Override
	public boolean isDirty() {
		return this.isDirty;
	}
	@Override
	public void resetDirty() {
		this.isDirty = false;
	}
	private void setDirty() {
		this.isDirty = true;
	}

	private final IManageObservers manager = new ObserverManager();

	@Override
	public IManageObservers getObserverManager() {
		return this.manager;
	}
}
