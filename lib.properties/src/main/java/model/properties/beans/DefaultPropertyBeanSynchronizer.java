package model.properties.beans;

import java.lang.reflect.Method;
import java.util.logging.Level;

import model.properties.IObservePropertyElement;
import model.properties.IPropertyElement;

import com.stereokrauts.lib.logging.StereoscopeLogManager;

/**
 * This class provides a synchronization mechanism between properties
 * and beans, so that the bean get's changed when the property is
 * changed. The synchronization into the other direction must be
 * done using the bean observing mechanisms.
 * @author th
 */
public final class DefaultPropertyBeanSynchronizer implements IObservePropertyElement {
	private final Object bean;
	private final IPropertyElement observed;

	public DefaultPropertyBeanSynchronizer(final Object bean, final IPropertyElement observed) {
		this.bean = bean;
		this.observed = observed;
		observed.registerObserver(this);
	}
	
	@Override
	public void valueChanged(final Object newValue) {
		try {
			final Method m = PropertyBeanUtil.getSetterMethod(this.bean, this.observed.getLocalName());
			m.invoke(this.bean, newValue);
		} catch (final Exception e) {
			StereoscopeLogManager.getLogger(DefaultPropertyBeanSynchronizer.class).log(Level.SEVERE, "Could not synchronize new bean value " + newValue + " on property " + this.observed + " on object " + this.bean, e);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (this.observed != null) {
			this.observed.unregisterObserver(this);
		}
	}

}
