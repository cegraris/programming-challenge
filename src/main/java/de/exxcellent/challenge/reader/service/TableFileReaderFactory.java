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
    <T> TableFileReader<T> create(String filepath, FileType type, Class<T> clazz);
}