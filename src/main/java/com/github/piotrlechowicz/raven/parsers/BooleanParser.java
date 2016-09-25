package com.github.piotrlechowicz.raven.parsers;

/**
 * Parses string to boolean value.<br/>
 * "True", "TRUE", "true", "t", "yes", "y" are parsed to true </br>
 * other are parsed to false.
 *
 * @author Piotr Lechowicz
 */
public class BooleanParser implements Parser<Boolean> {

	@Override
	public Boolean parse(String input) {
		if (input == null) {
			return Boolean.FALSE;
		} else if ("True".equals(input) ||
				"TRUE".equals(input) ||
				"true".equals(input) ||
				"t".equals(input) ||
				"yes".equals(input) ||
				"y".equals(input)
				) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
