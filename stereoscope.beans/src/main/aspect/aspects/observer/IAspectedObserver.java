package aspects.observer;

public interface IAspectedObserver {
	public void valueChangedEvent(Object sender, String fieldName, Object oldValue, Object newValue);
}
