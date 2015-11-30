package model.beans;

import javax.xml.bind.annotation.XmlType;

import aspects.observer.IAspectedObservable;
import aspects.observer.IManageObservers;
import aspects.observer.ObserverManager;
import model.beans.groups.ClientConfiguration;
import model.properties.DefaultPropertyGroup;
import model.properties.beans.PropertyBind;

@XmlType(name = "client")
public final class ClientBean implements IAutoDirty, IAspectedObservable {
	@PropertyBind(displayName="Client Name", group=DefaultPropertyGroup.class)
	private String clientName = "Unnamed Surface";

	@PropertyBind(displayName="Restricted client?", group=ClientConfiguration.class)
	private Boolean restrictedClient = Boolean.FALSE;

	@PropertyBind(displayName="Restricted to Aux", group=ClientConfiguration.class)
	private Integer accessibleAux;

	@PropertyBind(displayName="Snap Faders?", group=ClientConfiguration.class)
	private Boolean snapFaders = Boolean.TRUE;

	public Boolean getRestrictedClient() {
		return this.restrictedClient;
	}

	public void setRestrictedClient(final Boolean restrictedClient) {
		if (this.restrictedClient != restrictedClient) {
			this.setDirty();
		}
		this.restrictedClient = restrictedClient;
	}

	public Integer getAccessibleAux() {
		return this.accessibleAux;
	}

	public void setAccessibleAux(final Integer accessibleAux) {
		if (this.accessibleAux != accessibleAux) {
			this.setDirty();
		}
		this.accessibleAux = accessibleAux;
	}

	public Boolean getSnapFaders() {
		return this.snapFaders;
	}

	public void setSnapFaders(final Boolean snapFaders) {
		if (this.snapFaders != snapFaders) {
			this.setDirty();
		}
		this.snapFaders = snapFaders;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(final String clientName) {
		if (this.clientName != clientName) {
			this.setDirty();
		}
		this.clientName = clientName;
	}

	private boolean isDirty = false;

	private final IManageObservers manager = new ObserverManager();
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

	@Override
	public IManageObservers getObserverManager() {
		return manager ;
	}
}
