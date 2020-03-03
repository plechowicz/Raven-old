package com.github.piotrlechowicz.raven.parsers;

/**
 * <p>IntegerParser class.</p>
 *
 * @author Piotr Lechowicz
 * @version $Id: $Id
 */
public class IntegerParser implements Parser<Integer> {

	/** {@inheritDoc} */
	@Override
	public Integer parse(String input) {
		return input.isEmpty() ? null : Integer.parseInt(input);
	}
}
