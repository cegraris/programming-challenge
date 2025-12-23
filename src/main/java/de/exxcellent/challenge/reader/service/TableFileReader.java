package de.exxcellent.challenge.reader.service;

import de.exxcellent.challenge.exception.AppException;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A single-pass reader interface for tabular files.
 *
 * <p>Records of type {@code T} are read sequentially and can be consumed only once.
 * The reader holds resources and must be closed after use.
 * It also provides a Stream view for convenient sequential processing.</p>
 *
 * @param <T> the record type produced by this reader.
 */
public interface TableFileReader<T> extends AutoCloseable, Iterable<T> {

    /**
     * Reads the next row.
     *
     * @return an {@link Optional} containing the next legal mapped row, the mismatch rows will be skipped
     * @throws AppException if an unrecoverable error occurs
     */
    Optional<T> read();

    /**
     * Indicates whether one or more rows failed to be read or mapped
     *
     * @return {@code true} if any error occurred
     * {@code false} otherwise
     */
    boolean hasFailedRowsExceptions();

    /**
     * Get the List of {@link AppException}, which were thrown by parsing the mismatch rows.
     *
     * @return List of {@link AppException}
     */
    List<AppException> getFailedRowsExceptions();

    /**
     * A single-pass iterator view over {@link #read()}.
     */
    @Override
    default @NonNull Iterator<T> iterator() {
        return new Iterator<>() {
            Optional<T> next = fetch();

            private Optional<T> fetch() {
                return TableFileReader.this.read();
            }

            @Override
            public boolean hasNext() {
                return next.isPresent();
            }

            @Override
            public T next() {
                if (next.isEmpty()) throw new NoSuchElementException();
                T cur = next.get();
                next = fetch();
                return cur;
            }
        };
    }

    /**
     * Stream view over this reader.
     */
    default Stream<T> stream() {
        Stream<T> s = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator(), 0),
                false
        );
        return s.onClose(() -> {
            try {
                close();
            } catch (AppException e) {
                throw AppException.fileCloseFailure(e);
            }
        });
    }

    /**
     * Closes this reader and releases any underlying resources.
     * Throws {@link AppException} if an I/O error occurs while closing.
     */
    @Override
    void close();

}
