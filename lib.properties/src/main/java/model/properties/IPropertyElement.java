package model.properties;

public interface IPropertyElement {
	Class<? extends IPropertyGrouping> getGroup();
	void setGroup(Class<? extends IPropertyGrouping> group);
	
	String getName();
	void setName(String name);
	
	String getDisplayName();
	void setDisplayName(String name);
	
	Object getValue();
	void setValue(Object value);
	
	Class<?> getValueClass();
	void setValueClass(Class<?> clazz);
	
	void registerObserver(IObservePropertyElement observer);
	void unregisterObserver(IObservePropertyElement observer);
	String getLocalName();
}
