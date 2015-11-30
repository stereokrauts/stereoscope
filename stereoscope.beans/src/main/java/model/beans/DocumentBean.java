package model.beans;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import aspects.observer.IAspectedObservable;
import aspects.observer.IManageObservers;
import aspects.observer.ObserverManager;

@XmlRootElement(namespace = "com.stereokrauts.stereoscope.v2") 
public final class DocumentBean implements IAspectedObservable {
	private String name;

	@XmlElementWrapper(name = "busList")
	@XmlElement(name = "bus")
	private ArrayList<BusBean> busList;

	private final IManageObservers manager = new ObserverManager();

	public String getName() {
		return this.name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	public ArrayList<BusBean> getBussesList() {
		return this.busList;
	}
	public void setBusList(final ArrayList<BusBean> busList) {
		this.busList = busList;
	}

	@Override
	public IManageObservers getObserverManager() {
		return manager ;
	}
}
