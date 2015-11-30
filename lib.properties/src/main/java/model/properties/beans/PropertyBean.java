package model.properties.beans;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation marks a field of a class as object, which contains
 * further properties (i.e. further {@link PropertyBean} or {@link PropertyBind}
 * elements).
 * @author th
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyBean {
}
