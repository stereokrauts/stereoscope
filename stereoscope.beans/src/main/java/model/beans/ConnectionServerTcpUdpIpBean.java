package model.beans;

import javax.xml.bind.annotation.XmlType;

import model.beans.groups.OscServer;
import model.properties.beans.PropertyBind;
import aspects.observer.IAspectedObservable;
import aspects.observer.IManageObservers;
import aspects.observer.ObserverManager;

@XmlType(name="tcpUdpIpServer")
public final class ConnectionServerTcpUdpIpBean implements IAutoDirty, IAspectedObservable {
	@PropertyBind(displayName="Local Port", group=OscServer.class)
	private Integer portNumber;
		
	public Integer getPortNumber() {
		return this.portNumber;
	}
	public void setPortNumber(final Integer portNumber) {
		if (this.portNumber != portNumber) {
			this.setDirty();
		}
		this.portNumber = portNumber;
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
