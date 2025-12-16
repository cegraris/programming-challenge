package de.exxcellent.challenge.reader.serviceImpl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import de.exxcellent.challenge.reader.service.TableFileReader;

import java.io.Reader;
import java.util.Iterator;
import java.util.Optional;

/**
 * CSV implementation of {@link TableFileReader}.
 *
 * @param <T> the record type produced by this reader
 */
public class TableFileReaderCsvImpl<T> implements TableFileReader<T> {

    private final Reader reader;
    private final Iterator<T> iterator;

    public TableFileReaderCsvImpl(Reader reader, Class<T> clazz) {
        this.reader = reader;

        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                .withType(clazz)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        this.iterator = csvToBean.iterator();
    }

    @Override
    public Optional<T> read() throws Exception {
        if (iterator.hasNext()) {
            return Optional.of(iterator.next());
        }
        return Optional.empty();
    }

    @Override
    public void close() throws Exception {
        reader.close();
    }
}
