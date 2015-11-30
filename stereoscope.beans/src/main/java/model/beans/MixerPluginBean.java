package model.beans;

import javax.xml.bind.annotation.XmlType;

import aspects.observer.IAspectedObservable;
import aspects.observer.IManageObservers;
import aspects.observer.ObserverManager;

@XmlType(name="mixerPlugin")
public final class MixerPluginBean implements IAspectedObservable {
	private String pluginId;
	private final IManageObservers manager = new ObserverManager();

	public String getPluginId() {
		return this.pluginId;
	}

	public void setPluginId(final String pluginId) {
		this.pluginId = pluginId;
	}

	@Override
	public IManageObservers getObserverManager() {
		return manager ;
	}
}
