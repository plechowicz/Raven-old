package com.github.piotrlechowicz.raven.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates value of a field in provided text file
 *<br><br>
 * {@link PositionInFile#row()} defines row in text file <br/>
 * {@link PositionInFile#col()} defines col in text file<br/>
 *
 *<br>
 *     If values are not provided, the value for row and col is equal to 0.
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
 *      @literal @PositionInFile(col = 1, row = 3)
 *       int value;
 *   }
 * }
 * </pre>
 *
 * Denoets that field "{@code value}" will be initialized with a value 8 (1st column, 3st row in a file).
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
