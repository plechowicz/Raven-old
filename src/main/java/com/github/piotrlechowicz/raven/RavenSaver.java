package com.github.piotrlechowicz.raven;

import com.github.piotrlechowicz.raven.annotations.Savable;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IllegalFormatConversionException;
import java.util.List;

/**
 * Save result based on the annotations within class
 *
 * @author plechowicz
 * created on 12/9/2019.
 */
public class RavenSaver<T> {

	private static final Logger log = Logger.getLogger(RavenSaver.class);

	private static final String DEFAULT_DELIMITER = ";";

	private final Class<T> clazz;
	private final String delimiter;

	/**
	 * Headers' name, format and values' format for all savable fields in the T class
	 */
	private List<String> headerNames = new ArrayList<>();
	private List<String> headerFormats = new ArrayList<>();
	private List<String> valueFormats = new ArrayList<>();
	/**
	 * Which fields should be saved from class
	 */
	private List<Field> fieldsToSave = new ArrayList<>();

	public RavenSaver(Class<T> clazz, String delimiter) {
		this.clazz = clazz;
		this.delimiter = delimiter;
		initializeFields();
	}

	/**
	 * Constructor for FlatFileSaver.
	 *
	 * @param clazz Class for instances from which the file will be created
	 */
	public RavenSaver(Class<T> clazz) {
		this(clazz, DEFAULT_DELIMITER);
	}

	private void initializeFields() {
		for (Field field : clazz.getDeclaredFields()) {
			Savable annotation = field.getAnnotation(Savable.class);
			if (annotation != null) {
				field.setAccessible(true);
				String headerName = annotation.header();
				if (headerName.isEmpty()) headerName = field.getName();
				headerNames.add(headerName);
				String headerFormat = annotation.headerFormat();
				headerFormats.add(headerFormat);
				String valueFormat = annotation.valueFormat();
				valueFormats.add(valueFormat);
				fieldsToSave.add(field);
			}
		}
	}

	public void save(String path, boolean append, T row, boolean addHeader) throws IOException {
		save(path, append, Collections.singletonList(row), addHeader);
	}

	public void save(String path, boolean append, List<T> rows, boolean addHeader) throws IOException {
		File file = new File(path);
		file.getParentFile().mkdirs();
		if (!append && file.exists()) {
			file.delete();
			file.createNewFile();
		}
		try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
			StringBuilder builder = createTxtResultStringBuilder(rows, addHeader);
			// remove last new line character
//			builder.deleteCharAt(builder.lastIndexOf("\n"));
			writer.append(builder);
		}
	}

	public String getValueAsString(List<T> rows, boolean addHeader) {
		StringBuilder builder = createTxtResultStringBuilder(rows, addHeader);
		builder.deleteCharAt(builder.lastIndexOf("\n"));
		return builder.toString();
	}

	private StringBuilder createTxtResultStringBuilder(List<T> rows, boolean addHeader) {
		StringBuilder builder = new StringBuilder();
		if (rows != null && !rows.isEmpty()) {
			if (addHeader) {
				addTxtHeaderString(rows.get(0), builder);
			}
			addTxtValuesString(rows, builder);
		}
		return builder;
	}

	/**
	 * Add values to StringBuilder from the list of T instances (based on provided annotations)
	 *
	 * @param rows
	 * @param builder
	 */
	private void addTxtValuesString(List<T> rows, StringBuilder builder) {
		for (T t : rows) {
			for (int fieldId = 0; fieldId < fieldsToSave.size(); fieldId++) {
				try {
					Object value = fieldsToSave.get(fieldId).get(t);
					builder.append(String.format(valueFormats.get(fieldId), value));
				} catch (IllegalAccessException e) {
					log.error("Error in accessing field: " + fieldsToSave.get(fieldId), e);
					builder.append("ERROR");
				} catch (IllegalFormatConversionException e) {
					String message = String.format("Error in saving value from field: {%s}, valueFormat: {%s}, check correct format",
							fieldsToSave.get(fieldId), valueFormats.get(fieldId));
					log.error(message);
					throw new IllegalStateException(message, e);
				}
				builder.append(delimiter);
			}
			builder.append("\n");
		}
	}

	/**
	 * Add header to StringBuilder from the first row of T instance based on provided annotation
	 *
	 * @param t
	 * @param builder
	 */
	private void addTxtHeaderString(T t, StringBuilder builder) {
		for (int fieldId = 0; fieldId < fieldsToSave.size(); fieldId++) {
			builder.append(String.format(headerFormats.get(fieldId), headerNames.get(fieldId)));
			builder.append(delimiter);
		}
		builder.append("\n");
	}
}
