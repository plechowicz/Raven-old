package com.github.piotrlechowicz.raven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Concrete implementation of FlatFileParser. It overrides the method {@code createMatrixOfValues} to work with strings. Lines are separated with space sign.
 *
 * @author plechowicz
 */
public final class StringFaltFileParser extends FlatFileParser<String> {

	@Override
	protected List<List<String>> createMatrixOfValues(List<String> rawFileContent) {
		List<List<String>> rows = new ArrayList<>();
		for (String line : rawFileContent) {
			String[] split = line.split("\\s+");
			rows.add(Arrays.asList(split));
		}
		return rows;
	}
}
