package com.github.piotrlechowicz.raven.annotations;

import com.github.piotrlechowicz.raven.parsers.DummyParser;
import com.github.piotrlechowicz.raven.parsers.IntegerParser;
import com.github.piotrlechowicz.raven.parsers.Parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates position of a value of a field in provided text file
 * <br><br>
 * {@link com.github.piotrlechowicz.raven.annotations.Parsable#row()} defines row in text file <br>
 * {@link com.github.piotrlechowicz.raven.annotations.Parsable#col()} defines col in text file<br>
 * {@link com.github.piotrlechowicz.raven.annotations.Parsable#parser()} defines to which type field should be converted from String value<br>
 * <p>
 * <br>
 * To point which value from the text file should be used, provide {@code row} and {@code col} values. </br>
 * If values are not provided, the value for row and col is equal to 0.
 * <p>
 * If field is not of type String, the {@code parser} should be specified. For example, for parsing integer
 * parser should be equal to {@link IntegerParser
 * IntegerParser.class}.
 * <p>
 * <b>Example</b>
 * Text file:
 * <pre>
 * 1 2
 * 3 4 5 6
 * 7 8 9
 * </pre>
 * <p>
 * Example class :
 * <pre>
 * {@code public class ... {
 *
 *      @literal @Parsable(col = 1, row = 3)
 *       String value;
 *   }
 * }
 * </pre>
 * <p>
 * Denotes that field "{@code value}" will be initialized with a value {@code "8"} (1st column, 3st row in a file).
 * <p>
 * </p>
 * @author Piotr Lechowicz
 * @version $Id: $Id
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Parsable {

	/**
	 * @return index of row
	 */
	int row() default 0;

	/**
	 * @return index of column
	 */
	int col() default 0;

	/**
	 *
	 * @return parser used to parse field from String to desired value
	 */
	Class<? extends Parser<?>> parser() default DummyParser.class;
}
