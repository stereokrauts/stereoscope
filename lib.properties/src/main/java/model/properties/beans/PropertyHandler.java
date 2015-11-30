package model.properties.beans;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import model.properties.IPropertyElement;
import model.properties.IPropertyGrouping;
import model.properties.PropertiesException;
import model.properties.PropertyCollection;
import model.properties.PropertyElement;

/**
 * This class can be used to extract all property fields of an given
 * bean, or to set the properties of an bean from a list of key-value
 * pairs.
 * @author th
 *
 */
public final class PropertyHandler {
	private PropertyHandler() { }

	/**
	 * This method class extracts all properties of a given bean.
	 * @author th
	 *
	 */
	private static final class ExtractPropertiesMethod {
		private final PropertyCollection propertyCollection;

		private ExtractPropertiesMethod(final Object object) throws PropertiesException {
			this.propertyCollection = new PropertyCollection();

			this.extractProperties(object, "");
		}

		private void extractProperties(final Object object, final String namespace) throws PropertiesException {
			final Class<?> clazz = object.getClass();
			if (!Modifier.isFinal(clazz.getModifiers())) {
				/* User modifiable beans MUST be final */
				throw new PropertiesException("The class " + clazz + " is not declared final - this is illegal when used as property bean.");
			}
			//			while (clazz != Object.class) {
			this.internalLookupPropertyAnnotationsInClass(object, clazz, namespace);
			//				clazz = clazz.getSuperclass();
			//			}
		}

		private PropertyCollection getReturnValue() {
			return this.propertyCollection;
		}

		private void internalLookupPropertyAnnotationsInClass(final Object object, final Class<?> clazz, final String namespace)
				throws PropertiesException {
			final Field[] allFields = clazz.getDeclaredFields();
			for (final Field f : allFields) {
				if (f.isAnnotationPresent(PropertyBean.class)) {
					try {
						final Object fieldValue = PropertyBeanUtil.getPropertyFieldFromObject(object, f.getName());
						if (fieldValue == null) {
							final IPropertyElement el = new PropertyElement();
							el.setName(f.getName());		
							el.setValueClass(f.getType());
							this.propertyCollection.addElement(el);
						} else {
							this.extractProperties(fieldValue, f.getName() + ".");
						}
					} catch (final Exception e) {
						throw new PropertiesException("Could not access field " + f.getName() + " on class " + clazz + ", but it is declared as PropertyBean.", e);
					}
				} else if (f.isAnnotationPresent(PropertyBind.class)) {
					try {
						this.handlePropertyBinding(object, f.getName(), namespace + f.getName(),
								f.getAnnotation(PropertyBind.class).displayName(),
								f.getAnnotation(PropertyBind.class).group());
					} catch (final Exception e) {
						throw new PropertiesException("Could not access field " + f.getName() + " on class " + clazz + ", but it is declared as PropertyBind field.", e);
					}
				}
			}
		}

		private void handlePropertyBinding(final Object o, final String beanizedFieldName, final String nameSpacedFieldName, final String displayName, final Class<? extends IPropertyGrouping> group) throws PropertiesException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			try {
				final Object value = PropertyBeanUtil.getPropertyFieldFromObject(o, beanizedFieldName);
				final Class<?> valueType = PropertyBeanUtil.getPropertyTypeFromObject(o, beanizedFieldName);

				final IPropertyElement el = new PropertyElement();
				el.setName(nameSpacedFieldName);
				el.setDisplayName(displayName);
				el.setValueClass(valueType);
				el.setValue(value);
				el.setGroup(group);

				new DefaultPropertyBeanSynchronizer(o, el);
				this.propertyCollection.addElement(el);
			} catch (final SecurityException e) {
				throw new PropertiesException("Could not access field-getter " + beanizedFieldName + " on class " + o.getClass(), e);
			} catch (final NoSuchMethodException e) {
				throw new PropertiesException("The field-getter " + beanizedFieldName + " on class " + o.getClass() + " does not exist.", e);
			}
		}

	}

	/**
	 * This method class applies properties given as PropertyCollection on a bean.
	 * @author th
	 *
	 */
	private static final class ApplyPropertiesMethod {
		private final PropertyCollection propertyCollection;

		private ApplyPropertiesMethod(final Object object, final PropertyCollection properties) throws PropertiesException {
			this.propertyCollection = properties;
			final Class<?> clazz = object.getClass();
			//			while (clazz != Object.class) {
			this.lookupPropertyAnnotationsInClass(object, clazz, "");
			//				clazz = clazz.getSuperclass();
			//			}
		}

		private void lookupPropertyAnnotationsInClass(final Object object, final Class<?> clazz, final String namespace)
				throws PropertiesException {
			final Field[] allFields = clazz.getDeclaredFields();
			for (final Field f : allFields) {
				if (f.isAnnotationPresent(PropertyBean.class)) {
					try {
						final Object fieldValue = PropertyBeanUtil.getPropertyFieldFromObject(object, f.getName());
						if (fieldValue == null) {
							throw new PropertiesException("Can not assign properties to child: " + f);
						} else {
							this.lookupPropertyAnnotationsInClass(fieldValue, fieldValue.getClass(), f.getName() + ".");
						}
					} catch (final Exception e) {
						throw new PropertiesException("Could not access field " + f.getName() + " on class " + clazz + ", but it is declared as PropertyBean.", e);
					}
				} else if (f.isAnnotationPresent(PropertyBind.class)) {
					this.handlePropertyBinding(object, f.getName(), namespace + f.getName());
				}
			}
		}

		private void handlePropertyBinding(final Object o, final String beanizedFieldName, final String nameSpacedFieldName) throws PropertiesException {
			final Method m = PropertyBeanUtil.getSetterMethod(o, beanizedFieldName);
			final IPropertyElement element = this.propertyCollection.getElementWithName(nameSpacedFieldName);
			final Object value = element.getValue();
			try {
				if (value != null) {
					m.invoke(o, value);
				}
			} catch (final SecurityException e) {
				throw new PropertiesException("Could not access setter " + m + " on class " + o.getClass(), e);
			} catch (final IllegalArgumentException e) {
				throw new PropertiesException("Type mismatch in setter " + m + " on class " + o.getClass() + ". Expected argument type: " + value.getClass(), e);
			} catch (final IllegalAccessException e) {
				throw new PropertiesException("Could not access setter " + m + " on class " + o.getClass(), e);
			} catch (final InvocationTargetException e) {
				throw new PropertiesException(e);
			}
		}

	}

	public static PropertyCollection extractProperties(final Object object) throws PropertiesException {
		return new ExtractPropertiesMethod(object).getReturnValue();
	}

	public static void applyProperties(final Object object, final PropertyCollection properties) throws PropertiesException {
		new ApplyPropertiesMethod(object, properties);
	}
}
