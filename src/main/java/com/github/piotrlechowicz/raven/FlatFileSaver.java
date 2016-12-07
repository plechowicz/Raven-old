package com.github.piotrlechowicz.raven;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Piotr Lechowicz
 */
public class FlatFileSaver {

	protected File file;

	public FlatFileSaver(String path) throws IOException {
		this(path, false);
	}

	public FlatFileSaver(String path, boolean append) throws IOException {
		file = new File(path);
		file.getParentFile().mkdirs();

		if (!append && file.exists()) {
			file.delete();
			file.createNewFile();
		}
	}

	public void save(FileRow fileRow) throws IOException {
		try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
			writer.println(formatFileRow(fileRow));
		}
	}

	public void save(String line) throws IOException {
		try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
			writer.println(line);
		}
	}

	public void save(List<? extends FileRow> fileRows) throws IOException {
		try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
			fileRows.stream().forEach(element -> writer.println(formatFileRow(element)));
		}
	}

	private String formatFileRow(FileRow fileRow) {
		StringBuilder builder = new StringBuilder(100);
		for(int i = 0; i < fileRow.getNumberOfValues(); i++) {
			builder.append(String.format(fileRow.getFormatAt(i), fileRow.getValueAt(i)));
		}
		return builder.toString();
	}
}
