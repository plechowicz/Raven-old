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
 * Class created to parse different type of files. It contains abstract
 * method: {@code List<List<?>> createMatrixOfValues(List<String> rawFileContent)} which has to be
 * defined. It determines how the lines of the file will be interpreted. <br/>
 * The parameter <code>X</code> describes to which type values in file should be parsed.
 *
 * @author Piotr Lechowicz
 */
public abstract class FlatFileParser<X> {

	public static final int PARSE_TILL_END = -1;

	private static final Logger log = Logger.getLogger(FlatFileParser.class);

	private List<List<X>> matrix;

	public <T> T parseFile(Class<T> clazz, String path) throws IOException {

		InstanceBuilder<T> builder;

		try {
			builder = new InstanceBuilder<>(clazz);
		} catch (InstantiationException | IllegalAccessException e) {
			log.warn("Unable to create instance of: " + clazz);
			throw new RuntimeException(e.getCause());
		}

		List<String> rawFileContent = getFileContent(path);
		matrix = createMatrixOfValues(rawFileContent);
		builder.setMatrix(matrix);

		for (Field field : clazz.getDeclaredFields()) {
			PositionInFile annotation = field.getAnnotation(PositionInFile.class);
			if (annotation != null) {
				builder.initializeField(field, annotation);
			}
		}
		return builder.getInstance();
	}

	/**
	 * @param rawFileContent file content as a list of strings
	 * @return file content as matrix; outer list is a row, inner is a column
	 */
	protected abstract List<List<X>> createMatrixOfValues(List<String> rawFileContent);

	private List<String> getFileContent(String path) throws IOException {
		List<String> fileContent = null;
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

	private <T> T getNewInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			log.warn("Unable to create instance of: " + clazz);
			throw new RuntimeException(e.getCause());
		}
	}


	/**
	 * Inner class to help in creation of generic class form parsed flat file
	 *
	 * @param <T> Class to which the file should be parsed
	 * @author plechowicz
	 */
	private class InstanceBuilder<T> {

		private T t;
		private Matrix<X> matrix;

		public InstanceBuilder(Class<T> clazz) throws InstantiationException, IllegalAccessException {
			t = clazz.newInstance();
		}

		public T getInstance() {
			return t;
		}

		public void setMatrix(List<List<X>> matrix) {
			this.matrix = new Matrix<X>(matrix);
		}

		public void initializeField(Field field, PositionInFile position) {
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
				log.warn("Field cannot be properly set: " + field + " error: " + e);
			}
		}

		private void setSingleCell(Field field, PositionInFile position) throws IllegalAccessException {
			field.set(t, matrix.getValueAt(position.row(), position.col()));
		}

		private void setManyCols(Field field, PositionInFile position, ManyCols manyCols) throws IllegalAccessException {
			int startRow = position.row();
			int startCol = position.col();
			int colSize = manyCols.value();
			if (colSize == PARSE_TILL_END)
				field.set(t, matrix.getRow(startRow, startCol));
			else
				field.set(t, matrix.getRow(startRow, startCol, startCol + colSize));
		}

		private void setManyRows(Field field, PositionInFile position, ManyRows manyRows) throws IllegalAccessException {
			int startRow = position.row();
			int startCol = position.col();
			int rowSize = manyRows.value();
			if (rowSize == PARSE_TILL_END) {
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

			if (rowSize == PARSE_TILL_END) {
				if (colSize == PARSE_TILL_END) {
					field.set(t, matrix.getRowsAndCols(startRow, startCol));
				} else {
					field.set(t, matrix.getRowsAndColsInRange(startRow, startCol, startCol + colSize));
				}
			} else {
				if (colSize == PARSE_TILL_END) {
					field.set(t, matrix.getRowsInRangeAndCols(startRow, startRow + rowSize, startCol));
				} else {
					field.set(t, matrix.getRowsInRangeAndColsInRange(startRow, startRow + rowSize, startCol, startCol + colSize));
				}
			}
		}
	}
}
