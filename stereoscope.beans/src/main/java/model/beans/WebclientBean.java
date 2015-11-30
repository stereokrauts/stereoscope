package model.beans;

import javax.xml.bind.annotation.XmlType;

import aspects.observer.IAspectedObservable;
import model.properties.DefaultPropertyGroup;
import model.properties.beans.PropertyBean;
import model.properties.beans.PropertyBind;

@XmlType(name="webclient")
public final class WebclientBean extends BusAttendeeBean implements IAutoDirty, IAspectedObservable {
	@PropertyBean
	private ClientBean clientConfig;

	@PropertyBind(displayName="Port Number", group = DefaultPropertyGroup.class)
	private int portNumber;

	public ClientBean getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(final ClientBean clientConfig) {
		if (this.clientConfig != null && ! this.clientConfig.equals(clientConfig)) {
			setDirty();
		}
		this.clientConfig = clientConfig;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
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
	protected void setDirty() {
		this.isDirty = true;
	}
}
