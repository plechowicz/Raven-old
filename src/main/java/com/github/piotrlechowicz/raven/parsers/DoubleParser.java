package com.github.piotrlechowicz.raven.parsers;

/**
 * @author Piotr Lechowicz
 */
public class DoubleParser implements Parser<Double> {

	@Override
	public Double parse(String input) {
		return Double.parseDouble(input);
	}
}
