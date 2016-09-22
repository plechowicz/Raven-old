package com.github.piotrlechowicz.raven.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates value of field in the text file
 *<br><br>
 * {@link PositionInFile#row()} defines row in text file <br/>
 * {@link PositionInFile#col()} defines col in text file<br/>
 *
 *<br>
 *     If values are not provided, the default value 0 is taken.
 *<p>
 *<b>Example</b>
 * Text file:
 * <pre>
 * 1 2
 * 3 4 5 6
 * 7 8 9
 * </pre>
 *
 * Example class :
 * <pre>
 * {@code public class ... {
 *      @literal @PositionInFile(col = 3, row = 1)
 *       int value;
 *   }
 * }
 * </pre>
 *
 * Defines that field "{@code value}" will be 8.
 *
 *</p>
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
