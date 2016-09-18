package com.github.piotrlechowicz.raven.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates position of field in file
 *
 * @author Piotr Lechowicz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PositionInFile {

    /**
     * @return index of row
     */
    int row() default 0;

    /**
     * @return index of column
     */
    int col() default 0;
}
