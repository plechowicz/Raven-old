package com.github.piotrlechowicz.raven;

/**
 * <p>FileRow interface.</p>
 *
 * @author Piotr Lechowicz
 * @version $Id: $Id
 */
public interface FileRow {
	/**
	 * <p>getNumberOfValues.</p>
	 *
	 * @return a int.
	 */
	int getNumberOfValues();
	/**
	 * <p>getValueAt.</p>
	 *
	 * @param row a int.
	 * @return a {@link java.lang.Object} object.
	 */
	Object getValueAt(int row);
	/**
	 * <p>getFormatAt.</p>
	 *
	 * @param row a int.
	 * @return a {@link java.lang.String} object.
	 */
	String getFormatAt(int row);

	/**
	 * Optionally provide an error message
	 * @param row a int
	 * @return String description
	 */
	default String getErrorDescriptionAt(int row) {
		return "";
	};
}
