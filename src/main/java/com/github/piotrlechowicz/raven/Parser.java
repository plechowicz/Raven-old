package com.github.piotrlechowicz.raven;

import com.github.piotrlechowicz.raven.annotations.PositionInFile;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Piotr Lechowicz
 */
public abstract class Parser<F> {

    private static final Logger log = Logger.getLogger(Parser.class);

    private List<List<F>> matrix;

    public <T> T parseFile(Class<T> clazz, String path) throws IOException {

        T t = getNewInstance(clazz);
        List<String> rawFileContent = getFileContent(path);
        createFileContentAsMatrix(rawFileContent);

        for (Field field : clazz.getDeclaredFields()) {
            PositionInFile annotation = field.getAnnotation(PositionInFile.class);
            if (annotation != null) {

            }
        }

        //TODO: finish
        return null;
    }

    protected abstract List<F> parseLineFromFile(String row);

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

    private void createFileContentAsMatrix(List<String> rawFileContent) {
        matrix = new LinkedList<>();
        for (String row : rawFileContent) {
            matrix.add(parseLineFromFile(row));
        }
    }

    private <T> T getNewInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.warn("Unable to create instance of: " + clazz);
            throw new RuntimeException(e.getCause());
        }
    }
}
