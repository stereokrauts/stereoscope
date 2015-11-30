package model.properties.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import model.properties.PropertiesException;

/**
 * This class is for internal use of this bundle only and defines certain
 * helper methods for the access to fields of objects. There are also
 * implementations from Apache commons giving similar functionality, so
 * this class should not exist.
 * @author th
 *
 */
final class PropertyBeanUtil {
	private PropertyBeanUtil() { }

	public static Object getPropertyFieldFromObject(final Object object, final String beanName) throws IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
		final String getterMethodName = getGetterMethodName(beanName);
		return object.getClass().getMethod(getterMethodName, new Class<?>[0]).invoke(object, new Object[0]);
	}

	public static String firstCharUpper(final String beanizedFieldName) {
		return beanizedFieldName.substring(0, 1).toUpperCase() + beanizedFieldName.substring(1);
	}

	public static Method getGetterMethod(final Object bean, final String propertyName) throws PropertiesException {
		String analyzedName = propertyName;
		Object obj = bean;
		Method m = null;
		try {
			while (analyzedName.contains(".")) {
				final String myChunk = analyzedName.substring(0, analyzedName.indexOf("."));
				analyzedName = analyzedName.substring(analyzedName.indexOf(".") + 1);
				m = obj.getClass().getMethod(getGetterMethodName(myChunk), new Class<?>[0]);
				obj = m.invoke(obj, new Object[0]);
			}
			m = obj.getClass().getMethod(getGetterMethodName(analyzedName), new Class<?>[0]);
			if (m != null) { 
				return m;
			}
		} catch (final Exception e) {
			throw new PropertiesException("Could not get setter method for bean " + bean + ", propertyName " + propertyName, e);
		}
		throw new PropertiesException("Could not get setter method for bean " + bean + ", propertyName " + propertyName);
	}

	public static Method getSetterMethod(final Object bean, final String propertyName) throws PropertiesException {
		String analyzedName = propertyName;
		Object obj = bean;
		Method m;
		try {
			while (analyzedName.contains(".")) {
				final String myChunk = analyzedName.substring(0, analyzedName.indexOf("."));
				analyzedName = analyzedName.substring(analyzedName.indexOf(".") + 1);
				m = obj.getClass().getMethod(getGetterMethodName(myChunk), new Class<?>[0]);
				obj = m.invoke(obj, new Object[0]);
			}
			m = obj.getClass().getMethod(getSetterMethodName(analyzedName), getGetterMethod(obj, analyzedName).getReturnType());
			return m;
		} catch (final Exception e) {
			throw new PropertiesException("Could not get setter method for bean " + bean + ", propertyName " + propertyName, e);
		}
	}

	private static String getSetterMethodName(final String propertyName) {
		return "set" + firstCharUpper(propertyName);
	}

	public static String getGetterMethodName(final String propertyName) {
		return "get" + firstCharUpper(propertyName);
	}

	public static Class<?> getPropertyTypeFromObject(final Object object, final String beanizedFieldName) throws NoSuchMethodException {
		final String getterMethodName = getGetterMethodName(beanizedFieldName);
		return object.getClass().getMethod(getterMethodName, new Class<?>[0]).getReturnType();
	}
}
