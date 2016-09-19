package com.github.piotrlechowicz.raven.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.piotrlechowicz.raven.FlatFileParser;

/**
 * Annotates that filed has more than one value in rows. If parameter value is not specified, it means that the field should be parsed
 * from starting position till the last rcolumn.
 *
 * @author Piotr Lechowicz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyRows {

	/**
	 * @return size of a type (if it is -1 it is parsed till the last column)
	 */
	int value() default FlatFileParser.PARSE_TILL_END;
}
