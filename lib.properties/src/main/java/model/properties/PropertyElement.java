package model.properties;

import java.util.ArrayList;

public final class PropertyElement implements IPropertyElement {
	private String name;
	private String displayName;
	private Class<? extends IPropertyGrouping> group = DefaultPropertyGroup.class;
	private Object value;
	private Class<?> valueClass;
	private final ArrayList<IObservePropertyElement> observers = new ArrayList<IObservePropertyElement>();
	
	@Override
	public Class<? extends IPropertyGrouping> getGroup() {
		return this.group;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}
	
	@Override
	public Object getValue() {
		if (this.value == null) {
			if (this.valueClass == Long.class || this.valueClass == long.class) {
				return Long.valueOf(0);
			} else if (this.valueClass == Integer.class || this.valueClass == int.class) {
				return Integer.valueOf(0);
			} else if (this.valueClass == Short.class || this.valueClass == short.class) {
				return Short.valueOf((short) 0);
			} else if (this.valueClass == Byte.class || this.valueClass == byte.class) {
				return Byte.valueOf((byte) 0);
			} else if (this.valueClass == Boolean.class || this.valueClass == boolean.class) {
				return Boolean.FALSE;
			} else if (this.valueClass == String.class) {
				return "";
			}
		}
		return this.value;
	}

	@Override
	public void setValue(final Object value) {
		this.value = value;
		this.fireValueChanged(value);
	}

	private void fireValueChanged(final Object value) {
		for (final IObservePropertyElement observer : this.observers) {
			observer.valueChanged(value);
		}
	}

	@Override
	public void setGroup(final Class<? extends IPropertyGrouping> group) {
		this.group = group;
	}

	@Override
	public Class<?> getValueClass() {
		return this.valueClass;
	}

	@Override
	public void setValueClass(final Class<?> valueClass) {
		this.valueClass = valueClass;
	}

	@Override
	public void registerObserver(final IObservePropertyElement observer) {
		this.observers.add(observer);
	}

	@Override
	public void unregisterObserver(final IObservePropertyElement observer) {
		this.observers.remove(observer);
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public void setDisplayName(final String name) {
		this.displayName = name;
	}

	@Override
	public String getLocalName() {
		return this.getName().substring(this.getName().lastIndexOf(".") + 1);
	}

}
