package com.github.piotrlechowicz.raven.parser;

import java.lang.annotation.*;

/**
 * Annotates that field is a list and more than one value should be parsed<br/>
 * This can be used twice for the same field to parse matrix.
 *
 * @author Piotr Lechowicz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(Lists.class)
public @interface List {

    /**
     * @return direction in which field is a list
     */
    Direction value();

    /**
     * @return size of a type (if it is -1 it is parsed till the end of file)
     */
    int size() default -1;

    /**
     * @return marks whether field is list of array
     */
    FieldType storeAs() default FieldType.LIST;

    public enum Direction {
        HORIZONTAL,
        VERTICAL
    }

    public enum FieldType {
        LIST,
        ARRAY
    }
}
