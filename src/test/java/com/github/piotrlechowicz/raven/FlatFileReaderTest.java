package com.github.piotrlechowicz.raven;

import com.github.piotrlechowicz.raven.annotations.ManyCols;
import com.github.piotrlechowicz.raven.annotations.ManyRows;
import com.github.piotrlechowicz.raven.annotations.Parsable;
import com.github.piotrlechowicz.raven.parsers.DoubleParser;
import com.github.piotrlechowicz.raven.parsers.IntegerParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doReturn;

/**
 * <p>FlatFileReaderTest class.</p>
 *
 * @author plechowicz
 * @version $Id: $Id
 * @since 1.0
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FlatFileReader.class)
public class FlatFileReaderTest {

	private final FlatFileReader<ClassToParse> flatFileReader = PowerMockito.spy(new FlatFileReader<>(ClassToParse.class));
	private ClassToParse parsedClass;

	/**
	 * <p>setUp.</p>
	 *
	 * @throws java.lang.Exception if any.
	 */
	@Before
	public void setUp() throws Exception {
		doReturn(exampleFileContent).when(flatFileReader, "getFileContent", any(String.class));
		parsedClass = flatFileReader.create("");
	}

	/**
	 * <p>testSingleCell.</p>
	 */
	@Test
	public void testSingleCell() {
		assertEquals(2, parsedClass.singleValue);
	}

	/**
	 * <p>testOneRow.</p>
	 */
	@Test
	public void testOneRow() {
		final int[] result = {1, 2, 2};
		assertEquals(result.length, parsedClass.oneRow.size());
		for (int i = 0; i < result.length; i++) {
			assertEquals(result[i], (int) parsedClass.oneRow.get(i));
		}
	}

	/**
	 * <p>testOneRowTillEnd.</p>
	 */
	@Test
	public void testOneRowTillEnd() {
		final Double[] result = {1.1, 1.2};
		assertEquals(result.length, parsedClass.oneRowTillEnd.size());
		for (int i = 0; i < result.length; i++) {
			assertEquals(result[i], parsedClass.oneRowTillEnd.get(i));
		}
	}

	/**
	 * <p>testOneCol.</p>
	 */
	@Test
	public void testOneCol() {
		final String[] result = {"4", "1", "4"};
		assertEquals(result.length, parsedClass.oneCol.size());
		for (int i = 0; i < result.length; i++) {
			assertEquals(result[i], parsedClass.oneCol.get(i));
		}
	}

	/**
	 * <p>testOneColTillEnd.</p>
	 */
	@Test
	public void testOneColTillEnd() {
		final int[] result = {3, 4, 3, 2};
		assertEquals(result.length, parsedClass.oneColTillEnd.size());
		for (int i = 0; i < result.length; i++) {
			assertEquals(result[i], (int) parsedClass.oneColTillEnd.get(i));
		}
	}

	/**
	 * <p>testMatrix.</p>
	 */
	@Test
	public void testMatrix() {
		final int[][] result = {{0, 1, 1}, {3, 2, 4}};
		assertEquals(result.length, parsedClass.matrix.size());
		for (int i = 0; i < result.length; i++) {
			assertEquals(result[i].length, parsedClass.matrix.get(i).size());
			for (int j = 0; j < result[i].length; j++) {
				assertEquals(result[i][j], (int) parsedClass.matrix.get(i).get(j));
			}
		}
	}

	/**
	 * <p>testMatrixTillEnd.</p>
	 */
	@Test
	public void testMatrixTillEnd() {
		final int[][] result = {{2, 3, 4, 5, 6, 7, 8}, {0, 1, 1, 2, 2, 3, 3},
				{3, 2, 4, 3, 2, 4, 3, 2}, {8, 7, 6, 5, 4, 3, 2, 1},
				{5, 5, 4, 4, 2, 2, 0, 0}};
		assertEquals(result.length, parsedClass.matrixTillEnd.size());
		for (int i = 0; i < result.length; i++) {
			assertEquals(result[i].length, parsedClass.matrixTillEnd.get(i).size());
			for (int j = 0; j < result[i].length; j++) {
				assertEquals(result[i][j], (int) parsedClass.matrixTillEnd.get(i).get(j));
			}
		}
	}

	private static final List<String> exampleFileContent = new ArrayList<String>() {{
		add("1.1 1.2");
		add(" 1 2 3 4 5 6 7 8");
		add("0 0 1 1 2 2 3 3");
		add(" 4 3 2 4 3 2 4 3 2");
		add("9 8 7 6 5 4 3 2 1");
		add(" 6 5 5 4 4 2 2 0 0");
	}};

	public static class ClassToParse {

		//expected 2
		@Parsable(col = 1, row = 1, parser = IntegerParser.class)
		int singleValue;

		//expected {1, 2, 2}
		@Parsable(col = 3, row = 2, parser = IntegerParser.class)
		@ManyCols(3)
		List<Integer> oneRow;

		//expected {1.1, 1.2}
		@Parsable(col = 0, row = 0, parser = DoubleParser.class)
		@ManyCols
		List<Double> oneRowTillEnd;

		//expected {4, 1, 4}
		@Parsable(col = 3, row = 1)
		@ManyRows(3)
		List<String> oneCol;

		//expected {3, 4, 3, 2}
		@Parsable(col = 6, row = 2, parser = IntegerParser.class)
		@ManyRows
		List<Integer> oneColTillEnd;

		//expected {{0, 1, 1}, {3, 2, 4}}
		@Parsable(col = 1, row = 2, parser = IntegerParser.class)
		@ManyCols(3)
		@ManyRows(2)
		List<List<Integer>> matrix;

		//expected {{2, 3, 4, 5, 6, 7, 8}, {0, 1, 1, 2, 2, 3, 3},
		// {3, 2, 4, 3, 2, 4, 3, 2}, {8, 7, 6, 5, 4, 3, 2, 1},
		// {5, 5, 4, 4, 2, 2, 0, 0}}
		@Parsable(col = 1, row = 1, parser = IntegerParser.class)
		@ManyCols
		@ManyRows
		List<List<Integer>> matrixTillEnd;
	}
}
