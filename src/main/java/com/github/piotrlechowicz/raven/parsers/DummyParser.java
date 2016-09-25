package com.github.piotrlechowicz.raven.parsers;

/**
 * @author Piotr Lechowicz
 */
public class DummyParser implements Parser<String> {
	@Override
	public String parse(String input) {
		return input;
	}
}
