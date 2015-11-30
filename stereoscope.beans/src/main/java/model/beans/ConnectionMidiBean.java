package model.beans;

import javax.xml.bind.annotation.XmlType;

import aspects.observer.IAspectedObservable;
import aspects.observer.IManageObservers;
import aspects.observer.ObserverManager;
import model.beans.groups.MixerConnection;
import model.properties.beans.PropertyBind;

@XmlType(name = "connectionMidi")
public final class ConnectionMidiBean implements IAspectedObservable {

	@PropertyBind(displayName = "midiInputPort", group = MixerConnection.class)
	private String inputPortName;

	@PropertyBind(displayName = "midiOutputPort", group = MixerConnection.class)
	private String outputPortName;

	private final IManageObservers manager = new ObserverManager();

	public String getInputPortName() {
		return this.inputPortName;
	}
	public void setInputPortName(final String inputPortName) {
		this.inputPortName = inputPortName;
	}
	public String getOutputPortName() {
		return this.outputPortName;
	}
	public void setOutputPortName(final String outputPortName) {
		this.outputPortName = outputPortName;
	}

	@Override
	public IManageObservers getObserverManager() {
		return manager;
	}
}
