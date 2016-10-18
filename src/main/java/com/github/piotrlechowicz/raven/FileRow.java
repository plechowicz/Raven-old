package com.github.piotrlechowicz.raven;

/**
 * @author Piotr Lechowicz
 */
public interface FileRow {
	int getNumberOfValues();
	Object getValueAt(int row);
	String getFormatAt(int row);
}
