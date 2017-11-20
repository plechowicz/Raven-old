package com.github.piotrlechowicz.raven.parsers;

/**
 * <p>DoubleParser class.</p>
 *
 * @author Piotr Lechowicz
 * @version $Id: $Id
 */
public class DoubleParser implements Parser<Double> {

	/** {@inheritDoc} */
	@Override
	public Double parse(String input) {
		return Double.parseDouble(input);
	}
}
