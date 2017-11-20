package com.github.piotrlechowicz.raven.parsers;

/**
 * <p>DummyParser class.</p>
 *
 * @author Piotr Lechowicz
 * @version $Id: $Id
 */
public class DummyParser implements Parser<String> {
	/** {@inheritDoc} */
	@Override
	public String parse(String input) {
		return input;
	}
}
