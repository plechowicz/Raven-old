package com.github.piotrlechowicz.raven;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Concrete implementation of FlatFileParser. It overrides the method {@code FlatFileParser{@link #createMatrixOfValues(List)}}
 * to parse strings to integers.</br>
 * Columns in file should be separated with space sign.<br/>
 * Rows in file should be separated with return sign.
 *
 * @author plechowicz
 */
public class IntegerFlatFileParser extends FlatFileParser<Integer> {

	@Override
	protected List<List<Integer>> createMatrixOfValues(List<String> rawFileContent) {
		List<List<Integer>> matrix = new ArrayList<>();
		for (String line : rawFileContent) {
			List<Integer> row = new ArrayList<>();
			Scanner scanner = new Scanner(line);
			while (scanner.hasNextInt()) {
				row.add(scanner.nextInt());
			}
			scanner.close();
			matrix.add(row);
		}
		return matrix;
	}
}
