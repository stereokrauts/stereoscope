package aspects.observer;

import java.util.Collection;

public interface IManageObservers {
	public void addObserver(IAspectedObserver o);
	public void removeObserver(IAspectedObserver o);
	
	public Collection<IAspectedObserver> getObservers();
	public void notifyObservers(Object sender, String fieldName, Object oldValue, Object newValue);
}
