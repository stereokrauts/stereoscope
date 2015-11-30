package model.beans;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import aspects.observer.IAspectedObservable;
import aspects.observer.IManageObservers;
import aspects.observer.ObserverManager;

/**
 * This bean holds configuration for a bus attendee.
 * @author theide
 *
 */
@XmlType(name = "attendee")
@XmlSeeAlso({ BusBean.class, MixerBean.class, OscSurfaceBean.class, WebclientBean.class })
public class BusAttendeeBean implements IAspectedObservable {
	private final IManageObservers manager = new ObserverManager();

	@Override
	public final IManageObservers getObserverManager() {
		return this.manager;
	}
}
