package com.github.piotrlechowicz.raven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Concrete implementation of FlatFileParser. It overrides the method {@code FlatFileParser{@link #createMatrixOfValues(List)}} to
 * work with strings. <br/>
 * Columns in file should be separated with space sign.<br/>
 * Rows should be separated with return sign.
 *
 * @author plechowicz
 */
public class StringFlatFileParser extends FlatFileParser<String> {

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
