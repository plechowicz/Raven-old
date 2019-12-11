package com.github.piotrlechowicz.raven.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates field storing in result file.
 * <br/>
 * @author plechowicz
 * created on 12/9/2019.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Savable {

	/**
	 * @return specify how value in field should be formatted
	 */
	String valueFormat() default "%s";

	/**
	 * Specify name of the field, if no name is provided, the field name is used
	 * @return
	 */
	String header() default "";

	/**
	 * Specify how field name should be formatted
	 * @return
	 */
	String headerFormat() default "%s";
}
