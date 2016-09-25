package com.github.piotrlechowicz.raven.parsers;

/**
 * @author Piotr Lechowicz
 */
public class ByteParser implements Parser<Byte> {

	@Override
	public Byte parse(String input) {
		return Byte.parseByte(input);
	}
}
