package de.exxcellent.challenge.reader.model;

import de.exxcellent.challenge.reader.service.TableFileReaderFactory;

/**
 * Enumeration of supported file types for table data reading.
 * <p>
 * Used by {@link TableFileReaderFactory} to determine the appropriate
 * parsing strategy for input files.
 * </p>
 */
public enum FileType {CSV}
