package com.github.piotrlechowicz.raven.parsers;

/**
 * @author Piotr Lechowicz
 */
public class IntegerParser implements Parser<Integer> {

	@Override
	public Integer parse(String input) {
		return Integer.parseInt(input);
	}
}
