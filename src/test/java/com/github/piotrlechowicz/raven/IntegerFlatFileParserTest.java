package com.github.piotrlechowicz.raven;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.piotrlechowicz.raven.annotations.ManyCols;
import com.github.piotrlechowicz.raven.annotations.ManyRows;
import com.github.piotrlechowicz.raven.annotations.PositionInFile;

/**
 * @author plechowicz
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(IntegerFlatFileParser.class)
public class IntegerFlatFileParserTest {

	private IntegerFlatFileParser flatFileParser = PowerMockito.spy(new IntegerFlatFileParser());
	private ClassToParse parsedClass;


	@Before
	public void setUp() throws Exception {
		doReturn(exampleFileContent).when(flatFileParser, "getFileContent", any(String.class));
		parsedClass = flatFileParser.parseFile(ClassToParse.class, "");
	}

	@Test
	public void testSingleCell() {
		assertEquals(2, parsedClass.singleValue);
	}

	@Test
	public void testOneRow() {
		final int[] result = {1, 2, 2};
		assertEquals(result.length, parsedClass.oneRow.size());
		for (int i = 0; i < result.length; i++) {
			assertEquals(result[i], (int) parsedClass.oneRow.get(i));
		}
	}

	@Test
	public void testOneRowTillEnd() {
		final int[] result = {8, 7, 6, 5, 4, 3, 2, 1};
		assertEquals(result.length, parsedClass.oneRowTillEnd.size());
		for (int i = 0; i < result.length; i++) {
			assertEquals(result[i], (int) parsedClass.oneRowTillEnd.get(i));
		}
	}

	@Test
	public void testOneCol() {
		final int[] result = {4, 1, 4};
		assertEquals(result.length, parsedClass.oneCol.size());
		for (int i = 0; i < result.length; i++) {
			assertEquals(result[i], (int) parsedClass.oneCol.get(i));
		}
	}

	@Test
	public void testOneColTillEnd() {
		final int[] result = {3, 4, 3, 2};
		assertEquals(result.length, parsedClass.oneColTillEnd.size());
		for (int i = 0; i < result.length; i++) {
			assertEquals(result[i], (int) parsedClass.oneColTillEnd.get(i));
		}
	}

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

	@Test
	public void testMatrixTillEnd() {
		final int[][] result = {{20}, {2, 3, 4, 5, 6, 7, 8}, {0, 1, 1, 2, 2, 3, 3},
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
		add("5 20");
		add("1 2 3 4 5 6 7 8");
		add("0 0 1 1 2 2 3 3");
		add("4 3 2 4 3 2 4 3 2");
		add("9 8 7 6 5 4 3 2 1");
		add("6 5 5 4 4 2 2 0 0");
	}};

	public static class ClassToParse {

		//expected 2
		@PositionInFile(col = 1, row = 1)
		int singleValue;

		//expected {1, 2, 2}
		@PositionInFile(col = 3, row = 2)
		@ManyCols(3)
		List<Integer> oneRow;

		//expected {8, 7, 6, 5, 4, 3, 2, 1}
		@PositionInFile(col = 1, row = 4)
		@ManyCols
		List<Integer> oneRowTillEnd;

		//expected {4, 1, 4}
		@PositionInFile(col = 3, row = 1)
		@ManyRows(3)
		List<Integer> oneCol;

		//expected {3, 4, 3, 2}
		@PositionInFile(col = 6, row = 2)
		@ManyRows
		List<Integer> oneColTillEnd;

		//expected {{0, 1, 1}, {3, 2, 4}}
		@PositionInFile(col = 1, row = 2)
		@ManyCols(3)
		@ManyRows(2)
		List<List<Integer>> matrix;

		//expected {{20}, {2, 3, 4, 5, 6, 7, 8}, {0, 1, 1, 2, 2, 3, 3},
		// {3, 2, 4, 3, 2, 4, 3, 2}, {8, 7, 6, 5, 4, 3, 2, 1},
		// {5, 5, 4, 4, 2, 2, 0, 0}}
		@PositionInFile(col = 1, row = 0)
		@ManyCols
		@ManyRows
		List<List<Integer>> matrixTillEnd;
	}
}