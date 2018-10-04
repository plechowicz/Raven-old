package com.github.piotrlechowicz.raven;

/**
 * @author plechowicz
 * created on 04.10.18.
 */
public class IllegalRowFormatException extends RuntimeException {

	private Integer columnId;

	public IllegalRowFormatException(String message, Throwable cause, Integer columnId) {
		super(message, cause);
		this.columnId = columnId;
	}

	public int getColumnId() {
		return columnId;
	}

	public void setColumnId(int columnId) {
		this.columnId = columnId;
	}
}
