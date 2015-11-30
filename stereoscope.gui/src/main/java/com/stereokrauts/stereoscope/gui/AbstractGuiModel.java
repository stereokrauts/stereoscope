package com.stereokrauts.stereoscope.gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This class is a helper to provide access to JFace Data Binding API.
 * @author th
 *
 */
public abstract class AbstractGuiModel {
	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	public final void addPropertyChangeListener(final PropertyChangeListener listener) {
		this.changeSupport.addPropertyChangeListener(listener);
	}

	public final void removePropertyChangeListener(final PropertyChangeListener listener) {
		this.changeSupport.removePropertyChangeListener(listener);
	}

	public final void addPropertyChangeListener(final String propertyName,
			final PropertyChangeListener listener) {
		this.changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public final void removePropertyChangeListener(final String propertyName,
			final PropertyChangeListener listener) {
		this.changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	protected final void firePropertyChange(final String propertyName, final Object oldValue,
			final Object newValue) {
		this.changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}
}
