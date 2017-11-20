package com.github.piotrlechowicz.raven.parsers;

/**
 * <p>FloatParser class.</p>
 *
 * @author Piotr Lechowicz
 * @version $Id: $Id
 */
public class FloatParser implements Parser<Float> {

	/** {@inheritDoc} */
	@Override
	public Float parse(String input) {
		return Float.parseFloat(input);
	}
}
