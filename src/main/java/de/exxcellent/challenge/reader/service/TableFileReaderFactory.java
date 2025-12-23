package de.exxcellent.challenge.reader.service;

import de.exxcellent.challenge.reader.model.FileType;

/**
 * Factory interface for creating {@link TableFileReader} instances.
 * <p>
 * This factory abstracts the creation logic of table file readers,
 * allowing different implementations based on file type (CSV, Json, etc.).
 * </p>
 *
 * @see TableFileReader
 */
public interface TableFileReaderFactory {

    /**
     * Creates a {@link TableFileReader} for reading tabular files (e.g. CSV) and mapping each row to instances of
     * the provided target type.
     *
     * @param filepath path to the input file
     * @param type     the file type of the input
     * @param clazz    the target class used to map each row into an object
     * @param <T>      the row mapping target type
     * @return a {@link TableFileReader} for the given file and target type
     */
    <T> TableFileReader<T> create(String filepath, FileType type, Class<T> clazz);

}
