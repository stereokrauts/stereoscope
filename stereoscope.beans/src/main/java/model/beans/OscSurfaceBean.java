package model.beans;

import javax.xml.bind.annotation.XmlType;

import aspects.observer.IAspectedObservable;
import model.properties.beans.PropertyBean;

@XmlType(name="oscSurface")
public final class OscSurfaceBean extends BusAttendeeBean implements IAutoDirty, IAspectedObservable {
	@PropertyBean
	private ClientBean clientConfig;
	@PropertyBean
	private ConnectionTcpUdpIpBean remoteConnParams;

	@PropertyBean
	private ConnectionServerTcpUdpIpBean localConnParams;

	public ClientBean getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(final ClientBean clientConfig) {
		if (this.clientConfig != null && ! this.clientConfig.equals(clientConfig)) {
			setDirty();
		}
		this.clientConfig = clientConfig;
	}

	public ConnectionTcpUdpIpBean getRemoteConnParams() {
		return this.remoteConnParams;
	}

	public void setRemoteConnParams(final ConnectionTcpUdpIpBean connection) {
		if (this.remoteConnParams != null && ! this.remoteConnParams.equals(connection)) {
			this.setDirty();
		}
		this.remoteConnParams = connection;
	}

	public ConnectionServerTcpUdpIpBean getLocalConnParams() {
		return this.localConnParams;
	}

	public void setLocalConnParams(final ConnectionServerTcpUdpIpBean server) {
		if (this.localConnParams != null && !this.localConnParams.equals(server)) {
			this.setDirty();
		}
		this.localConnParams = server;
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
	protected void setDirty() {
		this.isDirty = true;
	}

}
