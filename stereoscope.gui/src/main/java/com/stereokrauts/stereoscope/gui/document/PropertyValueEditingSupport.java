package com.stereokrauts.stereoscope.gui.document;

import model.properties.IPropertyElement;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

public final class PropertyValueEditingSupport extends EditingSupport {

	private final TableViewer viewer;

	public PropertyValueEditingSupport(final TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(final Object element) {
		if (element instanceof IPropertyElement) {
			final IPropertyElement propertyElement = (IPropertyElement) element;
			final Class<?> valueClass =  propertyElement.getValueClass();
			if (valueClass == Boolean.class || valueClass == boolean.class) {
				return new CheckboxCellEditor(this.viewer.getTable());
			} else if (valueClass == String.class
					|| valueClass == Long.class    || valueClass == long.class
					|| valueClass == Integer.class || valueClass == int.class
					|| valueClass == Short.class   || valueClass == short.class
					|| valueClass == Byte.class    || valueClass == byte.class) {
				return new TextCellEditor(this.viewer.getTable());
			}
		}
		return null;
	}

	@Override
	protected boolean canEdit(final Object element) {
		return true;
	}

	@Override
	protected Object getValue(final Object element) {
		if (element instanceof IPropertyElement) {
			final IPropertyElement propertyElement = (IPropertyElement) element;
			if (propertyElement.getValueClass() == Boolean.class || propertyElement.getValueClass() == boolean.class) {
				return propertyElement.getValue();
			}
			return propertyElement.getValue().toString();
		} else {
			return null;
		}
	}

	@Override
	protected void setValue(final Object element, final Object value) {
		if (element instanceof IPropertyElement) {
			final IPropertyElement propertyElement = (IPropertyElement) element;
			final Class<?> valueClass = propertyElement.getValueClass();
			if (valueClass == Boolean.class || valueClass == boolean.class) {
				propertyElement.setValue(value);
			} else if (valueClass == String.class) {
				propertyElement.setValue(value.toString());
			} else if (valueClass == Long.class    || valueClass == long.class) {
				propertyElement.setValue(Long.parseLong(value.toString()));
			} else if (valueClass == Integer.class || valueClass == int.class) {
				propertyElement.setValue(Integer.parseInt(value.toString()));
			} else if (valueClass == Short.class   || valueClass == short.class) {
				propertyElement.setValue(Short.parseShort(value.toString()));
			} else if (valueClass == Byte.class    || valueClass == byte.class) {
				propertyElement.setValue(Byte.parseByte(value.toString()));
			} else {
				throw new IllegalArgumentException("Unknown type for value of property " + propertyElement.getName() + ". Expected: " + valueClass + ", got:" + value.getClass());
			}
			this.viewer.refresh();
		}
	}

}
