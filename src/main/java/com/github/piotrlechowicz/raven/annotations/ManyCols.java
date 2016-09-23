package com.github.piotrlechowicz.raven.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates that filed should be initialized with many columns). If parameter value is not specified, it means
 * that the field should be parsed from a starting position till the last column.
 *
 * @author Piotr Lechowicz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyCols {

	/**
	 * @return size of a type (if it is -1 it is parsed to the last row.)
	 */
	int value() default PARSE_TILL_END;

	int PARSE_TILL_END = -1;
}
