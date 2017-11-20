package com.github.piotrlechowicz.raven.parsers;

/**
 * Defines how field should be converted from String to type T
 *
 * @param <T> type of field
 * @author Piotr Lechowicz
 * @version $Id: $Id
 */
public interface Parser<T> {

	/**
	 * Converts value from String to type T
	 *
	 * @param input input value
	 * @return converted value
	 */
	T parse(String input);
}
