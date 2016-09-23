package com.github.piotrlechowicz.raven;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.github.piotrlechowicz.raven.annotations.ManyCols;
import com.github.piotrlechowicz.raven.annotations.ManyRows;
import com.github.piotrlechowicz.raven.annotations.PositionInFile;

/**
 * Abstract class which creates class instance and initializes fields based on provided text file and annotations.<br/>
 * Text file is parsed into matrix (two dimensional array) of values according to implementation of abstract
 * method: {@code List<List<X>> createMatrixOfValues(List<String> rawFileContent)}. <br/>
 * The parameter <code>X</code> describes to which type all values in the file should be parsed.
 * <p>
 * Fields which should be initialized have to be annotated with {@link PositionInFile}
 * </p>
 * <p>
 * <p>
 * <b>Example:</b><br/>
 * The method {@link FlatFileParser#createMatrixOfValues(List)} is implemented to parse integers (see
 * {@link IntegerFlatFileParser})
 * <br/><br/>
 * Text file has following structure:<br/>
 * <pre>
 * 1 2 3
 * 4 5 6
 *     </pre>
 * <p>
 * There is created a class:<br/>
 * <pre>{@code
 *   public class ExampleClass {
 *      @literal @PositionInFile(row = 1, col = 2)
 *       int value;
 *   }}
 * </pre>
 * After invoking {@link FlatFileParser#parseFile(Class, String)} with arguments {@code (ExampleClass.class, path)}
 * the field "{@code value}" will be set to 6.
 * </p>
 * <p>
 * To parse a list of values instead of a single value, use annotations {@link ManyCols} and/or {@link ManyRows}
 * </p>
 *
 * @author Piotr Lechowicz
 */
public abstract class FlatFileParser<X> {

	private static final Logger log = Logger.getLogger(FlatFileParser.class);

	/**
	 * Creates class instance and initializes fields with values in a text file pointed by the path. Values in text file are selected
	 * according to annotations.
	 *
	 * @param clazz Class which instance will be created
	 * @param path  path of text file
	 * @param <T>   class which is going to be created
	 * @return initialized instance of class
	 * @throws IOException when file does not exist
	 */
	public final <T> T parseFile(Class<T> clazz, String path) throws IOException {

		InstanceBuilder<T> builder;
		builder = new InstanceBuilder<>(clazz);
		return parseFile(builder, clazz, path);
	}

	/**
	 * Initializes class instance's fields with values in a text file pointed by the path. Values in text file are
	 * selected according to annotations.
	 *
	 * @param instance Instance which is going to be initialized
	 * @param path     path of text file
	 * @param <T>      class which is going to be initialized
	 * @return initialized instance
	 * @throws IOException when file does not exist
	 */
	public final <T> T parseFile(T instance, String path) throws IOException {
		InstanceBuilder<T> builder = new InstanceBuilder<T>(instance);
		return parseFile(builder, (Class<T>) instance.getClass(), path);
	}

	private final <T> T parseFile(InstanceBuilder<T> builder, Class<T> clazz, String path) throws IOException {
		List<String> rawFileContent = getFileContent(path);
		builder.setMatrix(createMatrixOfValues(rawFileContent));

		for (Field field : clazz.getDeclaredFields()) {
			PositionInFile annotation = field.getAnnotation(PositionInFile.class);
			if (annotation != null) {
				builder.initializeField(field, annotation);
			}
		}
		return builder.getInstance();
	}

	/**
	 * Defines how lines of text in a file are parsed to the matrix of values.
	 *
	 * @param rawFileContent file content as a list of strings
	 * @return file content as matrix; outer list is a row, inner is a column
	 */
	protected abstract List<List<X>> createMatrixOfValues(List<String> rawFileContent);

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


	/**
	 * Inner class to help in creation of generic class form parsed flat file
	 *
	 * @param <T> Class to which the file should be parsed
	 * @author plechowicz
	 */
	private class InstanceBuilder<T> {

		private final T t;
		private Matrix<X> matrix;


		InstanceBuilder(Class<T> clazz) {
			t = getNewInstance(clazz);
		}

		InstanceBuilder(T instance) {
			this.t = instance;
		}

		T getInstance() {
			return t;
		}

		void setMatrix(List<List<X>> matrix) {
			this.matrix = new Matrix<>(matrix);
		}

		void initializeField(Field field, PositionInFile position) {
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
				log.warn("Field=" + field.getName() + " cannot be properly set; " + ", row=" + position.row() + ", col=" + position.col());
			}
		}

		private void setSingleCell(Field field, PositionInFile position) throws IllegalAccessException {
			field.set(t, matrix.getValueAt(position.row(), position.col()));
		}

		private void setManyCols(Field field, PositionInFile position, ManyCols manyCols) throws IllegalAccessException {
			int startRow = position.row();
			int startCol = position.col();
			int colSize = manyCols.value();
			if (colSize == ManyCols.PARSE_TILL_END)
				field.set(t, matrix.getRow(startRow, startCol));
			else
				field.set(t, matrix.getRow(startRow, startCol, startCol + colSize));
		}

		private void setManyRows(Field field, PositionInFile position, ManyRows manyRows) throws IllegalAccessException {
			int startRow = position.row();
			int startCol = position.col();
			int rowSize = manyRows.value();
			if (rowSize == ManyRows.PARSE_TILL_END) {
				field.set(t, matrix.getColumn(startRow, startCol));
			} else {
				field.set(t, matrix.getColumn(startRow, startRow + rowSize, startCol));
			}
		}

		private void setManyColsAndManyRows(Field field, PositionInFile position, ManyCols manyCols, ManyRows manyRows) throws IllegalAccessException {
			int startRow = position.row();
			int startCol = position.col();
			int rowSize = manyRows.value();
			int colSize = manyCols.value();

			if (rowSize == ManyRows.PARSE_TILL_END) {
				if (colSize == ManyCols.PARSE_TILL_END) {
					field.set(t, matrix.getRowsAndCols(startRow, startCol));
				} else {
					field.set(t, matrix.getRowsAndColsInRange(startRow, startCol, startCol + colSize));
				}
			} else {
				if (colSize == ManyCols.PARSE_TILL_END) {
					field.set(t, matrix.getRowsInRangeAndCols(startRow, startRow + rowSize, startCol));
				} else {
					field.set(t, matrix.getRowsInRangeAndColsInRange(startRow, startRow + rowSize, startCol, startCol + colSize));
				}
			}
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
}
