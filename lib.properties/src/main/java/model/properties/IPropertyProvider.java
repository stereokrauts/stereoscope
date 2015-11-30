package model.properties;

public interface IPropertyProvider {
	PropertyCollection getPropertyCollection() throws PropertiesException;
}
