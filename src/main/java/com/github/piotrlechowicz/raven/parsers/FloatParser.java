package com.github.piotrlechowicz.raven.parsers;

/**
 * @author Piotr Lechowicz
 */
public class FloatParser implements Parser<Float> {

	@Override
	public Float parse(String input) {
		return Float.parseFloat(input);
	}
}
