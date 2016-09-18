package com.github.piotrlechowicz.raven.parser;

import java.lang.annotation.*;

/**
 * Allows for repetable annotation of {@link List}
 *
 * @author Piotr Lechowicz
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Lists {
    List[] value();
}
