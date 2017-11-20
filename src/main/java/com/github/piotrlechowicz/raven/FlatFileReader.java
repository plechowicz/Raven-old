package com.github.piotrlechowicz.raven;

import com.github.piotrlechowicz.raven.annotations.ManyCols;
import com.github.piotrlechowicz.raven.annotations.ManyRows;
import com.github.piotrlechowicz.raven.annotations.Parsable;
import com.github.piotrlechowicz.raven.parsers.DummyParser;
import com.github.piotrlechowicz.raven.parsers.Parser;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates class instance and initializes fields based on provided text file and annotations.<br>
 * Text file should be in the shape of matrix (two dimensional array) where columns are separated
 * with spaces and rows are separated with return sign.<br>
 * <p>
 * Fields which should be initialized have to be annotated with {@link com.github.piotrlechowicz.raven.annotations.Parsable}
 * </p>
 * <p>
 * To create new instance of class, use method {@link com.github.piotrlechowicz.raven.FlatFileReader#create(String)}.<br>
 * To initialize already existing instance, use method {@link com.github.piotrlechowicz.raven.FlatFileReader#initialize(Object, String)}.
 * </p>
 * <b>Example:</b><br>
 * <br>
 * Text file has following structure:<br>
 * <pre>
 * 1 2 3
 * 4 5 6
 * </pre>
 * <p>
 * There is created a class:<br>
 * <pre>{@code
 *   public class ExampleClass {
 *
 *      @literal @Parsable(row = 1, col = 2, parser = IntegerParser.class)
 *       int value;
 *   }}
 * </pre>
 * After invoking {@link com.github.piotrlechowicz.raven.FlatFileReader#create(String)} where argument points to location of the text file, the instance
 * of "{@code FlatFileReader}" class will be created and the field "{@code value}" will be set to 6.
 * </p>
 * <p>
 * To parse a list of values instead of a single value, use annotations {@link com.github.piotrlechowicz.raven.annotations.ManyCols} and/or {@link com.github.piotrlechowicz.raven.annotations.ManyRows}
 * </p>
 * @param <T> Class which will be created/initialized with the parser
 * @author Piotr Lechowicz
 * @version $Id: $Id
 */
public class FlatFileReader<T> {

	private static final Logger log = Logger.getLogger(FlatFileReader.class);

	private final Class<T> clazz;

	private T t;

	private Matrix matrix;

	/**
	 * <p>Constructor for FlatFileReader.</p>
	 *
	 * @param clazz Class which instances will be created
	 */
	public FlatFileReader(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Creates class instance and initializes fields with values in a text file pointed by the path.
	 * Values in text file are selected according to annotations.
	 *
	 * @param path path of text file
	 * @return initialized instance of class
	 * @throws java.io.IOException when file does not exist
	 */
	public T create(String path) throws IOException {

		this.t = getNewInstance(clazz);

		return parseFile(path);
	}

	/**
	 * Initializes class instance's fields with values in a text file pointed by the path. Values in text file are
	 * selected according to annotations.
	 *
	 * @param instance Instance which is going to be initialized
	 * @param path     path of text file
	 * @return initialized instance
	 * @throws java.io.IOException when file does not exist
	 */
	public T initialize(T instance, String path) throws IOException {
		this.t = instance;

		return parseFile(path);
	}

	private T parseFile(String path) throws IOException {
		List<String> rawFileContent = getFileContent(path);
		this.matrix = new Matrix(createMatrixOfValues(rawFileContent));

		for (Field field : clazz.getDeclaredFields()) {
			Parsable annotation = field.getAnnotation(Parsable.class);
			if (annotation != null) {
				initializeField(field, annotation);
			}
		}
		return t;
	}

	/**
	 * Converts lines of text in a file into two dimensional array of Strings.
	 *
	 * @param rawFileContent file content as a list of strings
	 * @return file content as matrix
	 */
	private List<List<String>> createMatrixOfValues(List<String> rawFileContent) {
		List<List<String>> matrix = new ArrayList<>();
		for (String line : rawFileContent) {
			String trimmed = line.trim();
			String[] split = trimmed.split("\\s+");
			matrix.add(Arrays.asList(split));
		}
		return matrix;
	}

	private List<String> getFileContent(String path) throws IOException {
		List<String> fileContent;
		try {
			fileContent = FileUtils.readLines(new File(path));
		} catch (IOException e) {
			log.warn("From directory: ");
			log.warn(System.getProperty("user.dir"));
			log.warn("File " + path + " is unreachable");
			log.warn(e);
			throw new IOException();
		}
		return fileContent;
	}

	private void initializeField(Field field, Parsable position) {
		field.setAccessible(true);

		ManyRows manyRows = field.getAnnotation(ManyRows.class);
		ManyCols manyCols = field.getAnnotation(ManyCols.class);
		try {
			if (manyRows == null) {
				if (manyCols == null) {
					setSingleCell(field, position);
				} else {
					setManyCols(field, position, manyCols);
				}
			} else {
				if (manyCols == null) {
					setManyRows(field, position, manyRows);
				} else {
					setManyColsAndManyRows(field, position, manyCols, manyRows);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.warn("Field=" + field.getName() + " cannot be properly set; " + ", row=" + position.row() + ", col="
					+ position.col(), e);
		}
	}

	private void setSingleCell(Field field, Parsable parsable) throws IllegalAccessException {
		Parser<?> parser = getConverter(parsable);
		field.set(t, convertField(parser, matrix.getValueAt(parsable.row(), parsable.col())));
	}

	private void setManyCols(Field field, Parsable parsable, ManyCols manyCols) throws IllegalAccessException {
		int startRow = parsable.row();
		int startCol = parsable.col();
		int colSize = manyCols.value();
		Parser<?> parser = getConverter(parsable);
		List<String> valuesInMatrix;
		if (colSize == ManyCols.PARSE_TILL_END) {
			valuesInMatrix = matrix.getRow(startRow, startCol);
		} else {
			valuesInMatrix = matrix.getRow(startRow, startCol, startCol + colSize);
		}
		field.set(t, convertList(parser, valuesInMatrix));
	}

	private void setManyRows(Field field, Parsable parsable, ManyRows manyRows) throws IllegalAccessException {
		int startRow = parsable.row();
		int startCol = parsable.col();
		int rowSize = manyRows.value();
		Parser<?> parser = getConverter(parsable);
		List<String> valuesInMatrix;
		if (rowSize == ManyRows.PARSE_TILL_END) {
			valuesInMatrix = matrix.getColumn(startRow, startCol);
		} else {
			valuesInMatrix = matrix.getColumn(startRow, startRow + rowSize, startCol);
		}
		field.set(t, convertList(parser, valuesInMatrix));
	}

	private void setManyColsAndManyRows(Field field, Parsable parsable, ManyCols manyCols, ManyRows manyRows) throws IllegalAccessException {
		int startRow = parsable.row();
		int startCol = parsable.col();
		int rowSize = manyRows.value();
		int colSize = manyCols.value();
		Parser<?> parser = getConverter(parsable);
		List<List<String>> valuesInMatrix;
		if (rowSize == ManyRows.PARSE_TILL_END) {
			if (colSize == ManyCols.PARSE_TILL_END) {
				valuesInMatrix = matrix.getRowsAndCols(startRow, startCol);
			} else {
				valuesInMatrix = matrix.getRowsAndColsInRange(startRow, startCol, startCol + colSize);
			}
		} else {
			if (colSize == ManyCols.PARSE_TILL_END) {
				valuesInMatrix = matrix.getRowsInRangeAndCols(startRow, startRow + rowSize, startCol);
			} else {
				valuesInMatrix = matrix.getRowsInRangeAndColsInRange(startRow, startRow + rowSize, startCol, startCol
						+ colSize);
			}
		}
		field.set(t, convertMatrix(parser, valuesInMatrix));
	}

	private Object convertField(Parser<?> parser, String value) {
		return parser.parse(value);
	}

	private List<Object> convertList(Parser<?> parser, List<String> values) {
		List<Object> result = new ArrayList<>();
		for (String value : values) {
			result.add(parser.parse(value));
		}
		return result;
	}

	private List<List<Object>> convertMatrix(Parser<?> parser, List<List<String>> values) {
		List<List<Object>> result = new ArrayList<>();
		for (List<String> row : values) {
			List<Object> resultRow = new ArrayList<>();
			for (String cell : row) {
				resultRow.add(parser.parse(cell));
			}
			result.add(resultRow);
		}
		return result;
	}

	private Parser<?> getConverter(Parsable parsable) {
		Parser<?> parser;
		try {
			parser = parsable.parser().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			log.warn(String.format("Unable to create parser=%s. " +
					"\nReturning DummyParser", parsable.parser()));
			parser = new DummyParser();
		}
		return parser;
	}

	private T getNewInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			log.warn("Unable to create instance of: " + clazz);
			throw new RuntimeException(e.getCause());
		}
	}
}
