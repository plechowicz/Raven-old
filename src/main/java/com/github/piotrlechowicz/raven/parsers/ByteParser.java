package com.github.piotrlechowicz.raven.parsers;

/**
 * <p>ByteParser class.</p>
 *
 * @author Piotr Lechowicz
 * @version $Id: $Id
 */
public class ByteParser implements Parser<Byte> {

	/** {@inheritDoc} */
	@Override
	public Byte parse(String input) {
		return Byte.parseByte(input);
	}
}
