package com.stereokrauts.stereoscope.gui.document;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import model.properties.IPropertyElement;
import model.properties.IPropertyProvider;
import model.properties.PropertiesException;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.stereokrauts.lib.logging.StereoscopeLogManager;
/**
 * Eclipse SWT Content Provider for Properties.
 * @author th
 *
 */
public final class PropertiesStructuredContentProvider implements
		IStructuredContentProvider {
	private PropertiesStructuredContentProvider() { }
	
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
	}

	@Override
	public Object[] getElements(final Object inputElement) {
		if (inputElement instanceof IPropertyProvider) {
			final ArrayList<Object> returnValue = new ArrayList<Object>();
			final Map<String, ArrayList<IPropertyElement>> elementsGrouped = new TreeMap<String, ArrayList<IPropertyElement>>();
			final IPropertyProvider properties = (IPropertyProvider) inputElement;
			try {
				/* find out groups and sort them into map */
				for (final IPropertyElement property : properties.getPropertyCollection().getElements()) {
					final String groupName = property.getGroup().newInstance().getName();
					if (!elementsGrouped.containsKey(groupName)) {
						elementsGrouped.put(groupName, new ArrayList<IPropertyElement>());
					}
					elementsGrouped.get(groupName).add(property);
				}
				/* turn map into a structured content compatible construct */
				for (final String group : elementsGrouped.keySet()) {
					returnValue.add(new PropertyTableHeading(group));
					for (final IPropertyElement element : elementsGrouped.get(group)) {
						returnValue.add(element);
					}
				}
				return returnValue.toArray();
			} catch (final PropertiesException e) {
				StereoscopeLogManager.getLogger(this.getClass()).log(Level.WARNING, "Could not get properties for " + inputElement, e);
			} catch (final Exception e) {
				StereoscopeLogManager.getLogger(this.getClass()).log(Level.WARNING, "Could not get properties for " + inputElement + " (the grouping failed)", e);
			}
		}
		return new Object[0];
	}
	
	private static PropertiesStructuredContentProvider instance;

	public static PropertiesStructuredContentProvider getInstance() {
		if (instance == null) {
			instance = new PropertiesStructuredContentProvider();
		}
		return instance;
	}

}
