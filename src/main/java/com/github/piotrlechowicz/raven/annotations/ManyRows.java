package com.github.piotrlechowicz.raven.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates that filed has more than one value in row. If parameter value is not specified, it means that the field should be parsed
 * from starting position till the last column.
 *
 * @author Piotr Lechowicz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyRows {

	/**
	 * @return size of a type (if it is -1 it is parsed till the last column)
	 */
	int value() default PARSE_TILL_END;

	int PARSE_TILL_END = -1;
}
