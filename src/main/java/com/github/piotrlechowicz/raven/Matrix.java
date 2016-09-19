package com.github.piotrlechowicz.raven;

import java.util.LinkedList;
import java.util.List;

/**
 * Class which represents matrix - list of list of parameterised value T
 * 
 * @author plechowicz
 *
 * @param <T> parameterised type for matrix
 * 
 */
class Matrix<T> {
	private List<List<T>> matrix;
	private int nrOfRows;

	public Matrix(List<List<T>> matrix) {
		this.matrix = matrix;
		nrOfRows = matrix.size();
	}

	/**
	 * Gets value in row and column
	 * 
	 * @param row index of row
	 * @param col index of column
	 * @return value T
	 */
	T getValueAt(int row, int col) {
		if (isInCorrectRange(row, col)) {
			return matrix.get(row).get(col);
		}
		return null;
	}

	/**
	 * Gets values in row
	 * 
	 * @param row index of row
	 * @return list of values T
	 */
	List<T> getRow(int row) {
		if (isInCorrectRange(row)) {
			return matrix.get(row);
		}
		return null;
	}

	/**
	 * Gets value in row, starting from column till end of row
	 * 
	 * @param row index of row
	 * @param col index of column
	 * @return list of values T
	 */
	List<T> getRow(int row, int col) {
		if (isInCorrectRange(row, col)) {
			List<T> line = matrix.get(row);
			return line.subList(col, line.size());
		}
		return null;
	}

	/**
	 * Gets value in row, starting from startCol till endCol
	 * @param row index of row
	 * @param startCol index of starting column
	 * @param endCol index of ending column
	 * @return list of values T
	 */
	List<T> getRow(int row, int startCol, int endCol) {
		if (isInCorrectRange(row, startCol, endCol)) {
			List<T> line = matrix.get(row);
			return line.subList(startCol, endCol);
		}
		return null;
	}

	/**
	 * Gets value in column, starting from row till end of the column
	 * 
	 * @param row index of row
	 * @param col index of column
	 * @return list of values T
	 */
	List<T> getColumn(int row, int col) {
		if (isInCorrectRange(row, col)) {
			List<T> column = new LinkedList<T>();
			for (List<T> slicedLine : matrix.subList(row, nrOfRows)) {
				column.add(slicedLine.get(col));
			}
			return column;
		}
		return null;
	}

	/**
	 * Gets value in column, starting from startRow till endRow
	 * 
	 * @param startRow index of starting row
	 * @param endRow index of ending row
	 * @param col index of column
	 * @return list of values T
	 */
	List<T> getColumn(int startRow, int endRow, int col) {
		if (isInCorrectRange(startRow, endRow, col, col + 1)) {
			List<T> column = new LinkedList<>();
			for (List<T> slicedLine : matrix.subList(startRow, endRow)) {
				column.add(slicedLine.get(col));
			}
			return column;
		}
		return null;
	}

	/**
	 * Gets part of matrix starting from column and row till end of the matrix
	 * 
	 * @param row index of row
	 * @param col index of column
	 * @return list of list of values T
	 */
	List<List<T>> getRowsAndCols(int row, int col) {
		if (isInCorrectRange(row, col)) {
			List<List<T>> slicedMatrix = new LinkedList<>();
			List<List<T>> rows = matrix.subList(row, nrOfRows);
			for (List<T> line : rows) {
				slicedMatrix.add(line.subList(col, line.size()));
			}
			return slicedMatrix;
		}
		return null;
	}

	/**
	 * Gets part of matrix starting from startCol and row till endCol and last row
	 * 
	 * @param row index of row
	 * @param startCol index of starting column
	 * @param endCol index of ending column
	 * @return list of list of values T
	 */
	List<List<T>> getRowsAndColsInRange(int row, int startCol, int endCol) {
		if (isInCorrectRange(row, startCol, endCol)) {
			List<List<T>> slicedMatrix = new LinkedList<>();
			List<List<T>> rows = matrix.subList(row, nrOfRows);
			for (List<T> line : rows) {
				slicedMatrix.add(line.subList(startCol, endCol));
			}
			return slicedMatrix;
		}
		return null;
	}

	/**
	 * Gets part of matrix starting from startCol and row till endCol and last row
	 *
	 * @param startRow index of starting row
	 * @param endRow index of ending row
	 * @param startCol index of starting column
	 * @return list of list of values T
	 */
	List<List<T>> getRowsInRangeAndCols(int startRow, int endRow, int startCol) {
		if (isInCorrectRange(startRow, endRow, startCol, startCol)) {
			List<List<T>> slicedMatrix = new LinkedList<>();

			List<List<T>> rows = matrix.subList(startRow, endRow);
			for (List<T> line : rows) {
				slicedMatrix.add(line.subList(startCol, line.size()));
			}
			return slicedMatrix;
		}
		return null;
	}

	/**
	 * Gets part of matrix starting from startCol and startRow till endCol and endRow
	 * 
	 * @param startRow index of starting row
	 * @param endRow index of ending row
	 * @param startCol index of starting column
	 * @param endCol index of ending column
	 * @return list of list of values T
	 */
	List<List<T>> getRowsInRangeAndColsInRange(int startRow, int endRow, int startCol, int endCol) {
		if (isInCorrectRange(startRow, endRow, startCol, endCol)) {
			List<List<T>> slicedMatrix = new LinkedList<>();
			List<List<T>> rows = matrix.subList(startRow, endRow);
			for (List<T> line : rows) {
				slicedMatrix.add(line.subList(startCol, endCol));
			}
			return slicedMatrix;
		}
		return null;
	}

	private boolean isInCorrectRange(int row) {
		if (row >= 0 && row < nrOfRows)
			return true;
		return false;
	}

	private boolean isInCorrectRange(int row, int col) {
		if (isInCorrectRange(row))
			if (col >= 0 && col < matrix.get(row).size())
				return true;
		return false;
	}

	private boolean isInCorrectRange(int row, int startCol, int endCol) {
		if (startCol >= endCol)
			return false;
		if (isInCorrectRange(row))
			if (startCol >= 0 && endCol <= matrix.get(row).size())
				return true;
		return false;
	}

	private boolean isInCorrectRange(int startRow, int endRow, int startCol, int endCol) {
		if (startRow >= endRow || startCol >= endCol)
			return false;
		if (startRow >= 0 && endRow <= nrOfRows && startCol >= 0 && endCol <= matrix.get(startRow).size())
			return true;
		return false;
	}
}