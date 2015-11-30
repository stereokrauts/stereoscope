package model.properties.beans;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import model.properties.IPropertyGrouping;

/**
 * This annotation defines that a field of an class is a property.
 * @author th
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyBind {
	/**
	 * @return a human readable name for this property, i.e. to display in a GUI.
	 */
	String displayName();
	/**
	 * @return an optional group to which this property belongs.
	 */
	Class<? extends IPropertyGrouping> group(); 
}
