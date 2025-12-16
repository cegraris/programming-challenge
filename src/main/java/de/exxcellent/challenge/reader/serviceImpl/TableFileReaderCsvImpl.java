package de.exxcellent.challenge.reader.serviceImpl;

import de.exxcellent.challenge.reader.service.TableFileReader;

import java.io.Reader;
import java.util.Optional;

/**
 * CSV implementation of {@link TableFileReader}.
 *
 * @param <T> the record type produced by this reader
 */
public class TableFileReaderCsvImpl<T> implements TableFileReader<T> {

    public TableFileReaderCsvImpl(Reader reader, Class<T> clazz) {
        // TODO: initialize components
    }

    @Override
    public Optional<T> read() throws Exception {
        // TODO: read and convert next CSV record
        return Optional.empty();
    }

    @Override
    public void close() throws Exception {
        // TODO: Implement closing underlying resources
    }
}
