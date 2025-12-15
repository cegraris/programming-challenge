package de.exxcellent.challenge.reader.service;

import lombok.NonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Spliterators;
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
     */
    Optional<T> read() throws Exception;

    /**
     * A single-pass iterator view over {@link #read()}.
     * Note: Iterator cannot throw checked exceptions, so we wrap them in RuntimeException.
     */
    @Override
    default @NonNull Iterator<T> iterator() {
        return new Iterator<>() {
            Optional<T> next = fetch();

            private Optional<T> fetch() {
                try {
                    return TableFileReader.this.read();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    void close() throws Exception;
}
