package com.github.piotrlechowicz.raven;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Concrete implementation of FlatFileParser. It overrides the method {@code createMatrixOfValues}
 * to work with integers.
 *
 * @author plechowicz
 */
public final class IntegerFlatFileParser extends FlatFileParser<Integer> {

	@Override
	protected List<List<Integer>> createMatrixOfValues(List<String> rawFileContent) {
		List<List<Integer>> rows = new ArrayList<>();
		for (String line : rawFileContent) {
			List<Integer> col = new ArrayList<>();
			Scanner scanner = new Scanner(line);
			while (scanner.hasNextInt()) {
				col.add(scanner.nextInt());
			}
			scanner.close();
			rows.add(col);
		}
		return rows;
	}
}
