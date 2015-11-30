package model.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PropertyCollection {
	private final List<IPropertyElement> elements = new ArrayList<IPropertyElement>();
	
	public List<IPropertyElement> getElements() {
		return Collections.unmodifiableList(this.elements);
	}
	
	public IPropertyElement getElementAt(final int index) {
		return this.elements.get(index);
	}
	
	public void addElement(final IPropertyElement element) {
		this.elements.add(element);
	}

	public int count() {
		return this.elements.size();
	}

	public IPropertyElement getElementWithName(final String keyToApply) throws PropertyElementNotFoundException {
		for (final IPropertyElement element : this.elements) {
			if (element.getName().equals(keyToApply)) {
				return element;
			}
		}
		throw new PropertyElementNotFoundException("No property with name " + keyToApply);
	}

	public List<String >getPropertyNames() {
		final List<String> propertyNames = new ArrayList<String>();
		for (final IPropertyElement element : this.getElements()) {
			propertyNames.add(element.getName());
		}
		return Collections.unmodifiableList(propertyNames);
	}

	public void setValue(final String name, final Object value) throws PropertyElementNotFoundException {
		this.getElementWithName(name).setValue(value);
	}
}
