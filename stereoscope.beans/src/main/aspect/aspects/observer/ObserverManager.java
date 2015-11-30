package aspects.observer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ObserverManager implements IManageObservers {
	private final List<IAspectedObserver> observers = new ArrayList<IAspectedObserver>();
	
	@Override
	public void addObserver(final IAspectedObserver o) {
		this.observers.add(o);
	}

	@Override
	public void removeObserver(final IAspectedObserver o) {
		this.observers.remove(o);
	}

	@Override
	public Collection<IAspectedObserver> getObservers() {
		return Collections.unmodifiableList(this.observers);
	}

	@Override
	public void notifyObservers(final Object sender, final String fieldName, final Object oldValue, final Object newValue) {
		for (final IAspectedObserver observer : this.observers) {
			observer.valueChangedEvent(sender, fieldName, oldValue, newValue);
		}
	}

}
