package de.exxcellent.challenge.reader.serviceImpl;

import de.exxcellent.challenge.reader.model.FileType;
import de.exxcellent.challenge.reader.service.TableFileReader;
import de.exxcellent.challenge.reader.service.TableFileReaderFactory;
import org.springframework.stereotype.Component;

import java.io.Reader;

/**
 * Default implementation of {@link TableFileReaderFactory}.
 * <p>
 * Creates appropriate {@link TableFileReader} instances based on the specified file type.
 * Currently supports CSV format, with extensibility for additional formats.
 * </p>
 */
@Component
public class TableFileReaderFactoryImpl implements TableFileReaderFactory {

    @Override
    public <T> TableFileReader<T> create(Reader reader, FileType type, Class<T> clazz) {
        return switch (type) {
            case CSV -> new TableFileReaderCsvImpl<>(reader, clazz);
        };
    }

}
